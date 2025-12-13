package com.myruoyi.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myruoyi.api.entity.ApiInfo;

import java.util.List;

/**
 * API接口服务接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
public interface ApiInfoService extends IService<ApiInfo> {

    /**
     * 查询API接口
     *
     * @param apiId API接口主键
     * @return API接口
     */
    ApiInfo selectApiInfoByApiId(Long apiId);

    /**
     * 查询API接口列表
     *
     * @param apiInfo API接口
     * @return API接口集合
     */
    List<ApiInfo> selectApiInfoList(ApiInfo apiInfo);

    /**
     * 分页查询API接口列表
     *
     * @param page 分页参数
     * @param apiInfo 查询条件
     * @return 分页结果
     */
    IPage<ApiInfo> selectApiInfoPage(Page<ApiInfo> page, ApiInfo apiInfo);

    /**
     * 根据应用ID查询接口列表
     *
     * @param appId 应用ID
     * @return 接口列表
     */
    List<ApiInfo> selectApisByAppId(Long appId);

    /**
     * 根据模块ID查询接口列表
     *
     * @param moduleId 模块ID
     * @return 接口列表
     */
    List<ApiInfo> selectApisByModuleId(Long moduleId);

    /**
     * 新增API接口
     *
     * @param apiInfo API接口
     * @return 结果
     */
    int insertApiInfo(ApiInfo apiInfo);

    /**
     * 修改API接口
     *
     * @param apiInfo API接口
     * @return 结果
     */
    int updateApiInfo(ApiInfo apiInfo);

    /**
     * 批量删除API接口
     *
     * @param apiIds 需要删除的API接口主键集合
     * @return 结果
     */
    int deleteApiInfoByApiIds(Long[] apiIds);

    /**
     * 删除API接口信息
     *
     * @param apiId API接口主键
     * @return 结果
     */
    int deleteApiInfoByApiId(Long apiId);

    /**
     * 修改API状态
     *
     * @param apiInfo API接口
     * @return 结果
     */
    int changeApiStatus(ApiInfo apiInfo);

    /**
     * 修改发布状态
     *
     * @param apiInfo API接口
     * @return 结果
     */
    int changePublishStatus(ApiInfo apiInfo);
}