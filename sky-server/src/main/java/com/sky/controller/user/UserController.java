package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtProperties jwtProperties;

    public UserController(JwtProperties jwtProperties, UserService userService) {
        this.jwtProperties = jwtProperties;
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("微信用户登陆凭证：{}", userLoginDTO.getCode());
//        微信登陆
        User user = userService.wxLogin(userLoginDTO);
//        为微信用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.generateJWT(jwtProperties.getUserkey(), jwtProperties.getUserexpire(), claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    //    退出登陆请求
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }
}
