package cn.vpclub.pinganquan.mobile.aspect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.vpclub.pinganquan.mobile.base.util.StringUtils;

/**
 * Created by Administrator on 2016/6/24 0024.
 */
public class BaseHandlerInterceptor extends HandlerInterceptorAdapter
{
    private static Logger logger = LoggerFactory.getLogger(BaseHandlerInterceptor.class);

    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>(
            "StopWatch-StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception
    {
        long beginTime = System.currentTimeMillis();// 1、开始时间
        startTimeThreadLocal.set(beginTime);// 线程绑定变量（该数据只有当前请求的线程可见）
        String path = request.getServletPath();
        if (StringUtils.isBlank(path))
        {
            path = request.getRequestURI();
        }
        logger.info("开始: " + path);
        return true;// 继续流程
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        long endTime = System.currentTimeMillis();// 2、结束时间
        long beginTime = startTimeThreadLocal.get();// 得到线程绑定的局部变量（开始时间）
        long consumeTime = endTime - beginTime;// 3、消耗的时间

        String path = request.getServletPath();
        if (StringUtils.isBlank(path))
        {
            path = request.getRequestURI();
        }
        logger.info("结束: " + path + ". Time: " + consumeTime + "ms.");

        if (consumeTime > 3000)
        {// 此处认为处理时间超过3000毫秒的请求为慢请求
            logger.info(String.format("%s consume %d millis", path, consumeTime));
        }
    }
}