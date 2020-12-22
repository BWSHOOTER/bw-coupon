package com.bw.coupon.converter;

import com.bw.coupon.enumeration.GoodsCategoryEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 产品线枚举的转换器
 * AttributeConverter<X,Y>
 *     X：实体属性的类型
 *     Y：数据库字段类型
 */

@Converter
public class ProductLineEnumConverter implements AttributeConverter<GoodsCategoryEnum, Integer> {
    /** 将X转换为Y存储到数据库中（插入和更新时执行的动作） */
    @Override
    public Integer convertToDatabaseColumn(GoodsCategoryEnum goodsCategoryEnum) {
        return goodsCategoryEnum.getCode();
    }

    /** 将Y转换为X（查询操作时执行的动作） */
    @Override
    public GoodsCategoryEnum convertToEntityAttribute(Integer integer) {
        return GoodsCategoryEnum.of(integer);
    }
}
