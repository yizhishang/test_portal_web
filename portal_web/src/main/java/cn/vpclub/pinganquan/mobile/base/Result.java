package cn.vpclub.pinganquan.mobile.base;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 描述: 结果集对象Result.java
 * 版权: Copyright (c) 2016
 * 公司: 285206405@qq.com
 * 作者: 袁永君
 * 版本: 1.0
 * 创建日期: 2016-1-5
 * 创建时间: 上午3:32:03
 */
public class Result implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private int errorNo;
    
    private String errorInfo;

    private Object data;

    private Map<String, Object> dataMap;

    public final static String successMsg = "操作成功！";

    public final static String errorMsg = "操作失败！";
    
    public Result()
    {
        this.errorNo = 0;
        this.errorInfo = successMsg;
        dataMap = Maps.newHashMap();
    }
    
    public Result(int errorNo)
    {
        this.errorNo = errorNo;
        dataMap = Maps.newHashMap();
    }
    
    public Result(int errorNo, String errorInfo)
    {
        this.errorNo = errorNo;
        this.errorInfo = errorInfo;
        dataMap = Maps.newHashMap();
    }
    public Result(int errorNo, String errorInfo, Object data)
    {
        this.errorNo = errorNo;
        this.errorInfo = errorInfo;
        this.data = data;
    }
    
    public int getErrorNo()
    {
        return errorNo;
    }
    
    public void setErrorNo(int errorNo)
    {
        this.errorNo = errorNo;
    }
    
    public String getErrorInfo()
    {
        return errorInfo;
    }
    
    public void setErrorInfo(String errorInfo)
    {
        this.errorInfo = errorInfo;
    }

    public Map<String, Object> getDataMap()
    {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public Object get(String key) {
        return dataMap.get(key);
    }

    public void put(String key, Object value) {
        dataMap.put(key, value);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "Result [errorNo=" + errorNo + ", errorInfo=" + errorInfo + ", dataMap=" + dataMap + "]";
    }

}
