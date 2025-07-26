package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetMeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;
    private final CategoryMapper categoryMapper;
    private final DishFlavorMapper dishFlavorMapper;
    private final SetMealDishMapper setMealDishMapper;
    private final SetMealMapper setMealMapper;

    public DishServiceImpl(DishMapper dishMapper, CategoryMapper categoryMapper, DishFlavorMapper dishFlavorMapper, SetMealDishMapper setMealDishMapper, SetMealMapper setMealMapper) {
        this.dishMapper = dishMapper;
        this.categoryMapper = categoryMapper;
        this.dishFlavorMapper = dishFlavorMapper;
        this.setMealDishMapper = setMealDishMapper;
        this.setMealMapper = setMealMapper;
    }

    // 菜品分页查询
    @Override
    public PageResult<DishVO> page(Integer page, Integer pageSize, String name, Integer categoryId, Integer status) {
        PageHelper.startPage(page, pageSize);
        // 执行分页查询，获取菜品列表
        List<DishVO> dishVOS = dishMapper.pageConditionalSelectAll(name, categoryId, status);
        for (DishVO dishVO : dishVOS) {
            dishVO.setCategoryName(categoryMapper.getNameById(dishVO.getCategoryId()));
        }
        // 将查询结果转换为Page对象
        Page<DishVO> p = (Page<DishVO>) dishVOS;
        // 创建PageResult对象，返回分页查询结果
        return new PageResult<DishVO>(p.getTotal(), p.getResult());
    }

    // 新增菜品
    @Override
    @Transactional
    public void addDishWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 菜品表添加一条数据
        dishMapper.insert(dish);
        Integer dishId = dish.getId();
        // 口味表添加多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            // 为每条口味数据设置菜品id
            for (DishFlavor dishFlavor : flavors) {
                dishFlavor.setDishId(dishId);
            }
            // 向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public DishVO getDishById(Integer id) {
        DishVO dishVO = new DishVO();
        // 根据id查询菜品
        Dish dish = dishMapper.getDishById(id);
        BeanUtils.copyProperties(dish, dishVO);
        // 根据菜品id查询口味
        List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(id);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    public List<Dish> list(Dish dish) {
        return dishMapper.list(dish);
    }

    @Override
    @Transactional
    public void updateDishStatus(DishDTO dishDTO) {
        // 菜品停售导致关联的套餐也要停售
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateDishStatus(dish);
        // 根据菜品id获取关联的所有套餐id
        List<Integer> setmealIds = setMealDishMapper.listSetmealDishByDishId(dishDTO.getId());
        // 停售所有这些套餐
        if (dish.getStatus().equals(StatusConstant.DISABLE))
            for (Integer setmealId : setmealIds)
                setMealMapper.startOrStopStatusByID(SetMeal.builder().id(setmealId).status(dish.getStatus()).build());
    }

    // 修改菜品
    @Override
    public void updateDishInformation(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateDishInformation(dish);
//        todo:当修改菜品的口味信息时，这里并没有考虑。
    }

    // 批量删除菜品
    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        // 根据ids查询所有菜品信息
        List<Dish> dishes = dishMapper.getDishByIds(ids);
        // 启售的菜品不能删除
        for (Dish dish : dishes) {
            if (dish.getStatus().equals(StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        // 当前菜品关联了套餐不能删除
        for (Dish dish : dishes) {
            List<SetmealDish> setMealDishes = setMealDishMapper.selectSetMealDishByDishId(dish.getId());
            if (!setMealDishes.isEmpty())
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        dishMapper.deleteBatch(ids);
        dishFlavorMapper.deleteBatch(ids);
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);
        List<DishVO> dishVOList = new ArrayList<>();
        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);
            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(d.getId());
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }
}
