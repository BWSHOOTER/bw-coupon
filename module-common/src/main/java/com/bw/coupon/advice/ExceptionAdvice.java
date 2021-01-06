package com.bw.coupon.advice;

import com.bw.coupon.vo.CommonException;
import com.bw.coupon.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理
 * 对 CommonException 进行统一处理，包装成code = -1, msg为"business error"的统一响应CommonResponse
 */
@RestControllerAdvice
public class ExceptionAdvice {
    // ExceptionHandler可以对指定的异常进行拦截
    @ExceptionHandler(value = CommonException.class)
    public CommonResponse<String> handlerCouponException(HttpServletRequest req, CommonException ex) {
        CommonResponse<String> response = new CommonResponse<>(-1, "business error");
        response.setData(ex.getMessage());
        return response;
    }
}
