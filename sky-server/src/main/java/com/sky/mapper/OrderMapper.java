package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);


    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);


    void update(Orders orders);


    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} " +
            "where number = #{orderNumber}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, String orderNumber);


    // select * from orders where status = ? and order_time < ?
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime time);


    @Select("select * from orders where id = #{id}")
    Orders getOrderById(Integer id);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    Double sumTurnoverByMap(Map<String, Object> map);

    Integer countOrdersByMap(Map<String, Object> map);

    // 统计指定时间内的销量前十
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);

    @Select("select count(id) from orders where status = #{status}")
    Integer getOrderNumberByStatus(Integer status);
}
