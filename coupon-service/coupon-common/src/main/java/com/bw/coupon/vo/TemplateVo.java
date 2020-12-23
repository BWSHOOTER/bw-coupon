package com.bw.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 微服务之间用的优惠券模板信息定义
 *
 * 比起实体，少了是否生效、是否过期、分发总数、创建时间、创建人员id、发放方式
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateVo {
    /** 优惠券模板主键 */
    private Integer id;

    /** 展示名称 */
    private String displayName;

    /** 优惠券 logo */
    private String logo;

    /** 优惠券描述 */
    private String intro;

    /** 折扣计算方式 */
    private Integer calculatingMethodCode;

    /** 适用商品类型编码列表 */
    private List<Integer> goodsCategoryCodes;

    /** 优惠券模板编号 */
    private String sn;

    /** 适用用户 */
    private Integer customerTypeCode;

    /** 优惠券规则 */
    private TemplateRuleVo rule;
}
