package com.bw.coupon.feign.hystrix;

import com.bw.coupon.feign.TemplateFeignClient;
import com.bw.coupon.vo.CommonResponse;
import com.bw.coupon.vo.TemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class TemplateFeignClientHystrix implements TemplateFeignClient {
    /**
     * 查找所有可用的优惠券模板
     * 127.0.0.1:7001/template/sdk/all
     */
    @Override
    public CommonResponse<List<TemplateSDK>> findAllUsableTemplate() {
        log.error("[coupon-template] findAllUsableTemplate request error");
        return new CommonResponse<List<TemplateSDK>>(-1,
                "[coupon-template] findAllUsableTemplate request error",
                Collections.EMPTY_LIST);
    }

    /**
     * 获取模板 ids 到 CouponTemplateSDK 的映射
     * 127.0.0.1:7001/coupon-template/template/sdk/infos
     */
    @Override
    public CommonResponse<Map<Integer, TemplateSDK>> findIds2TemplateSDK(Collection<Integer> ids) {
        log.error("[coupon-template] findIds2TemplateSDK request error");
        return new CommonResponse<Map<Integer, TemplateSDK>>(-1,
                "[coupon-template] findIds2TemplateSDK request error",
                Collections.EMPTY_MAP);
    }
}
