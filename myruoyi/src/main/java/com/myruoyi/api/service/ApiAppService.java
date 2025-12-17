package com.myruoyi.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myruoyi.api.entity.ApiApp;

import jakarta.servlet.http.HttpServletResponse;
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
     * 分页查询API应用列表
     *
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param appCode 应用编码
     * @param appName 应用名称
     * @param ownerName 负责人姓名
     * @param status 状态
     * @return 分页结果
     */
    IPage<ApiApp> selectApiAppPage(Integer pageNum, Integer pageSize, String appCode, String appName, String ownerName, String status);

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

    /**
     * 导出API应用
     *
     * @param response 响应对象
     * @param apiApp 查询条件
     */
    void exportApp(HttpServletResponse response, ApiApp apiApp);

    /**
     * 导入API应用数据
     *
     * @param file 导入文件
     * @return 结果
     */
    void importApp(org.springframework.web.multipart.MultipartFile file) throws Exception;

    /**
     * 获取应用导入模板
     *
     * @param response 响应对象
     */
    void importTemplate(HttpServletResponse response);
}