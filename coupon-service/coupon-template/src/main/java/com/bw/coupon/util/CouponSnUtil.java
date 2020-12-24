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
     * 优惠券码(对应于每一张优惠券, X位)
     *  前8位: 模板日期 yyMMddHH
     *  中间X位: 模板编码
     *  后8位: 随机8位，不以0开头
     */
    @SuppressWarnings("all")
    public static Set<String> buildCouponSnSet(CouponTemplate template) {
        // 计时开始
        Stopwatch watch = Stopwatch.createStarted();

        Set<String> result = new HashSet<>(template.getDistributionAmount());

        // 前X位
        String prefix = new SimpleDateFormat("yyMMddHH").format(template.getCreateTime()) +
                        template.getSn();

        int amount = template.getDistributionAmount();
        for (int i = 0; i < amount; ++i) {
            result.add(prefix + buildCouponSnLast());
        }
        // 防止上面出现重复
        while (result.size() < amount) {
            result.add(prefix + buildCouponSnLast());
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

    /** 构造优惠券码中间日期的8位：20052115 */
    private static String buildCouponSnDate8(String date) {
        List<Character> chars = date.chars()
                .mapToObj(e -> (char) e).collect(Collectors.toList());
        Collections.shuffle(chars);
        return chars.stream().map(Object::toString).collect(Collectors.joining());
    }

    /** 构造优惠券码末尾随机的8位，且不以0开头 */
    private static String buildCouponSnLast(){
        char[] bases = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        return RandomStringUtils.random(1, bases) + RandomStringUtils.randomNumeric(7);
    }
}
