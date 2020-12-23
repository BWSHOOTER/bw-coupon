package com.bw.coupon.converter;

import com.bw.coupon.enumeration.DistributionMethodEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DistributionMethodEnumConverter implements AttributeConverter<DistributionMethodEnum,Integer> {
    @Override
    public Integer convertToDatabaseColumn(DistributionMethodEnum distributionMethodEnum) {
        return distributionMethodEnum.getCode();
    }

    @Override
    public DistributionMethodEnum convertToEntityAttribute(Integer integer) {
        return DistributionMethodEnum.of(integer);
    }
}
