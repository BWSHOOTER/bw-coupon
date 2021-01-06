package com.bw.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 操作创建请求
 * @Author: BaoWei
 * @Date: 2021/01/05 15:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationRegisterRequest {
    private List<OperationInfo> operationInfos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OperationInfo{
        String operationDes;
        String operationMode;
        String requestService;
        String requestPath;
        String httpMethod;
    }
}
