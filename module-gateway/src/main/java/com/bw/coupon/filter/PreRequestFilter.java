package com.bw.coupon.filter;

import com.bw.coupon.filter.abst.AbstractPreZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PreRequestFilter extends AbstractPreZuulFilter {
    private static String startTimeKey = "startTime";

    @Override
    protected Object cRun() {
        requestContext.set(startTimeKey,System.currentTimeMillis());
        log.info("[1. PreRequestFilter Pass!]");
        return success();
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
