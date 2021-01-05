package com.bw.coupon.dao;

import com.bw.coupon.Entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *  JpaRepository<X,Y>
 *     X：实体类型
 *     Y：主键类型
 */
public interface CouponTemplateDao
        extends JpaRepository<CouponTemplate, Integer> {
    /** 根据模板名称查询模板 */
    CouponTemplate findByDisplayName(String displayName);
    /** 根据available、expired查询模板 */
    List<CouponTemplate> findAllByIsAvailableAndIsExpired(Boolean isAvailable, Boolean isExpired);
    /** 根据expired查询模板 */
    List<CouponTemplate> findAllByIsExpired(Boolean expired);

    /** 储存模板信息 */
    CouponTemplate save(CouponTemplate template);
}
