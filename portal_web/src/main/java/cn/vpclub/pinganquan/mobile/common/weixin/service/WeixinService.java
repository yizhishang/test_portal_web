package cn.vpclub.pinganquan.mobile.common.weixin.service;

import cn.vpclub.pinganquan.mobile.base.util.EncoderUtil;
import cn.vpclub.pinganquan.mobile.common.weixin.dto.WeixinJsapiConfigDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/6/26 0026.
 */
@Service
public class WeixinService {

    private static final Logger logger = LoggerFactory.getLogger(WeixinService.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${weixin.appId}")
    private String appId;

    @Value("${weixin.appSecret}")
    private String appSecret;

    @Value("${weixin.redirectUrl}")
    private String redirectUrl;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @return 获取access_token
     */
    public String getAccessToken() {
        try {
            String key = "weixin:access_token";
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            String accessToken = ops.get(key);
            // 若缓存中有则直接返回，否则调用微信api去取一个新的
            if (accessToken != null) {
                return accessToken;
            } else {
                // 从微信api中获取openid
                String url = String.format("https://api.weixin.qq.com/cgi-bin/token?" +
                                "grant_type=client_credential&appid=%s&secret=%s",
                        appId, appSecret);
                RestTemplate template = new RestTemplate();
                String result = template.getForObject(url, String.class);
                JsonNode rootNode = objectMapper.readTree(result);
                String newAccessToken = rootNode.get("access_token").asText();
                // 存入redis中
                ops.set(key, newAccessToken, 7200, TimeUnit.SECONDS);
                return newAccessToken;
            }
        } catch (Exception exp) {
            logger.error(exp.getMessage());
            throw null;
        }
    }


    /**
     * @return 获取JsapiTicket
     */
    public String getJsapiTicket() {
        try {
            String key = "weixin:jsapi_ticket";
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            String jsapiTicket = ops.get(key);
            // 若缓存中有则直接返回，否则调用微信api去取一个新的
            if (jsapiTicket != null) {
                return jsapiTicket;
            } else {
                String accessToken = this.getAccessToken();
                // 获取JsapiTicket
                String url = String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?" +
                        "access_token=%s&type=jsapi", accessToken);
                RestTemplate template = new RestTemplate();
                String result = template.getForObject(url, String.class);
                JsonNode rootNode = objectMapper.readTree(result);
                String newJsapiTicket = rootNode.get("ticket").asText();
                // 存入redis中
                ops.set(key, newJsapiTicket, 7200, TimeUnit.SECONDS);
                return newJsapiTicket;
            }
        } catch (Exception exp) {
            logger.error(exp.getMessage());
            throw null;
        }
    }


    /**
     * 获取jsapi的配置值
     *
     * @param url
     * @return
     */
    public WeixinJsapiConfigDto getWeixinJsapiConfig(String url) {
        String jsapiTicket = this.getJsapiTicket();
        if (jsapiTicket == null) {
            return new WeixinJsapiConfigDto();
        } else {
            long timestamp = new Date().getTime() / 1000;
            String nonceStr = Integer.toString(new Random().nextInt(1000000));
            // 签名时，需要将jsapi_ticket、noncestr、timestamp、url等四部分拼接在一起
            String strToSign = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%d&url=%s",
                    jsapiTicket, nonceStr, timestamp, url);
            String signature = EncoderUtil.SHA1(strToSign);

            WeixinJsapiConfigDto jsapiConfig = new WeixinJsapiConfigDto();
            jsapiConfig.setAppId(appId);
            jsapiConfig.setTimestamp(timestamp);
            jsapiConfig.setNonceStr(nonceStr);
            jsapiConfig.setSignature(signature);
            return jsapiConfig;
        }
    }

    public JsonNode getWeixinResult(String url){
        JsonNode rootNode = null;
        try {
            String result = restTemplate.getForObject(url, String.class);
            result = new String(result.getBytes("ISO-8859-1"),"UTF-8");
            rootNode = objectMapper.readTree(result);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return rootNode;
    }

    public String getWeixinAuthUrl(){
        String encodedRedirectUrl = null;
        try {
            encodedRedirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String weixinAuthUrl = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=123eex#wechat_redirect", appId, encodedRedirectUrl);

        logger.info("weixinAuthUrl:--->" + weixinAuthUrl);
        return weixinAuthUrl;
    }

    /**
     * 参考网页：http://mp.weixin.qq.com/wiki/4/9ac2e7b1f1d22e9e57260f6553822520.html
     * @param code
     * @return
     */
    public JsonNode getUserInfoJsonByCode(String code)
    {
        // 第二步：通过code换取网页授权access_token
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", appId, appSecret, code);
        JsonNode rootNode = getWeixinResult(url);
        logger.info("通过code换取网页授权access_token:rootNode-->{}", rootNode);

        String openId = rootNode.get("openid").asText();
        String accessToken = rootNode.get("access_token").asText();
        String refreshToken = rootNode.get("refresh_token").asText();

        //附：检验授权凭证（access_token）是否有效
        url = String.format("https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s", accessToken, openId);
        rootNode = getWeixinResult(url);
        logger.info("检验授权凭证（access_token）是否有效:rootNode-->{}", rootNode);

        // 第三步：刷新access_token（如果需要）
        String errorCode = rootNode.get("errcode").asText();
        if(!errorCode.equals("0")){
            url = String.format("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&refresh_token=%s&grant_type=refresh_token", appId, refreshToken);
            rootNode = getWeixinResult(url);
            logger.info("刷新access_token :rootNode-->{}", rootNode);

            accessToken = rootNode.get("access_token").asText();
        }

        //第四步：拉取用户信息(需scope为 snsapi_userinfo)
        url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN", accessToken, openId);
        rootNode = getWeixinResult(url);
        logger.info("获取微信用户信息: rootNode-->{}", rootNode);

        return rootNode;
    }


}
