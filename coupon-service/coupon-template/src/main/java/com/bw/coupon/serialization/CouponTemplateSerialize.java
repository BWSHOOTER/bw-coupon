package com.bw.coupon.serialization;

import com.bw.coupon.Entity.CouponTemplate;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 优惠券模板自定义序列化器
 **/

public class CouponTemplateSerialize extends JsonSerializer<CouponTemplate> {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void serialize(CouponTemplate template,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider)
            throws IOException {
        // 开始序列化对象
        generator.writeStartObject();

        generator.writeStringField("id", template.getId().toString());
        generator.writeStringField("name", template.getName());
        generator.writeStringField("logo", template.getLogo());
        generator.writeStringField("intro", template.getIntro());
        generator.writeStringField("discount",
                template.getDiscount().getDesc());
        generator.writeStringField("productLine",
                template.getProductLine().getDesc());
        generator.writeStringField("count", template.getCount().toString());
        generator.writeStringField("createTime",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(template.getCreateTime()));
        generator.writeStringField("userId", template.getUserId().toString());
        generator.writeStringField("sn",
                template.getKey() + String.format("%04d", template.getId()));
        generator.writeStringField("customer",
                template.getCustomer().getDesc());
        generator.writeStringField("rule",
                mapper.writeValueAsString(template.getRule()));

        // 结束序列化对象
        generator.writeEndObject();

    }
}
