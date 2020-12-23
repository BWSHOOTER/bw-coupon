package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 枚举：有效期规则类型
 */
@Getter
@AllArgsConstructor
public enum ExpirationTypeEnum {
    RegularExpiration(931,"固定"),
    ShiftExpiration(932,"变动");

    private Integer code;
    private String desc;

    public static ExpirationTypeEnum of(Integer code){
        if(code==null)
            throw new IllegalArgumentException("Enum ExpirationEnum code is NULL!");

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Enum ExpirationEnum code " + code + " is NOT EXISTS!"));
    }
}
