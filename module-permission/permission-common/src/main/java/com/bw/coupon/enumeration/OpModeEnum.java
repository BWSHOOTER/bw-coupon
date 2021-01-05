package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum OpModeEnum {
    Read("读"),
    Write("写");

    private final String des;
}
