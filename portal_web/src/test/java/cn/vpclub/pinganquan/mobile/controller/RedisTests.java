package cn.vpclub.pinganquan.mobile.controller;

import cn.vpclub.pinganquan.mobile.common.redis.sington.RedisSingtonService;
import cn.vpclub.pinganquan.mobile.domain.OfficialInfo;
import cn.vpclub.pinganquan.mobile.service.OfficialInfoService;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * 描述:
 * 版权: Copyright (c) 2016
 * 公司: 285206405@qq.com
 * 作者: 袁永君
 * 版本: 1.0
 * 创建日期: 2016/7/13 0013
 * 创建时间: 21:08
 */
public class RedisTests extends CaseControllerTest{

    @Test
    public void testRedis() throws InterruptedException {
        RedisSingtonService redisSingtonService = (RedisSingtonService)ctx.getBean("redisSingtonService");
        String ping = redisSingtonService.ping();//测试是否连接成功,连接成功输出PONG
        System.out.println(ping);

        //首先,我们看下redis服务里是否有数据
        long dbSizeStart = redisSingtonService.dbSize();
        System.out.println(dbSizeStart);

        redisSingtonService.set("username", "oyhk");//设值(查看了源代码,默认存活时间30分钟)
        String username = redisSingtonService.get("username");//取值
        System.out.println(username);
        redisSingtonService.set("username1", "oyhk1", 1);//设值,并且设置数据的存活时间(这里以秒为单位)
        String username1 = redisSingtonService.get("username1");
        System.out.println(username1);
        Thread.sleep(2000);//我睡眠一会,再去取,这个时间超过了,他的存活时间
        String liveUsername1 = redisSingtonService.get("username1");
        System.out.println(liveUsername1);//输出null

        //是否存在
        boolean exist = redisSingtonService.exists("username");
        System.out.println(exist);

        //查看keys
        Set<String> keys = redisSingtonService.keys("*");//这里查看所有的keys
        System.out.println(keys);//只有username username1(已经清空了)

        //删除
        redisSingtonService.set("username2", "oyhk2");
        String username2 = redisSingtonService.get("username2");
        System.out.println(username2);
        redisSingtonService.del("username2");
        String username2_2 = redisSingtonService.get("username2");
        System.out.println(username2_2);//如果为null,那么就是删除数据了

        //dbsize
        long dbSizeEnd = redisSingtonService.dbSize();
        System.out.println(dbSizeEnd);
    }

    @Test
    public void setRedisObject()
    {
        OfficialInfoService officialInfoService = (OfficialInfoService)ctx.getBean("officialInfoService");
        List<OfficialInfo> officialInfo = officialInfoService.findByActivityId("ed2c87cd833e4fa3952667bad78a7757");

        RedisSingtonService redisService = (RedisSingtonService)ctx.getBean("redisSingtonService");
        redisService.setRedisObject("officialInfo", officialInfo);
    }

    @Test
    public void getRedisObject()
    {
        RedisSingtonService redisSingtonService = (RedisSingtonService)ctx.getBean("redisSingtonService");
        List<OfficialInfo> officialInfo = (List<OfficialInfo>) redisSingtonService.getRedisObject("officialInfo");
        System.out.println(officialInfo.size());
    }
}
