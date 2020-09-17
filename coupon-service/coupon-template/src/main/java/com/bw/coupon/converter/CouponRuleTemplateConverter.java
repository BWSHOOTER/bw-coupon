package com.bw.coupon.converter;

import com.bw.coupon.vo.TemplateRuleVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class CouponRuleTemplateConverter implements AttributeConverter<TemplateRuleVo,String> {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public String convertToDatabaseColumn(TemplateRuleVo couponTemplateRuleVo) {
        try {
            return mapper.writeValueAsString(couponTemplateRuleVo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
        //return JSON.toJSONString(couponRuleTemplate);
    }

    @Override
    public TemplateRuleVo convertToEntityAttribute(String s) {
        try {
            return mapper.readValue(s, TemplateRuleVo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        //return JSON.parseObject(s,CouponRuleTemplate.class);
    }
}
