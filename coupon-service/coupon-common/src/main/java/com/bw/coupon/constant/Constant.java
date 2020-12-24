package com.bw.coupon.constant;

public class Constant {
    /** Kafka 消息的 Topic */
    public static final String TOPIC = "kafka_user_coupon_op";

    /**
     * Redis Key 前缀定义
     * */
    public static class RedisPrefix {
        /** 券码 sn 前缀 */
        public static final String COUPON_PREFIX = "coupon_";

        /** 用户当前所有可用的优惠券 key 前缀 */
        public static final String CUSTOMER_COUPON_USABLE_PREFIX = "customer_coupon_usable_";

        /** 用户当前所有已使用的优惠券 key 前缀 */
        public static final String CUSTOMER_COUPON_USED_PREFIX = "customer_coupon_used_";

        /** 用户当前所有已过期的优惠券 key 前缀 */
        public static final String CUSTOMER_COUPON_EXPIRED_PREFIX = "customer_coupon_expired_";
    }
}
