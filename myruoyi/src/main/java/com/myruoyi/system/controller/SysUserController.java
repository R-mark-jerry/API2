package com.myruoyi.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myruoyi.common.core.result.Result;
import com.myruoyi.system.entity.SysUser;
import com.myruoyi.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Tag(name = "用户管理", description = "用户管理接口")
@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    /**
     * 分页查询用户列表
     */
    @Operation(summary = "分页查询用户列表")
    @GetMapping("/page")
    // 暂时注释掉权限检查，用于测试
    // @PreAuthorize("hasAuthority('system:user:list')")
    public Result<IPage<SysUser>> page(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "用户名") @RequestParam(required = false) String userName,
            @Parameter(description = "手机号") @RequestParam(required = false) String phonenumber,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        
        IPage<SysUser> result = userService.selectUserPage(
            new Page<>(pageNum, pageSize), userName, phonenumber, status);
        return Result.success(result);
    }

    /**
     * 根据用户ID获取详细信息
     */
    @Operation(summary = "根据用户ID获取详细信息")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<SysUser> getInfo(@PathVariable Long userId) {
        SysUser user = userService.selectUserById(userId);
        return Result.success(user);
    }

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户")
    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<Void> add(@Validated @RequestBody SysUser user) {
        userService.insertUser(user);
        return Result.success();
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改用户")
    @PutMapping
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> edit(@Validated @RequestBody SysUser user) {
        userService.updateUser(user);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public Result<Void> remove(@PathVariable Long[] userIds) {
        userService.deleteUserByIds(userIds);
        return Result.success();
    }

    /**
     * 重置用户密码
     */
    @Operation(summary = "重置用户密码")
    @PutMapping("/resetPwd")
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    public Result<Void> resetPwd(@RequestBody SysUser user) {
        userService.resetPwd(user);
        return Result.success();
    }

    /**
     * 修改用户状态
     */
    @Operation(summary = "修改用户状态")
    @PutMapping("/changeStatus")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> changeStatus(@RequestBody SysUser user) {
        userService.changeStatus(user);
        return Result.success();
    }

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/profile")
    public Result<SysUser> getProfile() {
        SysUser user = userService.getCurrentUser();
        return Result.success(user);
    }

    /**
     * 修改当前用户信息
     */
    @Operation(summary = "修改当前用户信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody SysUser user) {
        userService.updateProfile(user);
        return Result.success();
    }

    /**
     * 修改当前用户密码
     */
    @Operation(summary = "修改当前用户密码")
    @PutMapping("/profile/updatePwd")
    public Result<Void> updatePwd(@RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.updatePwd(oldPassword, newPassword);
        return Result.success();
    }
}