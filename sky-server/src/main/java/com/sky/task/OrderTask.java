package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    private final OrderMapper orderMapper;

    public OrderTask(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Scheduled(cron = "0 0/1 * * * ?")// 每分钟发触发一次
    public void processTimeOutOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        // 未支付订单超过十五分钟未支付，则取消订单
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> orderslist = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if (orderslist != null && !orderslist.isEmpty()) {
            for (Orders orders : orderslist) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    @Scheduled(cron = "0 0 1  * * ?") // 每天凌晨一点执行一次
    public void processDeliveryOrder() {
        log.info("定时处理处于待派送状态的订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().minusMinutes(60);
        List<Orders> orderslist = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (orderslist != null && !orderslist.isEmpty()) {
            for (Orders orders : orderslist) {
                orders.setStatus(Orders.CANCELLED);
                orderMapper.update(orders);
            }
        }
    }
}
