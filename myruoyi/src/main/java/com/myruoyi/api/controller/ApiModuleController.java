package com.myruoyi.api.controller;

import com.myruoyi.api.entity.ApiModule;
import com.myruoyi.api.service.ApiModuleService;
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
    public Result<Void> add(@Validated @RequestBody ApiModule apiModule) {
        apiModuleService.insertApiModule(apiModule);
        return Result.success();
    }

    /**
     * 修改API模块
     */
    @Operation(summary = "修改API模块")
    @PutMapping
    @PreAuthorize("hasAuthority('api:module:edit')")
    public Result<Void> edit(@Validated @RequestBody ApiModule apiModule) {
        apiModuleService.updateApiModule(apiModule);
        return Result.success();
    }

    /**
     * 删除API模块
     */
    @Operation(summary = "删除API模块")
    @DeleteMapping("/{moduleIds}")
    @PreAuthorize("hasAuthority('api:module:remove')")
    public Result<Void> remove(@PathVariable Long[] moduleIds) {
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
}