package com.bw.coupon.filter.abst;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * ZuulFilter中需要实现类实现的四个方法
 * 1. 过滤器类型 String filterType()
 * 2. 过滤器排序 int filterOrder()
 * 3. 是否启用过滤 boolean shouldFilter
 * 4. 实际过滤逻辑 Object run() throws ZuulException
 */
public abstract class AbstractZuulFilter extends ZuulFilter {
    // 用于在过滤器之间传递消息, 数据保存在每个请求的 ThreadLocal 中，所以是线程安全的
    // 请求和响应都会在 RequestContext 中
    // 扩展了 ConcurrentHashMap
    protected RequestContext requestContext;

    // 是否继续执行下一个过滤器的标识的键
    private final static String NEXT = "next";

    @Override
    public boolean shouldFilter() {
        // RequestContext 的静态方法，获取当前线程的ConcurrentContext
        RequestContext ctx = RequestContext.getCurrentContext();
        return (boolean)ctx.getOrDefault(NEXT,true);
    }

    // 首先初始化requestContext
    // 然后调用了抽象方法 cRun() ，仍然是抽象方法
    @Override
    public Object run() throws ZuulException {
        requestContext = RequestContext.getCurrentContext();
        return cRun();
    }
    //run方法核心
    protected abstract Object cRun();

    // 失败方法
    //todo
    protected Object fail(int code, String msg){
        // 不再继续执行后面的过滤器
        requestContext.set(NEXT,false);
        requestContext.setSendZuulResponse(false);
        requestContext.getResponse().setContentType("text/html;charset=UTF-8");
        requestContext.setResponseStatusCode(code);
        requestContext.setResponseBody(String.format("{\"result\": \"%s!\"}",msg));
        return null;
    }
    // 成功方法
    protected Object success() {
        requestContext.set(NEXT, true);
        return null;
    }
}
