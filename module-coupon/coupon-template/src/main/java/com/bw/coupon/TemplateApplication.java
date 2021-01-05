package com.bw.coupon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling       //SpringBoot定时任务
@EnableJpaAuditing      //jpa审核注解，heminate可以自动进行数据的注入
@EnableEurekaClient
@SpringBootApplication
public class TemplateApplication {
    public static void main(String[] args){
        SpringApplication.run(TemplateApplication.class,args);
    }
}
