package com.bw.coupon.converter;

import com.bw.coupon.enumeration.CalculatingMethodEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CalculatingMethodEnumConverter implements AttributeConverter<CalculatingMethodEnum,Integer> {
    @Override
    public Integer convertToDatabaseColumn(CalculatingMethodEnum calculatingMethodEnum) {
        return calculatingMethodEnum.getCode();
    }

    @Override
    public CalculatingMethodEnum convertToEntityAttribute(Integer integer) {
        return CalculatingMethodEnum.of(integer);
    }
}
