package com.bw.coupon.dao;

import com.bw.coupon.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationDao
        extends JpaRepository<Operation, Integer> {
    List<Operation> findAllByRequestService(String requestService);

    Operation findByRequestPathAndHttpMethod(String requestPath, String httpMethod);
}
