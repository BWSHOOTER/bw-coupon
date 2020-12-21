package com.bw.coupon.service.impl;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.dao.CouponTemplateDao;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.service.ITemplateBaseService;
import com.bw.coupon.vo.TemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 优惠券模板基础服务接口实现
 */
@Slf4j
@Service
public class TemplateBaseServiceImpl implements ITemplateBaseService {
    private final CouponTemplateDao templateDao;

    @Autowired
    public TemplateBaseServiceImpl(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    /**
     * 根据优惠券模板 id 获取优惠券模板信息
     */
    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws CommonException {
        Optional<CouponTemplate> template = templateDao.findById(id);
        if (!template.isPresent()) {
            throw new CommonException("Template Is Not Exist: " + id);
        }
        return template.get();
    }

    /**
     * 查找所有可用的优惠券模板
     */
    @Override
    public List<TemplateSDK> findAllUsableTemplate() {
        List<CouponTemplate> templates =
                templateDao.findAllByAvailableAndExpired(
                        true, false);
        return templates.stream()
                .map(this::template2TemplateSDK).collect(Collectors.toList());
    }

    /**
     * 获取模板 ids 到 CouponTemplateSDK 的映射
     */
    @Override
    public Map<Integer, TemplateSDK> findIds2TemplateSDK(Collection<Integer> ids) {
        List<CouponTemplate> templates = templateDao.findAllById(ids);
        return templates.stream().map(this::template2TemplateSDK)
                .collect(Collectors.toMap(
                        TemplateSDK::getId, Function.identity()
                ));
    }

    /**
     * 将 CouponTemplate 转换为 CouponTemplateSDK
     * */
    private TemplateSDK template2TemplateSDK(CouponTemplate template) {

        return new TemplateSDK(
                template.getId(),
                template.getName(),
                template.getLogo(),
                template.getIntro(),
                template.getDiscount().getCode(),
                template.getProductLine().getCode(),
                template.getKey(),  // 并不是拼装好的 Template Key
                template.getCustomer().getCode(),
                template.getRule()
        );
    }
}
