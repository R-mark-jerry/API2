package com.myruoyi.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myruoyi.api.entity.ApiInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * API接口Mapper接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Mapper
public interface ApiInfoMapper extends BaseMapper<ApiInfo> {

    /**
     * 根据应用ID查询接口列表
     *
     * @param appId 应用ID
     * @return 接口列表
     */
    List<ApiInfo> selectApisByAppId(@Param("appId") Long appId);

    /**
     * 根据模块ID查询接口列表
     *
     * @param moduleId 模块ID
     * @return 接口列表
     */
    List<ApiInfo> selectApisByModuleId(@Param("moduleId") Long moduleId);
}