# API全生命周期管理平台 - 认证问题解决方案

## 问题描述
访问8080端口时返回：`{"code":401,"msg":"暂未登录或token已经过期","data":null,"timestamp"}`

## 问题原因
这是**正常现象**！您的后端项目已经成功启动，Spring Security安全配置正在正常工作。由于数据库中没有默认用户，所以无法进行身份验证。

## 解决方案

### 方案一：使用Flyway自动迁移（推荐）

1. **重启应用**
   ```bash
   mvn spring-boot:run
   ```
   
2. **应用启动时会自动执行数据库迁移**，插入默认管理员用户

3. **使用以下凭据登录**：
   - 用户名：`admin`
   - 密码：`admin123`

### 方案二：手动执行SQL

1. **连接到MySQL数据库**
   - 主机：172.28.77.74
   - 端口：3306
   - 数据库：api_lifecycle
   - 用户名：root
   - 密码：rjxzzb

2. **执行SQL脚本**
   ```sql
   -- 执行 src/main/resources/db/migration/V1.0.2__Insert_Default_User.sql
   ```

3. **使用默认用户登录**：
   - 用户名：`admin`
   - 密码：`admin123`

## 测试API接口

### 1. 访问API文档（无需认证）
```
http://localhost:8080/doc.html
```

### 2. 登录获取JWT令牌
```bash
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### 3. 使用令牌访问其他API
```bash
GET http://localhost:8080/api/app/list
Authorization: Bearer 您的JWT令牌
```

## 项目结构说明

- **认证相关**：
  - `AuthController` - 认证接口
  - `JwtAuthenticationFilter` - JWT过滤器
  - `SecurityConfig` - 安全配置

- **API模块**：
  - `ApiAppController` - API应用管理
  - `ApiModuleController` - API模块管理
  - `ApiInfoController` - API信息管理
  - `ApiPermissionController` - API权限管理

- **系统管理**：
  - `SysUserController` - 用户管理
  - `AuthService` - 认证服务

## 安全配置说明

根据`SecurityConfig.java`配置：
- `/auth/**` - 认证接口（无需认证）
- `/doc.html` - API文档（无需认证）
- `/druid/**` - 数据库监控（无需认证）
- 其他所有接口都需要JWT认证

## 注意事项

1. **JWT令牌有效期**：24小时
2. **密码加密方式**：BCrypt
3. **数据库迁移工具**：Flyway
4. **API文档工具**：Knife4j

## 下一步建议

1. 登录成功后，可以访问API文档查看所有可用接口
2. 根据业务需求创建更多用户和角色
3. 配置API权限和访问控制
4. 添加更多API应用和模块