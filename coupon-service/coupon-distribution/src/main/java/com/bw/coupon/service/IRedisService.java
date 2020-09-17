package com.bw.coupon.service;


import com.bw.coupon.entity.Coupon;
import com.bw.coupon.exception.CommonException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;


/**
 * Redis 相关的操作服务接口定义
 * 1. 用户的三个状态优惠券 Cache 相关操作
 * 2. 优惠券模板生成的优惠券码 Cache 操作
 */
public interface IRedisService {
    /**
     * 根据 userId 和状态找到缓存的优惠券列表数据
     * 注意, 可能会返回 null, 代表从没有过记录
     */
    List<Coupon> getCachedCoupons(Long userId, Integer status);

    /**
     * 保存空的优惠券列表到缓存中
     * 作用：避免缓存穿透（多次穿过缓存去库中查询不存在的值）
     */
    void saveEmptyCouponListToCache(Long userId, List<Integer> status) throws JsonProcessingException;

    /**
     * 根据 templateId 尝试从 Cache 中获取一个优惠券码
     * 可能会返回null
     */
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    /**
     * 将优惠券保存到 Cache 中
     * 返回：保存成功的个数
     */
    Integer addCouponToCache(Long userId,
                             List<Coupon> coupons,
                             Integer status)
            throws CommonException;
}
