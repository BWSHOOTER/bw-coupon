package com.bw.coupon.controller;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.service.ITemplateCreatingService;
import com.bw.coupon.service.ITemplateBaseService;
import com.bw.coupon.util.JacksonUtil;
import com.bw.coupon.vo.TemplateVo;
import com.bw.coupon.vo.TemplateCreatingRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TemplateController {
    private final ITemplateCreatingService templateCreatingService;
    private final ITemplateBaseService templateBaseService;

    private final JacksonUtil jackson;
    @Autowired
    public TemplateController(ITemplateCreatingService templateCreatingService, ITemplateBaseService templateBaseService, JacksonUtil jackson) {
        this.templateCreatingService = templateCreatingService;
        this.templateBaseService = templateBaseService;
        this.jackson = jackson;
    }

    /**
     * 1. 构建优惠券模板
     *      原地址：127.0.0.1:7001/coupon-template/template/build
     *      转发地址：127.0.0.1:9000/bw-coupon-zuul/coupon-template/template/build
     */
    @PostMapping("/template/build")
    public CouponTemplate buildTemplate(@RequestBody TemplateCreatingRequestVo request)
            throws CommonException {
        log.info("Build Template: {}", jackson.writeValueAsString(request));
        return templateCreatingService.createCouponTemplate(request);
    }

    /**
     * 2. 查询指定优惠券模板的Entity
     *      原地址：127.0.0.1:7001/coupon-template/template/info?id=1
     */
    @GetMapping("/template/info")
    public CouponTemplate findTemplateById(@RequestParam("id") Integer id)
            throws CommonException {
        log.info("Build Template Info For: {}", id);
        return templateBaseService.findTemplateById(id);
    }

    /**
     * 3. 查找所有可用的优惠券模板的Vo
     *      原地址：127.0.0.1:7001/coupon-template/template/sdk/all
     */
    @GetMapping("/template/sdk/all")
    public List<TemplateVo> findAllUsableTemplate() {
        log.info("Find All Usable Template.");
        return templateBaseService.findAllUsableTemplate();
    }

    /**
     * 4. 获取模板 ids 到 TemplateVo 的映射
     *      原地址：127.0.0.1:7001/coupon-template/template/sdk/infos
     */
    @GetMapping("/template/sdk/infos")
    public Map<Integer, TemplateVo> findIds2TemplateVos(@RequestParam("ids") Collection<Integer> ids) {
        log.info("FindIds2TemplateVos: {}", jackson.writeValueAsString(ids));
        return templateBaseService.findIds2TemplateVos(ids);
    }
}
