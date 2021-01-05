package com.bw.coupon.serialization;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.enumeration.GoodsCategoryEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 优惠券模板自定义序列化器
 */
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
        generator.writeStringField("displayName", template.getDisplayName());
        generator.writeStringField("logo", template.getLogo());
        generator.writeStringField("intro", template.getIntro());
        generator.writeStringField("calculatingMethod",
                template.getCalculatingMethod().getDesc());
        generator.writeStringField("goodsCategory",
                GoodsCategoryEnum.listToCodesStr(template.getGoodsCategories()));
        generator.writeStringField("distributionAmount",
                template.getDistributionAmount().toString());
        generator.writeStringField("createTime",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(template.getCreateTime()));
        generator.writeStringField("creatorId", template.getCreatorId().toString());
        generator.writeStringField("sn",
                template.getSn() + String.format("%04d", template.getId()));
        generator.writeStringField("customerType",
                template.getCustomerType().getDesc());
        generator.writeStringField("rule",
                mapper.writeValueAsString(template.getRule()));

        // 结束序列化对象
        generator.writeEndObject();

    }
}
