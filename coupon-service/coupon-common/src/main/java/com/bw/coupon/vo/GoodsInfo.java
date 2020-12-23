package com.bw.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * fake 商品信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfo {
    /** 商品类别 */
    private Integer CategoryCode;

    /** 商品价格 */
    private Double price;

    /** 商品数量 */
    private Integer count;
}
