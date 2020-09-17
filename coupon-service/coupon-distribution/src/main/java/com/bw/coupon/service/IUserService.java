package com.bw.coupon.service;

import com.bw.coupon.entity.Coupon;
import com.bw.coupon.exception.CommonException;
import com.bw.coupon.vo.AcquireTemplateRequest;
import com.bw.coupon.vo.TemplateSDK;
import com.bw.coupon.vo.SettlementInfo;

import java.util.List;

/**
 * 用户服务相关的接口定义
 * 1. 用户三类状态优惠券信息展示服务
 * 2. 查看用户当前可以领取的优惠券模板 - coupon-template 微服务配合实现
 * 3. 用户领取优惠券服务
 * 4. 用户消费优惠券服务 - coupon-settlement 微服务配合实现
 */

public interface IUserService {
    /**
     * 根据用户 id 和状态查询优惠券记录
     */
    List<Coupon> findCouponsByStatus(Long userId, Integer status)
            throws CommonException;

    /**
     * 根据用户 id 查找当前可以领取的优惠券模板
     */
    List<TemplateSDK> findAvailableTemplate(Long userId)
            throws CommonException;

    /**
     * 用户领取优惠券
     */
    Coupon acquireTemplate(AcquireTemplateRequest request)
            throws CommonException;

    /**
     * 结算(核销)优惠券
     * 入参中没有最终cost，出参中有
     */
    SettlementInfo settlement(SettlementInfo info) throws CommonException;
}
