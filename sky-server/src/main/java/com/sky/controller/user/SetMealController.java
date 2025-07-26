package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.SetMeal;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetMealController {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SetMealService setMealService;

    public SetMealController(RedisTemplate<String, Object> redisTemplate, SetMealService setMealService) {
        this.redisTemplate = redisTemplate;
        this.setMealService = setMealService;
    }

    //    根据分类id查询套餐
    @GetMapping("/list")
    //    todo: 使用Spring data cache缓存套餐数据，为什么无法正常缓存？
    public Result<List<SetMeal>> list(Integer categoryId) {
        String key = "setmealCache::" + categoryId;
        List<SetMeal> setMealList = (List<SetMeal>) redisTemplate.opsForValue().get(key);
        if (setMealList != null && !setMealList.isEmpty()) {
            return Result.success(setMealList);
        }
        SetMeal setmeal = new SetMeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);
        setMealList = setMealService.list(setmeal);
        redisTemplate.opsForValue().set(key, setMealList);
        return Result.success(setMealList);
    }

    //    根据套餐id查询包含的菜品
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Integer id) {
        SetMeal setmeal = new SetMeal();
        setmeal.setId(id);
        setmeal.setStatus(StatusConstant.ENABLE);
        return Result.success(setMealService.getDishItemById(setmeal));
    }
}
