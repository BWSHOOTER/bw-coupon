package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 枚举：产品线类别
 **/
@Getter
@AllArgsConstructor
public enum ProductLineEnum {
    ElectronicProduct(941,"电脑电子"),
    SkinProduct(942,"美妆护肤"),
    ClothingProduct(943,"服装"),
    LivingProduct(944,"生活家居"),
    ApplianceProduct(945,"电器"),
    EatingProduct(946,"食品");

    private Integer code;
    private String desc;

    public static ProductLineEnum of(Integer code){
        if(code==null)
            throw new IllegalArgumentException("Enum ProductLineEnum code is NULL!");

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Enum ProductLineEnum code " + code + " is NOT EXISTS!"));
    }

}
