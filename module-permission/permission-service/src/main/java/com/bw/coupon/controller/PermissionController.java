package com.bw.coupon.controller;


import com.bw.coupon.annotation.IgnoreResponseAdvice;
import com.bw.coupon.service.IOperationService;
import com.bw.coupon.service.IPermissionCheckService;
import com.bw.coupon.vo.OperationRegisterRequest;
import com.bw.coupon.vo.PermissionCheckRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class PermissionController {
    private final IOperationService operationService;
    private final IPermissionCheckService permissionCheckService;

    @Autowired
    public PermissionController(IOperationService operationService, IPermissionCheckService permissionCheckService) {
        this.operationService = operationService;
        this.permissionCheckService = permissionCheckService;
    }

    /** 注册操作接口 */
    @PostMapping("/register/operations")
    public List<Integer> registerOperations(@RequestBody OperationRegisterRequest request){
        log.info("Start Register Operations: num = {}]", request.getOperationInfos().size());
        return operationService.registerOperations(request);
    }

    /** 权限校验接口 */
    @PostMapping("check/permission")
    @IgnoreResponseAdvice
    public boolean checkPermission(@RequestBody PermissionCheckRequest request){
        log.info("Start Check Permission");
        return permissionCheckService.checkPermission(request);
    }
}
