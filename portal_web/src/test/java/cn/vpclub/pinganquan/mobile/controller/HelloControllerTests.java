package cn.vpclub.pinganquan.mobile.controller;

import cn.vpclub.pinganquan.mobile.service.OfficialInfoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HelloControllerTests  extends BaseControllerTest{

    @Autowired
    private HelloController controller;

    @Autowired
    OfficialInfoService service;

    @Test
    public void testException()
    {
        controller.testException(null,null);
    }

    @Test
    public void updateOfficialInfoById()
    {
        service.updateOfficialInfoById("0077ede8aead44908a381484f2bf78af");
    }

}

