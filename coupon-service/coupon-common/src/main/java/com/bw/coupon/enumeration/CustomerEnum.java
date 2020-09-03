package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerEnum {
    NewCustomer("001","新用户"),
    OldCustomer("002","老用户");

    private String code;
    private String desc;
}
