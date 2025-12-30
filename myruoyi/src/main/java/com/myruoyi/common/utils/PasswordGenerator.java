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
        // 测试数据库中的密码哈希
        String dbPasswordHash = "$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkMlDW";
        boolean matches123456 = matches("123456", dbPasswordHash);
        System.out.println("数据库密码哈希验证 '123456': " + matches123456);
        
        boolean matchesAdmin123 = matches("admin123", dbPasswordHash);
        System.out.println("数据库密码哈希验证 'admin123': " + matchesAdmin123);
        
        // 生成新的123456的BCrypt密码
        String newPassword = generateDefaultPassword();
        System.out.println("新生成的123456的BCrypt密码: " + newPassword);
        
        // 验证新密码
        boolean newMatches = matches("123456", newPassword);
        System.out.println("新密码验证结果: " + newMatches);
    }
}