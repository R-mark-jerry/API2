package com.myruoyi.system.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 字段自动填充处理器
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充...");
        // 设置创建时间
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        // 设置创建者（从安全上下文获取）
        this.setFieldValByName("createBy", getCurrentUsername(), metaObject);
        // 设置删除标志为0（未删除）
        this.setFieldValByName("deleted", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新填充...");
        // 设置更新时间
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        // 设置更新者（从安全上下文获取）
        this.setFieldValByName("updateBy", getCurrentUsername(), metaObject);
    }

    /**
     * 获取当前用户名
     */
    private String getCurrentUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
                return authentication.getName();
            }
            return "system";
        } catch (Exception e) {
            log.warn("获取当前用户名失败", e);
            return "system";
        }
    }
}