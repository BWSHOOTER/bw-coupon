package com.bw.coupon.entity;

import com.bw.coupon.converter.CouponStatusEnumConverter;
import com.bw.coupon.enumeration.CouponStatusEnum;
import com.bw.coupon.serialization.CouponSerialize;
import com.bw.coupon.vo.TemplateVo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon")
@JsonSerialize(using = CouponSerialize.class)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /** 关联优惠券模板的主键(逻辑外键) */
    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    /** 领取用户 */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    /** 优惠券码 */
    @Column(name = "sn", nullable = false)
    private String sn;

    /** 领取时间 */
    @CreatedDate
    @Column(name = "assign_time", nullable = false)
    private Date assignTime;

    /** 优惠券状态 */
    // @Basic //表示为这张表的某个列，是默认行为，与@Transient相对
    @Column(name = "status", nullable = false)
    @Convert(converter = CouponStatusEnumConverter.class)
    private CouponStatusEnum status;

    /** 用户优惠券对应的模板信息 */
    @Transient  // 表示不为当前表的某个列，但是需要定义在这里，方便操作
    private TemplateVo templateVo;

    /** 返回一个无效的 Coupon 对象 */
    public static Coupon invalidCoupon(){
        Coupon coupon = new Coupon();
        coupon.setId(-1);
        return coupon;
    }

    /** 自定义构造器 */
    public Coupon(Integer templateId,
                  Long customerId,
                  String sn,
                  CouponStatusEnum status) {
        this.templateId = templateId;
        this.customerId = customerId;
        this.sn = sn;
        this.status = status;
    }
}
