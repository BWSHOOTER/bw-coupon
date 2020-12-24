package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 枚举：可用用户类型
 */
@Getter
@AllArgsConstructor
public enum CustomerTypeEnum {
    AllCustomer(920,"所有用户"),
    NewCustomer(921,"老用户"),
    OldCustomer(922,"新用户");

    private final Integer code;
    private final String desc;

    public static CustomerTypeEnum of(Integer code){
        if(code==null)
            throw new IllegalArgumentException("Enum CustomerEnum code is NULL!");

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Enum CustomerEnum code " + code + " is NOT EXISTS!"));
    }
}
