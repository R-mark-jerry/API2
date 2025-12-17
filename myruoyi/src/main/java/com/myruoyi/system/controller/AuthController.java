package com.myruoyi.system.controller;

import com.myruoyi.common.core.result.Result;
import com.myruoyi.system.dto.LoginDTO;
import com.myruoyi.system.dto.LoginUserDTO;
import com.myruoyi.system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Tag(name = "认证管理", description = "认证相关接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginUserDTO> login(@Validated @RequestBody LoginDTO loginDTO) {
        LoginUserDTO loginUser = authService.login(loginDTO);
        return Result.success("登录成功", loginUser);
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.success("登出成功");
    }

    /**
     * 刷新令牌
     */
    @Operation(summary = "刷新令牌")
    @PostMapping("/refresh")
    public Result<LoginUserDTO> refresh() {
        LoginUserDTO loginUser = authService.refresh();
        return Result.success("刷新成功", loginUser);
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public Result<LoginUserDTO> info() {
        LoginUserDTO loginUser = authService.getUserInfo();
        return Result.success(loginUser);
    }
}