package com.bw.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Description: 权限校验请求
 * @Author: BaoWei
 * @Date: 2021/01/05 15:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCheckRequest {
    private Long operatorId;
    private String httpMethod;
    private String uri;
}
