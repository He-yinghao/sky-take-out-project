package com.sky;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 声明为一个配置类，将改类中声明的bean对象全部放入IOC容器中
@Configuration
// 将这个bean对象放入IOC容器
@EnableConfigurationProperties(AliyunOssProperties.class)
public class AliyunOssConfiguration {
    @Bean
    public AliyunOssUtil aliyunOssUtil(AliyunOssProperties aliyunOssProperties) {
        return new AliyunOssUtil(aliyunOssProperties);
    }
}
