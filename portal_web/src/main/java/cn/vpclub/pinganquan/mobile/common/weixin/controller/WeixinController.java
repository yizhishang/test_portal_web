package cn.vpclub.pinganquan.mobile.common.weixin.controller;

import cn.vpclub.pinganquan.mobile.aspect.MethodLog;
import cn.vpclub.pinganquan.mobile.common.weixin.dto.WeixinUserInfoDto;
import cn.vpclub.pinganquan.mobile.common.weixin.service.WeixinService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/6/26 0026.
 */
@Controller
public class WeixinController {

    private Logger logger = LoggerFactory.getLogger(WeixinController.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WeixinService weixinService;

    @RequestMapping("/weixin/auth/getCode")
    public String getCode(@RequestParam(value = "code", required = false) String code, Model model) throws UnsupportedEncodingException {

        if(code == null || code.equals("null")){
            return "redirect:" + weixinService.getWeixinAuthUrl();
        }

        JsonNode rootNode = weixinService.getUserInfoJsonByCode(code);
        String userInfoStr = URLEncoder.encode(rootNode.toString(),"UTF-8");
        model.addAttribute("userInfo",userInfoStr);

        return "redirect:/showWeixinUserInfo.action";
    }

    @RequestMapping("/showWeixinUserInfo.action")
    @MethodLog(description = "展示微信用户信息")
    public String showWeixinUserInfo(@RequestParam(value="userInfo", required = false) String userInfo, Model model) throws UnsupportedEncodingException {
        if (userInfo == null) {
            return "redirect:" + weixinService.getWeixinAuthUrl();
        }
        userInfo = URLDecoder.decode(userInfo, "UTF-8");

        WeixinUserInfoDto userInfoDto = new WeixinUserInfoDto();
        try {
            userInfoDto = objectMapper.readValue(userInfo, WeixinUserInfoDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("name",userInfoDto);

        return "index";
    }
}
