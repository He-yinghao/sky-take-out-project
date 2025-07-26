package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    // 类别ID，主键
    private Integer id;
    // 类别类型，用于区分不同的类别
    private Integer type;
    // 类别名称
    private String name;
    // 类别排序，用于在展示时对类别进行排序
    private Integer sort;
    // 类别状态，用于表示类别是否可用
    private Integer status;
    // 类别创建时间
    private LocalDateTime createTime;
    // 类别更新时间
    private LocalDateTime updateTime;
    // 类别创建者的用户ID
    private Integer createUser;
    // 类别更新者的用户ID
    private Integer updateUser;

}
