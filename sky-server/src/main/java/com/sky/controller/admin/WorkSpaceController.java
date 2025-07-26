package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/workspace")
public class WorkSpaceController {
    private final WorkSpaceService workspaceService;

    public WorkSpaceController(WorkSpaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    // 查询今日运营数据
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData() {
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        return Result.success(workspaceService.getBusinessData(begin, end));
    }

    // 查询订单管理数据
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> overviewOrders() {
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        return Result.success(workspaceService.getOrderData(begin, end));
    }

    // 菜品总览
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> overviewDishes() {
        // 查询已停售和启售的菜品数量

        return Result.success(workspaceService.getDishOverView());
    }

    // 套餐总览
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> overviewSetmeals() {
        return Result.success(workspaceService.getSetmealOverView());
    }

}
