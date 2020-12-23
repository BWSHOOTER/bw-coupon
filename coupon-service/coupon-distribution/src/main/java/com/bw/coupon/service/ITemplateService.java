package com.bw.coupon.service;

import com.bw.coupon.vo.CommonException;
import com.bw.coupon.vo.TemplateVo;

import java.util.List;

public interface ITemplateService {
    /**
     * 根据用户 id 查找当前可以领取的优惠券模板
     */
    List<TemplateVo> findAvailableTemplateByUserId(Long userId)
            throws CommonException;
}
