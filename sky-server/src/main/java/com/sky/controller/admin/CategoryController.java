package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {
    private final CategoryService categoryService;

    // 构造器注入
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 分页查询
    @RequestMapping("/page")
    public Result<PageResult<Category>> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult<Category> pageResult = categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    // 修改分类
    @PutMapping
    public Result<Object> update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategoryByID(categoryDTO);
        return Result.success();
    }

    // 启用或禁用分类
    @PostMapping("/status/{status}")
    public Result<Object> startOrStop(@PathVariable Integer status, Integer id) {
        categoryService.startOrStopCategoryByID(status, id);
        return Result.success();
    }

    // 新增分类
    @PostMapping
    public Result<Object> add(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    // 删除分类
    @DeleteMapping
    public Result<Object> delete(Integer id) {
        categoryService.deleteCategoryByID(id);
        return Result.success();
    }

    // 根据类型查询分类
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        return Result.success(categoryService.listAllByType(type));
    }
}
