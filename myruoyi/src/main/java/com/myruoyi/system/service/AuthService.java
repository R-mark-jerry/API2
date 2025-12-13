package com.myruoyi.system.service;

import com.myruoyi.system.dto.LoginDTO;
import com.myruoyi.system.dto.LoginUserDTO;

/**
 * 认证服务接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录参数
     * @return 登录用户信息
     */
    LoginUserDTO login(LoginDTO loginDTO);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 刷新令牌
     *
     * @return 登录用户信息
     */
    LoginUserDTO refresh();

    /**
     * 获取用户信息
     *
     * @return 登录用户信息
     */
    LoginUserDTO getUserInfo();
}