package com.bw.coupon.service;

import com.bw.coupon.vo.OperationRegisterRequest;

import java.util.List;

public interface IOperationService {
    List<Integer> registerOperations(OperationRegisterRequest request);
}
