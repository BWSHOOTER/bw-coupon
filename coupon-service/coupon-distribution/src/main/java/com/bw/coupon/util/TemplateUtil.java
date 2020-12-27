package com.bw.coupon.util;

import com.bw.coupon.vo.TemplateVo;

import java.util.Date;

public class TemplateUtil {
    /** 判断TemplateSDK是否过期 */
    public static boolean isExpiredByTemplateVo(TemplateVo templateVo){
        long curDate = new Date().getTime();
        return curDate > templateVo.getRule().getExpirationRule().getDeadLine();
    }
}
