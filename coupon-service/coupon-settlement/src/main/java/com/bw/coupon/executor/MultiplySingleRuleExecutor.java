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
public class MultiplySingleRuleExecutor extends AbstractRuleExecutor{
    /**
     * 规则类型定义
     */
    @Override
    public RuleUnionEnum ruleConfig() {
        return RuleUnionEnum.MultiplySingle;
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
            log.debug("MultiplyDiscount Template is not match to goodsType!");
            return processGoodsTypeNotSatisfy(settlementInfo);
        }

        double totalCost = PriceUtil.calGoodsTotalCost(settlementInfo.getGoodsInfos());

        // 判断满减是否符合折扣标准
        TemplateVo template = settlementInfo.getCouponAndTemplateInfos().get(0).getTemplateVo();
        double quota = template.getRule().getCalculatingRule().getQuota();
        double base = template.getRule().getCalculatingRule().getBase();

        // 如果不符合最低金额，直接返回总价
        if(totalCost<base){
            settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
            settlementInfo.setCost(totalCost);
            log.debug("[TotalCost] {} < [base] {}!", totalCost, base);
            return settlementInfo;
        }

        // 计算使用优惠券之后的价格
        settlementInfo.setCost(totalCost*quota);
        log.debug("Using MultiplyDiscount Coupon Make Cost From {} to {}", totalCost, settlementInfo.getCost());

        return settlementInfo;
    }
}
