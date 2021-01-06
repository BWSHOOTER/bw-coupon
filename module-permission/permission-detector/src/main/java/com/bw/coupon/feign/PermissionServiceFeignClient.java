package com.bw.coupon.feign;

import com.bw.coupon.vo.CommonResponse;
import com.bw.coupon.vo.OperationRegisterRequest;
import com.bw.coupon.vo.PermissionCheckRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "permission-service"/*,fallback = SettlementClientHystrix.class*/)
public interface PermissionServiceFeignClient {
    @PostMapping("/register/operations")
    CommonResponse<List<Integer>> registerOperations(@RequestBody OperationRegisterRequest request);

    @PostMapping("/check/permission")
    boolean checkPermission(@RequestBody PermissionCheckRequest request);
}
