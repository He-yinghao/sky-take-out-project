package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);


    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;


    OrderVO getOrderDetails(Orders orders);

    void cancelOrder(Integer id);

    PageResult pageQuery(int pageNum, int pageSize, Integer status);

    void repetition(Integer id);

    void reminder(Integer id);

    void cancle(OrdersCancelDTO ordersCancelDTO);

    OrderStatisticsVO statistics();

    void complete(Integer id);

    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    OrderVO details(Integer id);

    void delivery(Integer id);

    PageResult<Object> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
}
