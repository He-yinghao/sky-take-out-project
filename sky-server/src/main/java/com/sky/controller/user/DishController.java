package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    private final RedisTemplate<String, Object> redisTemplate;
    private final DishService dishService;

    public DishController(DishService dishService, RedisTemplate<String, Object> redisTemplate) {
        this.dishService = dishService;
        this.redisTemplate = redisTemplate;
    }

    //    根据分类id查询菜品
    @GetMapping("/list")
    public Result<List<DishVO>> list(Integer categoryId) {
        //        1.构造redis key，规则：dish_分类id
        String key = "dish_" + categoryId;
        //        2.查询redis中是否存在菜品数据
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && !list.isEmpty()) {
            //        3.如果存在，直接返回，无须查询数据库
            return Result.success(list);
        }
        //        4.如果不存在，查询数据库，将查询到的菜品数据保存到redis中，并返回数据
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//仅查询起售中的菜品
        list = dishService.listWithFlavor(dish);
        redisTemplate.opsForValue().set(key, list);
        return Result.success(list);
    }
}
