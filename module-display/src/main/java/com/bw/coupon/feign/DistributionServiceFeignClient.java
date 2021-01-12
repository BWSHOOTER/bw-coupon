package com.bw.coupon.feign;

import com.bw.coupon.entity.Coupon;
import com.bw.coupon.vo.AcquireTemplateRequest;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.vo.SettlementInfo;
import com.bw.coupon.vo.TemplateVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "coupon-distribution"/*,fallback = SettlementClientHystrix.class*/)
public interface DistributionServiceFeignClient {
        /** 功能1. 根据用户Id和优惠券状态 查找优惠券记录 */
    @RequestMapping(value = "/coupon/query", method = RequestMethod.GET)
    List<Coupon> findUserCouponsByStatus(@RequestParam("customerId") Long customerId,
                                         @RequestParam("status") Integer status)
            throws CommonException;

    /** 功能2. 根据用户Id，查找所有可领取的优惠券模板 */
    @RequestMapping(value = "/template/usable", method = RequestMethod.GET)
    List<TemplateVo> findAvailableTemplate(@RequestParam("customerId") Long customerId)
            throws CommonException;

    /** 功能3. 用户领取一个优惠券记录 */
    @RequestMapping(value = "/coupon/acquire", method = RequestMethod.POST)
    Coupon acquireCoupon(@RequestBody AcquireTemplateRequest request)
            throws CommonException;

    /** 功能4. 结算（核销）优惠券 */
    @RequestMapping(value = "/settlement/process", method = RequestMethod.POST)
    SettlementInfo settlement(@RequestBody SettlementInfo settlementInfo)
            throws CommonException;
}
