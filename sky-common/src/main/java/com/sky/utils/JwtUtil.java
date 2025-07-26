package com.sky.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JwtUtil {


    public static String generateJWT(String key, Long expire, Map<String, Object> claims) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + expire))
                .sign(algorithm);
    }


    public static Map<String, Object> parseJWT(String key, String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(key);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);
            // 返回载荷中的内容
            return decodedJWT.getClaim("claims").asMap();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
