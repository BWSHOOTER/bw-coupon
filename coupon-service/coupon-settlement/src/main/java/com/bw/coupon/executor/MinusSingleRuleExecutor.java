package com.bw.coupon.executor;

import com.bw.coupon.enumeration.RuleUnionEnum;
import com.bw.coupon.util.PriceUtil;
import com.bw.coupon.vo.SettlementInfo;
import com.bw.coupon.vo.TemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class MinusSingleRuleExecutor extends AbstractRuleExecutor{

    /**
     * 规则类型定义
     */
    @Override
    public RuleUnionEnum ruleConfig() {
        return RuleUnionEnum.MinusSingle;
    }

    /**
     * @Description: 优惠券规则计算
     * @Author: BaoWei
     * @Date: 2020/12/21 16:57
     * 入参包含了用户选择的优惠券
     * 返回修正过的计算信息
     */
    @Override
    public SettlementInfo computeRule(SettlementInfo settlementInfo) {
        if(!isGoodsTypeSatisfy(settlementInfo)){
            log.debug("MinusDiscount Template is not match to goodsType!");
            return processGoodsTypeNotSatisfy(settlementInfo);
        }

        double totalCost = PriceUtil.calGoodsTotalCost(settlementInfo.getGoodsInfos());

        // 判断满减是否符合折扣标准
        TemplateVo template = settlementInfo.getCouponAndTemplateInfos().get(0).getTemplateVo();
        double base = template.getRule().getCalculatingRule().getBase();
        double quota = template.getRule().getCalculatingRule().getQuota();

        // 如果不符合最低金额，直接返回总价
        if(totalCost<base){
            log.debug("[TotalCost] {} < [base] {}!", totalCost, base);
            settlementInfo.setCost(totalCost);
            settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
            return settlementInfo;
        }

        // 计算使用优惠券之后的价格
        settlementInfo.setCost(totalCost - quota > 0? totalCost - quota : 0);
        log.debug("Using MinusDiscount Coupon Make Cost From {} to {}",
                totalCost, settlementInfo.getCost());

        return settlementInfo;
    }
}
