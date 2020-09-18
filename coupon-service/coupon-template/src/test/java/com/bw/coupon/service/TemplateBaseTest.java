package com.bw.coupon.service;


import com.bw.coupon.exception.CommonException;
import com.bw.coupon.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TemplateBaseTest {
    private final ITemplateBaseService templateBaseService;
    private final JacksonUtil jacksonUtil;

    @Autowired
    public TemplateBaseTest(ITemplateBaseService templateBaseService, JacksonUtil jacksonUtil) {
        this.templateBaseService = templateBaseService;
        this.jacksonUtil = jacksonUtil;
    }

    @Test
    public void testBuildTemplateInfo() throws CommonException{
        System.out.println(jacksonUtil.writeValueAsString(templateBaseService.buildTemplateInfo(12)));
        //System.out.println(JSON.toJSONString(templateBaseService.buildTemplateInfo(2)));
    }

    @Test
    public void findAllUsableTemplate() throws CommonException{
        System.out.println(jacksonUtil.writeValueAsString(templateBaseService.findAllUsableTemplate()));
    }

    @Test
    public void findIds2TemplateSDK() throws CommonException{
        List<Integer> list = new ArrayList<>();
        list.add(12);
        list.add(2);
        System.out.println(jacksonUtil.writeValueAsString(templateBaseService.findIds2TemplateSDK(list)));
    }
}
