package com.bw.coupon.service;

import com.bw.coupon.dao.OperationDao;
import com.bw.coupon.entity.Operation;
import com.bw.coupon.vo.OperationRegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bw.coupon.vo.OperationRegisterRequest.OperationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 权限注册 服务
 * @Author: BaoWei
 * @Date: 2021/01/06 10:49
 */
@Slf4j
@Service
public class OperationServiceImpl implements IOperationService{
    private final OperationDao operationDao;

    @Autowired
    public OperationServiceImpl(OperationDao operationDao) {
        this.operationDao = operationDao;
    }

    /** 批量存储操作到数据库 */
    public List<Integer> registerOperations(OperationRegisterRequest request){
        List<OperationInfo> requestOperationInfos = request.getOperationInfos();
        String tarServiceName = requestOperationInfos.get(0).getRequestService();
        List<Operation> currentOperations = operationDao.findAllByRequestService(tarServiceName);
        List<OperationInfo> validInfos = new ArrayList<>(requestOperationInfos.size());

        // 校验请求添加的操作与已有操作有没有重复
        for(OperationInfo opInfo: requestOperationInfos){
            boolean isValid = true;
            for(Operation op: currentOperations){
                if(op.getRequestPath().equals(opInfo.getRequestPath()) &&
                        op.getHttpMethod().equals(opInfo.getHttpMethod())){
                    isValid = false;
                    break;
                }
            }
            if(isValid){
                validInfos.add(opInfo);
            }
        }

        // OperationInfo 转换成 Operation
        List<Operation> operations = new ArrayList<>();
        for(OperationInfo info: validInfos){
            operations.add(new Operation(info));
        }

        return operationDao.saveAll(operations).stream().map(Operation::getId).collect(Collectors.toList());
    }
}
