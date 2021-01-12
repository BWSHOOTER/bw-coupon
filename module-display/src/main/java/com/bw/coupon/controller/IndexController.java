package com.bw.coupon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    /** 选择为消费者还是操作者 */
    @GetMapping("/index/toCustomerLogin")
    public String toCustomerLogin(){
        return "customerLogin";
    }

    @GetMapping("/index/toOperatorLogin")
    public String toOperatorLogin(){
        return "operatorLogin";
    }
}
