package com.bw.coupon.util;

import com.bw.coupon.constant.Constant;
import com.bw.coupon.enumeration.CouponStatusEnum;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;

import java.util.Map;

public class RedisKeyUtil {
    /**
     * 根据 status 和userId 组装对应 Coupon 的 RedisKey
     */
    public static String getCouponKeyByUserIdAndStatus(Long userId, Integer status){
        String redisKey = null;
        CouponStatusEnum couponStatus = CouponStatusEnum.of(status);
        switch (couponStatus) {
            case USABLE:
                redisKey = String.format("%s%s",
                        Constant.RedisPrefix.USER_COUPON_USABLE, userId);
                break;
            case USED:
                redisKey = String.format("%s%s",
                        Constant.RedisPrefix.USER_COUPON_USED, userId);
                break;
            case EXPIRED:
                redisKey = String.format("%s%s",
                        Constant.RedisPrefix.USER_COUPON_EXPIRED, userId);
                break;
        }
        return redisKey;
    }

    /**
     * 根据templateId 组装 CouponTemplate的RedisKey
     */
    public static String getCouponTemplateKeyByTemplateId(Integer templateId){
        return String.format("%s%s",Constant.RedisPrefix.COUPON_TEMPLATE,templateId.toString());
    }

    /**
     * 获取一个随机的过期时间
     * 缓存雪崩: key 在同一时间失效
     */
    public static Long getRandomExpirationTime(Integer min, Integer max) {
        return RandomUtils.nextLong(
                min * 60 * 60,
                max * 60 * 60
        );
    }
}
