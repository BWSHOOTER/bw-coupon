package com.bw.coupon.service;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.exception.CommonException;
import com.bw.coupon.vo.TemplateRequestVo;


/**
 * 构建优惠券模板服务接口
 **/
public interface IBuildTemplateService {
    /** 创建优惠券模板 */
    CouponTemplate buildCouponTemplate(TemplateRequestVo request)
        throws CommonException;
}
