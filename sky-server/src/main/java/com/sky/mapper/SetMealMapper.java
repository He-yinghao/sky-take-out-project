package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetMeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetMealMapper {

    // 根据分类id查询套餐的数量
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Integer id);

    List<SetmealVO> pageConditionalSelectAll(SetmealPageQueryDTO setmealPageQueryDTO);

    @AutoFill(OperationType.UPDATE)
    @Update("update setmeal set name = #{name}, category_id = #{categoryId}, price = #{price}, image = #{image}, description = #{description}, status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    void updateSetmealByID(SetMeal setMeal);

    @Select("select * from setmeal where id = #{id}")
    SetmealVO getSetmealById(Integer id);

    @Select("select * from setmeal_dish where setmeal_id = #{setmealID}")
    List<SetmealDish> listSetmealDishBySetmealId(Integer setmealID);


    @AutoFill(OperationType.UPDATE)
    @Update("update setmeal set status = #{status} where id = #{id}")
    void startOrStopStatusByID(SetMeal setMeal);

    void deleteBatch(List<Integer> ids);

    @AutoFill(OperationType.INSERT)
    void insert(SetMeal setMeal);


    List<SetMeal> getSetmealByIds(List<Integer> ids);

    List<SetMeal> list(SetMeal setmeal);


    List<DishItemVO> getDishItemBySetmealId(SetMeal setmeal);

    @Select("select count(id) from setmeal where status = #{status}")
    Integer getSetmealNumberByStatus(Integer status);
}
