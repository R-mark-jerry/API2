package com.myruoyi.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myruoyi.api.entity.ApiPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * API权限Mapper接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Mapper
public interface ApiPermissionMapper extends BaseMapper<ApiPermission> {

    /**
     * 分页查询API权限列表
     *
     * @param page 分页参数
     * @param apiPermission 查询条件
     * @param appName 应用名称
     * @param status 状态
     * @return 分页结果
     */
    com.baomidou.mybatisplus.core.metadata.IPage<ApiPermission> selectApiPermissionPage(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<ApiPermission> page,
            @Param("apiPermission") ApiPermission apiPermission,
            @Param("appName") String appName,
            @Param("status") String status);

    /**
     * 根据API ID查询权限列表
     *
     * @param apiId API ID
     * @return 权限列表
     */
    List<ApiPermission> selectPermissionsByApiId(@Param("apiId") Long apiId);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<ApiPermission> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<ApiPermission> selectPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量删除API权限
     *
     * @param apiId API ID
     * @param permissionType 权限类型
     * @param permissionTargets 权限目标列表
     * @return 结果
     */
    int deleteBatchPermission(@Param("apiId") Long apiId, 
                          @Param("permissionType") String permissionType,
                          @Param("permissionTargets") List<Long> permissionTargets);

    /**
     * 检查用户是否有API权限
     *
     * @param apiId API ID
     * @param userId 用户ID
     * @param permissionScope 权限范围
     * @return 权限数量
     */
    int checkUserPermission(@Param("apiId") Long apiId, 
                         @Param("userId") Long userId,
                         @Param("permissionScope") String permissionScope);
}