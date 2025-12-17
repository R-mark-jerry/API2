package com.myruoyi.api.controller;

import com.myruoyi.api.entity.ApiPermission;
import com.myruoyi.api.service.ApiPermissionService;
import com.myruoyi.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API权限控制器
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Tag(name = "API权限管理", description = "API权限管理接口")
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class ApiPermissionController {

    private final ApiPermissionService apiPermissionService;

    /**
     * 分页查询API权限列表
     */
    @Operation(summary = "分页查询API权限列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('api:permission:list')")
    public Result<com.baomidou.mybatisplus.core.metadata.IPage<ApiPermission>> page(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "应用名称") @RequestParam(required = false) String appName,
            @Parameter(description = "权限类型") @RequestParam(required = false) String permissionType,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ApiPermission> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
        ApiPermission query = new ApiPermission();
        // 这里需要根据实际字段设置查询条件
        // query.setAppName(appName);
        // query.setPermissionType(permissionType);
        // query.setStatus(status);
        
        com.baomidou.mybatisplus.core.metadata.IPage<ApiPermission> result = apiPermissionService.selectApiPermissionPage(page, query);
        return Result.success(result);
    }

    /**
     * 查询API权限列表
     */
    @Operation(summary = "查询API权限列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('api:permission:list')")
    public Result<List<ApiPermission>> list(ApiPermission apiPermission) {
        List<ApiPermission> list = apiPermissionService.selectApiPermissionList(apiPermission);
        return Result.success(list);
    }

    /**
     * 根据API ID查询权限列表
     */
    @Operation(summary = "根据API ID查询权限列表")
    @GetMapping("/api/{apiId}")
    @PreAuthorize("hasAuthority('api:permission:list')")
    public Result<List<ApiPermission>> getPermissionsByApiId(@PathVariable Long apiId) {
        List<ApiPermission> list = apiPermissionService.selectPermissionsByApiId(apiId);
        return Result.success(list);
    }

    /**
     * 根据用户ID查询权限列表
     */
    @Operation(summary = "根据用户ID查询权限列表")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('api:permission:list')")
    public Result<List<ApiPermission>> getPermissionsByUserId(@PathVariable Long userId) {
        List<ApiPermission> list = apiPermissionService.selectPermissionsByUserId(userId);
        return Result.success(list);
    }

    /**
     * 根据角色ID查询权限列表
     */
    @Operation(summary = "根据角色ID查询权限列表")
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('api:permission:list')")
    public Result<List<ApiPermission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        List<ApiPermission> list = apiPermissionService.selectPermissionsByRoleId(roleId);
        return Result.success(list);
    }

    /**
     * 获取API权限详细信息
     */
    @Operation(summary = "获取API权限详细信息")
    @GetMapping("/{permissionId}")
    @PreAuthorize("hasAuthority('api:permission:query')")
    public Result<ApiPermission> getInfo(@PathVariable Long permissionId) {
        ApiPermission apiPermission = apiPermissionService.selectApiPermissionByPermissionId(permissionId);
        return Result.success(apiPermission);
    }

    /**
     * 新增API权限
     */
    @Operation(summary = "新增API权限")
    @PostMapping
    @PreAuthorize("hasAuthority('api:permission:add')")
    public Result<Void> add(@Validated @RequestBody ApiPermission apiPermission) {
        apiPermissionService.insertApiPermission(apiPermission);
        return Result.success();
    }

    /**
     * 批量新增API权限
     */
    @Operation(summary = "批量新增API权限")
    @PostMapping("/batch")
    @PreAuthorize("hasAuthority('api:permission:add')")
    public Result<Integer> batchAdd(@RequestParam Long apiId,
                                  @RequestParam String permissionType,
                                  @RequestParam List<Long> permissionTargets,
                                  @RequestParam String permissionScope) {
        int count = apiPermissionService.batchInsertPermission(apiId, permissionType, permissionTargets, permissionScope);
        return Result.success(count);
    }

    /**
     * 删除API权限
     */
    @Operation(summary = "删除API权限")
    @DeleteMapping("/{permissionId}")
    @PreAuthorize("hasAuthority('api:permission:remove')")
    public Result<Void> remove(@PathVariable Long permissionId) {
        apiPermissionService.deleteApiPermissionByPermissionId(permissionId);
        return Result.success();
    }

    /**
     * 批量删除API权限
     */
    @Operation(summary = "批量删除API权限")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('api:permission:remove')")
    public Result<Integer> batchRemove(@RequestParam Long apiId,
                                   @RequestParam String permissionType,
                                   @RequestParam List<Long> permissionTargets) {
        int count = apiPermissionService.deleteBatchPermission(apiId, permissionType, permissionTargets);
        return Result.success(count);
    }

    /**
     * 检查用户权限
     */
    @Operation(summary = "检查用户权限")
    @GetMapping("/check")
    @PreAuthorize("hasAuthority('api:permission:query')")
    public Result<Boolean> checkPermission(@RequestParam Long apiId,
                                      @RequestParam Long userId,
                                      @RequestParam String permissionScope) {
        boolean hasPermission = apiPermissionService.checkUserPermission(apiId, userId, permissionScope);
        return Result.success(hasPermission);
    }
}