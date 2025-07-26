package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private Integer dishId;
    private Integer setmealId;
    private String dishFlavor;

}
