package com.bw.coupon.dao;

import com.bw.coupon.entity.MappingOperatorAndRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingOperatorAndRoleDao
        extends JpaRepository<MappingOperatorAndRole, Integer> {
    MappingOperatorAndRole findByOperatorId(Long operatorId);
}
