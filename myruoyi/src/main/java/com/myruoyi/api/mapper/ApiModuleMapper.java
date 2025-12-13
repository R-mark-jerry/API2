package com.myruoyi.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myruoyi.api.entity.ApiModule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * API模块Mapper接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Mapper
public interface ApiModuleMapper extends BaseMapper<ApiModule> {

    /**
     * 根据应用ID查询模块列表
     *
     * @param appId 应用ID
     * @return 模块列表
     */
    List<ApiModule> selectModulesByAppId(@Param("appId") Long appId);

    /**
     * 查询子模块数量
     *
     * @param parentId 父模块ID
     * @return 子模块数量
     */
    int selectChildModuleCount(@Param("parentId") Long parentId);
}