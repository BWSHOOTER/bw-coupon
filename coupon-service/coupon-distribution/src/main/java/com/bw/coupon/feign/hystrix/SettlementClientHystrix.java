package com.bw.coupon.feign.hystrix;

import com.bw.coupon.exception.CommonException;
import com.bw.coupon.feign.SettlementClient;
import com.bw.coupon.vo.CommonResponse;
import com.bw.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SettlementClientHystrix implements SettlementClient {
    /**
     * 优惠券规则计算
     */
    @Override
    public CommonResponse<SettlementInfo> computeRule(SettlementInfo settlementInfo) throws CommonException {
        log.error("[coupon-settlement] computeRule request error");
        settlementInfo.setEmploy(false);    //不核销
        settlementInfo.setCost(-1.0);       //错误的金额，还可以优化改成原价
        return new CommonResponse<SettlementInfo>(-1,
                "[coupon-settlement] computeRule request error",
                settlementInfo);
    }
}
