package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.SetMeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final DishMapper dishMapper;
    private final SetMealMapper setMealMapper;

    public ShoppingCartServiceImpl(ShoppingCartMapper shoppingCartMapper, DishMapper dishMapper, SetMealMapper setMealMapper) {
        this.shoppingCartMapper = shoppingCartMapper;
        this.dishMapper = dishMapper;
        this.setMealMapper = setMealMapper;
    }

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
//        判断当前加入到的购物车项是否在购物车中
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Integer userID = BaseContext.getCurrentId();
        shoppingCart.setUserId(userID);
        log.info("用户ID：{}", userID);


        //        查询购物车当中是否存在当前商品数据
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        //        如果已经存在，只需要将数量加一

        if (list != null && !list.isEmpty()) {
            ShoppingCart cart = list.getFirst();
            cart.setNumber(cart.getNumber() + 1);
            //            update shopping_cart set number = ? where id = ?
            shoppingCartMapper.updateNumberById(cart);
        } else {
            //        如果不存在，需要插入一条购物车数据
            // 判断是菜品还是套餐
            Integer dishId = shoppingCartDTO.getDishId();
            //        判断本次添加到购物车的时菜品还是套餐
            if (dishId != null) {
                // 本次添加到购物车的是菜品
                Dish dish = dishMapper.getDishById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 本次添加到购物车的是套餐
                Integer setmealId = shoppingCartDTO.getSetmealId();
                SetmealVO setmealVO = setMealMapper.getSetmealById(setmealId);
                shoppingCart.setName(setmealVO.getName());
                shoppingCart.setImage(setmealVO.getImage());
                shoppingCart.setAmount(setmealVO.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            //            插入数据
            shoppingCartMapper.insert(shoppingCart);
        }


    }

    @Override
    public List<ShoppingCart> showShoppingCart() {
        // 获取当前请求发出用户的id
        Integer userID = BaseContext.getCurrentId();
        // 根据用户id查询购物车数据
        ShoppingCart shoppingCart = ShoppingCart.
                builder().
                userId(userID).
                build();
        return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        // 删除商品时需要先判断改商品的数量是多少
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list != null && !list.isEmpty()) {
            ShoppingCart cart = list.getFirst();
            Integer number = cart.getNumber();
            if(number == 1){
                // 如果购物车中该商品数量为1，则删除该商品
                shoppingCartMapper.deleteById(cart);
            }else{
                // 如果购物车中该商品数量大于1，则将商品数量减一
                cart.setNumber(number - 1);
                shoppingCartMapper.updateNumberById(cart);
            }
        }
    }

    @Override
    public void clean() {
        // 获取当前发出请求的用户id
        Integer userID = BaseContext.getCurrentId();
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(userID);
        // 根据用户id删除购物车数据
        shoppingCartMapper.deleteById(cart);
    }
}
