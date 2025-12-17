package com.myruoyi.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myruoyi.api.entity.ApiApp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * API应用Mapper接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Mapper
public interface ApiAppMapper extends BaseMapper<ApiApp> {

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
     * 删除API应用
     *
     * @param appId API应用主键
     * @return 结果
     */
    int deleteApiAppByAppId(Long appId);

    /**
     * 批量删除API应用
     *
     * @param appIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteApiAppByAppIds(Long[] appIds);
}