package com.bw.coupon.controller;


import com.alibaba.fastjson.JSON;
import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.exception.CommonException;
import com.bw.coupon.service.IBuildTemplateService;
import com.bw.coupon.service.ITemplateBaseService;
import com.bw.coupon.vo.CouponTemplateSDK;
import com.bw.coupon.vo.TemplateRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TemplateController {
    /** 构建优惠券模板服务 */
    private final IBuildTemplateService buildTemplateService;
    /** 优惠券模板基础服务 */
    private final ITemplateBaseService templateBaseService;
    @Autowired
    public TemplateController(IBuildTemplateService buildTemplateService,
                                    ITemplateBaseService templateBaseService) {
        this.buildTemplateService = buildTemplateService;
        this.templateBaseService = templateBaseService;
    }

    /**
     * 构建优惠券模板
     * 127.0.0.1:7001/coupon-template/template/build
     * 127.0.0.1:9000/bw-coupon-zuul/coupon-template/template/build
     * */
    @PostMapping("/template/build")
    public CouponTemplate buildTemplate(@RequestBody TemplateRequestVo request)
            throws CommonException {
        log.info("Build Template: {}", JSON.toJSONString(request));
        return buildTemplateService.buildCouponTemplate(request);
    }

    /**
     * 构造优惠券模板详情
     * 127.0.0.1:7001/coupon-template/template/info?id=1
     * */
    @GetMapping("/template/info")
    public CouponTemplate buildTemplateInfo(@RequestParam("id") Integer id)
            throws CommonException {
        log.info("Build Template Info For: {}", id);
        return templateBaseService.buildTemplateInfo(id);
    }

    /**
     * 查找所有可用的优惠券模板
     * 127.0.0.1:7001/coupon-template/template/sdk/all
     * sdk一般表示是提供给第三方/其它模块使用
     * */
    @GetMapping("/template/sdk/all")
    public List<CouponTemplateSDK> findAllUsableTemplate() {
        log.info("Find All Usable Template.");
        return templateBaseService.findAllUsableTemplate();
    }

    /**
     * 获取模板 ids 到 CouponTemplateSDK 的映射
     * 127.0.0.1:7001/coupon-template/template/sdk/infos
     * */
    @GetMapping("/template/sdk/infos")
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(
            @RequestParam("ids") Collection<Integer> ids
    ) {
        log.info("FindIds2TemplateSDK: {}", JSON.toJSONString(ids));
        return templateBaseService.findIds2TemplateSDK(ids);
    }
}
