package com.myruoyi.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myruoyi.api.entity.ApiModule;

import java.util.List;

/**
 * API模块服务接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
public interface ApiModuleService extends IService<ApiModule> {

    /**
     * 查询API模块
     *
     * @param moduleId API模块主键
     * @return API模块
     */
    ApiModule selectApiModuleByModuleId(Long moduleId);

    /**
     * 查询API模块列表
     *
     * @param apiModule API模块
     * @return API模块集合
     */
    List<ApiModule> selectApiModuleList(ApiModule apiModule);

    /**
     * 根据应用ID查询模块列表
     *
     * @param appId 应用ID
     * @return 模块列表
     */
    List<ApiModule> selectModulesByAppId(Long appId);

    /**
     * 新增API模块
     *
     * @param apiModule API模块
     * @return 新增的API模块
     */
    ApiModule insertApiModule(ApiModule apiModule);

    /**
     * 修改API模块
     *
     * @param apiModule API模块
     * @return 结果
     */
    int updateApiModule(ApiModule apiModule);

    /**
     * 批量删除API模块
     *
     * @param moduleIds 需要删除的API模块主键集合
     * @return 结果
     */
    int deleteApiModuleByModuleIds(Long[] moduleIds);

    /**
     * 删除API模块信息
     *
     * @param moduleId API模块主键
     * @return 结果
     */
    int deleteApiModuleByModuleId(Long moduleId);

    /**
     * 查询子模块数量
     *
     * @param parentId 父模块ID
     * @return 子模块数量
     */
    int selectChildModuleCount(Long parentId);

    /**
     * 构建树形结构
     *
     * @param modules 模块列表
     * @return 树形结构列表
     */
    List<ApiModule> buildModuleTree(List<ApiModule> modules);
}