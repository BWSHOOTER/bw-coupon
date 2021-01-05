package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 枚举：折扣计算方法
 */
@Getter
@AllArgsConstructor
public enum CalculatingMethodEnum {
    MinusCalculate(901,"减法计算折扣"),
    MultiplyCalculate(902,"乘法计算折扣");

    private final Integer code;
    private final String desc;

    /** 一般通过一个值返回枚举，都叫of */
    public static CalculatingMethodEnum of(Integer code){
        // Objects.requireNonNull(code);
        if(code==null)
            throw new IllegalArgumentException("Enum DiscountEnum code is NULL!");
        else if(code.equals(MinusCalculate.code))
            return MinusCalculate;
        else if(code.equals(MultiplyCalculate.code))
            return MultiplyCalculate;
        else
            throw new IllegalArgumentException("Enum CalculatingMethodEnum code " +
                                               code +
                                               " is NOT EXISTS!");
    }
}
