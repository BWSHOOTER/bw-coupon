package com.bw.coupon.service.impl;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.dao.CouponTemplateDao;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.service.ITemplateBaseService;
import com.bw.coupon.vo.TemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
     * 根据 id 获取优惠券模板信息
     */
    @Override
    public CouponTemplate findTemplateById(Integer id) throws CommonException {
        Optional<CouponTemplate> template = templateDao.findById(id);
        if (!template.isPresent()) {
            throw new CommonException("Template Is Not Exist: " + id);
        }
        return template.get();
    }

    /**
     * 查找所有可用（且未过期）的优惠券模板Vo
     */
    @Override
    public List<TemplateVo> findAllUsableTemplateVos() {
        List<CouponTemplate> templates = templateDao.findAllByIsAvailableAndIsExpired(true, false);
        List<TemplateVo> vos = new ArrayList<>(templates.size());
        for(CouponTemplate template: templates){
            vos.add(template.toVo());
        }
        return vos;
    }

    /**
     * 获取模板 ids 到 CouponTemplateVos 的映射
     */
    @Override
    public Map<Integer, TemplateVo> findIds2TemplateVos(Collection<Integer> ids) {
        List<CouponTemplate> templates = templateDao.findAllById(ids);
        return templates.stream().map(CouponTemplate::toVo)
                .collect(Collectors.toMap(
                        TemplateVo::getId, Function.identity()
                ));
    }
}
