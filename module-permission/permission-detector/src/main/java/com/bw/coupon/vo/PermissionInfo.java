package com.bw.coupon.vo;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @Description: 接口权限信息 组装类
 * @Author: BaoWei
 * @Date: 2021/01/06 16:18
 */
@Data
public class PermissionInfo {
    private String url;
    private String httpMethod;
    private boolean isReadOnly;
    private String description;
    private String extra;

    public String toString(){
        String res = "url = " + url +
                     ", httpMethod = " + httpMethod +
                     ", isReadOnly = " + isReadOnly +
                     ", description = " + description;
        return StringUtils.isEmpty(extra)? res : res + ", extra = " + extra;
    }
}
