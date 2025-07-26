package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    //    添加购物车
    @PostMapping("/add")
    public Result<Object> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    // 查询购物车
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        return Result.success(shoppingCartService.showShoppingCart());
    }

    // 删除购物成中的一个商品（数量减一）
    @PostMapping("/sub")
    public Result<Object> sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();
    }

    // 清空购物车
    @DeleteMapping("/clean")
    public Result<Object> clean() {
        shoppingCartService.clean();
        return Result.success();
    }
}
