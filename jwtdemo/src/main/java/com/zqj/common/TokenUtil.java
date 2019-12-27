package com.zqj.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author zqj
 * @create 2019-12-26 20:31
 */
public class TokenUtil {

    private static final String privateKey = "hello";

    private static final String publicKey = "world";

    //获取token
    public static String getToken(String uid,int exp){
        Long endTime = System.currentTimeMillis() + 60* exp * 1000;
        //生成一个jwt,设置用户ID，设置过期时间，签名
        return Jwts.builder().setSubject(uid).setExpiration(new Date(endTime)).signWith(SignatureAlgorithm.RS512,privateKey).compact();
    }

    //校验token是否合法
    public static boolean checkToken(String token){
        try{
            Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
            String sub = claims.get("sub", String.class);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
