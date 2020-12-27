package com.bw.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// 由于此模块没有与数据库交互，没有配置数据库信息，因此需要加入此配置，否则无法正常启动
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class SettlementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SettlementApplication.class,args);
    }
}
