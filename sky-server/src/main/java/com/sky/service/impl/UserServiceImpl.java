package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private final WeChatProperties weChatProperties;
    private final UserMapper userMapper;

    public UserServiceImpl(WeChatProperties weChatProperties, UserMapper userMapper) {
        this.weChatProperties = weChatProperties;
        this.userMapper = userMapper;
    }

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //        1.调用微信接口服务，获得当前微信用户的openid
        String openid = getOpenid(userLoginDTO.getCode());
        //        2.判断openid是否为空，如果为空表示登陆失败，抛出业务异常

        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //        3.判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);
        //        4.如果是新用户，自动完成注册
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
//        5.返回这个用户对象
        return user;
    }

    private String getOpenid(String code) {
        Map<String, String> claims = new HashMap<>();
        claims.put("appid", weChatProperties.getAppid());
        claims.put("secret", weChatProperties.getSecret());
        claims.put("js_code", code);
        claims.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN_URL, claims);
        return JSON.parseObject(json).getString("openid");
    }
}
