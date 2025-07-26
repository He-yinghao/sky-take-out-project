package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController("adminDishController")
@RequestMapping("/admin/dish")
public class DishController {
    private final RedisTemplate<String, Object> redisTemplate;
    private final DishService dishService;

    public DishController(RedisTemplate<String, Object> redisTemplate, DishService dishService) {
        this.redisTemplate = redisTemplate;
        this.dishService = dishService;
    }

    // 修改菜品
    @PutMapping
    public Result<Object> update(@RequestBody DishDTO dishDTO) {
        dishService.updateDishInformation(dishDTO);
        // 清理缓存数据,此时可能涉及多个分类的菜品，因此清理所有以dish_开头的缓存数据
        cleanCache("dish_*");
        return Result.success();
    }

    // 菜品分页查询
    @GetMapping("/page")
    public Result<PageResult<DishVO>> page(Integer page, Integer pageSize, String name, Integer categoryId, Integer status) {
        return Result.success(dishService.page(page, pageSize, name, categoryId, status));
    }


    // 新增菜品
    @PostMapping
    public Result<Object> addDish(@RequestBody DishDTO dishDTO) {
        dishService.addDishWithFlavor(dishDTO);
        // 清理缓存数据
        cleanCache("dish_" + dishDTO.getCategoryId());
        return Result.success();
    }


    // 根据id查询菜品
    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable("id") Integer id) {
        return Result.success(dishService.getDishById(id));
    }

    // 根据分类id查询菜品
    @GetMapping("/list")
    public Result<List<Dish>> list(Integer categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        return Result.success(dishService.list(dish));
    }

    // 菜品起售、停售
    @PostMapping("/status/{status}")
    public Result<Object> updateStatus(@PathVariable("status") Integer status, Integer id) {
        DishDTO dishDTO = DishDTO.builder()
                .id(id)
                .status(status)
                .build();
        dishService.updateDishStatus(dishDTO);
        // 清理缓存数据，根据菜品ID查询所属分类ID，对该分类的所有缓存数据进行清理。但是为了操作方便，直接清理掉所有菜品的缓存数据
        // 因为根据分类ID查询菜品，会增加IO操作，不如直接清理所有菜品的缓存数据
        cleanCache("dish_*");
        return Result.success();
    }

    // 批量删除菜品
    @DeleteMapping
    public Result<Object> delete(@RequestParam List<Integer> ids) {
        log.info("批量删除菜品，id为{}", ids);
        dishService.delete(ids);
        // 将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
        return Result.success();
    }

    // 清理缓存数据：根据key的集合进行删除（可以处理一个key和多个key的情况）
    private void cleanCache(String pattern) {
        redisTemplate.delete(redisTemplate.keys(pattern));
    }
}
