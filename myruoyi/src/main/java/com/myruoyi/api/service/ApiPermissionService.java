package com.myruoyi.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myruoyi.api.entity.ApiPermission;

import java.util.List;

/**
 * API权限服务接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
public interface ApiPermissionService extends IService<ApiPermission> {

    /**
     * 查询API权限
     *
     * @param permissionId API权限主键
     * @return API权限
     */
    ApiPermission selectApiPermissionByPermissionId(Long permissionId);

    /**
     * 分页查询API权限
     *
     * @param page 分页参数
     * @param apiPermission 查询条件
     * @return 分页结果
     */
    com.baomidou.mybatisplus.core.metadata.IPage<ApiPermission> selectApiPermissionPage(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<ApiPermission> page,
            ApiPermission apiPermission);

    /**
     * 查询API权限列表
     *
     * @param apiPermission API权限
     * @return API权限集合
     */
    List<ApiPermission> selectApiPermissionList(ApiPermission apiPermission);

    /**
     * 根据API ID查询权限列表
     *
     * @param apiId API ID
     * @return 权限列表
     */
    List<ApiPermission> selectPermissionsByApiId(Long apiId);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<ApiPermission> selectPermissionsByUserId(Long userId);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<ApiPermission> selectPermissionsByRoleId(Long roleId);

    /**
     * 新增API权限
     *
     * @param apiPermission API权限
     * @return 结果
     */
    int insertApiPermission(ApiPermission apiPermission);

    /**
     * 修改API权限
     *
     * @param apiPermission API权限
     * @return 结果
     */
    int updateApiPermission(ApiPermission apiPermission);

    /**
     * 批量新增API权限
     *
     * @param apiId API ID
     * @param permissionType 权限类型
     * @param permissionTargets 权限目标列表
     * @param permissionScope 权限范围
     * @return 结果
     */
    int batchInsertPermission(Long apiId, String permissionType, List<Long> permissionTargets, String permissionScope);

    /**
     * 删除API权限
     *
     * @param permissionId API权限主键
     * @return 结果
     */
    int deleteApiPermissionByPermissionId(Long permissionId);

    /**
     * 批量删除API权限
     *
     * @param apiId API ID
     * @param permissionType 权限类型
     * @param permissionTargets 权限目标列表
     * @return 结果
     */
    int deleteBatchPermission(Long apiId, String permissionType, List<Long> permissionTargets);

    /**
     * 检查用户是否有API权限
     *
     * @param apiId API ID
     * @param userId 用户ID
     * @param permissionScope 权限范围
     * @return true有权限 false无权限
     */
    boolean checkUserPermission(Long apiId, Long userId, String permissionScope);
}