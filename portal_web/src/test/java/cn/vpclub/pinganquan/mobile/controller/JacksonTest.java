package cn.vpclub.pinganquan.mobile.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/6/26 0026.
 */
public class JacksonTest {
    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        String json = "{\"username\":\"zhangsan\",\"性别\":\"男\",\"company\":{\"companyName\":\"中华\",\"address\":\"北京\"},\"cars\":[\"奔驰\",\"宝马\"]}";
        ObjectMapper mapper = new ObjectMapper();
        //JSON ----> JsonNode
        JsonNode rootNode = mapper.readTree(json);
        Iterator<String> keys = rootNode.fieldNames();
        while(keys.hasNext()){
            String fieldName = keys.next();
            System.out.println(fieldName + ": " + rootNode.path(fieldName).toString());
        }
        //JsonNode ----> JSON
        System.out.println(mapper.writeValueAsString(rootNode));

        json = "%7B%22openid%22%3A%22oslJouMDg_YhmFdJFebd1ncKyjvU%22%2C%22nickname%22%3A%22%E6%97%AD%E6%85%95%22%2C%22sex%22%3A1%2C%22language%22%3A%22zh_CN%22%2C%22city%22%3A%22%E6%B7%B1%E5%9C%B3%22%2C%22province%22%3A%22%E5%B9%BF%E4%B8%9C%22%2C%22country%22%3A%22%E4%B8%AD%E5%9B%BD%22%2C%22headimgurl%22%3A%22http%3A%2F%2Fwx.qlogo.cn%2Fmmopen%2FibhzWy4ibIEpDAdIdhYUf3qEia8ic5nXibErbIpo0Gxr3CqHRibib6d3ZJ9yu928NGPNaYt7PhqChUy5s2c8WGhIG6uLCgiaGaIO0uIS%2F0%22%2C%22privilege%22%3A%5B%5D%7D";

        json = URLDecoder.decode(json, "utf-8");
        System.out.println(json);
    }
}
