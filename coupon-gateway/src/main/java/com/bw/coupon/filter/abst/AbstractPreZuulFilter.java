package com.bw.coupon.filter.abst;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

public abstract class AbstractPreZuulFilter extends AbstractZuulFilter{
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
}
