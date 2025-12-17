package com.myruoyi.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myruoyi.api.entity.ApiPermission;
import com.myruoyi.api.mapper.ApiPermissionMapper;
import com.myruoyi.api.service.ApiPermissionService;
import com.myruoyi.common.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * API权限服务实现类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Service
public class ApiPermissionServiceImpl extends ServiceImpl<ApiPermissionMapper, ApiPermission> implements ApiPermissionService {

    @Override
    public ApiPermission selectApiPermissionByPermissionId(Long permissionId) {
        return getById(permissionId);
    }

    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<ApiPermission> selectApiPermissionPage(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<ApiPermission> page,
            ApiPermission apiPermission) {
        LambdaQueryWrapper<ApiPermission> wrapper = new LambdaQueryWrapper<>();
        // 这里需要根据实际字段设置查询条件
        // wrapper.eq(StringUtils.hasText(appName), ApiPermission::getAppName, appName);
        // wrapper.eq(StringUtils.hasText(permissionType), ApiPermission::getPermissionType, permissionType);
        // wrapper.eq(StringUtils.hasText(status), ApiPermission::getStatus, status);
        wrapper.orderByDesc(ApiPermission::getCreateTime);
        
        return page(page, wrapper);
    }

    @Override
    public List<ApiPermission> selectApiPermissionList(ApiPermission apiPermission) {
        LambdaQueryWrapper<ApiPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(apiPermission.getApiId() != null, ApiPermission::getApiId, apiPermission.getApiId())
               .eq(StringUtils.hasText(apiPermission.getPermissionType()), ApiPermission::getPermissionType, apiPermission.getPermissionType())
               .eq(apiPermission.getPermissionTarget() != null, ApiPermission::getPermissionTarget, apiPermission.getPermissionTarget())
               .eq(StringUtils.hasText(apiPermission.getPermissionScope()), ApiPermission::getPermissionScope, apiPermission.getPermissionScope())
               .orderByDesc(ApiPermission::getCreateTime);
        
        return list(wrapper);
    }

    @Override
    public List<ApiPermission> selectPermissionsByApiId(Long apiId) {
        return baseMapper.selectPermissionsByApiId(apiId);
    }

    @Override
    public List<ApiPermission> selectPermissionsByUserId(Long userId) {
        return baseMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public List<ApiPermission> selectPermissionsByRoleId(Long roleId) {
        return baseMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    public int insertApiPermission(ApiPermission apiPermission) {
        // 检查权限是否已存在
        LambdaQueryWrapper<ApiPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApiPermission::getApiId, apiPermission.getApiId())
               .eq(ApiPermission::getPermissionType, apiPermission.getPermissionType())
               .eq(ApiPermission::getPermissionTarget, apiPermission.getPermissionTarget())
               .eq(ApiPermission::getPermissionScope, apiPermission.getPermissionScope());
        if (count(wrapper) > 0) {
            throw new BusinessException("权限已存在");
        }
        
        return save(apiPermission) ? 1 : 0;
    }

    @Override
    public int batchInsertPermission(Long apiId, String permissionType, List<Long> permissionTargets, String permissionScope) {
        if (permissionTargets == null || permissionTargets.isEmpty()) {
            return 0;
        }
        
        List<ApiPermission> permissions = new ArrayList<>();
        for (Long target : permissionTargets) {
            // 检查权限是否已存在
            LambdaQueryWrapper<ApiPermission> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ApiPermission::getApiId, apiId)
                   .eq(ApiPermission::getPermissionType, permissionType)
                   .eq(ApiPermission::getPermissionTarget, target)
                   .eq(ApiPermission::getPermissionScope, permissionScope);
            if (count(wrapper) == 0) {
                ApiPermission permission = new ApiPermission();
                permission.setApiId(apiId);
                permission.setPermissionType(permissionType);
                permission.setPermissionTarget(target);
                permission.setPermissionScope(permissionScope);
                permissions.add(permission);
            }
        }
        
        return saveBatch(permissions) ? permissions.size() : 0;
    }

    @Override
    public int deleteApiPermissionByPermissionId(Long permissionId) {
        return removeById(permissionId) ? 1 : 0;
    }

    @Override
    public int deleteBatchPermission(Long apiId, String permissionType, List<Long> permissionTargets) {
        if (permissionTargets == null || permissionTargets.isEmpty()) {
            return 0;
        }
        
        return baseMapper.deleteBatchPermission(apiId, permissionType, permissionTargets);
    }

    @Override
    public boolean checkUserPermission(Long apiId, Long userId, String permissionScope) {
        int count = baseMapper.checkUserPermission(apiId, userId, permissionScope);
        return count > 0;
    }
}