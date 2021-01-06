package com.bw.coupon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 定义Controller接口的权限
 * @Author: BaoWei
 * @Date: 2021/01/06 15:46
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CouponInterfacePermission {
    /** 接口描述信息 */
    String description() default "";
    /** 接口是否为只读 */
    boolean readOnly() default true;
    /** 扩展属性（最好以Json格式存储） */
    String extra() default "";
}
