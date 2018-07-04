package cn.vpclub.pinganquan.mobile.controller;
import cn.vpclub.pinganquan.mobile.service.OfficialInfoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.Arrays;

public class CaseControllerTest
{
    
    private static final Logger logger = LoggerFactory.getLogger(CaseControllerTest.class);
    
    public XmlWebApplicationContext ctx;
    
    @Before
    public void setUp() throws Exception
    {
        String[] paths = { "classpath:spring/spring-context.xml","classpath:spring/spring-mvc.xml" };
        ctx = new XmlWebApplicationContext();
        ctx.setConfigLocations(paths);
        ctx.setServletContext(new MockServletContext(""));
        ctx.refresh();
        String[] names = ctx.getBeanDefinitionNames();
        logger.info("beanNames[]-->{}", Arrays.toString(names));
    }
    
    @After
    public void tearDown() throws Exception
    {
        ctx.close();
    }


    @Test
    public void updateOfficialInfoById()
    {
        OfficialInfoService service = (OfficialInfoService) ctx.getBean("officialInfoService");
        service.updateOfficialInfoById("0077ede8aead44908a381484f2bf78af");
    }

}
