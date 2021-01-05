package com.bw.coupon.service.impl;

import com.bw.coupon.entity.Coupon;
import com.bw.coupon.enumeration.CouponStatusEnum;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.service.IRedisService;
import com.bw.coupon.util.JacksonUtil;
import com.bw.coupon.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis相关的服务接口实现
 */

@Service
@Slf4j
public class RedisServiceImpl implements IRedisService {
    /** StringRedisTemplate 表示 Redis 所有 key 都是 String 类型，不要直接使用 RedisTemplate */
    private final StringRedisTemplate redisTemplate;
    private final JacksonUtil jackson;
    @Autowired
    public RedisServiceImpl(StringRedisTemplate redisTemplate, JacksonUtil jackson) {
        this.redisTemplate = redisTemplate;
        this.jackson = jackson;
    }

    /**
     * 根据 userId 和 status 找到缓存的优惠券列表数据
     * 注意：
     * 1. 可能会返回 null, 代表从没有过记录
     * 2. 如果没有查到记录，则向Redis中存一份无效的优惠券记录
     */
    @Override
    public List<Coupon> getCachedCoupons(Long userId, Integer status) {
        log.info("Get Coupons From Cache: {}, {}", userId, status);
        String redisKey = RedisKeyUtil.getCouponKeyByUserIdAndStatus(userId, status);

        List<String> couponStrs = redisTemplate.opsForHash().values(redisKey)
                .stream()
                .map(o -> Objects.toString(o, null))
                .collect(Collectors.toList());

        // 如果没有查到记录，则向Redis中存一份无效的优惠券记录
        if (CollectionUtils.isEmpty(couponStrs)) {
            saveEmptyCouponListToCache(userId,
                    Collections.singletonList(status));
            return Collections.emptyList();
        }

        return couponStrs.stream()
                .map(cs -> jackson.readValue(cs, Coupon.class))
                .collect(Collectors.toList());
    }

    /**
     * 保存空的优惠券列表到缓存中
     * 作用：避免缓存穿透（多次穿过缓存去库中查询不存在的值）
     */
    @Override
    public void saveEmptyCouponListToCache(Long userId, List<Integer> status) {
        log.info("Save Empty List To Cache For User: {}, Status: {}",
                userId, jackson.writeValueAsString(status));

        // key 是 coupon_id, value 是序列化的 Coupon
        Map<String, String> invalidCouponMap = new HashMap<>();
        invalidCouponMap.put("-1", jackson.writeValueAsString(Coupon.invalidCoupon()));

        // 用户优惠券缓存信息
        // K: userId + status -> redisKey       V: {coupon_id: 序列化的 Coupon}
        // 使用 SessionCallback 把数据命令放入到 Redis 的 pipeline
        // 多个命令往redis发送，所以使用redis的pipeline，多个命令一个返回
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                status.forEach(s -> {
                    String redisKey = RedisKeyUtil.getCouponKeyByUserIdAndStatus(userId, s);
                    operations.opsForHash().putAll(redisKey, invalidCouponMap);
                });
                return null;
            }
        };
        log.info("Pipeline Exe Result: {}",
                jackson.writeValueAsString(redisTemplate.executePipelined(sessionCallback)));
    }

    /**
     * 根据 templateId 尝试从 Cache 中获取一个优惠券码
     * 可能会返回null
     */
    @Override
    public String tryToAcquireCouponCodeFromCache(Integer templateId) {
        String redisKey = RedisKeyUtil.getCouponSnListKeyByTemplateId(templateId);
        // 因为优惠券码不存在顺序关系, 左边 pop 或右边 pop, 没有影响
        String couponCode = redisTemplate.opsForList().leftPop(redisKey);
        log.info("Acquire Coupon Code: {}, {}, {}",
                templateId, redisKey, couponCode);
        return couponCode;
    }

    /**
     * 将优惠券保存到 Cache 中
     * 返回：保存成功的个数
     */
    @Override
    public Integer addCouponToCache(Long userId,
                                    List<Coupon> coupons,
                                    Integer status)
            throws CommonException {
        log.info("Add Coupon To Cache: {}, {}, {}",
                userId, jackson.writeValueAsString(coupons), status);

        Integer result = -1;
        CouponStatusEnum couponStatus = CouponStatusEnum.of(status);

        switch (couponStatus) {
            case USABLE:
                result = addCouponToCacheForUsable(userId, coupons);
                break;
            case USED:
                result = addCouponToCacheForUsed(userId, coupons);
                break;
            case EXPIRED:
                result = addCouponToCacheForExpired(userId, coupons);
                break;
        }
        return result;
    }

    /**
     * 新增加优惠券到 Cache 中
     * 只会影响一个 Cache: USER_COUPON_USABLE
     */
    private Integer addCouponToCacheForUsable(Long userId, List<Coupon> coupons) {
        log.debug("Add Coupon To Cache For Usable.");

        Map<String, String> needCachedObject = new HashMap<>();
        coupons.forEach(c ->
            needCachedObject.put(c.getId().toString(), jackson.writeValueAsString(c))
        );

        String redisKey = RedisKeyUtil.getCouponKeyByUserIdAndStatus(userId, CouponStatusEnum.USABLE.getCode());
        redisTemplate.opsForHash().putAll(redisKey, needCachedObject);
        log.info("Add {} Coupons To Cache: {}, {}",
                needCachedObject.size(), userId, redisKey);

        redisTemplate.expire(
                redisKey,
                RedisKeyUtil.getRandomExpirationTime(1, 2),
                TimeUnit.SECONDS
        );
        return needCachedObject.size();
    }

    /**
     * 将已使用的优惠券加入到 Cache 中
     * 会影响到两个Cache：USABLE、USED
     */
    @SuppressWarnings("all")
    private Integer addCouponToCacheForUsed(Long userId, List<Coupon> coupons)
            throws CommonException {
        log.debug("Add Coupon To Cache For Used.");
        String redisKeyForUsable = RedisKeyUtil.getCouponKeyByUserIdAndStatus(
                userId, CouponStatusEnum.USABLE.getCode());
        String redisKeyForUsed = RedisKeyUtil.getCouponKeyByUserIdAndStatus(
                userId, CouponStatusEnum.USED.getCode());

        // 获取当前用户可用的优惠券，当前可用的优惠券个数一定是大于1的
        List<Coupon> curUsableCoupons = getCachedCoupons(userId, CouponStatusEnum.USABLE.getCode());
        assert curUsableCoupons.size() > coupons.size();

        Map<String, String> needCachedForUsed = new HashMap<>(coupons.size());
        coupons.forEach(c -> {
            needCachedForUsed.put(c.getId().toString(),jackson.writeValueAsString(c));
        });

        // 校验当前的优惠券参数是否与 Cached 中的匹配
        List<Integer> curUsableIds = curUsableCoupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());
        if (!CollectionUtils.isSubCollection(paramIds, curUsableIds)) {
            log.error("CurCoupons Is Not Equal ToCache: {}, {}, {}",
                userId, jackson.writeValueAsString(curUsableIds),
                jackson.writeValueAsString(paramIds));
            throw new CommonException("CurCoupons Is Not Equal To Cache!");
        }

        List<String> needCleanKey = paramIds.stream()
                .map(i -> i.toString()).collect(Collectors.toList());
        SessionCallback<Objects> sessionCallback = new SessionCallback<Objects>() {
            @Override
            public Objects execute(RedisOperations operations) throws DataAccessException {

                // 1. 已使用的优惠券 Cache 缓存添加
                operations.opsForHash().putAll(
                        redisKeyForUsed, needCachedForUsed
                );
                // 2. 可用的优惠券 Cache 需要清理
                operations.opsForHash().delete(
                        redisKeyForUsable, needCleanKey.toArray()
                );
                // 3. 重置过期时间
                operations.expire(
                        redisKeyForUsable,
                        RedisKeyUtil.getRandomExpirationTime(1, 2),
                        TimeUnit.SECONDS
                );
                operations.expire(
                        redisKeyForUsed,
                        RedisKeyUtil.getRandomExpirationTime(1, 2),
                        TimeUnit.SECONDS
                );
                return null;
            }
        };
        log.info("Pipeline Exe Result: {}",
                jackson.writeValueAsString(
                        redisTemplate.executePipelined(sessionCallback)));
        return coupons.size();
    }

    /**
     * 将过期优惠券加入到 Cache 中
     * 会影响到两个Cache： USABLE, EXPIRED
     */
    @SuppressWarnings("all")
    private Integer addCouponToCacheForExpired(Long userId, List<Coupon> coupons)
            throws CommonException {
        log.debug("Add Coupon To Cache For Expired.");

        // 最终需要保存的 Cache
        Map<String, String> needCachedForExpired = new HashMap<>(coupons.size());

        String redisKeyForUsable = RedisKeyUtil.getCouponKeyByUserIdAndStatus(userId, CouponStatusEnum.USABLE.getCode());
        String redisKeyForExpired = RedisKeyUtil.getCouponKeyByUserIdAndStatus(userId, CouponStatusEnum.EXPIRED.getCode());

        List<Coupon> curUsableCoupons = getCachedCoupons(
                userId, CouponStatusEnum.USABLE.getCode()
        );

        // 当前可用的优惠券个数一定是大于1的
        assert curUsableCoupons.size() > coupons.size();

        coupons.forEach(c -> needCachedForExpired.put(
                c.getId().toString(),
                jackson.writeValueAsString(c)
        ));

        // 校验当前的优惠券参数是否与 Cached 中的匹配
        List<Integer> curUsableIds = curUsableCoupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());
        if (!CollectionUtils.isSubCollection(paramIds, curUsableIds)) {
            log.error("CurCoupons Is Not Equal To Cache: {}, {}, {}",
                    userId, jackson.writeValueAsString(curUsableIds),
                    jackson.writeValueAsString(paramIds));
            throw new CommonException("CurCoupon Is Not Equal To Cache.");
        }

        List<String> needCleanKey = paramIds.stream()
                .map(i -> i.toString()).collect(Collectors.toList());

        SessionCallback<Objects> sessionCallback = new SessionCallback<Objects>() {
            @Override
            public Objects execute(RedisOperations operations) throws DataAccessException {

                // 1. 已过期的优惠券 Cache 缓存
                operations.opsForHash().putAll(
                        redisKeyForExpired, needCachedForExpired
                );
                // 2. 可用的优惠券 Cache 需要清理
                operations.opsForHash().delete(
                        redisKeyForUsable, needCleanKey.toArray()
                );
                // 3. 重置过期时间
                operations.expire(
                        redisKeyForUsable,
                        RedisKeyUtil.getRandomExpirationTime(1, 2),
                        TimeUnit.SECONDS
                );
                operations.expire(
                        redisKeyForExpired,
                        RedisKeyUtil.getRandomExpirationTime(1, 2),
                        TimeUnit.SECONDS
                );

                return null;
            }
        };

        log.info("Pipeline Exe Result: {}",
                jackson.writeValueAsString(
                        redisTemplate.executePipelined(sessionCallback)
                ));
        return coupons.size();
    }
}
