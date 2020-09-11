package com.bw.coupon.service;


import com.alibaba.fastjson.JSON;
import com.bw.coupon.vo.CouponTemplateRuleVo.*;
import com.bw.coupon.enumeration.*;
import com.bw.coupon.vo.CouponTemplateRuleVo;
import com.bw.coupon.vo.TemplateRequestVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * 构造模板服务测试
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class BuildTemplateTest {
    @Autowired
    private IBuildTemplateService buildTemplateService;

    @Test
    public void testBuildTemplate() throws Exception{
        buildTemplateService.buildCouponTemplate(fakeTemplateRequest());
        //String j = JSON.toJSONString();
        //System.out.println(j);
        Thread.sleep(5000);
    }

    /**
     * fake TemplateRequest
     **/
    private TemplateRequestVo fakeTemplateRequest(){
        ExpirationEnum expiration = ExpirationEnum.ShiftExpiration;
        Long expireGap = 50000L;

        TemplateRequestVo request = new TemplateRequestVo();
        request.setName("测试模板-" + new Date().getTime());
        request.setIntro("测试模板-" + new Date().getTime());
        request.setLogo("No Logo");
        request.setCount(1000);
        request.setUserId(10001L);
        request.setCustomer(CustomerEnum.AllCustomer.getCode());
        request.setProductLine(ProductLineEnum.SkinProduct.getCode());
        request.setDiscount(DiscountEnum.MultiplyDiscount.getCode());
        request.setDistribute(DistributeEnum.MultiDistribute.getCode());


        ExpirationRuleTemplate expirationRule =
                new CouponTemplateRuleVo.ExpirationRuleTemplate
                        (expiration.getCode(),
                                expireGap);
        CustomerRuleTemplate customerRule =
                new CustomerRuleTemplate(CustomerEnum.AllCustomer.getCode());
        DiscountRuleTemplate discountRule =
                new DiscountRuleTemplate(DiscountEnum.MultiplyDiscount.getCode(),5,1);
        DistributeRuleTemplate distributeRule =
                new DistributeRuleTemplate(DistributeEnum.MultiDistribute.getCode());
        CouponTemplateRuleVo rule = new CouponTemplateRuleVo(expirationRule,
                                                                discountRule,
                                                                customerRule,
                                                                distributeRule);

        request.setRule(rule);
        return request;
    }
}
