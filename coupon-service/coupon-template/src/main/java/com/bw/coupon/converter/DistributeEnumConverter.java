package com.bw.coupon.converter;

import com.bw.coupon.enumeration.DiscountEnum;
import com.bw.coupon.enumeration.DistributeEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DistributeEnumConverter implements AttributeConverter<DistributeEnum,Integer> {
    @Override
    public Integer convertToDatabaseColumn(DistributeEnum distributeEnum) {
        return distributeEnum.getCode();
    }

    @Override
    public DistributeEnum convertToEntityAttribute(Integer integer) {
        return DistributeEnum.of(integer);
    }
}
