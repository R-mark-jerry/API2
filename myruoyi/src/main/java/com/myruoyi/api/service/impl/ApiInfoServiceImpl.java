package com.myruoyi.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myruoyi.api.entity.ApiInfo;
import com.myruoyi.api.mapper.ApiInfoMapper;
import com.myruoyi.api.service.ApiInfoService;
import com.myruoyi.common.core.exception.BusinessException;
import com.myruoyi.system.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * API接口服务实现类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Service
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo> implements ApiInfoService {

    private final SysUserService sysUserService;

    public ApiInfoServiceImpl(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public ApiInfo selectApiInfoByApiId(Long apiId) {
        return getById(apiId);
    }

    @Override
    public List<ApiInfo> selectApiInfoList(ApiInfo apiInfo) {
        LambdaQueryWrapper<ApiInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(apiInfo.getAppId() != null, ApiInfo::getAppId, apiInfo.getAppId())
               .eq(apiInfo.getModuleId() != null, ApiInfo::getModuleId, apiInfo.getModuleId())
               .like(StringUtils.hasText(apiInfo.getApiCode()), ApiInfo::getApiCode, apiInfo.getApiCode())
               .like(StringUtils.hasText(apiInfo.getApiName()), ApiInfo::getApiName, apiInfo.getApiName())
               .like(StringUtils.hasText(apiInfo.getRequestUrl()), ApiInfo::getRequestUrl, apiInfo.getRequestUrl())
               .eq(StringUtils.hasText(apiInfo.getApiStatus()), ApiInfo::getApiStatus, apiInfo.getApiStatus())
               .eq(StringUtils.hasText(apiInfo.getPublishStatus()), ApiInfo::getPublishStatus, apiInfo.getPublishStatus())
               .orderByDesc(ApiInfo::getCreateTime);
        
        List<ApiInfo> list = list(wrapper);
        
        // 如果指定了应用ID，则查询应用和模块名称
        if (apiInfo.getAppId() != null && !list.isEmpty()) {
            list = baseMapper.selectApisByAppId(apiInfo.getAppId());
        } else if (apiInfo.getModuleId() != null && !list.isEmpty()) {
            list = baseMapper.selectApisByModuleId(apiInfo.getModuleId());
        }
        
        return list;
    }

    @Override
    public IPage<ApiInfo> selectApiInfoPage(Page<ApiInfo> page, ApiInfo apiInfo) {
        LambdaQueryWrapper<ApiInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(apiInfo.getAppId() != null, ApiInfo::getAppId, apiInfo.getAppId())
               .eq(apiInfo.getModuleId() != null, ApiInfo::getModuleId, apiInfo.getModuleId())
               .like(StringUtils.hasText(apiInfo.getApiCode()), ApiInfo::getApiCode, apiInfo.getApiCode())
               .like(StringUtils.hasText(apiInfo.getApiName()), ApiInfo::getApiName, apiInfo.getApiName())
               .like(StringUtils.hasText(apiInfo.getRequestUrl()), ApiInfo::getRequestUrl, apiInfo.getRequestUrl())
               .eq(StringUtils.hasText(apiInfo.getApiStatus()), ApiInfo::getApiStatus, apiInfo.getApiStatus())
               .eq(StringUtils.hasText(apiInfo.getPublishStatus()), ApiInfo::getPublishStatus, apiInfo.getPublishStatus())
               .orderByDesc(ApiInfo::getCreateTime);
        
        return page(page, wrapper);
    }

    @Override
    public List<ApiInfo> selectApisByAppId(Long appId) {
        return baseMapper.selectApisByAppId(appId);
    }

    @Override
    public List<ApiInfo> selectApisByModuleId(Long moduleId) {
        return baseMapper.selectApisByModuleId(moduleId);
    }

    @Override
    public int insertApiInfo(ApiInfo apiInfo) {
        // 检查API编码是否重复
        LambdaQueryWrapper<ApiInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApiInfo::getAppId, apiInfo.getAppId())
               .eq(ApiInfo::getApiCode, apiInfo.getApiCode());
        if (count(wrapper) > 0) {
            throw new BusinessException("API编码已存在");
        }
        
        // 设置默认内容类型
        if (!StringUtils.hasText(apiInfo.getContentType())) {
            apiInfo.setContentType("application/json");
        }
        
        // 设置默认状态
        if (!StringUtils.hasText(apiInfo.getApiStatus())) {
            apiInfo.setApiStatus("0"); // 开发中
        }
        
        if (!StringUtils.hasText(apiInfo.getPublishStatus())) {
            apiInfo.setPublishStatus("0"); // 未发布
        }
        
        // 设置负责人信息
        if (apiInfo.getOwnerId() != null) {
            String ownerName = sysUserService.selectUserNameById(apiInfo.getOwnerId());
            apiInfo.setOwnerName(ownerName);
        }
        
        return save(apiInfo) ? 1 : 0;
    }

    @Override
    public int updateApiInfo(ApiInfo apiInfo) {
        // 如果修改了API编码，检查是否重复
        if (StringUtils.hasText(apiInfo.getApiCode())) {
            LambdaQueryWrapper<ApiInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ApiInfo::getAppId, apiInfo.getAppId())
                   .eq(ApiInfo::getApiCode, apiInfo.getApiCode())
                   .ne(ApiInfo::getApiId, apiInfo.getApiId());
            if (count(wrapper) > 0) {
                throw new BusinessException("API编码已存在");
            }
        }
        
        // 设置负责人信息
        if (apiInfo.getOwnerId() != null) {
            String ownerName = sysUserService.selectUserNameById(apiInfo.getOwnerId());
            apiInfo.setOwnerName(ownerName);
        }
        
        return updateById(apiInfo) ? 1 : 0;
    }

    @Override
    public int deleteApiInfoByApiIds(Long[] apiIds) {
        return removeByIds(Arrays.asList(apiIds)) ? apiIds.length : 0;
    }

    @Override
    public int deleteApiInfoByApiId(Long apiId) {
        return removeById(apiId) ? 1 : 0;
    }

    @Override
    public int changeApiStatus(ApiInfo apiInfo) {
        return updateById(apiInfo) ? 1 : 0;
    }

    @Override
    public int changePublishStatus(ApiInfo apiInfo) {
        return updateById(apiInfo) ? 1 : 0;
    }
}