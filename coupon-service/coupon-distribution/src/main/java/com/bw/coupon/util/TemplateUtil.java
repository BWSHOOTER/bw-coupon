package com.bw.coupon.util;

import com.bw.coupon.vo.TemplateSDK;

import java.util.Date;

public class TemplateUtil {
    /** 判断TemplateSDK是否过期 */
    public static boolean isExpiredByTemplateSDK(TemplateSDK templateSDK){
        long curDate = new Date().getTime();
        return curDate > templateSDK.getRule().getExpiration().getDeadLine();
    }
}
