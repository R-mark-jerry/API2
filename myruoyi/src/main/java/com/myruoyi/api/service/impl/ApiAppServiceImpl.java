package com.myruoyi.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myruoyi.api.entity.ApiApp;
import com.myruoyi.api.mapper.ApiAppMapper;
import com.myruoyi.api.service.ApiAppService;
import com.myruoyi.common.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * API应用服务实现类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Service
public class ApiAppServiceImpl extends ServiceImpl<ApiAppMapper, ApiApp> implements ApiAppService {

    @Override
    public ApiApp selectApiAppByAppId(Long appId) {
        return getById(appId);
    }

    @Override
    public List<ApiApp> selectApiAppList(ApiApp apiApp) {
        LambdaQueryWrapper<ApiApp> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(apiApp.getAppCode()), ApiApp::getAppCode, apiApp.getAppCode())
               .like(StringUtils.hasText(apiApp.getAppName()), ApiApp::getAppName, apiApp.getAppName())
               .like(StringUtils.hasText(apiApp.getOwnerName()), ApiApp::getOwnerName, apiApp.getOwnerName())
               .eq(StringUtils.hasText(apiApp.getStatus()), ApiApp::getStatus, apiApp.getStatus())
               .orderByDesc(ApiApp::getCreateTime);
        return list(wrapper);
    }

    @Override
    public int insertApiApp(ApiApp apiApp) {
        // 检查应用编码是否重复
        LambdaQueryWrapper<ApiApp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApiApp::getAppCode, apiApp.getAppCode());
        if (count(wrapper) > 0) {
            throw new BusinessException("应用编码已存在");
        }
        
        // 设置默认版本
        if (!StringUtils.hasText(apiApp.getAppVersion())) {
            apiApp.setAppVersion("1.0.0");
        }
        
        return save(apiApp) ? 1 : 0;
    }

    @Override
    public int updateApiApp(ApiApp apiApp) {
        // 如果修改了应用编码，检查是否重复
        LambdaQueryWrapper<ApiApp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApiApp::getAppCode, apiApp.getAppCode())
               .ne(ApiApp::getAppId, apiApp.getAppId());
        if (count(wrapper) > 0) {
            throw new BusinessException("应用编码已存在");
        }
        
        return updateById(apiApp) ? 1 : 0;
    }

    @Override
    public int deleteApiAppByAppIds(Long[] appIds) {
        return removeByIds(Arrays.asList(appIds)) ? appIds.length : 0;
    }

    @Override
    public int deleteApiAppByAppId(Long appId) {
        return removeById(appId) ? 1 : 0;
    }

    @Override
    public int changeStatus(ApiApp apiApp) {
        return updateById(apiApp) ? 1 : 0;
    }
}