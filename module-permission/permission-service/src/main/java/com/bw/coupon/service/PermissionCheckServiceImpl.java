package com.bw.coupon.service;

import com.bw.coupon.dao.MappingOperatorAndRoleDao;
import com.bw.coupon.dao.MappingRoleAndOpDao;
import com.bw.coupon.dao.OperationDao;
import com.bw.coupon.dao.OperationRoleDao;
import com.bw.coupon.entity.MappingOperatorAndRole;
import com.bw.coupon.entity.MappingRoleAndOp;
import com.bw.coupon.entity.Operation;
import com.bw.coupon.entity.OperatorRole;
import com.bw.coupon.vo.PermissionCheckRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Description: 权限校验 服务
 * @Author: BaoWei
 * @Date: 2021/01/06 10:48
 */
@Slf4j
@Service
public class PermissionCheckServiceImpl implements IPermissionCheckService{
    private final MappingOperatorAndRoleDao mappingOperatorAndRoleDao;
    private final MappingRoleAndOpDao mappingRoleAndOpDao;
    private final OperationDao operationDao;
    private final OperationRoleDao operationRoleDao;

    @Autowired
    public PermissionCheckServiceImpl(MappingOperatorAndRoleDao mappingOperatorAndRoleDao, MappingRoleAndOpDao mappingRoleAndOpDao, OperationDao operationDao, OperationRoleDao operationRoleDao) {
        this.mappingOperatorAndRoleDao = mappingOperatorAndRoleDao;
        this.mappingRoleAndOpDao = mappingRoleAndOpDao;
        this.operationDao = operationDao;
        this.operationRoleDao = operationRoleDao;
    }

    /** 权限校验 */
    public boolean checkPermission(PermissionCheckRequest request){
        Long operatorId = request.getOperatorId();

        MappingOperatorAndRole mappingOperatorAndRole = mappingOperatorAndRoleDao.findByOperatorId(operatorId);
        // 如果找不到mapping或者role
        if(mappingOperatorAndRole == null){
            log.error("OperatorId[{}] is not Exist in MappingOperatorAndRole!", operatorId);
            return false;
        }
        Optional<OperatorRole> role = operationRoleDao.findById(mappingOperatorAndRole.getRoleId());
        if(!role.isPresent()){
            log.error("RoleId[{}] is not Exist!", mappingOperatorAndRole.getRoleId());
            return false;
        }

        Operation operation = operationDao.findByRequestPathAndHttpMethod(request.getUri(), request.getHttpMethod());
        if(operation == null){
            log.error("Operation[{}] is not registered!", request.getUri() + ": " + request.getHttpMethod());
            return false;
        }

        MappingRoleAndOp mappingRoleAndOp = mappingRoleAndOpDao.findByRoleIdAndOpId(role.get().getId(), operation.getId());
        if(mappingRoleAndOp == null){
            log.warn("Operator[{}] NOT PASS the permission of Operation[{}]!", operatorId, operation.getId());
            return false;
        }

        log.info("Operator[{}] PASS the permission of Operation[{}]!", operatorId, operation.getId());
        return true;
    }
}
