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
    MinusDiscount("减法折扣"),
    MultiplyDiscount("乘法折扣");

    // 多类别优惠券定义

    // todo 更多组合

    private String desc;


}
