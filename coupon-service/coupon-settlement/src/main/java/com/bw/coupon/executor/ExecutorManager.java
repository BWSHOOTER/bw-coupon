package com.bw.coupon.executor;

import com.bw.coupon.enumeration.DiscountEnum;
import com.bw.coupon.enumeration.RuleFlagEnum;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 优惠券规则模板处理器的管理器
 * @Author: BaoWei
 * @Date: 2020/12/22 17:49
 * 根据用户的Settlement找到对应的Executor，去做结算
 */
@Slf4j
@Component
// 注意：BeanPostProcessor Bean后置处理器，是由Spring提供的接口
public class ExecutorManager implements BeanPostProcessor {
    /** 规则执行器的映射 */
    private static Map<RuleFlagEnum, RuleExecutor> executorIndex =
            new HashMap<>(RuleFlagEnum.values().length);

    /**
     * @Description: 优惠券结算规则入口
     * @Author: BaoWei
     * @Date: 2020/12/22 18:05
     * 注意：调用方要保证传递进来的优惠券个数>=1
     */
    public SettlementInfo computeRule(SettlementInfo settlementInfo)
        throws CommonException {
        SettlementInfo res = null;
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = settlementInfo.getCouponAndTemplateInfos();
        // 单类优惠券
        if(ctInfos.size() == 1){
            DiscountEnum discount = DiscountEnum.of(settlementInfo.getCouponAndTemplateInfos().
                    get(0).getTemplate().getDiscount());
            if(discount == DiscountEnum.MinusDiscount){
                res = executorIndex.get(RuleFlagEnum.MinusDiscount).computeRule(settlementInfo);
            }
            else if(discount == DiscountEnum.MultiplyDiscount){
                res = executorIndex.get(RuleFlagEnum.MultiplyDiscount).computeRule(settlementInfo);
            }
        }
        // 多类优惠券
        else if(ctInfos.size() == 2){
            // todo 判断是否两张正好是乘和减
            res = executorIndex.get(RuleFlagEnum.MixDiscount).computeRule(settlementInfo);
        }
        else {
            throw new CommonException("Not Support For More Than 2 CouponTemplates");
        }
        return res;
    }

    // 在Bean初始化之前执行
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if(!(bean instanceof RuleExecutor))
            return bean;
        RuleExecutor executor = (RuleExecutor)bean;
        RuleFlagEnum ruleFlagEnum = executor.ruleConfig();

        if(executorIndex.containsKey(ruleFlagEnum)){
            throw new IllegalStateException("The RuleExecutor for RuleFlag [" +
                    ruleFlagEnum +
                    "] is already exist!");
        }
        log.info("Load RuleExecutor {} for RuleFlag {}", executor.getClass(), ruleFlagEnum);
        return bean;
    }

    // 在Bean初始化之后执行
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return null;
    }
}
