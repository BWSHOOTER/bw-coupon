package com.bw.coupon.service.impl;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.constant.Constant;
import com.bw.coupon.dao.CouponTemplateDao;
import com.bw.coupon.service.IBuildCouponService;
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
public class BuildCouponServiceImpl implements IBuildCouponService {
    /** CouponTemplate Dao */
    private final CouponTemplateDao templateDao;
    /** Redis 模板类 */
    private final StringRedisTemplate redisTemplate;
    @Autowired
    public BuildCouponServiceImpl(CouponTemplateDao templateDao,
                                  StringRedisTemplate redisTemplate) {
        this.templateDao = templateDao;
        this.redisTemplate = redisTemplate;
    }

    /** 根据模板异步的创建优惠券码 */
    @Async("getAsyncExecutor")  //使用指定的异步线程池
    @Override
    @SuppressWarnings("all")
    public void asyncConstructCouponByTemplate(CouponTemplate template) {
        // 计时，可不要
        Stopwatch watch = Stopwatch.createStarted();

        //1. 生成优惠券码的Set
        Set<String> couponCodes = buildCouponCode(template);

        //2. 生成优惠券集合在Redis中的Key：coupon_template_code_1
        String redisKey = String.format("%s%s",
                Constant.RedisPrefix.COUPON_TEMPLATE, template.getId().toString());
        //3. 将优惠券码放入Redis的集合中
        log.info("Push CouponCode To Redis: {}",
                redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));

        // 计时结束
        watch.stop();
        log.info("Construct CouponCode By Template[id = {}] Cost：{}ms.",
                template.getId(),
                watch.elapsed(TimeUnit.MILLISECONDS));

        //更新MySQL中CouponTemplate的可用状态
        template.setAvailable(true);
        templateDao.save(template);

        log.info("CouponTemplate({}) Is Available!", template.getId());

        // TODO 异步成功后的通知操作
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
