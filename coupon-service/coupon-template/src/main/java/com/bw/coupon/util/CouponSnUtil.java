package com.bw.coupon.util;

import com.bw.coupon.Entity.CouponTemplate;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class CouponSnUtil {
    /**
     * 构造优惠券码
     * 优惠券码(对应于每一张优惠券, 18位)
     *  前四位: 产品线 + 类型
     *  中间六位: 日期随机(190101)
     *  后八位: 0 ~ 9 随机数构成
     */
    @SuppressWarnings("all")
    public static Set<String> buildCouponCodeSet(CouponTemplate template) {
        // 计时开始
        Stopwatch watch = Stopwatch.createStarted();

        Set<String> result = new HashSet<>(template.getDistributionAmount());

        // 前四位
        String prefix4 = template.getGoodsCategory().getCode().toString()
                + template.getCalculatingMethod().getCode().toString();
        String date = new SimpleDateFormat("yyMMdd")
                .format(template.getCreateTime());

        int amount = template.getDistributionAmount();
        for (int i = 0; i < amount; ++i) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }
        // 防止上面出现重复
        while (result.size() < amount) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }

        // 断言：结果集的券码数是否为模板拟分发总数
        assert result.size() == template.getDistributionAmount();

        // 计时结束
        watch.stop();
        log.info("Build CouponCodeSet By Template[id = {}] Cost: {}ms",
                template.getId(),
                watch.elapsed(TimeUnit.MILLISECONDS));

        return result;
    }

    /** 构造优惠券码的后 14 位 */
    private static String buildCouponCodeSuffix14(String date) {
        char[] bases = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};

        // 中间六位
        List<Character> chars = date.chars()
                .mapToObj(e -> (char) e).collect(Collectors.toList());
        Collections.shuffle(chars);
        String mid6 = chars.stream()
                .map(Object::toString).collect(Collectors.joining());

        // 后八位
        String suffix8 = RandomStringUtils.random(1, bases)
                + RandomStringUtils.randomNumeric(7);

        return mid6 + suffix8;
    }
}
