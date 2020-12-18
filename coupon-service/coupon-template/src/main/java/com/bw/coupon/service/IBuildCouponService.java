package com.bw.coupon.service;

import com.bw.coupon.Entity.CouponTemplate;

public interface IBuildCouponService {
    /** 根据模板异步创建优惠券码 */
    void asyncConstructCouponByTemplate(CouponTemplate template);
}
