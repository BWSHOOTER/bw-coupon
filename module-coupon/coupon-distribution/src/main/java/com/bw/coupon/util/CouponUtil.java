package com.bw.coupon.util;

import com.bw.coupon.entity.Coupon;
import com.bw.coupon.enumeration.CouponStatusEnum;

import java.util.*;

public class CouponUtil {
    public static Map<CouponStatusEnum, List<Coupon>> classifyCoupon(List<Coupon> coupons){
        Map<CouponStatusEnum, List<Coupon>> res = new HashMap<>();

        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());

        long curTime = new Date().getTime();
        for(Coupon coupon: coupons){
            // 判断优惠券是否过期
            boolean isTimeExpire = coupon.getTemplateVo().getRule().getExpirationRule()
                    .getDeadLine() <= curTime;
            /*
            if (c.getTemplateSDK().getRule().getExpiration().getType().
                    equals(ExpirationEnum.RegularExpiration.getCode())) {
                isTimeExpire = c.getTemplateSDK().getRule().getExpiration()
                        .getDeadLine() <= curTime;
            } else {
                isTimeExpire = DateUtils.addDays(
                        c.getAssignTime(),
                        c.getTemplateSDK().getRule().getExpiration().getGap()
                ).getTime() <= curTime;
            }*/
            if (coupon.getStatus() == CouponStatusEnum.USED) {
                used.add(coupon);
            }
            else if (coupon.getStatus() == CouponStatusEnum.EXPIRED || isTimeExpire) {
                expired.add(coupon);
            } else {
                usable.add(coupon);
            }
        }
        if(used.size()>0)
            res.put(CouponStatusEnum.USED, used);
        if(usable.size()>0)
            res.put(CouponStatusEnum.USABLE, usable);
        if(expired.size()>0)
            res.put(CouponStatusEnum.EXPIRED, expired);
        return res;
    }
}
