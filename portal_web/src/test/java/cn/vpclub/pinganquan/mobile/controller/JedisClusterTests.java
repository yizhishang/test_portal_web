package cn.vpclub.pinganquan.mobile.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

/**
 * 描述:
 * 版权: Copyright (c) 2016
 * 公司: 285206405@qq.com
 * 作者: 袁永君
 * 版本: 1.0
 * 创建日期: 2016/7/12 0012
 * 创建时间: 21:40
 */
public class JedisClusterTests extends CaseControllerTest{

    private Logger logger = LoggerFactory.getLogger(JedisClusterTests.class);

    @Test
    public void testJedisCluster()
    {
        JedisCluster jedisCluster = (JedisCluster) ctx.getBean("jedisCluster");
        logger.info("{}", jedisCluster);
    }
}
