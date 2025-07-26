package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableCaching  // 开启缓存数据管理
@EnableTransactionManagement// 开启注解方式的事务管理
@EnableScheduling // 开启定时任务调度
@Slf4j
public class SkyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyServerApplication.class, args);
        log.info("server started");
    }
}
