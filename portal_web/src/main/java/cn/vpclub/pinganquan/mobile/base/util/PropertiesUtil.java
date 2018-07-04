package cn.vpclub.pinganquan.mobile.base.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2016/6/25 0025.
 */
public class PropertiesUtil {

    public static Properties prop = null;

    static{
        prop = PropertiesUtil.getPrp("config.properties");
    }

    /**
     * 读取配置文件config.properties
     * @param filePath
     * @return
     */
    public static Properties  getPrp(String filePath){
        Properties properties = new Properties();
        try{
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(filePath));
        } catch (IOException e){
            e.printStackTrace();
        }
        return properties;
    }

    public static String getPropValue(String key)
    {
        String value = null;
        if(null != prop){
            value = prop.getProperty(key);
        }
        return value;
    }

    public static String getPropValue(String key, String defaultValue)
    {
        String value = getPropValue(key);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public static int getIntPropValue(String key){
        String value = getPropValue(key);
        if(StringUtils.isEmpty(value))
        {
            return 0;
        }else{
            return Integer.parseInt(value);
        }
    }

}
