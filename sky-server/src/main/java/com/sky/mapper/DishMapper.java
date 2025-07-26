package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {
    //根据分类id查询菜品数量
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Integer categoryId);

    // 条件分页查询菜品
    List<DishVO> pageConditionalSelectAll(String name, Integer categoryId, Integer status);

    // 插入菜品
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    // 根据id查询菜品
    @Select("select * from dish where id = #{id}")
    Dish getDishById(Integer id);

    // 根据分类id查询菜品
    List<Dish> list(Dish dish);

    // 修改菜品状态
    @AutoFill(OperationType.UPDATE)
    @Update("update dish set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id =#{id}")
    void updateDishStatus(Dish dish);

    // 修改菜品信息
    @AutoFill(OperationType.UPDATE)
    @Update("update dish set name = #{name}, category_id = #{categoryId}, price = #{price}, image = #{image}, description = #{description}, status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    void updateDishInformation(Dish dish);

    // 批量删除菜品
    void deleteBatch(List<Integer> ids);

    // 根据ids查询菜品
    List<Dish> getDishByIds(List<Integer> ids);

    @Select("select count(id) from dish where status = #{status}")
    Integer getDishNumberByStatus(Integer status);
}
