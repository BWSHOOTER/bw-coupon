package com.bw.coupon.vo;

import com.bw.coupon.enumeration.DistributeEnum;
import com.bw.coupon.enumeration.ExpirationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * 优惠券规则对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRuleVo {

    private ExpirationRuleTemplate expiration;
    private DiscountRuleTemplate discount;
    private CustomerRuleTemplate customer;
    private DistributeRuleTemplate distribute;
    private Integer limitation;

    // todo
    public boolean validate(){
        return true;
    }

    /**
     * 过期规则
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpirationRuleTemplate{
        // 对应ExpirationEnum的code
        private Integer type;
        private Long gap;
        private Long deadLine;

        public ExpirationRuleTemplate(Integer type,Long mills){
            this.type = type;
            if(type == ExpirationEnum.RegularExpiration.getCode()){
                this.deadLine = mills;
            }
            else if(type == ExpirationEnum.ShiftExpiration.getCode()){
                Long dll = new Date().getTime() + mills;
                Date dl = new Date(dll);
                this.deadLine = DateUtils.addMilliseconds(dl,1).getTime();
            }
        }
    }

    /**
     * 折扣规则
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscountRuleTemplate{
        // 对应DiscountEnum的code
        private Integer type;
        private Integer quota;
        private Integer base;
    }

    /**
     * 面向用户规则
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerRuleTemplate{
        // 对应CustomerEnum的code
        private Integer type;
    }

    /**
     * 发放规则
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DistributeRuleTemplate{
        // 对应DistributeEnum的code
        private Integer type;

    }
}
