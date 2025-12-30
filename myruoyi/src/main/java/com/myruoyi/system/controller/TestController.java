package com.myruoyi.system.controller;

import com.myruoyi.common.core.result.Result;
import com.myruoyi.system.entity.SysUser;
import com.myruoyi.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试控制器
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Tag(name = "测试接口", description = "测试接口")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final SysUserService userService;

    /**
     * 测试数据库连接
     */
    @Operation(summary = "测试数据库连接")
    @GetMapping("/db")
    public Result<String> testDb() {
        try {
            // 测试数据库连接
            long count = userService.count();
            return Result.success("数据库连接正常，用户总数：" + count);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("数据库连接失败：" + e.getMessage());
        }
    }

    /**
     * 测试用户表查询
     */
    @Operation(summary = "测试用户表查询")
    @GetMapping("/user")
    public Result<List<SysUser>> testUser() {
        try {
            // 简单查询用户表
            List<SysUser> list = userService.list();
            return Result.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询用户表失败：" + e.getMessage());
        }
    }
}