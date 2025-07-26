package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    @Select("select dish_id from setmeal_dish where setmeal_id = #{id}")
    List<Integer> listSetmealDishBySetmealId(Integer id);

    // 根据ids进行删除
    void deleteSetmealDishBySetmealIds(List<Integer> ids);


    @Insert("insert into setmeal_dish (setmeal_id,dish_id,name,price,copies) values (#{setmealId},#{dishId},#{name},#{price},#{copies})")
    void insert(SetmealDish setmealDish);


    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId} and dish_id=#{dishId}")
    void delete(SetmealDish setmealDish);


    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    void deleteSetmealDishBySetmealId(Integer id);

    @Select("select * from setmeal_dish where dish_id=#{dishId}")
    List<SetmealDish> selectSetMealDishByDishId(Integer dishId);

    @Select("select setmeal_id from setmeal_dish where dish_id = #{id}")
    List<Integer> listSetmealDishByDishId(Integer id);
}
