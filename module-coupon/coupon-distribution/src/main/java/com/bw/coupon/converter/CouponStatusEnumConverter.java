package com.bw.coupon.converter;

import com.bw.coupon.enumeration.CouponStatusEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CouponStatusEnumConverter implements AttributeConverter<CouponStatusEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CouponStatusEnum couponStatusEnum) {
        return couponStatusEnum.getCode();
    }

    @Override
    public CouponStatusEnum convertToEntityAttribute(Integer code) {
        return CouponStatusEnum.of(code);
    }
}
