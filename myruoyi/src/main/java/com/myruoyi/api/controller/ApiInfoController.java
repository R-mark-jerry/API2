package com.myruoyi.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myruoyi.api.entity.ApiInfo;
import com.myruoyi.api.service.ApiInfoService;
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
 * API接口控制器
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Tag(name = "API接口管理", description = "API接口管理接口")
@RestController
@RequestMapping("/api/info")
@RequiredArgsConstructor
public class ApiInfoController {

    private final ApiInfoService apiInfoService;

    /**
     * 分页查询API接口列表
     */
    @Operation(summary = "分页查询API接口列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('api:info:list')")
    public Result<IPage<ApiInfo>> page(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "应用ID") @RequestParam(required = false) Long appId,
            @Parameter(description = "模块ID") @RequestParam(required = false) Long moduleId,
            @Parameter(description = "API编码") @RequestParam(required = false) String apiCode,
            @Parameter(description = "API名称") @RequestParam(required = false) String apiName,
            @Parameter(description = "请求URL") @RequestParam(required = false) String requestUrl,
            @Parameter(description = "API状态") @RequestParam(required = false) String apiStatus,
            @Parameter(description = "发布状态") @RequestParam(required = false) String publishStatus) {
        
        Page<ApiInfo> page = new Page<>(pageNum, pageSize);
        ApiInfo query = new ApiInfo();
        query.setAppId(appId);
        query.setModuleId(moduleId);
        query.setApiCode(apiCode);
        query.setApiName(apiName);
        query.setRequestUrl(requestUrl);
        query.setApiStatus(apiStatus);
        query.setPublishStatus(publishStatus);
        
        IPage<ApiInfo> result = apiInfoService.selectApiInfoPage(page, query);
        return Result.success(result);
    }

    /**
     * 查询API接口列表
     */
    @Operation(summary = "查询API接口列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('api:info:list')")
    public Result<List<ApiInfo>> list(ApiInfo apiInfo) {
        List<ApiInfo> list = apiInfoService.selectApiInfoList(apiInfo);
        return Result.success(list);
    }

    /**
     * 根据应用ID查询接口列表
     */
    @Operation(summary = "根据应用ID查询接口列表")
    @GetMapping("/app/{appId}")
    @PreAuthorize("hasAuthority('api:info:list')")
    public Result<List<ApiInfo>> getApisByAppId(@PathVariable Long appId) {
        List<ApiInfo> list = apiInfoService.selectApisByAppId(appId);
        return Result.success(list);
    }

    /**
     * 根据模块ID查询接口列表
     */
    @Operation(summary = "根据模块ID查询接口列表")
    @GetMapping("/module/{moduleId}")
    @PreAuthorize("hasAuthority('api:info:list')")
    public Result<List<ApiInfo>> getApisByModuleId(@PathVariable Long moduleId) {
        List<ApiInfo> list = apiInfoService.selectApisByModuleId(moduleId);
        return Result.success(list);
    }

    /**
     * 获取API接口详细信息
     */
    @Operation(summary = "获取API接口详细信息")
    @GetMapping("/{apiId}")
    @PreAuthorize("hasAuthority('api:info:query')")
    public Result<ApiInfo> getInfo(@PathVariable Long apiId) {
        ApiInfo apiInfo = apiInfoService.selectApiInfoByApiId(apiId);
        return Result.success(apiInfo);
    }

    /**
     * 新增API接口
     */
    @Operation(summary = "新增API接口")
    @PostMapping
    @PreAuthorize("hasAuthority('api:info:add')")
    public Result<Void> add(@Validated @RequestBody ApiInfo apiInfo) {
        apiInfoService.insertApiInfo(apiInfo);
        return Result.success();
    }

    /**
     * 修改API接口
     */
    @Operation(summary = "修改API接口")
    @PutMapping
    @PreAuthorize("hasAuthority('api:info:edit')")
    public Result<ApiInfo> edit(@Validated @RequestBody ApiInfo apiInfo) {
        apiInfoService.updateApiInfo(apiInfo);
        // 查询更新后的API信息返回
        ApiInfo updatedApiInfo = apiInfoService.selectApiInfoByApiId(apiInfo.getApiId());
        return Result.success(updatedApiInfo);
    }

    /**
     * 删除API接口
     */
    @Operation(summary = "删除API接口")
    @DeleteMapping("/{apiIds}")
    @PreAuthorize("hasAuthority('api:info:remove')")
    public Result<Void> remove(@PathVariable Long[] apiIds) {
        apiInfoService.deleteApiInfoByApiIds(apiIds);
        return Result.success();
    }

    /**
     * 修改API状态
     */
    @Operation(summary = "修改API状态")
    @PutMapping("/changeApiStatus")
    @PreAuthorize("hasAuthority('api:info:edit')")
    public Result<Void> changeApiStatus(@RequestBody ApiInfo apiInfo) {
        apiInfoService.changeApiStatus(apiInfo);
        return Result.success();
    }

    /**
     * 修改发布状态
     */
    @Operation(summary = "修改发布状态")
    @PutMapping("/changePublishStatus")
    @PreAuthorize("hasAuthority('api:info:publish')")
    public Result<Void> changePublishStatus(@RequestBody ApiInfo apiInfo) {
        apiInfoService.changePublishStatus(apiInfo);
        return Result.success();
    }
}