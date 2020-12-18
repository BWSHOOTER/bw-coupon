package com.bw.coupon.controller;

import com.bw.coupon.annotation.IgnoreCommonResponseAdvice;
import com.bw.coupon.util.JacksonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

/**
 * Ribbon 应用 Controller
 */
@Slf4j
@RestController
public class RibbonController {
    private final RestTemplate restTemplate;
    private final JacksonUtil jackson;

    @Autowired
    public RibbonController(RestTemplate restTemplate, JacksonUtil jackson) {
        this.restTemplate = restTemplate;
        this.jackson = jackson;
    }

    /**
     *  通过Ribbon组件调用模板微服务
     *  /distribution/info
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @IgnoreCommonResponseAdvice     //防止返回的Template又被包装一次
    public TemplateInfo getTemplate(){
        String infoUrl = "http://coupon-template/template/info";
        return restTemplate.getForEntity(infoUrl, TemplateInfo.class).getBody();
    }

    /** 模板微服务的元信息 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TemplateInfo{
        private Integer code;
        private String message;
        private List<Map<String, Object>> data;
    }
}
