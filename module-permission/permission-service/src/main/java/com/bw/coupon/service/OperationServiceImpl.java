package com.bw.coupon.service;

import com.bw.coupon.dao.OperationDao;
import com.bw.coupon.entity.Operation;
import com.bw.coupon.vo.OperationCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.bw.coupon.vo.OperationCreateRequest.OperationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OperationServiceImpl {
    private final OperationDao operationDao;

    public OperationServiceImpl(OperationDao operationDao) {
        this.operationDao = operationDao;
    }

    /**
     * @Description: 批量存储操作到数据库
     * @Author: BaoWei
     * @Date: 2021/01/06 2:08
     */
    public List<Integer> createOperations(OperationCreateRequest request){
        List<OperationInfo> operationInfos = request.getOperationInfos();
        String tarServiceName = operationInfos.get(0).getRequestService();
        List<OperationInfo> validInfos = new ArrayList<>(operationInfos.size());
        List<Operation> currentOperations = operationDao.findAllByRequestService(tarServiceName);

        // 校验请求添加的操作与已有操作有没有重复
        if(!CollectionUtils.isEmpty(currentOperations)){
            for(OperationInfo opInfo: operationInfos){
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
        }
        else{
            validInfos.addAll(operationInfos);
        }
        // 转换
        List<Operation> operations = new ArrayList<>();

        return operationDao.saveAll(operations).stream().map(Operation::getId).collect(Collectors.toList());
    }
}
