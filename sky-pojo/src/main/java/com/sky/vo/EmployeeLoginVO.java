package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeLoginVO implements Serializable {
    private Integer id; // 主键
    private String username; // 用户名
    private String name; // 姓名
    private String token; // JWT 令牌

}
