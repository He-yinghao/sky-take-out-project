package com.sky.controller.admin;

import com.sky.dto.SetMealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
public class SetMealController {
    private final SetMealService setMealService;

    public SetMealController(SetMealService setMealService) {
        this.setMealService = setMealService;
    }

    // 套餐分页查询
    @GetMapping("/page")
    public Result<PageResult<SetmealVO>> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询:{}", setmealPageQueryDTO);
        PageResult<SetmealVO> pageResult = setMealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }


    // 根据ID查询套餐
    @GetMapping("/{id}")
    public Result<SetmealVO> getSetmealById(@PathVariable Integer id) {
        log.info("根据ID查询套餐:{}", id);
        SetmealVO setmealVO = setMealService.getSetmealById(id);
        return Result.success(setmealVO);
    }


    // 修改套餐
    @PutMapping
    @CachePut(cacheNames = "setmealCache", key = "#setMealDTO.id")
    public Result<Object> edit(@RequestBody SetMealDTO setMealDTO) {
        log.info("修改套餐:{}", setMealDTO);
        setMealService.updateSetmealByID(setMealDTO);
        return Result.success();
    }

    // 起售停售套餐
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<Object> startOrStop(@PathVariable Integer status, Integer id) {
        log.info("起售停售套餐:{},{}", status, id);
        setMealService.startOrStop(status, id);
        return Result.success();
    }

    // 批量删除套餐
    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<Object> delete(@RequestParam List<Integer> ids) {
        log.info("批量删除套餐:{}", ids);
        setMealService.delete(ids);
        return Result.success();
    }

    // 新增套餐
    @PostMapping
    @CachePut(cacheNames = "setmealCache", key = "#setMealDTO.id")
    public Result<Object> add(@RequestBody SetMealDTO setMealDTO) {
        log.info("新增套餐:{}", setMealDTO);
        setMealService.addDishWithFlavor(setMealDTO);
        return Result.success();
    }
}
