import com.alibaba.fastjson.JSON;
import com.bw.coupon.entity.Coupon;
import com.bw.coupon.vo.TemplateRuleVo.*;

public class test {
    public static void main(String[] args) {
        DiscountRuleTemplate discount = new DiscountRuleTemplate();
        Coupon coupon = new Coupon();
        JSON.toJSONString(
                coupon.getTemplateSDK().getRule().getDiscount());
    }
}
