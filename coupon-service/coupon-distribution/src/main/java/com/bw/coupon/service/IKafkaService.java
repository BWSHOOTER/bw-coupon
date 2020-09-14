package com.bw.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface IKafkaService {
    /**
     * 消费优惠券 Kafka 消息
     **/
    void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record);
}
