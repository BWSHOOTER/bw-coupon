package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountEnum {
    MinusDiscount("001","立减折扣"),
    MultiplyDiscount("002","乘法折扣");

    //一般通过一个值返回枚举，都叫of
    public static DiscountEnum of(String code){
        if(code==null)
            throw new IllegalArgumentException("Enum DiscountCategory code is NULL!");
        else if(code.equals(MinusDiscount.code))
            return MinusDiscount;
        else if(code.equals(MultiplyDiscount.code))
            return MultiplyDiscount;
        else
            throw new IllegalArgumentException("Enum DiscountCategory code is NOT EXISTS!");
    }

    private String code;
    private String desc;



}
