package cn.vpclub.pinganquan.mobile.controller;

import cn.vpclub.pinganquan.mobile.aspect.MethodLog;
import cn.vpclub.pinganquan.mobile.base.Result;
import cn.vpclub.pinganquan.mobile.domain.OfficialInfo;
import cn.vpclub.pinganquan.mobile.service.OfficialInfoService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HelloController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Value("${jdbc.driver}")
    private String jdbc_driver;

    @Autowired
    private OfficialInfoService officialInfoService;

    @ResponseBody
    @ExceptionHandler
    @RequestMapping("/greeting")
    @MethodLog(description = "欢迎")
    public Result greeting(Model model, @RequestParam(value = "userName") String userName) {
        logger.info("jdbc_driver-->{}", jdbc_driver);
        if(model != null){
            model.addAttribute("driver", jdbc_driver);
        }

        List<OfficialInfo> officialInfo = officialInfoService.findByActivityId("ed2c87cd833e4fa3952667bad78a7757");
        logger.info("officialInfo--->{}", officialInfo);
        Result result = new Result();
        result.put("name", userName);
        result.put("driver", jdbc_driver);
        result.put("officialInfo", officialInfo);
        return result;
    }

    @RequestMapping("/index")
    @MethodLog(description = "首页")
    public String welComeIndex(Model model) {
        model.addAttribute("list", Lists.newArrayList("a", "b", "c"));
        model.addAttribute("name","首页spring-mvc");
        return "index";
    }

    @RequestMapping("/testException")
    @MethodLog(description = "测试Exception")
    public String testException(@RequestParam(value="name", required = true) String name,Model model)
    {
        logger.info("{}", name);
        model.addAttribute("name","首页spring-mvc");
        String age = null;
        int number = Integer.parseInt(age);

        return "index";
    }

}
