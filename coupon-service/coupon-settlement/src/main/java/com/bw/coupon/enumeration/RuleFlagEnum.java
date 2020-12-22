package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则类型枚举定义
 */
@Getter
@AllArgsConstructor
public enum RuleFlagEnum {
    // 单类别优惠券定义
    MinusDiscount("减法优惠"),
    MultiplyDiscount("乘法优惠"),

    // 多类别优惠券定义
    MixDiscount("先减再乘优惠");

    // todo 更多组合

    private String desc;
}
