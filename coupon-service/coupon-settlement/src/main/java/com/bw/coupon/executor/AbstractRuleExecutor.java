package com.bw.coupon.executor;

import com.bw.coupon.vo.SettlementInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 规则执行器抽象类，定义一些通用方法
 * @Author: BaoWei
 * @Date: 2020/12/21 17:19
 */
public abstract class AbstractRuleExecutor implements RuleExecutor{
    /**
     * @Description: 校验商品类型与优惠券是否匹配
     * @Author: BaoWei
     * @Date: 2020/12/21 17:20
     * 注意：
     * 1. 这里实现的是单品类优惠券的校验，多品类商品需要重载此方法
     * 2. 商品只需要有一个优惠券要求的商品类型去匹配即可以
     */
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlementInfo){
        return true;
    }

    /**
     * @Description: 处理商品类型与优惠券限制不匹配的情况
     * @Author: BaoWei
     * @Date: 2020/12/21 17:25
     */
    protected SettlementInfo processGoodsTypeNotSatisfy(SettlementInfo settlementInfo, double goodsSum) {
        return null;
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
