package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetMealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.SetMeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {
    private final CategoryMapper categoryMapper;
    private final SetMealMapper setMealMapper;
    private final SetMealDishMapper setMealDishMapper;
    private final DishMapper dishMapper;

    public SetMealServiceImpl(SetMealMapper setMealMapper, CategoryMapper categoryMapper, SetMealDishMapper setMealDishMapper, DishMapper dishMapper) {
        this.setMealMapper = setMealMapper;
        this.categoryMapper = categoryMapper;
        this.setMealDishMapper = setMealDishMapper;
        this.dishMapper = dishMapper;
    }

    @Override
    public PageResult<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 分页查询，使用PageHelper工具
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        // 执行分页查询，获取套餐列表
        List<SetmealVO> setMealVOs = setMealMapper.pageConditionalSelectAll(setmealPageQueryDTO);
        // 为每个套餐设置分类名称
        for (SetmealVO setMealVO : setMealVOs) {
            setMealVO.setCategoryName(categoryMapper.getNameById(setMealVO.getCategoryId()));
        }
        // 将查询结果转换为Page对象
        Page<SetmealVO> p = (Page<SetmealVO>) setMealVOs;
        // 创建PageResult对象，返回分页查询结果
        return new PageResult<>(p.getTotal(), p.getResult());
    }


    // 修改套餐
    @Override
    @Transactional
    public void updateSetmealByID(SetMealDTO setMealDTO) {
        // 1.修改套餐信息
        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setMeal);
        setMealMapper.updateSetmealByID(setMeal);
        // 2.修改套餐中的菜品组成信息
        List<SetmealDish> setmealDishes = setMealDTO.getSetmealDishes();
        // 2.1.删除当前套餐下的所有菜品信息
        setMealDishMapper.deleteSetmealDishBySetmealId(setMealDTO.getId());
        // 2.2.将新的菜品信息插入
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setMealDTO.getId());
            setMealDishMapper.insert(setmealDish);
        }
    }

    @Override
    public SetmealVO getSetmealById(Integer id) {
        // 根据id查询套餐信息
        SetmealVO setmealVO = setMealMapper.getSetmealById(id);
        // 根据套餐id查询套餐菜品关系
        setmealVO.setSetmealDishes(setMealMapper.listSetmealDishBySetmealId(id));
        return setmealVO;
    }

    @Override
    public void startOrStop(Integer status, Integer id) {
        SetMeal setMeal = SetMeal.builder()
                .id(id)
                .status(status)
                .build();
        // 获取当前套餐的所有菜品id
        List<Integer> dishIds = setMealDishMapper.listSetmealDishBySetmealId(id);
        // 根据菜品id查询菜品是否被启售
        List<Dish> dishes = dishMapper.getDishByIds(dishIds);
        // 套餐内包含未启售菜品，不能启售
        for (Dish dish : dishes) {
            if (dish.getStatus().equals(StatusConstant.DISABLE))
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
        }
        setMealMapper.startOrStopStatusByID(setMeal);
    }

    @Override
    @Transactional
    public void delete(List<Integer> ids) {
        // 根据id查询每个套餐信息
        List<SetMeal> setMeals = setMealMapper.getSetmealByIds(ids);
        // 起售中的套餐不能删除
        for (SetMeal setMeal : setMeals) {
            if (setMeal.getStatus().equals(StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        // 删除套餐
        setMealMapper.deleteBatch(ids);
        // 删除套餐菜品关系
        setMealDishMapper.deleteSetmealDishBySetmealIds(ids);
    }

    // 新增套餐
    @Override
    @Transactional
    public void addDishWithFlavor(SetMealDTO setMealDTO) {
        try {
            // 新增套餐
            SetMeal setMeal = new SetMeal();
            BeanUtils.copyProperties(setMealDTO, setMeal);
            setMeal.setStatus(StatusConstant.DISABLE);
            setMealMapper.insert(setMeal);
            // 新增套餐菜品关系
            List<SetmealDish> setmealDishes = setMealDTO.getSetmealDishes();
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setMeal.getId());
                setMealDishMapper.insert(setmealDish);
            }
        } catch (Exception e) {
            throw new BaseException(MessageConstant.SETMEAL_ADD_FAILED);
        }
    }

    @Override
    public List<SetMeal> list(SetMeal setMeal) {
        return setMealMapper.list(setMeal);
    }
    @Override
    public List<DishItemVO> getDishItemById(SetMeal setmeal) {
        return setMealMapper.getDishItemBySetmealId(setmeal);
    }
}
