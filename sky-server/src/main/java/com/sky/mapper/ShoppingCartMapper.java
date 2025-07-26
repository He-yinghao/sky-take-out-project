package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    // 根据参数查询购物车数据
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image,create_time) values (#{name},#{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{image}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    // 可以根据每行的数据id和数据中的userId进行删除
    void deleteById(ShoppingCart cart);

    void insertBatch(List<ShoppingCart> shoppingCartList);
}
