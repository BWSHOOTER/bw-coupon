package com.bw.coupon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 定制 HTTP 消息转换器
 * 用来将java数据对象转换成http数据流
 * SpringBoot底层用HttpMessageConverter，将java对象转换成jackson数据串格式
 * 当有多个转换器时，会选择最合适的使用
 *
 * 直接指定，不用选择
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    //这里的converters对应当前系统中所有的消息转换器
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}