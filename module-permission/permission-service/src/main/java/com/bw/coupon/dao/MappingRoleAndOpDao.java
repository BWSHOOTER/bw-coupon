package com.bw.coupon.dao;

import com.bw.coupon.entity.MappingRoleAndOp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingRoleAndOpDao
        extends JpaRepository<MappingRoleAndOp, Integer> {
    MappingRoleAndOp findByRoleIdAndOpId(Integer roleId, Integer opId);
}
