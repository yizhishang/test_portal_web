package cn.vpclub.pinganquan.mobile.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 描述: 日志方面组件
 * 版权: Copyright (c) 2015
 * 公司: 285206405@qq.com
 * 作者: 袁永君
 * 版本: 1.0
 * 创建日期: 2015-10-18
 * 创建时间: 下午10:32:11
 */
@Component
@Aspect
public class BaseLogInterceptor
{
	
	private Logger logger;

	@Pointcut("execution(* cn.vpclub.pinganquan.mobile.controller.*.*(..))")
	public void pointCut()
	{
	}

	@Before(value = "pointCut()")
	public void before()
	{
		logger.info("=====before=====");
	}

	@After(value = "pointCut()")
	public void after()
	{
		logger.info("=====后置通知开始=====");
		logger.info("=====后置通知结束=====");
	}
	
	@Around(value = "pointCut()")
	public Object AroundExcute(ProceedingJoinPoint pjp) throws Throwable
	{
		logger = LoggerFactory.getLogger(pjp.getTarget().getClass());
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		HttpSession session = request.getSession();
		//请求的IP
		String ip = request.getRemoteAddr();
		try
		{
//			String className = pjp.getTarget().getClass().getName();
			String method = pjp.getSignature().getName();
			String remark = getMthodRemark(pjp);
			Map requestMap = request.getParameterMap();
			ObjectMapper objectMapper = new ObjectMapper();

			String params = objectMapper.writeValueAsString(requestMap);
			
			if (logger.isInfoEnabled())
			{
				//*========控制台输出=========*//
				logger.info("=====前置通知开始=====");
				logger.info("请求方法: [" + method + "]");
				logger.info("方法描述: [" + remark + "]");
				logger.info("请求IP:" + ip);
				logger.info("请求参数:" + params);
				logger.info("=====前置通知结束=====");
			}
			long startTime = System.currentTimeMillis();
			
			//在进入相应的function对应的方法前，先调用before，在调用相应的方法后，再调用after
			Object result = pjp.proceed();
			if (logger.isInfoEnabled())
			{
				logger.info("执行完成 times=" + String.valueOf(System.currentTimeMillis() - startTime) + " millisecond");
			}
			return (result == null) ? "none" : result;
		}
		catch (Exception ex)
		{
			//记录本地异常日志
			logger.error("==前置通知异常==");
			logger.error("异常信息:{}", ex);
			request.setAttribute("ex", ex);
		}
		return "error";
	}

	// 获取方法的中文备注____用于记录用户的操作日志描述
	private static String getMthodRemark(ProceedingJoinPoint joinPoint) throws Exception
	{
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		
		Method[] method = Class.forName(targetName).getMethods();
		String methode = "";
		for (Method m : method)
		{
			if (m.getName().equals(methodName))
			{
				if (m.getParameterTypes().length == arguments.length)
				{
					MethodLog methodCache = m.getAnnotation(MethodLog.class);
					if (methodCache != null)
					{
						methode = methodCache.description();
					}
					break;
				}
			}
		}
		return methode;
	}

}
