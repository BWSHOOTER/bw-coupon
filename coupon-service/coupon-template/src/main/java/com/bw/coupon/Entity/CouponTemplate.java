package com.bw.coupon.Entity;

import com.bw.coupon.converter.*;
import com.bw.coupon.enumeration.CustomerTypeEnum;
import com.bw.coupon.enumeration.CalculatingMethodEnum;
import com.bw.coupon.enumeration.DistributionMethodEnum;
import com.bw.coupon.enumeration.GoodsCategoryEnum;
import com.bw.coupon.serialization.CouponTemplateSerialize;
import com.bw.coupon.vo.TemplateRuleVo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 优惠券模板实体：基础属性+规则属性
 * 注意：利用converter实现convert
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
// xx中表明是一个实体
@Entity
// 让hemernate自动生成一些属性值
@EntityListeners(AuditingEntityListener.class)
@Table(name="coupon_template")
@JsonSerialize(using = CouponTemplateSerialize.class)
public class CouponTemplate implements Serializable {
    /** 1.自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /** 2.是否生效 */
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    /** 3.是否过期 */
    @Column(name = "is_expired", nullable = false)
    private Boolean isExpired;

    /** 4.展示名称 */
    @Column(name = "display_name", nullable = false)
    private String displayName;

    /** 5.logo */
    @Column(name = "logo", nullable = true)
    private String logo;

    /** 6.描述 */
    @Column(name = "intro", nullable = false)
    private String intro;

    /** 7.折扣计算方式 */
    @Column(name = "calculating_method_code", nullable = false)
    @Convert(converter = CalculatingMethodEnumConverter.class)
    private CalculatingMethodEnum calculatingMethod;

    /** 8.适用商品类型 */
    @Column(name = "goods_category_codes", nullable = false)
    @Convert(converter = GoodsCategoryEnumConverter.class)
    private List<GoodsCategoryEnum> goodsCategories;

    /** 9.分发总数 */
    @Column(name = "distribution_amount", nullable = false)
    private Integer distributionAmount;

    /** 10.创建时间 */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    /** 11.创建用户Id */
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    /** 12.模板编号 */
    @Column(name = "sn", nullable = false)
    private String sn;

    /** 13.发放方式 */
    @Column(name = "distribution_method_code", nullable = false)
    @Convert(converter = DistributionMethodEnumConverter.class)
    private DistributionMethodEnum distributionMethod;

    /** 14.适用用户 */
    @Column(name = "customer_type_code", nullable = false)
    @Convert(converter = CustomerTypeEnumConverter.class)
    private CustomerTypeEnum customerType;

    /** 15.规则 */
    @Column(name = "rule", nullable = false)
    @Convert(converter = TemplateRuleConverter.class)
    private TemplateRuleVo rule;

    /**
     * 自定义构造函数
     * */
    public CouponTemplate(String displayName,
                          String logo,
                          String intro,
                          Integer calculating_method_code,
                          String goods_category_codes,
                          Integer distribution_amount,
                          Long creatorId,
                          Integer distribution_method_code,
                          Integer customer_type_code,
                          TemplateRuleVo rule) {
        this.isAvailable = false;
        this.isExpired = false;
        this.displayName = displayName;
        this.logo = logo;
        this.intro = intro;
        this.calculatingMethod = CalculatingMethodEnum.of(calculating_method_code);
        this.goodsCategories = GoodsCategoryEnum.of(goods_category_codes);
        this.distributionAmount = distribution_amount;
        this.creatorId = creatorId;
        this.distributionMethod = DistributionMethodEnum.of(distribution_method_code);
        this.customerType = CustomerTypeEnum.of(customer_type_code);
        this.rule = rule;

        // todo 优惠券模板唯一编码 = 折扣计算方式 + 8(日期: 19010115) + id(扩充为4位)
        this.sn = calculating_method_code +
                new SimpleDateFormat("yyyyMMdd").format(new Date()) + "0000";
    }
}
