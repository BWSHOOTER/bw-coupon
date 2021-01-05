package com.bw.coupon.serialization;


//import com.alibaba.fastjson.JSON;
import com.bw.coupon.entity.Coupon;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 优惠券实体类自定义序列化器
 */
public class CouponSerialize extends JsonSerializer<Coupon> {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void serialize(Coupon coupon, JsonGenerator generator, SerializerProvider serializerProvider)
            throws IOException {
        // 开始序列化
        generator.writeStartObject();

        generator.writeStringField("id", coupon.getId().toString());
        generator.writeStringField("templateId",coupon.getTemplateId().toString());
        generator.writeStringField("userId", coupon.getUserId().toString());
        generator.writeStringField("couponCode", coupon.getCouponCode());
        generator.writeStringField("assignTime",
                objectMapper.writeValueAsString(coupon.getAssignTime()));
        /*
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(coupon.getAssignTime()));
         */
        generator.writeStringField("name", coupon.getTemplateVo().getDisplayName());
        generator.writeStringField("logo", coupon.getTemplateVo().getLogo());
        generator.writeStringField("intro", coupon.getTemplateVo().getIntro());
        generator.writeStringField("expiration",
                objectMapper.writeValueAsString(coupon.getTemplateVo().getRule().getExpirationRule()));
        generator.writeStringField("discount",
                objectMapper.writeValueAsString(coupon.getTemplateVo().getRule().getCalculatingRule()));

        // 结束序列化
        generator.writeEndObject();

    }
}
