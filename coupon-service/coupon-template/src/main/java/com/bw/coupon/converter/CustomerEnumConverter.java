package com.bw.coupon.converter;

import com.bw.coupon.enumeration.CustomerEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 适用用户类型枚举的转换器
 **/
@Converter
public class CustomerEnumConverter  implements AttributeConverter<CustomerEnum, Integer> {
    /** 将X转换为Y存储到数据库中（插入和更新时执行的动作） */
    @Override
    public Integer convertToDatabaseColumn(CustomerEnum customerEnum) {
        return customerEnum.getCode();
    }

    /** 将Y转换为X（查询操作时执行的动作） */
    @Override
    public CustomerEnum convertToEntityAttribute(Integer integer) {
        return CustomerEnum.of(integer);
    }
}
