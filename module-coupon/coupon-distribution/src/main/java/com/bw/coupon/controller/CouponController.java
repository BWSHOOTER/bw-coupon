package com.bw.coupon.controller;

import com.bw.coupon.entity.Coupon;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.service.ICouponService;
import com.bw.coupon.service.ITemplateService;
import com.bw.coupon.util.JacksonUtil;
import com.bw.coupon.vo.AcquireTemplateRequest;
import com.bw.coupon.vo.SettlementInfo;
import com.bw.coupon.vo.TemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务Controller
 */
@Slf4j
@RestController
public class CouponController {
    private final ICouponService couponService;
    private final JacksonUtil jackson;
    private final ITemplateService templateService;

    public CouponController(ICouponService couponService,
                            ITemplateService templateService,
                            JacksonUtil jacksonUtil) {
        this.couponService = couponService;
        this.templateService = templateService;
        this.jackson = jacksonUtil;
    }

    /** 功能1. 根据用户Id和优惠券状态 查找优惠券记录 */
    @RequestMapping(value = "/coupon/query", method = RequestMethod.GET)
    public List<Coupon> findUserCouponsByStatus(@RequestParam("customerId") Long customerId,
                                                @RequestParam("status") Integer status)
            throws CommonException{
        log.info("Find Customer [id = {}] Coupons By Status [{}]",customerId, status);
        return couponService.findUserCouponsByStatus(customerId, status);
    }

    /** 功能2. 根据用户Id，查找所有可领取的优惠券模板 */
    @RequestMapping(value = "/template/usable", method = RequestMethod.GET)
    public List<TemplateVo> findAvailableTemplate(@RequestParam("customerId") Long customerId)
        throws CommonException{
        log.info("Find Customer [id = {}] Available Coupon Template",customerId);
        return templateService.findAvailableTemplateByUserId(customerId);
    }

    /** 功能3. 用户领取一个优惠券记录 */
    @RequestMapping(value = "/coupon/acquire", method = RequestMethod.POST)
    public Coupon acquireCoupon(@RequestBody AcquireTemplateRequest request)
            throws CommonException{
        log.info("User [{}] Try to Acquire Coupon, TemplateId: {}",
                request.getCreatorId(), request.getTemplateVo().getId());
        return couponService.acquireCoupon(request);
    }

    /** 功能4. 结算（核销）优惠券 */
    @RequestMapping(value = "/settlement/process", method = RequestMethod.POST)
    public SettlementInfo settlement(@RequestBody SettlementInfo settlementInfo)
        throws CommonException{
        log.info("Customer [{}] Settlement [{}]",
                settlementInfo.getCustomerId(), jackson.writeValueAsString(settlementInfo));
        return couponService.settlement(settlementInfo);
    }
}
