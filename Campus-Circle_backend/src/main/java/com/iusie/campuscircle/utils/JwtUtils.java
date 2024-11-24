package com.iusie.campuscircle.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

import static com.iusie.campuscircle.constant.UserConstant.EXPIRE_TIME;
import static com.iusie.campuscircle.constant.UserConstant.SECRET_KEY;


/**
 * @author iusie
 * @description
 * @date 2024/11/20
 */
public class JwtUtils {


    //接收业务数据,生成token并返回
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME ))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    //接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }
}
