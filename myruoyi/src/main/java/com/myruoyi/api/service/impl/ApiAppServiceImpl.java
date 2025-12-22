package com.myruoyi.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myruoyi.api.entity.ApiApp;
import com.myruoyi.api.mapper.ApiAppMapper;
import com.myruoyi.api.service.ApiAppService;
import com.myruoyi.common.core.exception.BusinessException;
import com.myruoyi.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * API应用服务实现类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Slf4j
@Service
public class ApiAppServiceImpl extends ServiceImpl<ApiAppMapper, ApiApp> implements ApiAppService {

    private final SysUserService sysUserService;

    public ApiAppServiceImpl(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

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
    public IPage<ApiApp> selectApiAppPage(Integer pageNum, Integer pageSize, String appCode, String appName, String ownerName, String status) {
        Page<ApiApp> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ApiApp> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(appCode), ApiApp::getAppCode, appCode)
               .like(StringUtils.hasText(appName), ApiApp::getAppName, appName)
               .like(StringUtils.hasText(ownerName), ApiApp::getOwnerName, ownerName)
               .eq(StringUtils.hasText(status), ApiApp::getStatus, status)
               .orderByDesc(ApiApp::getCreateTime);
        return page(page, wrapper);
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
        
        // 设置创建时间
        apiApp.setCreateTime(java.time.LocalDateTime.now());
        
        return baseMapper.insertApiApp(apiApp);
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
        
        return baseMapper.updateApiApp(apiApp);
    }

    @Override
    public int deleteApiAppByAppIds(Long[] appIds) {
        return baseMapper.deleteApiAppByAppIds(appIds);
    }

    @Override
    public int deleteApiAppByAppId(Long appId) {
        return baseMapper.deleteApiAppByAppId(appId);
    }

    @Override
    public int changeStatus(ApiApp apiApp) {
        // 记录状态变更日志
        ApiApp oldApp = getById(apiApp.getAppId());
        if (oldApp != null && !oldApp.getStatus().equals(apiApp.getStatus())) {
            String oldStatus = "0".equals(oldApp.getStatus()) ? "正常" : "停用";
            String newStatus = "0".equals(apiApp.getStatus()) ? "正常" : "停用";
            String operation = "0".equals(apiApp.getStatus()) ? "启用应用" : "停用应用";
            
            log.info("应用状态变更日志 - 应用ID: {}, 应用编码: {}, 应用名称: {}, 操作: {}, 原状态: {}, 新状态: {}, 操作人: {}, 操作时间: {}",
                apiApp.getAppId(),
                apiApp.getAppCode(),
                apiApp.getAppName(),
                operation,
                oldStatus,
                newStatus,
                sysUserService.getCurrentUser() != null ? sysUserService.getCurrentUser().getUserName() : "系统",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
            );
        }
        
        return baseMapper.updateApiApp(apiApp);
    }

    @Override
    public void exportApp(HttpServletResponse response, ApiApp apiApp) {
        try {
            // 查询数据
            List<ApiApp> list = selectApiAppList(apiApp);
            
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("API应用数据", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            
            // 创建工作簿
            OutputStream out = response.getOutputStream();
            
            // 写入Excel文件头
            StringBuilder sb = new StringBuilder();
            sb.append("应用编号,应用编码,应用名称,应用描述,应用版本,负责人,状态,创建时间\n");
            
            // 写入数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (ApiApp app : list) {
                sb.append(app.getAppId()).append(",")
                  .append(app.getAppCode()).append(",")
                  .append(app.getAppName()).append(",")
                  .append(app.getAppDesc() != null ? app.getAppDesc() : "").append(",")
                  .append(app.getAppVersion()).append(",")
                  .append(app.getOwnerName()).append(",")
                  .append("0".equals(app.getStatus()) ? "正常" : "停用").append(",")
                  .append(app.getCreateTime() != null ? sdf.format(app.getCreateTime()) : "").append("\n");
            }
            
            // 写入响应
            out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            
            log.info("导出API应用数据成功，共{}条记录", list.size());
        } catch (IOException e) {
            log.error("导出API应用数据失败", e);
            throw new BusinessException("导出失败：" + e.getMessage());
        }
    }

    @Override
    public void importApp(org.springframework.web.multipart.MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new BusinessException("导入文件不能为空");
        }
        
        try {
            // 读取文件内容
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            String[] lines = content.split("\n");
            
            // 跳过标题行
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (StringUtils.hasText(line)) {
                    String[] fields = line.split(",");
                    if (fields.length >= 7) {
                        ApiApp apiApp = new ApiApp();
                        apiApp.setAppCode(fields[0].trim());
                        apiApp.setAppName(fields[1].trim());
                        apiApp.setAppDesc(fields[2].trim());
                        apiApp.setAppVersion(fields[3].trim());
                        
                        // 处理负责人信息
                        String ownerName = fields[4].trim();
                        if (StringUtils.hasText(ownerName)) {
                            // 这里可以根据负责人姓名查找用户ID
                            apiApp.setOwnerName(ownerName);
                        }
                        
                        // 处理状态
                        String status = fields[5].trim();
                        apiApp.setStatus("正常".equals(status) ? "0" : "1");
                        
                        // 插入数据
                        insertApiApp(apiApp);
                    }
                }
            }
            
            log.info("导入API应用数据成功，共{}条记录", lines.length - 1);
        } catch (Exception e) {
            log.error("导入API应用数据失败", e);
            throw new BusinessException("导入失败：" + e.getMessage());
        }
    }

    @Override
    public void importTemplate(HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("API应用导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".csv");
            
            // 创建模板内容
            OutputStream out = response.getOutputStream();
            StringBuilder sb = new StringBuilder();
            sb.append("应用编码,应用名称,应用描述,应用版本,负责人,状态,备注\n");
            sb.append("APP001,示例应用,这是一个示例应用,1.0.0,管理员,正常,这是备注信息\n");
            
            // 写入响应
            out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            
            log.info("下载API应用导入模板成功");
        } catch (IOException e) {
            log.error("下载API应用导入模板失败", e);
            throw new BusinessException("下载模板失败：" + e.getMessage());
        }
    }
}