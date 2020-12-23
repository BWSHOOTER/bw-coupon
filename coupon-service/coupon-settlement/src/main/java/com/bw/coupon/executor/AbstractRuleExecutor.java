package com.bw.coupon.executor;

import com.bw.coupon.util.PriceUtil;
import com.bw.coupon.vo.GoodsInfo;
import com.bw.coupon.vo.SettlementInfo;
import com.bw.coupon.vo.TemplateRuleVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 规则执行器抽象类，定义一些通用方法
 * @Author: BaoWei
 * @Date: 2020/12/21 17:19
 */
public abstract class AbstractRuleExecutor implements RuleExecutor{
    /**
     * @Description: 校验是否每一件商品的类型都与每一张优惠券适用品类相匹配
     * @Author: BaoWei
     * @Date: 2020/12/21 17:20
     */
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlementInfo){
        // 对每一件商品进行所有优惠券的匹配校验
        for(GoodsInfo goodsInfo: settlementInfo.getGoodsInfos()){
            // 当前商品的匹配校验结果
            boolean curRes = true;
            // 所有优惠券都需要匹配
            for(SettlementInfo.CouponAndTemplateInfo ctInfo: settlementInfo.getCouponAndTemplateInfos()){
                if(!ctInfo.getTemplateVo().getGoodsCategoryCodes().contains(goodsInfo.getCategoryCode())){
                    curRes = false;
                }
            }
            if(!curRes){
                return false;
            }
        }
        return true;
    }

    /**
     * @Description: 处理商品类型与优惠券限制不匹配的情况。即将Cost设为原价，并清空优惠券与模板列表。
     * @Author: BaoWei
     * @Date: 2020/12/21 17:25
     */
    protected SettlementInfo processGoodsTypeNotSatisfy(SettlementInfo settlementInfo) {
        double goodsSum = PriceUtil.calGoodsTotalCost(settlementInfo.getGoodsInfos());
        settlementInfo.setCost(goodsSum);
        settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
        return settlementInfo;
    }
}
