package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final DishMapper dishMapper;
    private final SetMealMapper setMealMapper;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper, DishMapper dishMapper, SetMealMapper setMealMapper) {
        this.categoryMapper = categoryMapper;
        this.dishMapper = dishMapper;
        this.setMealMapper = setMealMapper;
    }

    // 分页查询
    @Override
    public PageResult<Category> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryPageQueryDTO, category);
        // 设置分页参数
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        // 执行分页查询，获取员工列表
        List<Category> employees = categoryMapper.pageConditionalSelectAll(category);
        // 将查询结果转换为Page对象
        Page<Category> p = (Page<Category>) employees;
        // 创建PageResult对象，返回分页查询结果
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    // 修改分类
    @Override
    public void updateCategoryByID(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.updateCategoryByID(category);
    }

    // 启用或禁用分类
    @Override
    public void startOrStopCategoryByID(Integer status, Integer id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.startOrStopCategoryByID(category);
    }

    // 新增分类
    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(StatusConstant.DISABLE);
        categoryMapper.addCategory(category);
    }

    // 根据id删除分类
    @Override
    public void deleteCategoryByID(Integer id) {
        // 先判断分类是否关联菜品若关联则不能删除
        Integer count = dishMapper.countByCategoryId(id);
        if (count > 0) {
            // 当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 先判断分类是否关联套餐若关联则不能删除
        count = setMealMapper.countByCategoryId(id);
        if (count > 0) {
            // 当前分类下有套餐，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        // 正常删除分类
        categoryMapper.deleteCategoryByID(id);
    }

    // 根据type查询分类
    @Override
    public List<Category> listAllByType(Integer type) {
        return categoryMapper.searchAllByType(type);
    }
}
