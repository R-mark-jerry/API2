package com.myruoyi.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myruoyi.api.entity.ApiModule;
import com.myruoyi.api.mapper.ApiModuleMapper;
import com.myruoyi.api.service.ApiModuleService;
import com.myruoyi.common.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * API模块服务实现类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Service
public class ApiModuleServiceImpl extends ServiceImpl<ApiModuleMapper, ApiModule> implements ApiModuleService {

    @Override
    public ApiModule selectApiModuleByModuleId(Long moduleId) {
        return getById(moduleId);
    }

    @Override
    public List<ApiModule> selectApiModuleList(ApiModule apiModule) {
        LambdaQueryWrapper<ApiModule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(apiModule.getAppId() != null, ApiModule::getAppId, apiModule.getAppId())
               .like(StringUtils.hasText(apiModule.getModuleCode()), ApiModule::getModuleCode, apiModule.getModuleCode())
               .like(StringUtils.hasText(apiModule.getModuleName()), ApiModule::getModuleName, apiModule.getModuleName())
               .orderByAsc(ApiModule::getParentId, ApiModule::getOrderNum);
        
        List<ApiModule> modules = list(wrapper);
        
        // 如果指定了应用ID，则查询应用名称
        if (apiModule.getAppId() != null && !modules.isEmpty()) {
            modules = baseMapper.selectModulesByAppId(apiModule.getAppId());
        }
        
        return modules;
    }

    @Override
    public List<ApiModule> selectModulesByAppId(Long appId) {
        return baseMapper.selectModulesByAppId(appId);
    }

    @Override
    public int insertApiModule(ApiModule apiModule) {
        // 检查模块编码是否重复
        LambdaQueryWrapper<ApiModule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApiModule::getAppId, apiModule.getAppId())
               .eq(ApiModule::getModuleCode, apiModule.getModuleCode());
        if (count(wrapper) > 0) {
            throw new BusinessException("模块编码已存在");
        }
        
        // 设置默认排序
        if (apiModule.getOrderNum() == null) {
            LambdaQueryWrapper<ApiModule> orderWrapper = new LambdaQueryWrapper<>();
            orderWrapper.eq(ApiModule::getParentId, apiModule.getParentId())
                       .orderByDesc(ApiModule::getOrderNum)
                       .last("LIMIT 1");
            ApiModule lastModule = getOne(orderWrapper);
            apiModule.setOrderNum(lastModule != null ? lastModule.getOrderNum() + 1 : 1);
        }
        
        return save(apiModule) ? 1 : 0;
    }

    @Override
    public int updateApiModule(ApiModule apiModule) {
        // 如果修改了模块编码，检查是否重复
        if (StringUtils.hasText(apiModule.getModuleCode())) {
            LambdaQueryWrapper<ApiModule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ApiModule::getAppId, apiModule.getAppId())
                   .eq(ApiModule::getModuleCode, apiModule.getModuleCode())
                   .ne(ApiModule::getModuleId, apiModule.getModuleId());
            if (count(wrapper) > 0) {
                throw new BusinessException("模块编码已存在");
            }
        }
        
        // 检查是否将父模块设置为自己或自己的子模块
        if (apiModule.getParentId() != null && apiModule.getParentId().equals(apiModule.getModuleId())) {
            throw new BusinessException("父模块不能是自己");
        }
        
        return updateById(apiModule) ? 1 : 0;
    }

    @Override
    public int deleteApiModuleByModuleIds(Long[] moduleIds) {
        // 检查是否有子模块
        for (Long moduleId : moduleIds) {
            if (baseMapper.selectChildModuleCount(moduleId) > 0) {
                throw new BusinessException("存在子模块,不允许删除");
            }
        }
        
        return removeByIds(Arrays.asList(moduleIds)) ? moduleIds.length : 0;
    }

    @Override
    public int deleteApiModuleByModuleId(Long moduleId) {
        // 检查是否有子模块
        if (baseMapper.selectChildModuleCount(moduleId) > 0) {
            throw new BusinessException("存在子模块,不允许删除");
        }
        
        return removeById(moduleId) ? 1 : 0;
    }

    @Override
    public int selectChildModuleCount(Long parentId) {
        return baseMapper.selectChildModuleCount(parentId);
    }

    @Override
    public List<ApiModule> buildModuleTree(List<ApiModule> modules) {
        List<ApiModule> returnList = new ArrayList<>();
        List<Long> tempList = modules.stream().map(ApiModule::getModuleId).collect(Collectors.toList());
        
        for (ApiModule module : modules) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(module.getParentId())) {
                recursionFn(modules, module);
                returnList.add(module);
            }
        }
        
        if (returnList.isEmpty()) {
            returnList = modules;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<ApiModule> list, ApiModule t) {
        // 得到子节点列表
        List<ApiModule> childList = getChildList(list, t);
        t.setChildren(childList);
        for (ApiModule tChild : childList) {
            // 判断是否有子节点
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<ApiModule> getChildList(List<ApiModule> list, ApiModule t) {
        List<ApiModule> tlist = new ArrayList<>();
        for (ApiModule n : list) {
            if (n.getParentId() != null && n.getParentId().longValue() == t.getModuleId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<ApiModule> list, ApiModule t) {
        return getChildList(list, t).size() > 0;
    }
}