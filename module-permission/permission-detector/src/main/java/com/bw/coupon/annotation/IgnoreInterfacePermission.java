package com.bw.coupon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 权限忽略注解：忽略当前表识的Controller接口，不注册权限
 * @Author: BaoWei
 * @Date: 2021/01/06 15:50
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreInterfacePermission {

}
