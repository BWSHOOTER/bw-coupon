package com.bw.coupon.feign;

import com.bw.coupon.feign.hystrix.TemplateFeignClientHystrix;
import com.bw.coupon.vo.CommonResponse;
import com.bw.coupon.vo.TemplateSDK;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板服务Feign接口定义
 */
//application.yml中定义的应用名
@FeignClient(value = "coupon-template", fallback = TemplateFeignClientHystrix.class)
public interface TemplateFeignClient {
    /**
     * 查找所有可用的优惠券模板
     * 127.0.0.1:7001/template/sdk/all
     */
    @RequestMapping(value = "/template/sdk/all", method = RequestMethod.GET)
    CommonResponse<List<TemplateSDK>> findAllUsableTemplate();
    //这里的方法名其实可以随便指定，因为关键是mapping的url，feignClient名称，以及方法的参数和响应

    /**
     * 获取模板 ids 到 CouponTemplateSDK 的映射
     * 127.0.0.1:7001/coupon-template/template/sdk/infos
     */
    @RequestMapping(value = "/template/sdk/infos", method = RequestMethod.GET)
    CommonResponse<Map<Integer, TemplateSDK>> findIds2TemplateSDK(@RequestParam("ids") Collection<Integer> ids);
}
