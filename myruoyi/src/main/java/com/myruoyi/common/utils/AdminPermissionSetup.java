package com.myruoyi.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 管理员权限设置工具类
 * 系统启动时自动为admin用户添加用户管理权限
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Slf4j
@Component
@Order(1) // 确保在其他组件初始化之前执行
@RequiredArgsConstructor
public class AdminPermissionSetup implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("开始设置管理员权限...");
            
            // 1. 确保admin用户存在且密码为123456
            ensureAdminUser();
            
            // 2. 添加用户管理相关的菜单权限
            addUserManagementMenus();
            
            // 3. 为超级管理员角色添加用户管理权限
            addPermissionsToAdminRole();
            
            // 4. 确保admin用户拥有超级管理员角色
            ensureAdminUserRole();
            
            log.info("管理员权限设置完成");
        } catch (Exception e) {
            log.error("设置管理员权限时发生错误", e);
        }
    }

    /**
     * 确保admin用户存在且密码为123456
     */
    private void ensureAdminUser() {
        // 检查admin用户是否存在（包括被删除的）
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sys_user WHERE user_name = 'admin'", Integer.class);
        
        if (count == 0) {
            // 创建admin用户
            String encodedPassword = PasswordGenerator.generateDefaultPassword();
            jdbcTemplate.update(
                "INSERT INTO sys_user (user_name, nick_name, user_type, email, phonenumber, sex, " +
                "password, status, create_by, create_time, remark, deleted) VALUES " +
                "('admin', '管理员', '00', 'admin@myruoyi.com', '15888888888', '1', " +
                "?, '0', 'admin', ?, '管理员', 0)",
                encodedPassword, LocalDateTime.now());
            log.info("创建admin用户成功，密码已加密");
        } else {
            // 检查用户是否被删除
            Integer deletedCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_user WHERE user_name = 'admin' AND deleted = 1", Integer.class);
            
            if (deletedCount > 0) {
                // 恢复被删除的用户并更新密码
                String encodedPassword = PasswordGenerator.generateDefaultPassword();
                jdbcTemplate.update(
                    "UPDATE sys_user SET password = ?, status = '0', deleted = 0 WHERE user_name = 'admin'",
                    encodedPassword);
                log.info("恢复admin用户并更新密码为123456，密码已加密");
            } else {
                // 更新admin用户密码为123456
                String encodedPassword = PasswordGenerator.generateDefaultPassword();
                jdbcTemplate.update(
                    "UPDATE sys_user SET password = ?, status = '0' WHERE user_name = 'admin'",
                    encodedPassword);
                log.info("更新admin用户密码为123456，密码已加密");
            }
        }
        
        // 验证用户是否正确创建/更新
        Integer finalCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sys_user WHERE user_name = 'admin' AND deleted = 0", Integer.class);
        log.info("admin用户验证结果：{} 个有效用户", finalCount);
    }

    /**
     * 添加用户管理相关的菜单权限
     */
    private void addUserManagementMenus() {
        // 检查用户管理菜单是否存在
        Integer menuCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sys_menu WHERE menu_name = '用户管理'", Integer.class);
        
        if (menuCount == 0) {
            // 添加用户管理菜单
            jdbcTemplate.update(
                "INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, " +
                "is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, " +
                "create_time, remark, deleted) VALUES " +
                "('用户管理', 1, 1, 'user', 'system/user/index', 1, 0, 'C', 0, 0, " +
                "'system:user:list', 'user', 'admin', ?, '用户管理菜单', 0)",
                LocalDateTime.now());
            log.info("添加用户管理菜单成功");
        }
        
        // 获取用户管理菜单ID
        Long userMenuId = jdbcTemplate.queryForObject(
            "SELECT menu_id FROM sys_menu WHERE menu_name = '用户管理'", Long.class);
        
        // 添加用户管理相关的按钮权限
        String[][] permissions = {
            {"用户列表", "system:user:list"},
            {"用户查询", "system:user:query"},
            {"用户新增", "system:user:add"},
            {"用户修改", "system:user:edit"},
            {"用户删除", "system:user:remove"},
            {"用户重置密码", "system:user:resetPwd"},
            {"用户修改状态", "system:user:changeStatus"}
        };
        
        for (int i = 0; i < permissions.length; i++) {
            String permName = permissions[i][0];
            String permCode = permissions[i][1];
            
            // 检查权限是否已存在
            Integer permCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_menu WHERE perms = ?", Integer.class, permCode);
            
            if (permCount == 0) {
                jdbcTemplate.update(
                    "INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, " +
                    "is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, " +
                    "create_time, remark, deleted) VALUES " +
                    "(?, ?, ?, '', '', 1, 0, 'F', 0, 0, ?, '#', 'admin', ?, ?, 0)",
                    permName, userMenuId, i + 1, permCode, LocalDateTime.now(), permName + "按钮");
                log.info("添加{}权限成功", permName);
            }
        }
    }

    /**
     * 为超级管理员角色添加用户管理权限
     */
    private void addPermissionsToAdminRole() {
        // 获取超级管理员角色ID
        Long adminRoleId = jdbcTemplate.queryForObject(
            "SELECT role_id FROM sys_role WHERE role_key = 'admin'", Long.class);
        
        // 获取所有用户管理相关的菜单ID
        jdbcTemplate.query(
            "SELECT menu_id FROM sys_menu WHERE perms LIKE 'system:user:%'", 
            rs -> {
                Long menuId = rs.getLong("menu_id");
                // 检查角色菜单关联是否已存在
                Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM sys_role_menu WHERE role_id = ? AND menu_id = ?", 
                    Integer.class, adminRoleId, menuId);
                
                if (count == 0) {
                    jdbcTemplate.update(
                        "INSERT INTO sys_role_menu (role_id, menu_id) VALUES (?, ?)", 
                        adminRoleId, menuId);
                }
            });
        
        // 确保用户管理菜单也关联到超级管理员角色
        Long userMenuId = jdbcTemplate.queryForObject(
            "SELECT menu_id FROM sys_menu WHERE menu_name = '用户管理'", Long.class);
        
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sys_role_menu WHERE role_id = ? AND menu_id = ?", 
            Integer.class, adminRoleId, userMenuId);
        
        if (count == 0) {
            jdbcTemplate.update(
                "INSERT INTO sys_role_menu (role_id, menu_id) VALUES (?, ?)", 
                adminRoleId, userMenuId);
        }
        
        log.info("为超级管理员角色添加用户管理权限成功");
    }

    /**
     * 确保admin用户拥有超级管理员角色
     */
    private void ensureAdminUserRole() {
        // 获取admin用户ID和超级管理员角色ID
        Long adminUserId = jdbcTemplate.queryForObject(
            "SELECT user_id FROM sys_user WHERE user_name = 'admin'", Long.class);
        
        Long adminRoleId = jdbcTemplate.queryForObject(
            "SELECT role_id FROM sys_role WHERE role_key = 'admin'", Long.class);
        
        // 检查用户角色关联是否已存在
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sys_user_role WHERE user_id = ? AND role_id = ?", 
            Integer.class, adminUserId, adminRoleId);
        
        if (count == 0) {
            jdbcTemplate.update(
                "INSERT INTO sys_user_role (user_id, role_id) VALUES (?, ?)", 
                adminUserId, adminRoleId);
            log.info("为admin用户分配超级管理员角色成功");
        }
    }
}