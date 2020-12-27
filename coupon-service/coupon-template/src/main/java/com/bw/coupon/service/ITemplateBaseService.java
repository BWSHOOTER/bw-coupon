package com.bw.coupon.service;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.vo.TemplateVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板基础(view, delete...)服务定义
 */
public interface ITemplateBaseService {

    /**
     * 根据优惠券模板 id 获取优惠券模板信息
     */
    CouponTemplate findTemplateById(Integer id) throws CommonException;

    /**
     * 查找所有可用的优惠券模板
     */
    List<TemplateVo> findAllUsableTemplateVos();

    /**
     * 获取模板 ids 到 CouponTemplateVos 的映射
     * ids 模板 ids
     * Map<sn: 模板 id， value: TemplateVo>
     */
    Map<Integer, TemplateVo> findIds2TemplateVos(Collection<Integer> ids);
}
