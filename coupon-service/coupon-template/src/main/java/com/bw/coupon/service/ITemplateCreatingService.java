package com.bw.coupon.service;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.vo.TemplateCreatingRequestVo;


/**
 * 生成券模板服务接口
 */
public interface ITemplateCreatingService {
    /**
     * @Description: 根据生成优惠券模板的请求，生成优惠券模板，并启动异步生成优惠券的任务
     * @Author: BaoWei
     * @Date: 2020/12/15 15:34
     */
    CouponTemplate createCouponTemplate(TemplateCreatingRequestVo request)
        throws CommonException;
}
