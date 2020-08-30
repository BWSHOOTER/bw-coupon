package com.bw.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import com.bw.coupon.filter.abst.AbstractPreZuulFilter;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 校验请求中传递的Token
 */
@Slf4j
@Component
public class PreTokenFilter extends AbstractPreZuulFilter {
    private final static String TokenKey = "token";

    @Override
    protected Object cRun() {
        HttpServletRequest request = requestContext.getRequest();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        Object token = request.getParameter(TokenKey);
        if(null == token){
            log.error("error: token is empty");
            return fail(401,"error: token is empty");
        }
        if(isTokenPass(token))
            return success();
        else
            return fail(402,"error: token is not pass");
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    // token校验逻辑
    private boolean isTokenPass(Object token){
        return true;
    }
}
