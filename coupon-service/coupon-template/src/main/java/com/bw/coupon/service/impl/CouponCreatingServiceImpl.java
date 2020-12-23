package com.bw.coupon.service.impl;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.constant.Constant;
import com.bw.coupon.dao.CouponTemplateDao;
import com.bw.coupon.service.ICouponCreatingService;
import com.bw.coupon.util.CouponSnUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CouponCreatingServiceImpl implements ICouponCreatingService {
    private final CouponTemplateDao templateDao;
    private final StringRedisTemplate redisTemplate;
    @Autowired
    public CouponCreatingServiceImpl(CouponTemplateDao templateDao,
                                     StringRedisTemplate redisTemplate) {
        this.templateDao = templateDao;
        this.redisTemplate = redisTemplate;
    }

    /** 根据模板异步的创建优惠券码 */
    @Async("getAsyncExecutor")  //使用指定的异步线程池
    @Override
    @SuppressWarnings("all")
    public void asyncConstructCouponByTemplate(CouponTemplate template) {
        //1. 生成优惠券码的Set
        Set<String> couponCodes = CouponSnUtil.buildCouponCodeSet(template);

        // 计时，可不要
        Stopwatch watch = Stopwatch.createStarted();
        //2. 生成优惠券集合在Redis中的Key：如coupon_template_code_1
        String redisKey = String.format("%s%s",
                Constant.RedisPrefix.COUPON_TEMPLATE, template.getId().toString());
        //3. 将优惠券码放入Redis的集合中
        log.info("Push CouponCode To Redis: {}",
                redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));
        // 计时结束
        watch.stop();
        log.info("Save CouponCodeSet To Redis By Template[id = {}] Cost：{}ms.",
                template.getId(),
                watch.elapsed(TimeUnit.MILLISECONDS));

        // 4. 更新MySQL中CouponTemplate的可用状态
        template.setIsAvailable(true);
        templateDao.save(template);

        log.info("CouponTemplate({}) Is Available!", template.getId());

        // TODO 异步成功后的通知操作
    }
}
