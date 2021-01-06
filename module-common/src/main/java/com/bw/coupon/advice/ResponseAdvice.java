package com.bw.coupon.advice;


import com.bw.coupon.annotation.IgnoreResponseAdvice;
import com.bw.coupon.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 对响应进行统一化增强处理
 */
@RestControllerAdvice //@Documented + @ControllerAdvice + @ResponseBody
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    //判断是否需要对响应进行处理
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        //方法标识，不处理
        if(methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class))
            return false;
        //类标识，不处理
        else if(methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class))
            return false;
        return true;
    }

    //响应返回之前的处理
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        // 如果 o 已经是 CommonResponse, 不需要增强
        if (o instanceof CommonResponse)
            return o;       // return (CommonResponse<Object>) o;


        // 如果 o 是 null, response 不需要设置 data，否则, 把响应对象作为 CommonResponse 的 data 部分
        CommonResponse<Object> response = new CommonResponse<>(0, "");
        if (null != o)
            response.setData(o);

        return response;
    }
}
