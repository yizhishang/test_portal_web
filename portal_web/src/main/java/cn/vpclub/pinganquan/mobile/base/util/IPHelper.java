package cn.vpclub.pinganquan.mobile.base.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述:
 * 版权:	 Copyright (c) 2015
 * 公司:	 285206405@qq.com
 * 作者:	 袁永君
 * 版本:	 1.0
 * 创建日期: 2015-12-31
 * 创建时间: 12:29:55
 */
public class IPHelper
{
    /**
     * 获取IP地址.
     *
     * @param request  HTTP请求.
     * @param response HTTP响应.
     * @param url      需转发到的URL.
     */
    public static String getIpAddr(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * 隐藏IP的最后一段
     *
     * @param ip 需要进行处理的IP
     * @return 隐藏后的IP
     */
    public static String hideIp(String ip)
    {
        if (StringUtils.isEmpty(ip))
        {
            return "";
        }

        int pos = ip.lastIndexOf(".");
        if (pos == -1)
        {
            return ip;
        }

        ip = ip.substring(0, pos + 1);
        ip = ip + "*";
        return ip;
    }

    /**
     * 判断该字串是否为IP
     * @param ipStr IP字串
     * @return
     */
    public static boolean isIP(String ipStr)
    {
        String ip = "(25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|\\d)";
        String ipDot = ip + "\\.";
        return ipStr.matches(ipDot + ipDot + ipDot + ip);
    }
}
