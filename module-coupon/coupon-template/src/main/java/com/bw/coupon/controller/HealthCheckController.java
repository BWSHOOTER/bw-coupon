package com.bw.coupon.controller;


import com.bw.coupon.vo.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健康检查接口
 */
@Slf4j
@RestController
public class HealthCheckController {
    /**
     * 服务发现客户端
     * 可以获得Eureka上注册的微服务信息
     */
    private final DiscoveryClient discoveryClient;
    /**
     * 服务注册接口
     * 提供了获取服务id的方法
     */
    private final Registration registration;
    public HealthCheckController(DiscoveryClient discoveryClient, @Qualifier("eurekaRegistration") Registration registration) {
        this.discoveryClient = discoveryClient;
        this.registration = registration;
    }

    /**
     * 健康检查接口
     * 127.0.0.1:7001/template/health
     * 127.0.0.1:9000/bw-coupon-zuul/template/health
     * http://127.0.0.1:9000/template/health?token=456
     */
    @GetMapping("/health")
    public String health(){
        log.debug("view health api");
        return "CouponTemplate is OK!";
    }

    /**
     * 异常检查接口
     * 127.0.0.1:7001/template/exception
     */
    @GetMapping("/exception")
    public String exception() throws CommonException{
        log.debug("view exception api");
        throw new CommonException("CouponTemplate CommonException.");
    }
    /**
     * 获取Eureka Server上的微服务元信息
     * 127.0.0.1:7001/coupon-template/info
     */
    @GetMapping("/info")
    public List<Map<String,Object>> info(){
        // 大约需要等待两分钟时间才能获取到注册信息
        List<ServiceInstance> instances =
                discoveryClient.getInstances(registration.getServiceId());
        List<Map<String,Object>> result =
                new ArrayList<>(instances.size());
        for(ServiceInstance i: instances){
            Map<String,Object> info = new HashMap<>();
            info.put("serviceId",i.getServiceId());
            info.put("instanceId",i.getInstanceId());
            info.put("port",i.getPort());
            result.add(info);
        }
        return result;
    }
}
