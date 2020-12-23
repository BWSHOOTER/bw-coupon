package com.bw.coupon.util;

import com.bw.coupon.vo.GoodsInfo;

import java.math.BigDecimal;
import java.util.List;

public class PriceUtil {
    /**
     * @Description: 根据商品信息列表计算商品总价
     * @Author: BaoWei
     * @Date: 2020/12/21 17:36
     */
    public static double calGoodsTotalCost(List<GoodsInfo> goodsInfos){
        double totalCost = 0;
        for(GoodsInfo goodsInfo: goodsInfos){
            totalCost += goodsInfo.getCount()*goodsInfo.getPrice();
        }
        return retain2Decimals(totalCost);
    }

    /** 保留两位小数 */
    public static double retain2Decimals(double value) {
        // BigDecimal.ROUND_HALF_UP 代表四舍五入
        return new BigDecimal(value)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
}
