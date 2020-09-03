package com.bw.coupon.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DistributeEnum {
    SingleDistribute("001","单用户领取"),
    MultiDistribute("002","多用户群发");

    private String code;
    private String desc;
}
