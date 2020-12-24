package com.bw.coupon.vo;

import com.bw.coupon.entity.Coupon;
import com.bw.coupon.enumeration.CouponStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户优惠券的分类, 根据优惠券状态
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponClassify {
    /** 可以使用的 */
    private List<Coupon> usable;
    /** 已使用的 */
    private List<Coupon> used;
    /** 已过期的 */
    private List<Coupon> expired;

    /** 对当前的优惠券进行分类 */
    public CouponClassify(List<Coupon> coupons) {
        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());

        coupons.forEach(c -> {
            // 判断优惠券是否过期
            boolean isTimeExpire;
            long curTime = new Date().getTime();
            isTimeExpire = c.getTemplateVo().getRule().getExpirationRule().getDeadLine() <= curTime;
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
            if (c.getStatus() == CouponStatusEnum.USED) {
                used.add(c);
            } else if (c.getStatus() == CouponStatusEnum.EXPIRED || isTimeExpire) {
                expired.add(c);
            } else {
                usable.add(c);
            }
        });
        this.expired = expired;
        this.usable = usable;
        this.used = used;
    }
}
