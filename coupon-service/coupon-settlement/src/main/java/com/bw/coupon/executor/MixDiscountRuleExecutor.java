package com.bw.coupon.executor;

import com.bw.coupon.enumeration.DiscountEnum;
import com.bw.coupon.enumeration.RuleFlagEnum;
import com.bw.coupon.vo.SettlementInfo;
import com.bw.coupon.vo.TemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Iterator;

@Slf4j
@Component
public class MixDiscountRuleExecutor extends AbstractRuleExecutor{
    /**
     * 规则类型定义
     */
    @Override
    public RuleFlagEnum ruleConfig() {
        return RuleFlagEnum.MixDiscount;
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
        if(!isGoodsTypeSatisfy(settlementInfo)){
            log.debug("MixDiscount Template is not match to goodsType!");
            return processGoodsTypeNotSatisfy(settlementInfo);
        }

        double totalCost = calGoodsTotalCost(settlementInfo.getGoodsInfos());

        // 获得
        TemplateSDK minusSdk = null, multiplySdk = null;
        for(SettlementInfo.CouponAndTemplateInfo ctInfo : settlementInfo.getCouponAndTemplateInfos()){
            if(ctInfo.getTemplate().getDiscount() == DiscountEnum.MinusDiscount.getCode())
                minusSdk = ctInfo.getTemplate();
            else if(ctInfo.getTemplate().getDiscount() == DiscountEnum.MultiplyDiscount.getCode())
                multiplySdk = ctInfo.getTemplate();
        }
        if(minusSdk==null || multiplySdk==null){
            return null;
        }

        double baseMinus = minusSdk.getRule().getDiscount().getBase();
        double baseMultiply = multiplySdk.getRule().getDiscount().getBase();
        double quotaMinus = minusSdk.getRule().getDiscount().getQuota();
        double quotaMultiply = multiplySdk.getRule().getDiscount().getQuota();

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
