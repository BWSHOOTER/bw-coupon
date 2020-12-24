package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 用户优惠券的状态
 */
@AllArgsConstructor
@Getter
public enum  CouponStatusEnum {
    USABLE("可用",1),
    USED("已使用",2),
    EXPIRED("已过期",3);

    private final String intro;
    private final Integer code;

    /** 根据 code 获取到 CouponStatusEnum */
    public static CouponStatusEnum of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(code + " not exists")
                );
    }
}
