package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component//将这个类交给IOC容器管理
@ConfigurationProperties("com.sky.jwt")//这个类属性绑定配置文件中的信息
public class JwtProperties {
    private  String adminkey;
    private  Long adminexpire;
    private String adminTokenName;

    private  String userkey;
    private  Long userexpire;
    private String userTokenName;
}
