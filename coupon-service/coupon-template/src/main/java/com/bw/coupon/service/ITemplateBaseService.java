package com.bw.coupon.service;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.exception.CommonException;
import com.bw.coupon.vo.CouponTemplateSDK;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板基础(view, delete...)服务定义
 **/
public interface ITemplateBaseService {

    /**
     * 根据优惠券模板 id 获取优惠券模板信息
     * */
    CouponTemplate buildTemplateInfo(Integer id) throws CommonException;

    /**
     * 查找所有可用的优惠券模板
     **/
    List<CouponTemplateSDK> findAllUsableTemplate();

    /**
     * 获取模板 ids 到 CouponTemplateSDK 的映射
     * ids 模板 ids
     * Map<key: 模板 id， value: CouponTemplateSDK>
     * */
    Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids);


}
