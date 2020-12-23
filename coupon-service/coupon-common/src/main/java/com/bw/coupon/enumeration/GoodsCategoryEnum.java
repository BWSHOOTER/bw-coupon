package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 枚举：产品线类别
 */
@Getter
@AllArgsConstructor
public enum GoodsCategoryEnum {
    ElectronicProduct(941,"电脑电子"),
    SkinProduct(942,"美妆护肤"),
    ClothingProduct(943,"服装"),
    LivingProduct(944,"生活家居"),
    ApplianceProduct(945,"电器"),
    EatingProduct(946,"食品");

    private Integer code;
    private String desc;

    public static GoodsCategoryEnum of(Integer code){
        if(code==null)
            throw new IllegalArgumentException("Enum ProductLineEnum code is NULL!");

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Enum ProductLineEnum code " + code + " is NOT EXISTS!"));
    }

    /** 将多个code组成的String，还原成枚举的列表 */
    public static List<GoodsCategoryEnum> of(String codesStr){
        String[] codes = codesStr.split(",");
        List<GoodsCategoryEnum> list = new ArrayList<>(codes.length);
        for(String code: codes){
            list.add(GoodsCategoryEnum.of(Integer.parseInt(code)));
        }
        return list;
    }

    /** 将枚举列表转换成多个code组成的String */
    public static String listToCodesStr(List<GoodsCategoryEnum> list) {
        String[] codes = new String[list.size()];
        for(int i = 0; i<list.size(); i++){
            codes[i] = list.get(i).getCode().toString();
        }
        return String.join(",",codes);
    }
    /** 从枚举的列表提取出code的列表 */
    public static List<Integer> getCodesByEnums(List<GoodsCategoryEnum> enums){
        List<Integer> codes = new ArrayList<>(enums.size());
        for(GoodsCategoryEnum goodsCategoryEnum: enums){
            codes.add(goodsCategoryEnum.getCode());
        }
        return codes;
    }
}
