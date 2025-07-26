package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult<Category> page(CategoryPageQueryDTO categoryPageQueryDTO);

    void updateCategoryByID(CategoryDTO categoryDTO);

    void startOrStopCategoryByID(Integer status, Integer id);

    void addCategory(CategoryDTO categoryDTO);

    void deleteCategoryByID(Integer id);

    List<Category> listAllByType(Integer type);

}
