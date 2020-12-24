package com.bw.coupon.vo;

import com.bw.coupon.Entity.CouponTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 优惠券模板创建请求对象
 * VO：Value Object 通常用于service层之间传递对象
 *
 * 注意：比起CouponTemplate实体，少了
 * 是否可用、是否过期
 * 自增主键、模板编码、创建时间
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateCreatingRequestVo {
    private TemplateRuleVo rule;
    private Integer calculatingMethodCode;
    private String goodsCategoryCode;

    private Integer customerTypeCode;
    private Integer distributionMethodCode;
    private Integer distributionAmount;

    private String displayName;
    private String logo;
    private String intro;

    private Long creatorId;

    /** 对象合法性校验 */
    public boolean validate(){
        boolean StringValidate = StringUtils.isNoneEmpty(displayName)
                && StringUtils.isNotEmpty(intro);
        boolean enumValidate = true;
        boolean countValidate = distributionAmount > 0;
        boolean ruleVoValidate = rule.validate();
        return StringValidate && enumValidate && countValidate && ruleVoValidate;
    }

    /** 将 TemplateCreatingRequestVo 转换为 CouponTemplate实体 */
    public CouponTemplate toEntity(){
        return new CouponTemplate(
                this.getDisplayName(),
                this.getLogo(),
                this.getIntro(),
                this.getCalculatingMethodCode(),
                this.getGoodsCategoryCode(),
                this.getDistributionAmount(),
                this.getCreatorId(),
                this.getDistributionMethodCode(),
                this.getCustomerTypeCode(),
                this.getRule()
        );
    }
}
