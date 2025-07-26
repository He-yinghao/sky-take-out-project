package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 取消订单
    @PutMapping("/cancel")
    public Result<OrdersCancelDTO> cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancle(ordersCancelDTO);
        return Result.success();
    }

    // 各个状态的订单数量统计
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        return Result.success(orderService.statistics());
    }

    // 完成订单
    @PutMapping("/complete/{id}")
    public Result<Object> complete(@PathVariable("id") Integer id) {
        orderService.complete(id);
        return Result.success();
    }

    // 拒单
    @PutMapping("/rejection")
    public Result<Object> rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    // 接单
    @PutMapping("/confirm")
    public Result<Object> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    // 查询订单详情
    @GetMapping("/details/{id}")
    public Result<OrderVO> details(@PathVariable("id") Integer id) {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    // 派送订单
    @PutMapping("/delivery/{id}")
    public Result<Object> delivery(@PathVariable("id") Integer id) {
        orderService.delivery(id);
        return Result.success();
    }

    // 订单搜索
    @GetMapping("/conditionSearch")
    public Result<PageResult<Object>> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult<Object> pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }
}
