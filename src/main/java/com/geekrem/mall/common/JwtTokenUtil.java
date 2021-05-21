package com.geekrem.mall.common;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成JWTToken的工具类
 * @author geekrem
 * @version 1.0
 * @date 2021/5/21 17:54
 */
@Component
@Slf4j
public class JwtTokenUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String  CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value ("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据负责生成JWT的Token
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder ()
                .setClaims (claims)
                .setExpiration (generateExpirationDate())
                .signWith (SignatureAlgorithm.HS512,secret)
                .compact ();
    }

    /**
     * 生成Token的过期时间
     */
    private Date  generateExpirationDate(){
        return new Date (System.currentTimeMillis () + expiration * 1000);
    }

    /**
     * 从Token中获取jwt负载
     */
    public Claims getClaimsFromToken(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser ()
                    .setSigningKey (secret)
                    .parseClaimsJws (token)
                    .getBody ();
        }catch (Exception e){
            log.debug ("JWT的token验证失败：{}",token);
        }
        return claims;
    }

    /**
     * 从Token中获取用户名
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject ();
        }catch (Exception e){
            username = null;
            log.debug ("获取用户名失败：{}",token);
        }
        return username;
    }

    /**
     * 验证Token是否有效
     *
     * @param token 客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUserNameFromToken (token);
        return username.equals (userDetails.getUsername ()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否有效（是否过期）
     * @param token
     * @return
     */

    private boolean isTokenExpired(String token){
        Date date = getExpiredDateFromToken (token);
        return date.before (new Date ());
    }

    /**
     * 从token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token){
        Claims claims = getClaimsFromToken (token);
        return claims.getExpiration ();
    }

    /**
     * 根据用户信息生成Token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        HashMap<String, Object> claims = new HashMap<> ();
        claims.put (CLAIM_KEY_USERNAME,userDetails.getUsername ());
        claims.put (CLAIM_KEY_CREATED,new Date ());
        return generateToken(claims);
    }

    /**
     * 判断token是否可以被刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    public String refreshToken(String token){
        Claims claims = getClaimsFromToken (token);
        claims.put (CLAIM_KEY_CREATED,new Date ());
        return generateToken (claims);
    }
}
