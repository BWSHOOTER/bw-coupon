package com.bw.coupon.service.impl;

import com.bw.coupon.entity.Coupon;
import com.bw.coupon.enumeration.CouponStatusEnum;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.feign.TemplateFeignClient;
import com.bw.coupon.service.ITemplateService;
import com.bw.coupon.service.ICouponService;
import com.bw.coupon.vo.TemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.bw.coupon.util.TemplateUtil;

import java.util.*;

@Slf4j
@Service
public class TemplateServiceImpl implements ITemplateService {
    private final ICouponService couponService;
    /** 模板微服务客户端 */
    private final TemplateFeignClient templateFeignClient;

    public TemplateServiceImpl(TemplateFeignClient templateFeignClient, ICouponService couponService) {
        this.templateFeignClient = templateFeignClient;
        this.couponService = couponService;
    }

    /**
     * @Description: 根据用户 id 查找当前可以领取的优惠券模板
     * @Author: BaoWei
     * @Date: 2020/12/16 15:32
     * 1. 获取所有优惠券模板
     * 2. 剔除过期模板
     * 3. 获取用户所有优惠券，统计后剔除已达到领取上限的模板
     * 4. 返回剩下的模板列表
     */
    @Override
    public List<TemplateVo> findAvailableTemplateByUserId(Long customerId) throws CommonException {
        // 1. 获取所有优惠券模板
        List<TemplateVo> unfilteredTemplateVos = templateFeignClient.findAllUsableTemplate().getData();
        log.debug("Find all Template From TemplateClient Count:{}", unfilteredTemplateVos.size());

        // 2. 过滤过期的优惠券模板
        Map<Integer, TemplateVo> unExpiredTemplateVoMap = new HashMap<>(unfilteredTemplateVos.size());
        for(TemplateVo templateVo: unfilteredTemplateVos) {
            if (!templateVo.isExpired()) {
                unExpiredTemplateVoMap.put(templateVo.getId(), templateVo);
            }
        }
        log.info("Find Usable Template Count: {}", unExpiredTemplateVoMap.size());

        // 3. 获得用户已有的可用优惠券，并按模板分别统计已领取数目
        List<Coupon> customerUsableCoupons = couponService.findUserCouponsByStatus(customerId, CouponStatusEnum.USABLE.getCode());
        log.debug("Current Customer Has Usable Coupons: {}, {}", customerId, customerUsableCoupons.size());
        Map<Integer, Integer> couponCount = new HashMap<>(customerUsableCoupons.size());
        for(Coupon coupon : customerUsableCoupons){
            Integer templateId = coupon.getTemplateId();
            couponCount.put(templateId,couponCount.getOrDefault(templateId,0));
        }

        // 4. 剔除已达到当前用户领取上限的模板
        for(Integer key : couponCount.keySet()){
            if(unExpiredTemplateVoMap.containsKey(key) &&
                    unExpiredTemplateVoMap.get(key).getDistributionAmount() <= couponCount.get(key)){
                unExpiredTemplateVoMap.remove(key);
            }
        }

        // 5. 将剔除后的Map转为List并返回
        List<TemplateVo> result = new ArrayList<>(unExpiredTemplateVoMap.size());
        for(Integer id: unExpiredTemplateVoMap.keySet()){
            result.add(unExpiredTemplateVoMap.get(id));
        }
        return result;
    }
}
