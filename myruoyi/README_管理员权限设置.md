# 管理员用户管理权限设置指南

## 概述

本文档说明如何为管理员admin（密码123456）添加用户管理权限。

## 实现方式

系统提供了两种方式来设置管理员权限：

### 方式一：自动设置（推荐）

系统启动时会自动执行权限设置，通过 `AdminPermissionSetup` 类实现：

1. **自动创建/更新admin用户**
   - 确保admin用户存在
   - 设置密码为123456（BCrypt加密）
   - 确保用户状态为正常

2. **自动添加用户管理菜单**
   - 用户管理主菜单
   - 用户列表权限（system:user:list）
   - 用户查询权限（system:user:query）
   - 用户新增权限（system:user:add）
   - 用户修改权限（system:user:edit）
   - 用户删除权限（system:user:remove）
   - 用户重置密码权限（system:user:resetPwd）
   - 用户修改状态权限（system:user:changeStatus）

3. **自动分配角色权限**
   - 为超级管理员角色添加所有用户管理权限
   - 确保admin用户拥有超级管理员角色

### 方式二：手动SQL脚本

如果需要手动执行，可以使用以下脚本：

1. **添加用户管理权限**：使用 `add_user_management_permissions.sql` 脚本
```sql
-- 执行SQL脚本
mysql -u username -p database_name < add_user_management_permissions.sql
```

2. **修复密码问题**：如果遇到登录问题，使用 `fix_admin_password.sql` 脚本
```sql
-- 执行密码修复脚本
mysql -u username -p database_name < fix_admin_password.sql
```

## 权限说明

| 权限标识 | 说明 | 对应接口 |
|---------|------|---------|
| system:user:list | 用户列表 | GET /api/system/user/page |
| system:user:query | 用户查询 | GET /api/system/user/{userId} |
| system:user:add | 用户新增 | POST /api/system/user |
| system:user:edit | 用户修改 | PUT /api/system/user |
| system:user:remove | 用户删除 | DELETE /api/system/user/{userIds} |
| system:user:resetPwd | 用户重置密码 | PUT /api/system/user/resetPwd |
| system:user:changeStatus | 用户修改状态 | PUT /api/system/user/changeStatus |

## 使用说明

1. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

2. **登录系统**
   - 用户名：admin
   - 密码：123456

3. **访问用户管理**
   - 导航到：系统管理 > 用户管理
   - 现在应该可以看到完整的用户管理功能

## 技术细节

### 权限验证流程

1. 用户登录后，系统通过 `UserDetailsServiceImpl` 加载用户权限
2. 权限数据来源于 `sys_user_role`、`sys_role_menu` 和 `sys_menu` 表
3. 控制器方法使用 `@PreAuthorize` 注解进行权限验证
4. 前端使用 `hasPermi` 指令控制按钮显示

### 数据库表结构

- **sys_user**：用户信息表
- **sys_role**：角色信息表
- **sys_menu**：菜单权限表
- **sys_user_role**：用户角色关联表
- **sys_role_menu**：角色菜单关联表

### 关键代码文件

- `AdminPermissionSetup.java`：权限自动设置工具类
- `SysUserController.java`：用户管理控制器
- `UserDetailsServiceImpl.java`：用户详情服务实现
- `SecurityConfig.java`：安全配置

## 故障排除

### 问题1：admin用户无法访问用户管理

**解决方案**：
1. 检查 `AdminPermissionSetup` 是否正常执行
2. 查看日志中的权限设置信息
3. 手动执行SQL脚本

### 问题2：登录失败，提示"Bad credentials"

**解决方案**：
1. 检查数据库中admin用户的密码是否为正确的BCrypt格式
2. 执行 `fix_admin_password.sql` 脚本修复密码
3. 确认使用用户名admin和密码123456登录

### 问题3：权限验证失败

**解决方案**：
1. 检查 `sys_menu` 表中的权限标识是否正确
2. 确认 `sys_role_menu` 表中存在对应关联
3. 验证 `sys_user_role` 表中admin用户的角色关联

## 注意事项

1. 系统启动时会自动执行权限设置，无需手动干预
2. 如果数据库中已存在相关数据，系统会进行增量更新
3. 权限设置过程会有详细的日志输出
4. 建议在生产环境中谨慎使用自动权限设置功能

## 密码问题修复

如果遇到登录问题，可能是因为数据库中的密码格式不正确。请执行以下步骤：

1. 停止应用程序
2. 执行 `fix_admin_password.sql` 脚本
3. 重新启动应用程序
4. 使用 admin/123456 登录系统

这个修复脚本会将admin用户的密码更新为正确的BCrypt加密格式。