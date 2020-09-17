package com.bw.coupon.Entity;

import com.bw.coupon.converter.*;
import com.bw.coupon.enumeration.CustomerEnum;
import com.bw.coupon.enumeration.DiscountEnum;
import com.bw.coupon.enumeration.DistributeEnum;
import com.bw.coupon.enumeration.ProductLineEnum;
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

/**
 * 优惠券模板实体：基础属性+规则属性
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
    private Boolean available;

    /** 3.是否过期 */
    @Column(name = "expired", nullable = false)
    private Boolean expired;

    /** 4.名称 */
    @Column(name = "coupon_name", nullable = false)
    private String name;

    /** 5.logo */
    @Column(name = "logo", nullable = true)
    private String logo;

    /** 6.描述 */
    @Column(name = "intro", nullable = false)
    private String intro;

    /** 7.折扣方式 */
    @Column(name = "discount", nullable = false)
    //利用converter实现convert
    @Convert(converter = DiscountEnumConverter.class)
    private DiscountEnum discount;

    /** 8.产品线 */
    @Column(name = "product_line", nullable = false)
    @Convert(converter = ProductLineEnumConverter.class)
    private ProductLineEnum productLine;

    /** 9.总数 */
    @Column(name = "coupon_count", nullable = false)
    private Integer count;

    /** 10.创建时间 */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    /** 11.创建用户 */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 12.编码 */
    @Column(name = "sn", nullable = false)
    private String key;

    /** 13.发放方式 */
    @Column(name = "distribute", nullable = false)
    @Convert(converter = DistributeEnumConverter.class)
    private DistributeEnum distribute;

    /** 14.适用用户 */
    @Column(name = "customer", nullable = false)
    @Convert(converter = CustomerEnumConverter.class)
    private CustomerEnum customer;

    /** 15.规则 */
    @Column(name = "rule", nullable = false)
    @Convert(converter = CouponRuleTemplateConverter.class)
    private TemplateRuleVo rule;

    /**
     * 自定义构造函数
     * */
    public CouponTemplate(String name,
                          String logo,
                          String intro,
                          Integer discount,
                          Integer productLine, Integer count, Long userId,
                          Integer distribute,
                          Integer customer, TemplateRuleVo rule) {

        this.available = false;
        this.expired = false;
        this.name = name;
        this.logo = logo;
        this.intro = intro;
        this.discount = DiscountEnum.of(discount);
        this.productLine = ProductLineEnum.of(productLine);
        this.count = count;
        this.userId = userId;
        this.distribute = DistributeEnum.of(distribute);
        // 优惠券模板唯一编码 = 4(产品线和类型) + 8(日期: 20190101) + id(扩充为4位)
        this.key = productLine.toString() + discount +
                new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.customer = CustomerEnum.of(customer);
        this.rule = rule;
    }
}
