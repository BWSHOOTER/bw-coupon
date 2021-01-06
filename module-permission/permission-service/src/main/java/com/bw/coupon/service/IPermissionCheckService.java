package com.bw.coupon.service;

import com.bw.coupon.vo.PermissionCheckRequest;

public interface IPermissionCheckService {
    boolean checkPermission(PermissionCheckRequest request);
}
