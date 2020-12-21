package com.bw.coupon.executor;

import com.bw.coupon.enumeration.RuleFlagEnum;
import com.bw.coupon.vo.SettlementInfo;
import com.bw.coupon.vo.TemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class MinusDiscountRuleExecutor extends AbstractRuleExecutor{

    /**
     * 规则类型定义
     */
    @Override
    public RuleFlagEnum ruleConfig() {
        return RuleFlagEnum.MinusDiscount;
    }

    /**
     * @param settlementInfo
     * @Description: 优惠券规则计算
     * @Author: BaoWei
     * @Date: 2020/12/21 16:57
     * 入参包含了用户选择的优惠券
     * 返回修正过的计算信息
     */
    @Override
    public SettlementInfo computeRule(SettlementInfo settlementInfo) {
        double totalCost = calGoodsTotalCost(settlementInfo.getGoodsInfos());
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlementInfo, totalCost);
        if(null != probability){
            log.debug("MinusDiscount Template is not match to goodsType!");
            return probability;
        }

        // 判断满减是否符合折扣标准
        TemplateSDK sdk = settlementInfo.getCouponAndTemplateInfos().get(0).getTemplate();
        double base = sdk.getRule().getDiscount().getBase();
        double quota = sdk.getRule().getDiscount().getQuota();

        // 如果不符合标准，直接返回总价
        if(totalCost<base){
            log.debug("[TotalCost] {} < [base] {}!", totalCost, base);
            settlementInfo.setCost(totalCost);
            settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
            return settlementInfo;
        }

        // 计算使用优惠券之后的价格
        settlementInfo.setCost(totalCost - quota > 0? totalCost - quota : 0);
        log.debug("Using MinusDiscount Coupon Make Cost From {} to {}", totalCost, settlementInfo.getCost());

        return settlementInfo;
    }
}
