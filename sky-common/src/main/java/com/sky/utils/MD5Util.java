package com.sky.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {
    public static String md5(String password) {
        // 使用 DigestUtils 进行 MD5 加密
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
