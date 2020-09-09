package com.bw.coupon.service.impl;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.dao.CouponTemplateDao;
import com.bw.coupon.exception.CommonException;
import com.bw.coupon.service.IAsyncService;
import com.bw.coupon.service.IBuildTemplateService;
import com.bw.coupon.vo.TemplateRequestVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 构建优惠券模板接口实现
 **/
@Data
@NoArgsConstructor
public class BuildTemplateServiceImpl implements IBuildTemplateService {
    /** 异步服务 */
    private IAsyncService asyncService;
    /** CouponTemplate Dao */
    private CouponTemplateDao templateDao;
    @Autowired
    public BuildTemplateServiceImpl(IAsyncService asyncService,
                                    CouponTemplateDao templateDao) {
        this.asyncService = asyncService;
        this.templateDao = templateDao;
    }


    @Override
    public CouponTemplate buildCouponTemplate(TemplateRequestVo request) throws CommonException {
        if(!request.validate()){
            throw new CommonException("BuildTemplate Param Is Not Valid!");
        }

        // 判断同名的优惠券模板是否存在
        if (null != templateDao.findByName(request.getName())) {
            throw new CommonException("Exist Same Name Template!");
        }

        // 构造 CouponTemplate 并保存到数据库中
        CouponTemplate template = requestToTemplate(request);
        template = templateDao.save(template);  //这里的template具有了完整映射信息

        // 根据优惠券模板异步生成优惠券码
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
