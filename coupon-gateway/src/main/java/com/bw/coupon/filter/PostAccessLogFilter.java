package com.bw.coupon.filter;

import com.bw.coupon.filter.abst.AbstractPostZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class PostAccessLogFilter extends AbstractPostZuulFilter {
    private static String startTimeKey = "startTime";

    @Override
    protected Object cRun() {
        HttpServletRequest httpServletRequest = requestContext.getRequest();
        String uri = httpServletRequest.getRequestURI();

        // 获取 PreRequestFilter 中设置的请求时间戳
        Long startTime = (Long)requestContext.get(startTimeKey);
        Long duration = System.currentTimeMillis() - startTime;

        //从网关通过的请求都会打印这条日志记录：uri + duration
        log.info("uri: {}, duration: {}", uri, duration);

        return success();
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;  //999
    }
}
