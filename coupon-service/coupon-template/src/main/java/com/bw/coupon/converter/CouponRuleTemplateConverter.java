package com.bw.coupon.converter;

import com.bw.coupon.vo.CouponRuleTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class CouponRuleTemplateConverter implements AttributeConverter<CouponRuleTemplate,String> {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public String convertToDatabaseColumn(CouponRuleTemplate couponRuleTemplate) {
        try {
            return mapper.writeValueAsString(couponRuleTemplate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
        //return JSON.toJSONString(couponRuleTemplate);
    }

    @Override
    public CouponRuleTemplate convertToEntityAttribute(String s) {
        try {
            return mapper.readValue(s,CouponRuleTemplate.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        //return JSON.parseObject(s,CouponRuleTemplate.class);
    }
}
