package com.bw.coupon.service;

import com.bw.coupon.enumeration.*;
import com.bw.coupon.vo.TemplateRuleVo;
import com.bw.coupon.vo.TemplateCreatingRequestVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * 构造模板服务测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BuildTemplateTest {
    /*
    @Autowired
    private ITemplateCreatingService buildTemplateService;

    @Test
    public void testBuildTemplate() throws Exception{
        buildTemplateService.buildCouponTemplate(fakeTemplateRequest());
        //String j = JSON.toJSONString();
        //System.out.println(j);
        Thread.sleep(5000);
    }
    */

    /**
     * fake TemplateRequest
     */
    /*
    private TemplateCreatingRequestVo fakeTemplateRequest(){
        ExpirationTypeEnum expiration = ExpirationTypeEnum.ShiftExpiration;
        Long expireGap = 50000L;

        TemplateCreatingRequestVo request = new TemplateCreatingRequestVo();
        request.setName("测试模板-" + new Date().getTime());
        request.setIntro("测试模板-" + new Date().getTime());
        request.setLogo("No Logo");
        request.setCount(1000);
        request.setUserId(10001L);
        request.setCustomer(CustomerTypeEnum.AllCustomer.getCode());
        request.setProductLine(GoodsCategoryEnum.SkinProduct.getCode());
        request.setDiscount(CalculatingMethodEnum.MultiplyDiscount.getCode());
        request.setDistribute(DistributionMethodEnum.MultiDistribute.getCode());


        ExpirationRuleTemplate expirationRule =
                new TemplateRuleVo.ExpirationRuleTemplate
                        (expiration.getCode(),
                                expireGap);
        CustomerRuleTemplate customerRule =
                new CustomerRuleTemplate(CustomerTypeEnum.AllCustomer.getCode());
        DiscountRuleTemplate discountRule =
                new DiscountRuleTemplate(CalculatingMethodEnum.MultiplyDiscount.getCode(),5,1);
        DistributeRuleTemplate distributeRule =
                new DistributeRuleTemplate(DistributionMethodEnum.MultiDistribute.getCode());
        TemplateRuleVo rule = new TemplateRuleVo(expirationRule,discountRule,
                customerRule, distributeRule, 1000);

        request.setRule(rule);
        return request;
    }
    */
}
