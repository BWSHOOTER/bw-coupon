package com.bw.coupon.feign;

import com.bw.coupon.exception.CommonException;
import com.bw.coupon.feign.hystrix.SettlementClientHystrix;
import com.bw.coupon.vo.CommonResponse;
import com.bw.coupon.vo.SettlementInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "coupon-settlement",fallback = SettlementClientHystrix.class)
public interface SettlementClient {

    /**
     * 优惠券规则计算
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    CommonResponse<SettlementInfo> computeRule(@RequestBody SettlementInfo settlementInfo)
            throws CommonException;
}
