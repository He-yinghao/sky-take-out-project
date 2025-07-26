package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final DishMapper dishMapper;
    private final SetMealMapper setmealMapper;

    public WorkSpaceServiceImpl(OrderMapper orderMapper, UserMapper userMapper, DishMapper dishMapper, SetMealMapper setmealMapper) {
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
        this.dishMapper = dishMapper;
        this.setmealMapper = setmealMapper;
    }

    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        // 1.计算今日营业额
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", Orders.COMPLETED);
        Double turnOver = orderMapper.sumTurnoverByMap(map);
        // 2.计算今日有效订单数
        Integer orderCount = getOrderCount(begin, end, null);
        Integer validOrderCount = getOrderCount(begin, end, Orders.COMPLETED);
        // 3.计算今日订单完成率
        double orderCompletionRate = 0.0;
        if (orderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / orderCount;
        }
        // 4.计算今日平均客单价
        double unitPrice = 0.0;
        if (validOrderCount != 0) {
            unitPrice = orderMapper.sumTurnoverByMap(map) / validOrderCount;
        }
        // 5. 计算今日新增用户数
        Integer newUserCount = userMapper.countByMap(map);
        return BusinessDataVO.builder()
                .turnover(turnOver)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(Math.round(unitPrice * 1000.0) / 1000.0)
                .newUsers(newUserCount)
                .build();
    }

    @Override
    public OrderOverViewVO getOrderData(LocalDateTime begin, LocalDateTime end) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", null);
        // 1.查询今日全部订单数量
        Integer allOrders = orderMapper.countOrdersByMap(map);
        // 2.查询今日待接单数量
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = orderMapper.countOrdersByMap(map);
        // 3.查询今日待派送数量
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countOrdersByMap(map);
        // 4.查询今日已完成数量
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countOrdersByMap(map);
        // 5.查询今日已取消数量
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countOrdersByMap(map);
        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    @Override
    public DishOverViewVO getDishOverView() {
        // 查询当前菜品的启售和停售数量
        Integer sold = dishMapper.getDishNumberByStatus(StatusConstant.ENABLE);
        Integer discontinued = dishMapper.getDishNumberByStatus(StatusConstant.DISABLE);
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    @Override
    public SetmealOverViewVO getSetmealOverView() {
        Integer sold = setmealMapper.getSetmealNumberByStatus(StatusConstant.ENABLE);
        Integer discontinued = setmealMapper.getSetmealNumberByStatus(StatusConstant.DISABLE);
        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return orderMapper.countOrdersByMap(map);
    }
}
