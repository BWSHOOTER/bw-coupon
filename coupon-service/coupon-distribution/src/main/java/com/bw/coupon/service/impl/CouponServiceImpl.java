package com.bw.coupon.service.impl;

import com.bw.coupon.constant.Constant;
import com.bw.coupon.dao.CouponDao;
import com.bw.coupon.entity.Coupon;
import com.bw.coupon.enumeration.CouponStatusEnum;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.feign.SettlementClient;
import com.bw.coupon.feign.TemplateFeignClient;
import com.bw.coupon.service.IRedisService;
import com.bw.coupon.service.ICouponService;
import com.bw.coupon.util.JacksonUtil;
import com.bw.coupon.util.TemplateUtil;
import com.bw.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>用户服务相关的接口实现</h1>
 * 所有的操作过程, 状态都保存在 Redis 中, 并通过 Kafka 把消息传递到 MySQL 中
 * 为什么使用 Kafka, 而不是直接使用 SpringBoot 中的异步处理 ?
 * 安全性：即使消费失败，也可以从队列中重新获取消息，保持cache和库中的一致性
 */
@Slf4j
@Service
public class CouponServiceImpl implements ICouponService {
    /** Coupon Dao */
    private final CouponDao couponDao;
    /** Redis 服务 */
    private final IRedisService redisService;
    /** 模板微服务客户端 */
    private TemplateFeignClient templateFeignClient = null;
    /** 结算微服务客户端 */
    private final SettlementClient settlementClient;
    /** Kafka 客户端 */
    private final KafkaTemplate<String, String> kafkaTemplate;
    /** Jackson */
    private final JacksonUtil jackson;

    public CouponServiceImpl(CouponDao couponDao, IRedisService redisService,
                             TemplateFeignClient templateFeignClient, SettlementClient settlementClient,
                             KafkaTemplate<String, String> kafkaTemplate, JacksonUtil jackson) {
        this.couponDao = couponDao;
        this.redisService = redisService;
        this.templateFeignClient = templateFeignClient;
        this.settlementClient = settlementClient;
        this.kafkaTemplate = kafkaTemplate;
        this.jackson = jackson;
    }

    /**
     * @Description: 根据用户 id 和状态查询优惠券记录
     * @Author: BaoWei
     * @Date: 2020/12/16 15:13
     *
     * 1. 先查询Cache，如果为空，说明从来没有操作过优惠券，且此时加入了空的优惠券信息，再去数据库中查
     * 2. 如果数据库中没有查到，可以直接返回，因为Cache中已经有空的优惠券信息，不会缓存穿透
     * 3. 如果查到了，再结合TemplateClient的接口，根据templateId获取TemplateSDK，填充到Coupon中
     * 4. 此时再对无效优惠券进行剔除，如果status为可用、已过期，则重新分类，并更新cache和数据库，最后返回
     */
    @Override
    public List<Coupon> findUserCouponsByStatus(Long userId, Integer status) throws CommonException {
        List<Coupon> preTarget;
        /********************* 查询有效的优惠券 *********************/
        // 1. 从Cache中读取缓存的优惠券记录。
        // 注意：方法中若没有查到，则会插入一份无效优惠券记录（id = -1），防止缓存穿透。
        List<Coupon> curCached = redisService.getCachedCoupons(userId, status);
        // cache中查到了
        if (CollectionUtils.isNotEmpty(curCached)) {
            log.debug("coupon cache is not empty: {}, {}", userId, status);

            // 将无效优惠券剔除
            preTarget = curCached.stream()
                    .filter(c -> c.getId() != -1)
                    .collect(Collectors.toList());
        }
        // cache中没查到
        else {
            log.debug("coupon cache is empty, get coupon from db: {}, {}", userId, status);
            // 查数据库
            List<Coupon> dbCoupons = couponDao.findAllByUserIdAndStatus(userId, CouponStatusEnum.of(status));

            // 数据库没查到：直接返回null。因为 Cache 中已经加入了一张无效的优惠券
            if (CollectionUtils.isEmpty(dbCoupons)) {
                log.debug("current user do not have coupon: {}, {}", userId, status);
                return dbCoupons;
            }

            // 数据库查到：填充 dbCoupons的 templateSDK 字段（通过TemplateClient）
            List<Integer> ids = dbCoupons.stream()
                                    .map(Coupon::getTemplateId)
                                    .collect(Collectors.toList());
            Map<Integer, TemplateSDK> id2TemplateSDK = templateFeignClient.findIds2TemplateSDK(ids).getData();
            for(Coupon dbCoupon: dbCoupons){
                dbCoupon.setTemplateSDK(id2TemplateSDK.get(dbCoupon.getTemplateId()));
            }
            // 将记录写入 Cache
            redisService.addCouponToCache(userId, dbCoupons, status);
            // 数据库中不会有无效优惠券，因此不用剔除
            preTarget = dbCoupons;
        }

        /********************* 对优惠券进行判断分类和更新 *********************/
        // 如果当前获取的是已使用优惠券, 则直接返回。否则，还需要做对可用优惠券的进行判断是否过期
        if (CouponStatusEnum.of(status) == CouponStatusEnum.USED)
            return preTarget;

        CouponClassify classify = new CouponClassify(preTarget);
        // 如果已过期状态不为空, 需要做延迟处理
        if (CollectionUtils.isNotEmpty(classify.getExpired())) {
            log.info("Add Expired Coupons To Cache From FindCouponsByStatus: {}, {}", userId, status);
            // 更新Cache
            redisService.addCouponToCache(
                    userId,
                    classify.getExpired(),
                    CouponStatusEnum.EXPIRED.getCode()
            );
            /*
            redisService.addCouponToCache(
                    userId,
                    classify.getUsable(),
                    CouponStatusEnum.USABLE.getCode()
            );
             */
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
            /*
            kafkaTemplate.send(
                    Constant.TOPIC,
                    jackson.writeValueAsString(new CouponKafkaMessage(
                            CouponStatusEnum.USABLE.getCode(),
                            classify.getUsable().stream()
                                    .map(Coupon::getId)
                                    .collect(Collectors.toList())
                    ))
            );
             */
        }

        //return CouponStatusEnum.of(status) == CouponStatusEnum.USABLE? classify.getUsable(): classify.getExpired();
        return classify.getUsable();
    }

    /**
     * @Description: 用户领取优惠券
     * @Author: BaoWei
     * @Date: 2020/12/17 14:42
     *
     * 1. 根据 templateId 从 TemplateClient 拿到模板
     * 2. 检查模板是否过期
     * 3. 判断用户是否符合发放用户
     * 4. 根据用户已有优惠券情况判断用户是否达到领取上限
     * 5. 根据 templateId 从 Cache 中获得券码，构造不带的 TemplateSDK 的Coupon，并存入MySQL
     * 6. 填充 CouponTemplateSDK 得到完整的 Coupon 对象
     * 5. 将 Coupon 存入 Cache
     */
    @Override
    public Coupon acquireCoupon(AcquireTemplateRequest request) throws CommonException {
        Integer templateId = request.getTemplateSDK().getId();
        Long userId = request.getUserId();
        // 1. 根据request中的TemplateId，去TemplateClient中获取新的TemplateSDK
        Map<Integer, TemplateSDK> map = templateFeignClient.findIds2TemplateSDK(Collections.singletonList(templateId)).getData();
        if(map==null || map.size()!=1){
            log.error("Can Not Acquire Template From TemplateClient: {}",
                    request.getTemplateSDK().getId());
            throw new CommonException("Can Not Acquire Template From TemplateClient");
        }
        TemplateSDK sdk = map.get(templateId);

        // 2. 判断此SDK是否过期
        if(TemplateUtil.isExpiredByTemplateSDK(sdk)){
            log.error("Template Expire: {}", templateId);
            throw new CommonException("Template Expire");
        }

        // 3. 判断判断用户是否符合领取规则 todo

        // 4. 判断用户是否达到领取上限
        int limit = sdk.getRule().getLimitation();
        List<Coupon> userUsableCoupons = findUserCouponsByStatus(userId, CouponStatusEnum.USABLE.getCode());
        for(Coupon coupon: userUsableCoupons){
            if(coupon.getTemplateId() == templateId){
                limit--;
            }
        }
        if(limit<=0){
            log.error("Exceed Template Assign Limitation: {}" + templateId);
            throw new CommonException("Exceed Template Assign Limitation");
        }

        // 5.1 尝试根据TemplateId获取一个CouponCode
        String couponCode = redisService.tryToAcquireCouponCodeFromCache(templateId);
        if(StringUtils.isEmpty(couponCode)){
            log.error("Can Not Acquire Coupon Code: {}", request.getTemplateSDK().getId());
            throw new CommonException("Can Not Acquire Coupon Code");
        }
        // 5.2 根据获取的CouponCode构造一个可用的Coupon
        Coupon coupon = new Coupon(templateId,userId,couponCode,CouponStatusEnum.USABLE);
        // 5.3 将Coupon存进Coupon表，得到返回结果
        coupon = couponDao.save(coupon);
        // 6. 填充 CouponTemplateSDK 得到完整的 Coupon 对象
        coupon.setTemplateSDK(request.getTemplateSDK());

        // 7. 将完整的Coupon加进RedisCache
        redisService.addCouponToCache(userId,
                                      Collections.singletonList(coupon),
                                      CouponStatusEnum.USABLE.getCode());
        return coupon;
    }

    /**
     * 结算(核销)优惠券
     * 入参中没有最终cost，出参中有
     * 这里需要注意, 规则相关处理需要由 Settlement 系统去做
     * 当前系统仅仅做业务处理过程(校验过程)
     */
    @Override
    public SettlementInfo settlement(SettlementInfo info) throws CommonException {
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = info.getCouponAndTemplateInfos();
        // 当没有传递优惠券时, 直接返回商品总价
        if (CollectionUtils.isEmpty(ctInfos)) {
            log.info("Empty Coupons For Settle.");
            double goodsSum = 0.0;
            for (SettlementInfo.GoodsInfo gi : info.getGoodsInfos()) {
                goodsSum += gi.getPrice() * gi.getCount();
            }
            // 没有优惠券也就不存在优惠券的核销, SettlementInfo 其他的字段不需要修改
            info.setCost(retain2Decimals(goodsSum));
            return info;
        }

        // 校验传递的优惠券是否在该用户可用优惠券集合中
        List<Coupon> usableCoupons = findUserCouponsByStatus(info.getUserId(), CouponStatusEnum.USABLE.getCode());
        Map<Integer, Coupon> userCouponMap = new HashMap<>(usableCoupons.size());
        for(Coupon coupon: usableCoupons){
            userCouponMap.put(coupon.getId(), coupon);
        }
        List<Coupon> toUseCoupons = new ArrayList<>(ctInfos.size());
        for(SettlementInfo.CouponAndTemplateInfo ctinfo: ctInfos){
            Integer couponId = ctinfo.getId();
            if(!userCouponMap.containsKey(couponId)){
                log.error("Coupon is Not User's: {}", couponId);
                throw new CommonException("Coupon is Not User's: " + couponId);
            }
            toUseCoupons.add(userCouponMap.get(couponId));
        }

        // todo 通过结算服务获取结算信息
        SettlementInfo processedInfo = settlementClient.computeRule(info).getData();
        // 核销，且成功
        if (processedInfo.getEmploy()
                /*&& CollectionUtils.isNotEmpty(processedInfo.getCouponAndTemplateInfos())*/) {
            log.info("Settle User Coupon: {}, {}",
                    info.getUserId(),
                    jackson.writeValueAsString(toUseCoupons));
            // 将这些券的状态由可使用改成已使用
            // 更新缓存
            redisService.addCouponToCache(
                    info.getUserId(),
                    toUseCoupons,
                    CouponStatusEnum.USED.getCode()
            );
            // 更新 db
            kafkaTemplate.send(
                    Constant.TOPIC,
                    jackson.writeValueAsString(new CouponKafkaMessage(
                                                CouponStatusEnum.USED.getCode(),
                                                toUseCoupons.stream().map(Coupon::getId)
                                                .collect(Collectors.toList())
                    ))
            );
        }
        return processedInfo;
    }


    /** 保留两位小数 */
    private double retain2Decimals(double value) {
        // BigDecimal.ROUND_HALF_UP 代表四舍五入
        return new BigDecimal(value)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
}
