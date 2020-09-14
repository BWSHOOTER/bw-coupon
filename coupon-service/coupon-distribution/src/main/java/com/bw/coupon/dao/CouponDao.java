package com.bw.coupon.dao;

import com.bw.coupon.entity.Coupon;
import com.bw.coupon.enumeration.CouponStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JpaRepository<X, Y>
 *     X：实体类
 *     Y：主键类型
 **/
public interface CouponDao extends JpaRepository<Coupon, Integer> {

    /**
     * 根据 userId + 状态寻找优惠券记录
     * */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatusEnum status);
}
