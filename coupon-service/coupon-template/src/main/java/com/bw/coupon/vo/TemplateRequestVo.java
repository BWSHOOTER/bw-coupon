package com.bw.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 优惠券模板创建请求对象
 * VO：Value Object 通常用于service层之间传递对象
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequestVo {
    private String name;
    private String logo;
    private String intro;
    private Integer productLine;
    private Integer count;
    private Integer discount;
    private Integer distribute;
    private Long userId;
    private Integer customer;
    private CouponTemplateRuleVo rule;

    /** 对象合法性校验 */
    public boolean validate(){
        boolean StringValidate = StringUtils.isNoneEmpty(name)
                && StringUtils.isNotEmpty(intro);
        boolean enumValidate = true;
        boolean countValidate = count > 0;
        boolean ruleVoValidate = rule.validate();
        return StringValidate && enumValidate && countValidate && ruleVoValidate;
    }
}
