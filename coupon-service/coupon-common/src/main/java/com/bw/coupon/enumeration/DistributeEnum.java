package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 枚举：发放方式
 **/
@Getter
@AllArgsConstructor
public enum DistributeEnum {
    SingleDistribute(911,"单用户领取"),
    MultiDistribute(912,"多用户群发");

    private Integer code;
    private String desc;

    public static DistributeEnum of(Integer code){
        if(code==null)
            throw new IllegalArgumentException("Enum DistributeEnum code is NULL!");

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Enum DistributeEnum code " + code + " is NOT EXISTS!"));
    }
}