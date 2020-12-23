package com.bw.coupon.service.impl;

import com.bw.coupon.Entity.CouponTemplate;
import com.bw.coupon.dao.CouponTemplateDao;
import com.bw.coupon.vo.CommonException;
import com.bw.coupon.service.ICouponCreatingService;
import com.bw.coupon.service.ITemplateCreatingService;
import com.bw.coupon.vo.TemplateCreatingRequestVo;
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
public class TemplateCreatingServiceImpl implements ITemplateCreatingService {
    /** 异步服务 */
    private ICouponCreatingService asyncService;
    /** CouponTemplate Dao */
    private CouponTemplateDao templateDao;
    @Autowired
    public TemplateCreatingServiceImpl(ICouponCreatingService asyncService,
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
    public CouponTemplate createCouponTemplate(TemplateCreatingRequestVo request) throws CommonException {
        // 1. 对生成模板的请求 request 进行参数校验
        if(!request.validate()){
            throw new CommonException("BuildTemplate Param Is Not Valid!");
        }
        // 2. 判断同名的优惠券模板是否存在
        if (null != templateDao.findByName(request.getDisplayName())) {
            throw new CommonException("Exist Same Name Template!");
        }
        // 3. 根据 request 构造 CouponTemplate
        CouponTemplate templateEntity = request.toEntity();

        // 4. 将 CouponTemplate 保存到数据库中（返回的 CouponTemplate 具有了完整映射信息）
        templateEntity = templateDao.save(templateEntity);

        // 5. 根据模板异步生成优惠券码，并更新数据库模板可用状态为True
        asyncService.asyncConstructCouponByTemplate(templateEntity);

        return templateEntity;
    }
}
