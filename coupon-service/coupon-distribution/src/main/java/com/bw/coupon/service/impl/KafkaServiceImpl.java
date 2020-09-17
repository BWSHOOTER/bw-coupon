package com.bw.coupon.service.impl;

import com.bw.coupon.constant.Constant;
import com.bw.coupon.dao.CouponDao;
import com.bw.coupon.entity.Coupon;
import com.bw.coupon.enumeration.CouponStatusEnum;
import com.bw.coupon.service.IKafkaService;
import com.bw.coupon.util.JacksonUtil;
import com.bw.coupon.vo.CouponKafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Kafka 相关的服务接口实现
 * 核心思想: 将 Cache 中的 Coupon 的状态变化同步到 DB 中
 */
@Slf4j
@Component
public class KafkaServiceImpl implements IKafkaService {
    private final CouponDao couponDao;
    private final JacksonUtil jackson;
    @Autowired
    public KafkaServiceImpl(CouponDao couponDao, JacksonUtil jackson) {
        this.couponDao = couponDao;
        this.jackson = jackson;
    }

    /** 消费优惠券 Kafka 消息 */
    @Override
    @KafkaListener(topics = {Constant.TOPIC}, groupId = "coupon-1")
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            CouponKafkaMessage couponInfo = jackson.readValue(message.toString(), CouponKafkaMessage.class);

            log.info("Receive CouponKafkaMessage: {}", message.toString());

            CouponStatusEnum status = CouponStatusEnum.of(couponInfo.getStatus());

            switch (status) {
                case USABLE:
                    break;
                case USED:
                    processUsedCoupons(couponInfo, status);
                    break;
                case EXPIRED:
                    processExpiredCoupons(couponInfo, status);
                    break;
            }
        }
    }

    /** 处理已使用的用户优惠券 */
    private void processUsedCoupons(CouponKafkaMessage kafkaMessage,
                                    CouponStatusEnum status) {
        // 【进行已使用的相关处理】
        processCouponsByStatus(kafkaMessage, status);
    }

    /** 处理过期的用户优惠券 */
    private void processExpiredCoupons(CouponKafkaMessage kafkaMessage,
                                       CouponStatusEnum status) {
        // 【进行过期的相关处理】
        processCouponsByStatus(kafkaMessage, status);
    }


    /** 根据状态对数据库中的优惠券信息进行处理 */
    private void processCouponsByStatus(CouponKafkaMessage kafkaMessage,
                                        CouponStatusEnum status) {
        List<Coupon> coupons = couponDao.findAllById(kafkaMessage.getIds());
        if (CollectionUtils.isEmpty(coupons)
                || coupons.size() != kafkaMessage.getIds().size()) {
            log.error("Can Not Find Right Coupon Info: {}",
                    jackson.writeValueAsString(kafkaMessage));
            // 【通知管理人员】
            return;
        }

        coupons.forEach(c -> c.setStatus(status));
        log.info("CouponKafkaMessage Op Coupon Count: {}",
                couponDao.saveAll(coupons).size());
    }
}
