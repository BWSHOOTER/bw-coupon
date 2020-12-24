package com.bw.coupon.controller;

import com.bw.coupon.executor.ExecutorManager;
import com.bw.coupon.util.JacksonUtil;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class SettlementController {
    private final ExecutorManager executorManager;
    private final JacksonUtil jackson;

    @Autowired
    public SettlementController(ExecutorManager executorManager, JacksonUtil jackson) {
        this.executorManager = executorManager;
        this.jackson = jackson;
    }

    @PostMapping("/settlement/compute")
    public SettlementInfo computeSettlementRule(SettlementInfo settlementInfo)
        throws CommonException {
        log.info("Start to compute SettlementInfo: {}",
                jackson.writeValueAsString(settlementInfo));
        return executorManager.computeRule(settlementInfo);
    }
}
