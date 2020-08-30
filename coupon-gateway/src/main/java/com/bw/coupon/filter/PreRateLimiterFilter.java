package com.bw.coupon.filter;

import com.bw.coupon.filter.abst.AbstractPreZuulFilter;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
@SuppressWarnings("all")
public class PreRateLimiterFilter extends AbstractPreZuulFilter {

    // 每秒可以取到两个令牌
    RateLimiter rateLimiter = RateLimiter.create(2.0);

    @Override
    protected Object cRun() {
        HttpServletRequest httpServletRequest = requestContext.getRequest();
        if(rateLimiter.tryAcquire()){
            log.info("get rate token success");
            return success();
        }
        else{
            log.info("rate limit: {}", httpServletRequest.getRequestURI());
            return fail(501,"error: rate limit");
        }
    }

    @Override
    public int filterOrder() {
        return 2;
    }
}
