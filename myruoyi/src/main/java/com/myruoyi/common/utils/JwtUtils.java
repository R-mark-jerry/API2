package com.myruoyi.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Slf4j
@Component
public class JwtUtils {

    /**
     * JWT密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT过期时间（毫秒）
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成JWT令牌
     *
     * @param claims 声明
     * @return JWT令牌
     */
    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(claims.get("sub").toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 生成JWT令牌
     *
     * @param subject 主题
     * @return JWT令牌
     */
    public String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        return generateToken(claims);
    }

    /**
     * 生成JWT令牌
     *
     * @param subject 主题
     * @param userId  用户ID
     * @param username 用户名
     * @return JWT令牌
     */
    public String generateToken(String subject, Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        claims.put("userId", userId);
        claims.put("username", username);
        return generateToken(claims);
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("JWT令牌解析失败: {}", e.getMessage());
            throw new JwtException("无效的JWT令牌");
        }
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从令牌中获取主题
     *
     * @param token 令牌
     * @return 主题
     */
    public String getSubjectFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 从令牌中获取过期时间
     *
     * @param token 令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    /**
     * 验证令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @return 是否有效
     */
    public Boolean validateToken(String token) {
        try {
            parseToken(token);
            return !isTokenExpired(token);
        } catch (JwtException e) {
            log.error("JWT令牌验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        Claims claims = parseToken(token);
        // 创建新的claims，因为JWT的Claims对象是不可变的
        Map<String, Object> newClaims = new HashMap<>(claims);
        // 移除原有的时间相关声明，让JWT builder重新设置
        newClaims.remove("iat");
        newClaims.remove("exp");
        
        return Jwts.builder()
                .claims(newClaims)
                .subject(claims.getSubject())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 获取密钥
     *
     * @return 密钥
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}