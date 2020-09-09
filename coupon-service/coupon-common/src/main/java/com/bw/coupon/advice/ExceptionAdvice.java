package com.bw.coupon.advice;

import com.bw.coupon.exception.CommonException;
import com.bw.coupon.vo.CommonResponseVo;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理
 **/
@RestControllerAdvice
public class ExceptionAdvice {

    //对 CommonException 进行统一处理
    //ExceptionHandler可以对指定的异常进行拦截
    @ExceptionHandler(value = CommonException.class)
    public CommonResponseVo<String> handlerCouponException(
            HttpServletRequest req,
            CommonException ex) {
        CommonResponseVo<String> response = new CommonResponseVo<>(-1, "business error");
        response.setData(ex.getMessage());
        return response;
    }

}
