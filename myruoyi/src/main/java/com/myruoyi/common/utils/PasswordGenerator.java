package com.myruoyi.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
public class PasswordGenerator {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 生成BCrypt加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 验证密码
     *
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 生成123456的BCrypt密码
     *
     * @return 加密后的密码
     */
    public static String generateDefaultPassword() {
        return encode("123456");
    }

    public static void main(String[] args) {
        // 生成123456的BCrypt密码
        String password = generateDefaultPassword();
        System.out.println("123456的BCrypt密码: " + password);
        
        // 验证密码
        boolean matches = matches("123456", password);
        System.out.println("密码验证结果: " + matches);
    }
}