package com.bw.coupon.dao;

import com.bw.coupon.Entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *  JpaRepository<X,Y>
 *     X：实体类型
 *     Y：主键类型
 **/
public interface CouponTemplateDao
        extends JpaRepository<CouponTemplate, Integer> {
    /** 根据模板名称查询模板 */
    CouponTemplate findByName(String name);
    /** 根据available、expired查询模板 */
    List<CouponTemplate> findAllByAvailableAndExpired(Boolean available, Boolean expired);
    /** 根据expired查询模板 */
    List<CouponTemplate> findAllByExpired(Boolean expired);
    /** 根据expired查询模板 */
    //int saveAll(Boolean expired);
}
