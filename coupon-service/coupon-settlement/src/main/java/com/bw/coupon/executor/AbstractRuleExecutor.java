package com.bw.coupon.executor;

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
        // 获取每一张优惠券的使用品类列表
        List<List<Integer>> categoryCodeLists = new ArrayList<>();
        for(SettlementInfo.CouponAndTemplateInfo ctInfo: settlementInfo.getCouponAndTemplateInfos()){
            List<Integer> categoryCodeList = new ArrayList<>();
            for(TemplateRuleVo.GoodsCategoryRuleTemplate goodsCategory:
                    ctInfo.getTemplate().getRule().getGoodsCategories()){
                categoryCodeList.add(goodsCategory.getCode());
            }
            categoryCodeLists.add(categoryCodeList);
        }
        // 对每一件商品进行所有优惠券的匹配校验
        List<SettlementInfo.GoodsInfo> goodsInfos = settlementInfo.getGoodsInfos();
        for(SettlementInfo.GoodsInfo goodsInfo: goodsInfos){
            boolean curRes = true;
            for(List<Integer> categoryList: categoryCodeLists){
                if(!categoryList.contains(goodsInfo.getProductLine()))
                    curRes = false;
            }
            if(!curRes)
                return false;
        }
        return true;
    }

    /**
     * @Description: 处理商品类型与优惠券限制不匹配的情况
     * @Author: BaoWei
     * @Date: 2020/12/21 17:25
     */
    protected SettlementInfo processGoodsTypeNotSatisfy(SettlementInfo settlementInfo) {
        double goodsSum = calGoodsTotalCost(settlementInfo.getGoodsInfos());
        settlementInfo.setCost(goodsSum);
        settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
        return settlementInfo;
    }

    /**
     * @Description: 计算商品总价
     * @Author: BaoWei
     * @Date: 2020/12/21 17:36
     */
    protected double calGoodsTotalCost(List<SettlementInfo.GoodsInfo> goodsInfos){
        double totalCost = 0;
        for(SettlementInfo.GoodsInfo goodsInfo: goodsInfos){
            totalCost += goodsInfo.getCount()*goodsInfo.getPrice();
        }
        return retain2Decimals(totalCost);
    }

    /** 保留两位小数 */
    protected double retain2Decimals(double value) {
        // BigDecimal.ROUND_HALF_UP 代表四舍五入
        return new BigDecimal(value)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
}
