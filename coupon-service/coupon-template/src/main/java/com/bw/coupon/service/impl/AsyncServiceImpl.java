package com.bw.coupon.service.impl;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.constant.Constant;
import com.bw.coupon.dao.CouponTemplateDao;
import com.bw.coupon.service.IAsyncService;
import com.bw.coupon.until.StringUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {
    /** CouponTemplate Dao */
    private final CouponTemplateDao templateDao;
    /** Redis 模板类 */
    private final StringRedisTemplate redisTemplate;
    @Autowired
    public AsyncServiceImpl(CouponTemplateDao templateDao,
                            StringRedisTemplate redisTemplate) {
        this.templateDao = templateDao;
        this.redisTemplate = redisTemplate;
    }

    /** 根据模板异步的创建优惠券码 */
    @Async("getAsyncExecutor")  //使用指定的异步线程池
    @Override
    @SuppressWarnings("all")
    public void asyncConstructCouponByTemplate(CouponTemplate template) {
        Stopwatch watch = Stopwatch.createStarted();

        Set<String> couponCodes = buildCouponCode(template);

        //coupon_template_code_1
        String redisKey = String.format("%s%s",
                Constant.RedisPrefix.COUPON_TEMPLATE, template.getId().toString());

        log.info("Push CouponCode To Redis: {}",
                redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));

        //更新MySQL中CouponTemplate的可用状态
        template.setAvailable(true);
        templateDao.save(template);

        watch.stop();

        log.info("Construct CouponCode By Template Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));

        // TODO 异步成功后的通知操作
        log.info("CouponTemplate({}) Is Available!", template.getId());
    }

    /**
     * 构造优惠券码
     * 优惠券码(对应于每一张优惠券, 18位)
     *  前四位: 产品线 + 类型
     *  中间六位: 日期随机(190101)
     *  后八位: 0 ~ 9 随机数构成
     */
    @SuppressWarnings("all")
    private Set<String> buildCouponCode(CouponTemplate template) {
        Stopwatch watch = Stopwatch.createStarted();

        Set<String> result = new HashSet<>(template.getCount());

        // 前四位
        String prefix4 = template.getProductLine().getCode().toString()
                + template.getDiscount().getCode();
        String date = new SimpleDateFormat("yyMMdd")
                .format(template.getCreateTime());

        for (int i = 0; i < template.getCount(); ++i) {
            result.add(prefix4 + StringUtil.buildCouponCodeSuffix14(date));
        }

        // 防止上面出现重复
        while (result.size() < template.getCount()) {
            result.add(prefix4 + StringUtil.buildCouponCodeSuffix14(date));
        }

        // 断言
        assert result.size() == template.getCount();

        watch.stop();

        log.info("Build Coupon Code Cost: {}ms", watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }


}
