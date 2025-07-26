package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    PageResult<DishVO> page(Integer page, Integer pageSize, String name, Integer categoryId, Integer status);

    void addDishWithFlavor(DishDTO dishDTO);

    DishVO getDishById(Integer id);

    List<Dish> list(Dish dish);

    void updateDishStatus(DishDTO dishDTO);

    void updateDishInformation(DishDTO dishDTO);

    void delete(List<Integer> ids);

    List<DishVO> listWithFlavor(Dish dish);

}
