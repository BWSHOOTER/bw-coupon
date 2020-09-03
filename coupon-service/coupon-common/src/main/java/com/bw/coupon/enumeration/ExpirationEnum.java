package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ExpirationEnum {
    RegularExpiration(931,"固定"),
    ShiftExpiration(932,"变动");

    private Integer code;
    private String desc;

    public static ExpirationEnum of(Integer code){
        if(code==null)
            throw new IllegalArgumentException("Enum ExpirationEnum code is NULL!");

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Enum ExpirationEnum code " + code + " is NOT EXISTS!"));
    }
}
