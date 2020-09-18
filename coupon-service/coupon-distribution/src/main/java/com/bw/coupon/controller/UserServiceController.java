package com.bw.coupon.controller;

import com.bw.coupon.entity.Coupon;
import com.bw.coupon.enumeration.CouponStatusEnum;
import com.bw.coupon.exception.CommonException;
import com.bw.coupon.service.IUserService;
import com.bw.coupon.util.JacksonUtil;
import com.bw.coupon.vo.AcquireTemplateRequest;
import com.bw.coupon.vo.SettlementInfo;
import com.bw.coupon.vo.TemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务Controller
 */
@Slf4j
@RestController
public class UserServiceController {
    private final IUserService userService;
    private final JacksonUtil jackson;

    public UserServiceController(IUserService userService, JacksonUtil jacksonUtil) {
        this.userService = userService;
        this.jackson = jacksonUtil;
    }

    /** 根据用户Id和优惠券状态 查找优惠券记录 */
    @RequestMapping(value = "/coupons", method = RequestMethod.GET)
    public List<Coupon> findUserCouponsByStatus(@RequestParam("userId") Long userId,
                                                @RequestParam("status") Integer status)
            throws CommonException{
        log.info("Find User [{}] Coupons By Status [{}]",userId, status);
        return userService.findUserCouponsByStatus(userId,status);
    }

    /** 根据用户Id，查找所有可领取的优惠券模板 */
    @RequestMapping(value = "template", method = RequestMethod.GET)
    public List<TemplateSDK> findAvailableTemplate(@RequestParam("userId") Long userId)
        throws CommonException{
        log.info("Find User [{}] Available Coupon Template",userId);
        return userService.findAvailableTemplate(userId);
    }

    /** 用户领取一个优惠券记录 */
    @RequestMapping(value = "/newCoupon", method = RequestMethod.POST)
    public Coupon acquireCoupon(@RequestBody AcquireTemplateRequest request) throws CommonException{
        log.info("User [{}] Try to Acquire Coupon, TemplateId: {}",
                request.getUserId(), request.getTemplateSDK().getId());
        return userService.acquireCoupon(request);
    }

    /** 结算（核销）优惠券 */
    @RequestMapping(value = "/settlement", method = RequestMethod.POST)
    public SettlementInfo settlement(@RequestBody SettlementInfo settlementInfo)
        throws CommonException{
        log.info("User [{}] Settlement [{}]",
                settlementInfo.getUserId(), jackson.writeValueAsString(settlementInfo));
        return userService.settlement(settlementInfo);
    }
}
