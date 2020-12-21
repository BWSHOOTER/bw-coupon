package com.bw.coupon.service.impl;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.dao.CouponTemplateDao;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.service.IBuildCouponService;
import com.bw.coupon.service.IBuildTemplateService;
import com.bw.coupon.vo.TemplateRequestVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 生成券模板接口实现
 */
@Data
@NoArgsConstructor
@Service
public class BuildTemplateServiceImpl implements IBuildTemplateService {
    /** 异步服务 */
    private IBuildCouponService asyncService;
    /** CouponTemplate Dao */
    private CouponTemplateDao templateDao;
    @Autowired
    public BuildTemplateServiceImpl(IBuildCouponService asyncService,
                                    CouponTemplateDao templateDao) {
        this.asyncService = asyncService;
        this.templateDao = templateDao;
    }

    /**
     * @Description: 根据生成优惠券模板的请求，生成优惠券模板，并启动异步生成优惠券的任务
     * @Author: BaoWei
     * @Date: 2020/12/15 15:34
     */
    @Override
    public CouponTemplate buildCouponTemplate(TemplateRequestVo request) throws CommonException {
        // 1. 对生成模板的请求 request 进行参数校验
        if(!request.validate()){
            throw new CommonException("BuildTemplate Param Is Not Valid!");
        }
        // 2. 判断同名的优惠券模板是否存在
        if (null != templateDao.findByName(request.getName())) {
            throw new CommonException("Exist Same Name Template!");
        }
        // 3. 根据 request 构造 CouponTemplate
        CouponTemplate template = requestToTemplate(request);
        // 4. 将 CouponTemplate 保存到数据库中（返回的 CouponTemplate 具有了完整映射信息）
        template = templateDao.save(template);

        // 5. 根据模板异步生成优惠券码
        asyncService.asyncConstructCouponByTemplate(template);

        return template;
    }

    /**
     * 将 TemplateRequest 转换为 CouponTemplate
     * */
    private CouponTemplate requestToTemplate(TemplateRequestVo request) {
        return new CouponTemplate(
                request.getName(),
                request.getLogo(),
                request.getIntro(),
                request.getDiscount(),
                request.getProductLine(),
                request.getCount(),
                request.getUserId(),
                request.getDistribute(),
                request.getCustomer(),
                request.getRule()
        );
    }
}
