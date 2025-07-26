package com.sky;

import com.sky.constant.JwtClaimsConstant;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import com.sky.utils.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


//@SpringBootTest
class SkyServerApplicationTests {
//    @Autowired
//    JwtProperties jwtProperties;


//    @Test
//    void generateJwt() {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(JwtClaimsConstant.EMP_ID, 1);
//        String jwt = JwtUtil.generateJWT(jwtProperties.getAdminkey(), jwtProperties.getAdminexpire(), claims);
//        System.out.println(jwt);
//    }
//
//    @Test
//    void parseJwt() {
//        System.out.println(JwtUtil.parseJWT(jwtProperties.getAdminkey(), "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsiZW1wSWQiOjF9LCJleHAiOjE3MzQzNDAxODl9.2p_K-1hyKRKlC3EncHLouzaIcfMjW_OvRWG5kk5aum8"));
//    }

    @Test
    void md5Test() {
        System.out.println(MD5Util.md5("123456"));
        System.out.println(MD5Util.md5("123823"));
    }

    @Test
    void test() {
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        System.out.println(begin);
        System.out.println(end);
    }

}
