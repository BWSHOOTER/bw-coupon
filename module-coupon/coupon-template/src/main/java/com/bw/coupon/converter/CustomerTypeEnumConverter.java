package com.bw.coupon.converter;

import com.bw.coupon.enumeration.CustomerTypeEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 适用用户类型枚举的转换器
 */
@Converter
public class CustomerTypeEnumConverter implements AttributeConverter<CustomerTypeEnum, Integer> {
    /** 将X转换为Y存储到数据库中（插入和更新时执行的动作） */
    @Override
    public Integer convertToDatabaseColumn(CustomerTypeEnum customerTypeEnum) {
        return customerTypeEnum.getCode();
    }

    /** 将Y转换为X（查询操作时执行的动作） */
    @Override
    public CustomerTypeEnum convertToEntityAttribute(Integer integer) {
        return CustomerTypeEnum.of(integer);
    }
}
