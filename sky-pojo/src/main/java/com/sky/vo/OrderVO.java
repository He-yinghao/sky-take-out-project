package com.sky.vo;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true) // Lombok生成的方法可以操作父类属性
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {

    // 订单菜品信息
    private String orderDishes;

    // 订单详情
    private List<OrderDetail> orderDetailList;

}
