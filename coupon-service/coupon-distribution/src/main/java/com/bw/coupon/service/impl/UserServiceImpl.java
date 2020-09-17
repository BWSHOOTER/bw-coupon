package com.bw.coupon.service.impl;

import com.bw.coupon.constant.Constant;
import com.bw.coupon.dao.CouponDao;
import com.bw.coupon.entity.Coupon;
import com.bw.coupon.enumeration.CouponStatusEnum;
import com.bw.coupon.exception.CommonException;
import com.bw.coupon.feign.SettlementClient;
import com.bw.coupon.feign.TemplateClient;
import com.bw.coupon.service.IRedisService;
import com.bw.coupon.service.IUserService;
import com.bw.coupon.util.JacksonUtil;
import com.bw.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <h1>用户服务相关的接口实现</h1>
 * 所有的操作过程, 状态都保存在 Redis 中, 并通过 Kafka 把消息传递到 MySQL 中
 * 为什么使用 Kafka, 而不是直接使用 SpringBoot 中的异步处理 ?
 * 安全性：即使消费失败，也可以从队列中重新获取消息，保持cache和库中的一致性
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    /** Coupon Dao */
    private final CouponDao couponDao;
    /** Redis 服务 */
    private final IRedisService redisService;
    /** 模板微服务客户端 */
    private final TemplateClient templateClient;
    /** 结算微服务客户端 */
    private final SettlementClient settlementClient;
    /** Kafka 客户端 */
    private final KafkaTemplate<String, String> kafkaTemplate;
    /** Jackson */
    private final JacksonUtil jackson;

    public UserServiceImpl(CouponDao couponDao, IRedisService redisService,
                           TemplateClient templateClient, SettlementClient settlementClient,
                           KafkaTemplate<String, String> kafkaTemplate, JacksonUtil jackson) {
        this.couponDao = couponDao;
        this.redisService = redisService;
        this.templateClient = templateClient;
        this.settlementClient = settlementClient;
        this.kafkaTemplate = kafkaTemplate;
        this.jackson = jackson;
    }

    /**
     * 根据用户 id 和状态查询优惠券记录
     * 先查询Cache，如果为空，说明从来没有操作过优惠券，且此时加入了空的优惠券信息，再去数据库中查
     * 如果数据库中没有查到，可以直接返回，因为Cache中已经有空的优惠券信息，不会缓存穿透
     * 如果查到了，再结合TemplateClient的接口，根据templateId获取TemplateSDK，填充到Coupon中
     *
     * 此时再对无效优惠券进行剔除，如果status为可用、已过期，则重新分类，并更新cache和数据库，最后返回
     */
    @Override
    public List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CommonException {
        List<Coupon> curCached = redisService.getCachedCoupons(userId,status);
        List<Coupon> preTarget;
        if (CollectionUtils.isNotEmpty(curCached)) {
            log.debug("coupon cache is not empty: {}, {}", userId, status);
            preTarget = curCached;
        } else {
            log.debug("coupon cache is empty, get coupon from db: {}, {}", userId, status);
            List<Coupon> dbCoupons = couponDao.findAllByUserIdAndStatus(userId, CouponStatusEnum.of(status));

            // 如果数据库中没有记录, 直接返回就可以, Cache 中已经加入了一张无效的优惠券
            if (CollectionUtils.isEmpty(dbCoupons)) {
                log.debug("current user do not have coupon: {}, {}", userId, status);
                return dbCoupons;
            }

            // 如果有，则填充 dbCoupons的 templateSDK 字段（通过TemplateClient）
            Map<Integer, TemplateSDK> id2TemplateSDK =
                    templateClient.findIds2TemplateSDK(
                            dbCoupons.stream()
                                    .map(Coupon::getTemplateId)
                                    .collect(Collectors.toList())
                    ).getData();
            dbCoupons.forEach(
                    dc -> dc.setTemplateSDK(
                            id2TemplateSDK.get(dc.getTemplateId())
                    )
            );
            // 数据库中存在记录
            preTarget = dbCoupons;
            // 将记录写入 Cache
            redisService.addCouponToCache(userId, preTarget, status);
        }
        // 将无效优惠券剔除
        preTarget = preTarget.stream()
                .filter(c -> c.getId() != -1)
                .collect(Collectors.toList());
        // 如果当前获取的是可用、已过期优惠券, 还需要做对可用优惠券的进行判断是否过期
        if (CouponStatusEnum.of(status) == CouponStatusEnum.USED) {
            return preTarget;
        }

        CouponClassify classify = new CouponClassify(preTarget);
        // 如果已过期状态不为空, 需要做延迟处理
        if (CollectionUtils.isNotEmpty(classify.getExpired())) {
            log.info("Add Expired Coupons To Cache From FindCouponsByStatus: {}, {}", userId, status);
            // 更新Cache
            redisService.addCouponToCache(
                    userId, classify.getExpired(),
                    CouponStatusEnum.EXPIRED.getCode()
            );
            // 发送到 kafka 中做异步处理
            kafkaTemplate.send(
                    Constant.TOPIC,
                    jackson.writeValueAsString(new CouponKafkaMessage(
                            CouponStatusEnum.EXPIRED.getCode(),
                            classify.getExpired().stream()
                                    .map(Coupon::getId)
                                    .collect(Collectors.toList())
                    ))
            );
        }
        return classify.getUsable();
    }

    /**
     * 根据用户 id 查找当前可以领取的优惠券模板
     */
    @Override
    public List<TemplateSDK> findAvailableTemplate(Long userId) throws CommonException {
        return null;
    }

    /**
     * 用户领取优惠券
     */
    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) throws CommonException {
        return null;
    }

    /**
     * 结算(核销)优惠券
     * 入参中没有最终cost，出参中有
     */
    @Override
    public SettlementInfo settlement(SettlementInfo info) throws CommonException {
        return null;
    }
}
