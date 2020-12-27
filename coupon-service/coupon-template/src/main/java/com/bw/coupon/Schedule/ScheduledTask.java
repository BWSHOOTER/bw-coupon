package com.bw.coupon.Schedule;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.dao.CouponTemplateDao;
import com.bw.coupon.vo.TemplateRuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时清理已过期的优惠券模板
 */
@Slf4j
@Component
public class ScheduledTask {
    private final CouponTemplateDao couponTemplateDao;

    @Autowired
    public ScheduledTask(CouponTemplateDao couponTemplateDao) {
        this.couponTemplateDao = couponTemplateDao;
    }

    // 定时任务注解
    @Scheduled(fixedRate = 60*60*1000)
    public void offlineCouponTemplate(){
        log.info("Start to Get Expired CouponTemplate");
        List<CouponTemplate> templates = couponTemplateDao.findAllByIsExpired(false);
        if(CollectionUtils.isEmpty(templates)){
            log.info("Done To Expire CouponTemplate: ExpiredTemplates is NULL.");
            return;
        }
        Date cur = new Date();
        List<CouponTemplate> expiredTemplates = new ArrayList<>(templates.size());
        for (CouponTemplate t: templates) {
            // 根据优惠券模板规则中的 "过期规则" 校验模板是否过期
            TemplateRuleVo rule = t.getRule();
            if (rule.getExpirationRule().getDeadLine() < cur.getTime()) {
                t.setIsExpired(true);
                expiredTemplates.add(t);
            }
        }

        if (CollectionUtils.isNotEmpty(expiredTemplates)) {
            // todo 更改数据库和缓存状态
            //log.info("Expired CouponTemplate Num: {}", CouponTemplateDao.saveAll(expiredTemplates));
        }
        log.info("Done To Expire CouponTemplate.");
    }
}
