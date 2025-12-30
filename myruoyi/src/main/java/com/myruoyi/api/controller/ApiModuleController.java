package com.myruoyi.api.controller;

import com.myruoyi.api.entity.ApiModule;
import com.myruoyi.api.service.ApiModuleService;
import com.myruoyi.common.core.result.Result;
import com.myruoyi.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API模块控制器
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Tag(name = "API模块管理", description = "API模块管理接口")
@RestController
@RequestMapping("/api/module")
@RequiredArgsConstructor
public class ApiModuleController {

    private final ApiModuleService apiModuleService;
    private final SysUserService sysUserService;

    /**
     * 查询API模块列表
     */
    @Operation(summary = "查询API模块列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('api:module:list')")
    public Result<List<ApiModule>> list(ApiModule apiModule) {
        List<ApiModule> list = apiModuleService.selectApiModuleList(apiModule);
        return Result.success(list);
    }

    /**
     * 查询API模块树形结构
     */
    @Operation(summary = "查询API模块树形结构")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('api:module:list')")
    public Result<List<ApiModule>> tree(@Parameter(description = "应用ID") @RequestParam(required = false) Long appId) {
        ApiModule query = new ApiModule();
        query.setAppId(appId);
        List<ApiModule> list = apiModuleService.selectApiModuleList(query);
        List<ApiModule> tree = apiModuleService.buildModuleTree(list);
        return Result.success(tree);
    }

    /**
     * 根据应用ID查询模块列表
     */
    @Operation(summary = "根据应用ID查询模块列表")
    @GetMapping("/app/{appId}")
    @PreAuthorize("hasAuthority('api:module:list')")
    public Result<List<ApiModule>> getModulesByAppId(@PathVariable Long appId) {
        List<ApiModule> list = apiModuleService.selectModulesByAppId(appId);
        return Result.success(list);
    }

    /**
     * 获取API模块详细信息
     */
    @Operation(summary = "获取API模块详细信息")
    @GetMapping("/{moduleId}")
    @PreAuthorize("hasAuthority('api:module:query')")
    public Result<ApiModule> getInfo(@PathVariable Long moduleId) {
        ApiModule apiModule = apiModuleService.selectApiModuleByModuleId(moduleId);
        return Result.success(apiModule);
    }

    /**
     * 新增API模块
     */
    @Operation(summary = "新增API模块")
    @PostMapping
    @PreAuthorize("hasAuthority('api:module:add')")
    public Result<ApiModule> add(@Validated @RequestBody ApiModule apiModule) {
        // 如果没有指定负责人，则设置为当前用户
        if (apiModule.getResponsibleUserId() == null) {
            var currentUser = sysUserService.getCurrentUser();
            if (currentUser != null) {
                apiModule.setResponsibleUserId(currentUser.getUserId());
                apiModule.setResponsibleUserName(currentUser.getUserName());
            }
        } else {
            // 设置负责人信息
            String responsibleUserName = sysUserService.selectUserNameById(apiModule.getResponsibleUserId());
            apiModule.setResponsibleUserName(responsibleUserName);
        }
        
        ApiModule savedModule = apiModuleService.insertApiModule(apiModule);
        return Result.success(savedModule);
    }

    /**
     * 修改API模块
     */
    @Operation(summary = "修改API模块")
    @PutMapping
    @PreAuthorize("hasAuthority('api:module:edit')")
    public Result<ApiModule> edit(@Validated @RequestBody ApiModule apiModule) {
        // 检查模块权限
        if (!checkModulePermission(apiModule, "edit")) {
            return Result.error("您没有权限在该模块下进行操作");
        }
        
        // 设置负责人信息
        if (apiModule.getResponsibleUserId() != null) {
            String responsibleUserName = sysUserService.selectUserNameById(apiModule.getResponsibleUserId());
            apiModule.setResponsibleUserName(responsibleUserName);
        }
        
        apiModuleService.updateApiModule(apiModule);
        // 查询更新后的模块信息返回
        ApiModule updatedModule = apiModuleService.selectApiModuleByModuleId(apiModule.getModuleId());
        return Result.success(updatedModule);
    }

    /**
     * 删除API模块
     */
    @Operation(summary = "删除API模块")
    @DeleteMapping("/{moduleIds}")
    @PreAuthorize("hasAuthority('api:module:remove')")
    public Result<Void> remove(@PathVariable Long[] moduleIds) {
        // 检查模块权限
        for (Long moduleId : moduleIds) {
            ApiModule apiModule = new ApiModule();
            apiModule.setModuleId(moduleId);
            if (!checkModulePermission(apiModule, "delete")) {
                return Result.error("您没有权限删除模块ID为" + moduleId + "的模块");
            }
        }
        
        apiModuleService.deleteApiModuleByModuleIds(moduleIds);
        return Result.success();
    }

    /**
     * 查询子模块数量
     */
    @Operation(summary = "查询子模块数量")
    @GetMapping("/childCount/{parentId}")
    @PreAuthorize("hasAuthority('api:module:query')")
    public Result<Integer> getChildCount(@PathVariable Long parentId) {
        int count = apiModuleService.selectChildModuleCount(parentId);
        return Result.success(count);
    }

    /**
     * 检查模块权限
     *
     * @param apiModule 模块信息
     * @param operation 操作类型
     * @return 是否有权限
     */
    private boolean checkModulePermission(ApiModule apiModule, String operation) {
        // 获取当前用户
        var currentUser = sysUserService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        // 超级管理员拥有所有权限
        if (isAdmin(currentUser)) {
            return true;
        }
        
        // 检查是否是模块负责人
        if (apiModule.getModuleId() != null) {
            ApiModule existingModule = apiModuleService.selectApiModuleByModuleId(apiModule.getModuleId());
            if (existingModule != null && existingModule.getResponsibleUserId() != null &&
                existingModule.getResponsibleUserId().equals(currentUser.getUserId())) {
                return true;
            }
        }
        
        // 检查是否是新模块且当前用户被指定为负责人
        if (apiModule.getModuleId() == null && apiModule.getResponsibleUserId() != null &&
            apiModule.getResponsibleUserId().equals(currentUser.getUserId())) {
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