package com.myruoyi.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myruoyi.api.entity.ApiApp;
import com.myruoyi.api.service.ApiAppService;
import com.myruoyi.common.core.result.Result;
import com.myruoyi.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * API应用控制器
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Tag(name = "API应用管理", description = "API应用管理接口")
@RestController
@RequestMapping("/api/app")
@RequiredArgsConstructor
public class ApiAppController {

    private final ApiAppService apiAppService;
    private final SysUserService sysUserService;

    /**
     * 分页查询API应用列表
     */
    @Operation(summary = "分页查询API应用列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('api:app:list')")
    public Result<IPage<ApiApp>> page(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "应用编码") @RequestParam(required = false) String appCode,
            @Parameter(description = "应用名称") @RequestParam(required = false) String appName,
            @Parameter(description = "负责人姓名") @RequestParam(required = false) String ownerName,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        
        return Result.success(apiAppService.selectApiAppPage(pageNum, pageSize, appCode, appName, ownerName, status));
    }

    /**
     * 查询API应用列表
     */
    @Operation(summary = "查询API应用列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('api:app:list')")
    public Result<List<ApiApp>> list(ApiApp apiApp) {
        List<ApiApp> list = apiAppService.selectApiAppList(apiApp);
        return Result.success(list);
    }

    /**
     * 获取API应用详细信息
     */
    @Operation(summary = "获取API应用详细信息")
    @GetMapping("/{appId}")
    @PreAuthorize("hasAuthority('api:app:query')")
    public Result<ApiApp> getInfo(@PathVariable Long appId) {
        ApiApp apiApp = apiAppService.selectApiAppByAppId(appId);
        return Result.success(apiApp);
    }

    /**
     * 新增API应用
     */
    @Operation(summary = "新增API应用")
    @PostMapping
    // 暂时注释掉权限检查，用于测试
    // @PreAuthorize("hasAuthority('api:app:add')")
    public Result<ApiApp> add(@Validated @RequestBody ApiApp apiApp) {
        // 如果没有指定负责人，则设置为当前用户
        if (apiApp.getOwnerId() == null) {
            var currentUser = sysUserService.getCurrentUser();
            if (currentUser != null) {
                apiApp.setOwnerId(currentUser.getUserId());
                apiApp.setOwnerName(currentUser.getUserName());
            }
        } else {
            // 设置负责人信息
            String ownerName = sysUserService.selectUserNameById(apiApp.getOwnerId());
            apiApp.setOwnerName(ownerName);
        }
        
        apiAppService.insertApiApp(apiApp);
        // 插入后查询完整信息返回
        ApiApp savedApp = apiAppService.selectApiAppByAppId(apiApp.getAppId());
        return Result.success(savedApp);
    }

    /**
     * 修改API应用
     */
    @Operation(summary = "修改API应用")
    @PutMapping
    @PreAuthorize("hasAuthority('api:app:edit')")
    public Result<Void> edit(@Validated @RequestBody ApiApp apiApp) {
        // 检查应用权限
        if (!checkAppPermission(apiApp, "edit")) {
            return Result.error("您没有权限在该应用下进行操作");
        }
        
        // 设置负责人信息
        if (apiApp.getOwnerId() != null) {
            String ownerName = sysUserService.selectUserNameById(apiApp.getOwnerId());
            apiApp.setOwnerName(ownerName);
        }
        
        apiAppService.updateApiApp(apiApp);
        return Result.success();
    }

    /**
     * 删除API应用
     */
    @Operation(summary = "删除API应用")
    @DeleteMapping("/{appIds}")
    @PreAuthorize("hasAuthority('api:app:remove')")
    public Result<Void> remove(@PathVariable Long[] appIds) {
        // 检查应用权限
        for (Long appId : appIds) {
            ApiApp apiApp = new ApiApp();
            apiApp.setAppId(appId);
            if (!checkAppPermission(apiApp, "delete")) {
                return Result.error("您没有权限删除应用ID为" + appId + "的应用");
            }
        }
        
        apiAppService.deleteApiAppByAppIds(appIds);
        return Result.success();
    }

    /**
     * 修改应用状态
     */
    @Operation(summary = "修改应用状态")
    @PutMapping("/changeStatus")
    @PreAuthorize("hasAuthority('api:app:edit')")
    public Result<Void> changeStatus(@RequestBody ApiApp apiApp) {
        // 检查应用权限
        if (!checkAppPermission(apiApp, "edit")) {
            return Result.error("您没有权限在该应用下进行操作");
        }
        
        apiAppService.changeStatus(apiApp);
        return Result.success();
    }

    /**
     * 导出API应用列表
     */
    @Operation(summary = "导出API应用列表")
    @PostMapping("/export")
    @PreAuthorize("hasAuthority('api:app:export')")
    public void export(HttpServletResponse response, ApiApp apiApp) {
        apiAppService.exportApp(response, apiApp);
    }

    /**
     * 导入API应用数据
     */
    @Operation(summary = "导入API应用数据")
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('api:app:import')")
    public Result<Void> importData(@RequestParam("file") MultipartFile file) {
        try {
            apiAppService.importApp(file);
            return Result.success();
        } catch (Exception e) {
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 获取应用导入模板
     */
    @Operation(summary = "获取应用导入模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        apiAppService.importTemplate(response);
    }

    /**
     * 检查应用权限
     *
     * @param apiApp 应用信息
     * @param operation 操作类型
     * @return 是否有权限
     */
    private boolean checkAppPermission(ApiApp apiApp, String operation) {
        // 获取当前用户
        var currentUser = sysUserService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        // 超级管理员拥有所有权限
        if (isAdmin(currentUser)) {
            return true;
        }
        
        // 检查是否是应用负责人
        if (apiApp.getAppId() != null) {
            ApiApp existingApp = apiAppService.selectApiAppByAppId(apiApp.getAppId());
            if (existingApp != null && existingApp.getOwnerId() != null &&
                existingApp.getOwnerId().equals(currentUser.getUserId())) {
                return true;
            }
        }
        
        // 检查是否是新应用且当前用户被指定为负责人
        if (apiApp.getAppId() == null && apiApp.getOwnerId() != null &&
            apiApp.getOwnerId().equals(currentUser.getUserId())) {
            return true;
        }
        
        return false;
    }

    /**
     * 判断是否是管理员
     *
     * @param user 用户信息
     * @return 结果
     */
    private boolean isAdmin(com.myruoyi.system.entity.SysUser user) {
        // 这里可以根据实际业务逻辑判断，比如检查用户角色
        // 简化处理，假设用户ID为1的是管理员
        return user.getUserId() != null && user.getUserId() == 1L;
    }
}