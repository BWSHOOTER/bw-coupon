package com.bw.coupon.executor;

import com.bw.coupon.enumeration.CalculatingMethodEnum;
import com.bw.coupon.enumeration.RuleUnionEnum;
import com.bw.coupon.util.PriceUtil;
import com.bw.coupon.vo.SettlementInfo;
import com.bw.coupon.vo.TemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MinusUnionMultiplyRuleExecutor extends AbstractRuleExecutor{
    /**
     * 规则类型定义
     */
    @Override
    public RuleUnionEnum ruleConfig() {
        return RuleUnionEnum.MinusUnionMultiply;
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
            log.debug("MinusUnionMultiply SettlementRule is not match to goodsType!");
            return processGoodsTypeNotSatisfy(settlementInfo);
        }

        double totalCost = PriceUtil.calGoodsTotalCost(settlementInfo.getGoodsInfos());

        // 获得
        TemplateVo minusTemplate = null, multiplyTemplate = null;
        for(SettlementInfo.CouponAndTemplateInfo ctInfo : settlementInfo.getCouponAndTemplateInfos()){
            if(ctInfo.getTemplateVo().getCalculatingMethodCode() == CalculatingMethodEnum.MinusCalculate.getCode())
                minusTemplate = ctInfo.getTemplateVo();
            else if(ctInfo.getTemplateVo().getCalculatingMethodCode() == CalculatingMethodEnum.MultiplyCalculate.getCode())
                multiplyTemplate = ctInfo.getTemplateVo();
        }
        if(minusTemplate==null || multiplyTemplate==null){
            return null;
        }

        double baseMinus = minusTemplate.getRule().getCalculatingRule().getBase();
        double baseMultiply = multiplyTemplate.getRule().getCalculatingRule().getBase();
        double quotaMinus = minusTemplate.getRule().getCalculatingRule().getQuota();
        double quotaMultiply = multiplyTemplate.getRule().getCalculatingRule().getQuota();

        double discountMinus = 0;
        double discountMultiply = 0;

        // 计算减法优惠的金额
        if(totalCost<baseMinus){
            // todo 删除乘法优惠券
            log.debug("[TotalCost] {} < [base] {}!", totalCost, baseMinus);
        }
        else{
            discountMinus = quotaMinus;
        }

        // 计算乘法优惠的金额
        if(totalCost<baseMultiply){
            // todo 删除乘法优惠券
            log.debug("[TotalCost] {} < [base] {}!", totalCost, baseMultiply);
        }
        else{
            discountMultiply = quotaMultiply * (totalCost - discountMinus);
        }

        // 计算使用优惠券之后的价格
        settlementInfo.setCost(totalCost - discountMinus -discountMultiply);
        log.debug("Using MixDiscount Coupon Make Cost From {} to {}", totalCost, settlementInfo.getCost());

        return settlementInfo;
    }
}
