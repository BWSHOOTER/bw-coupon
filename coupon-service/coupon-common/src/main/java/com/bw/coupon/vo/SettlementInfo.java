package com.bw.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 结算信息对象定义
 * 包含:
 *  1. userId
 *  2. 商品信息(列表)
 *  3. 优惠券列表
 *  4. 结算结果金额
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementInfo {
    /** 1.用户 id */
    private Long customerId;

    /** 2.商品列表 */
    private List<GoodsInfo> goodsInfos;

    /** 3.是结算还是核销。 true：核销  false：结算 */
    private Boolean employ;

    /** 优惠券与其模板信息CouponAndTemplateInfo列表 */
    private List<CouponAndTemplateInfo> couponAndTemplateInfos;

    /** 5. 结果结算金额 */
    private Double cost;

    /**
     * 优惠券Id与其模板信息打包的对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponAndTemplateInfo {
        /** Coupon 的主键 */
        private Integer couponId;
        /** 优惠券对应的模板对象 */
        private TemplateVo templateVo;
    }
}

