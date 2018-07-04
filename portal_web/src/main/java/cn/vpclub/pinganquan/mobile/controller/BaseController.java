package cn.vpclub.pinganquan.mobile.controller;

import cn.vpclub.pinganquan.mobile.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述: BaseController.java
 * 版权: Copyright (c) 2016
 * 公司: 285206405@qq.com
 * 作者: 袁永君
 * 版本: 1.0
 * 创建日期: 2016-2-29
 * 创建时间: 上午11:22:58
 */
@ControllerAdvice
public class BaseController
{
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Result handleException(Exception exp, HttpServletRequest request, HttpServletResponse response) {
		printLog(request);
		logger.error("错误码：5000", exp);
		return new Result(5000, "服务器处理错误", exp.getMessage());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException exp, HttpServletRequest request, HttpServletResponse response) {
		printLog(request);
		logger.error("错误码：4001", exp);
		return new Result(4001, "缺少必要的参数", exp.getMessage());
	}

	@ExceptionHandler(ServletRequestBindingException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Result handleServletRequestBindingException(ServletRequestBindingException exp, HttpServletRequest request, HttpServletResponse response) {
		printLog(request);
		logger.error("错误码：4002", exp);
		return new Result(4002, "请求绑定错误", exp.getMessage());
	}

	public static void printLog(HttpServletRequest request)
	{
		logger.info(request.getRequestURL().toString() + "?" + request.getQueryString());
	}
	
}
