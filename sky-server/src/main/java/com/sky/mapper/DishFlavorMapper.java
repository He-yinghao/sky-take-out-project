package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    // 批量插入菜品口味
    void insertBatch(List<DishFlavor> flavors);

    // 根据菜品id查询菜品口味
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> selectByDishId(Integer id);

    void deleteBatch(List<Integer> ids);
}
