package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 用户下单
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单，参数为：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    // 订单支付
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        return Result.success(orderPaymentVO);
    }

    // 查询订单详情
    @GetMapping("/orderDetail/{id}")
    public Result<Object> getOrderDetails(@PathVariable("id") Integer id) {
        log.info("查询订单详情：{}", id);
        Orders orders = new Orders();
        orders.setId(id);
        return Result.success(orderService.getOrderDetails(orders));
    }

    // 取消订单
    @PutMapping("/cancel/{id}")
    public Result<Object> cancel(@PathVariable("id") Integer id) {
        log.info("取消订单：{}", id);
        orderService.cancelOrder(id);
        return Result.success();
    }

    // 历史订单查询
    @GetMapping("/historyOrders")
    public Result<PageResult> getHistoryOrders(int page, int pageSize, Integer status) {
        // 历史订单查询：分页查询
        PageResult pageResult = orderService.pageQuery(page, pageSize, status);
        return Result.success(pageResult);
    }

    // 再来一单
    @PostMapping("/repetition/{id}")
    public Result<Object> repetition(@PathVariable("id") Integer id) {
        log.info("再来一单：{}", id);
        orderService.repetition(id);
        return Result.success();
    }

    // 客户催单
    @GetMapping("/reminder/{id}")
    public Result<Object> reminder(@PathVariable("id") Integer id) {
        log.info("客户催单：{}", id);
        orderService.reminder(id);
        return Result.success();
    }
}
