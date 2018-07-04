package cn.vpclub.pinganquan.mobile.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.vpclub.pinganquan.mobile.aspect.MethodLog;

@Controller
public class LoginController extends BaseController
{
	@Value("${jdbc.driver}")
	private String jdbc_driver;

	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping("/login.action")
	@MethodLog(description = "进入登录页面")
	public ModelAndView doLogin()
	{
		ModelAndView mv = new ModelAndView("/login");
		logger.info(jdbc_driver);
		return mv;
	}
	
}
