package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
public class ShopController {
    private  static final String  key = "SHOP_STATUS";
    private final RedisTemplate<String, Object> redisTemplate;

    public ShopController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 获取当前营业状态
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        log.info("获取当前营业状态");
        String status = (String) redisTemplate.opsForValue().get(key);
        return Result.success(Integer.parseInt(Objects.requireNonNull(status)));
    }

    // 设置营业状态
    @PutMapping("/{status}")
    public Result<Object> setStatus(@PathVariable String status) {
        log.info("修改营业状态:{}", Objects.equals(status, "1") ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(key, status);
        return Result.success();
    }
}
