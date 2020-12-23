package com.bw.coupon.vo;

import com.bw.coupon.enumeration.ExpirationTypeEnum;
import com.bw.coupon.enumeration.GoodsCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * 优惠券规则对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRuleVo {

    private ExpirationRuleVo expirationRule;
    private CalculatingRuleVo calculatingRule;
    //private Integer limitation;
    //private CustomerRuleTemplate customerType;
    //private DistributeRuleTemplate distribute;
    //private List<GoodsCategoryRuleVo> goodsCategories;

    // todo
    public boolean validate(){
        return true;
    }

    /**
     * 折扣计算规则
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalculatingRuleVo{
        // 对应CalculatingMethodEnum的code
        private Integer type;
        private Integer quota;
        private Integer base;
    }

    /**
     * 过期规则
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpirationRuleVo{
        // 对应ExpirationTypeEnum的code
        private Integer type;
        private Long gap;
        private Long deadLine;

        public ExpirationRuleVo(Integer type,Long mills){
            this.type = type;
            if(type == ExpirationTypeEnum.RegularExpiration.getCode()){
                this.deadLine = mills;
            }
            else if(type == ExpirationTypeEnum.ShiftExpiration.getCode()){
                Long dll = new Date().getTime() + mills;
                Date dl = new Date(dll);
                this.deadLine = DateUtils.addMilliseconds(dl,1).getTime();
            }
        }
    }


    /**
     * 发放规则
     */
    /*
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DistributionMethodRuleVo{
        // 对应DistributionMethodEnum的code
        private Integer type;
    }
     */

    /**
     * 适用商品品类
     */
    /*
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsCategoryRuleVo{
        private Integer code;
        private String desc;
        public GoodsCategoryRuleVo(GoodsCategoryEnum goodsCategoryEnum){
            this.code = goodsCategoryEnum.getCode();
            this.desc = goodsCategoryEnum.getDesc();
        }
    }
     */

    /**
     * 面向用户规则
     */
    /*
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerTypeRuleVo{
        // 对应CustomerEnum的code
        private Integer type;
    }
     */
}
