package com.bw.coupon.executor;

import com.bw.coupon.enumeration.RuleUnionEnum;
import com.bw.coupon.vo.SettlementInfo;

/**
 * @Description: 优惠券规则模板处理器的接口
 * @Author: BaoWei
 * @Date: 2020/12/21 16:56
 */
public interface RuleExecutor {
    /** 规则类型定义 */
    RuleUnionEnum ruleConfig();

    /**
     * @Description: 优惠券规则计算
     * @Author: BaoWei
     * @Date: 2020/12/21 16:57
     * 入参包含了用户选择的优惠券
     * 返回修正过的计算信息
     */
    SettlementInfo computeRule(SettlementInfo settlementInfo);
}
