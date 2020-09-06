package com.bw.coupon.converter;

import com.bw.coupon.enumeration.DiscountEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DiscountEnumConverter implements AttributeConverter<DiscountEnum,Integer> {
    @Override
    public Integer convertToDatabaseColumn(DiscountEnum discountEnum) {
        return discountEnum.getCode();
    }

    @Override
    public DiscountEnum convertToEntityAttribute(Integer integer) {
        return DiscountEnum.of(integer);
    }
}
