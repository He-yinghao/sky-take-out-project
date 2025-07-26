package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
@Slf4j
// 自定义的定时任务类
public class MyTask {
    @Scheduled(cron = "0/1 * * * * ?")
    public void executeTask() {
        log.info("task1 is running:{}", new Date());
    }
}
