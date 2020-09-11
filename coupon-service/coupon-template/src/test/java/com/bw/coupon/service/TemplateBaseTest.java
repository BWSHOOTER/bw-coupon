package com.bw.coupon.service;


import com.alibaba.fastjson.JSON;
import com.bw.coupon.exception.CommonException;
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
    @Autowired
    private ITemplateBaseService templateBaseService;

    @Test
    public void testBuildTemplateInfo() throws CommonException{
        System.out.println(JSON.toJSONString(templateBaseService.buildTemplateInfo(12)));
        //System.out.println(JSON.toJSONString(templateBaseService.buildTemplateInfo(2)));
    }

    @Test
    public void findAllUsableTemplate() throws CommonException{
        System.out.println(JSON.toJSONString(templateBaseService.findAllUsableTemplate()));
    }

    @Test
    public void findIds2TemplateSDK() throws CommonException{
        List<Integer> list = new ArrayList<>();
        list.add(12);
        list.add(2);
        System.out.println(JSON.toJSONString(templateBaseService.findIds2TemplateSDK(list)));
    }
}
