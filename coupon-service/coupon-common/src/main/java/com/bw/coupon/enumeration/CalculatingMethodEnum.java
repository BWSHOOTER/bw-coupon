package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 枚举：折扣规则类型
 */
@Getter
@AllArgsConstructor
public enum CalculatingMethodEnum {
    MinusDiscount(901,"减法折扣"),
    MultiplyDiscount(902,"乘法折扣");

    //一般通过一个值返回枚举，都叫of
    public static CalculatingMethodEnum of(Integer code){
        /*
        //如果为空，则抛出一个异常
        //尽早抛出 fail-fast思想
        Objects.requireNonNull(code);
         */

        if(code==null)
            throw new IllegalArgumentException("Enum DiscountEnum code is NULL!");
        /*
        else if(code.equals(MinusDiscount.code))
            return MinusDiscount;
        else if(code.equals(MultiplyDiscount.code))
            return MultiplyDiscount;
        else
            throw new IllegalArgumentException("Enum DiscountEnum code is NOT EXISTS!");
         */
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Enum DiscountEnum code " + code + " is NOT EXISTS!"));
    }

    private Integer code;
    private String desc;



}
