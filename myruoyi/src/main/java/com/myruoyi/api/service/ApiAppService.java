package com.myruoyi.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myruoyi.api.entity.ApiApp;

import java.util.List;

/**
 * API应用服务接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
public interface ApiAppService extends IService<ApiApp> {

    /**
     * 查询API应用
     *
     * @param appId API应用主键
     * @return API应用
     */
    ApiApp selectApiAppByAppId(Long appId);

    /**
     * 查询API应用列表
     *
     * @param apiApp API应用
     * @return API应用集合
     */
    List<ApiApp> selectApiAppList(ApiApp apiApp);

    /**
     * 新增API应用
     *
     * @param apiApp API应用
     * @return 结果
     */
    int insertApiApp(ApiApp apiApp);

    /**
     * 修改API应用
     *
     * @param apiApp API应用
     * @return 结果
     */
    int updateApiApp(ApiApp apiApp);

    /**
     * 批量删除API应用
     *
     * @param appIds 需要删除的API应用主键集合
     * @return 结果
     */
    int deleteApiAppByAppIds(Long[] appIds);

    /**
     * 删除API应用信息
     *
     * @param appId API应用主键
     * @return 结果
     */
    int deleteApiAppByAppId(Long appId);

    /**
     * 修改应用状态
     *
     * @param apiApp API应用
     * @return 结果
     */
    int changeStatus(ApiApp apiApp);
}