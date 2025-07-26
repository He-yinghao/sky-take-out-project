package com.sky.service;

import com.sky.dto.SetMealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetMeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    PageResult<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    void updateSetmealByID(SetMealDTO setMealDTO);

    SetmealVO getSetmealById(Integer id);

    void startOrStop(Integer status, Integer id);

    void delete(List<Integer> ids);

    void addDishWithFlavor(SetMealDTO setMealDTO);

    List<SetMeal> list(SetMeal setMeal);

    List<DishItemVO> getDishItemById(SetMeal setmeal);
}
