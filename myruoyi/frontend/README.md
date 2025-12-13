# API全生命周期管理平台前端

基于Vue3 + Element Plus构建的现代化前端项目

## 技术栈

- Vue 3.3.4
- Vue Router 4.2.4
- Pinia 2.1.6
- Element Plus 2.3.9
- Axios 1.5.0
- Vite 4.4.9
- Sass

## 项目结构

```
frontend/
├── public/                 # 静态资源
├── src/                    # 源代码
│   ├── api/               # API接口
│   ├── assets/             # 资源文件
│   ├── components/         # 公共组件
│   ├── layout/            # 布局组件
│   ├── router/            # 路由配置
│   ├── stores/            # 状态管理
│   ├── styles/            # 全局样式
│   ├── utils/             # 工具函数
│   └── views/             # 页面组件
│       ├── api/           # API管理相关页面
│       ├── dashboard/     # 工作台
│       ├── error/         # 错误页面
│       ├── login/         # 登录页面
│       └── system/        # 系统管理页面
├── index.html              # HTML模板
├── package.json           # 项目配置
└── vite.config.js         # Vite配置
```

## 安装依赖

```bash
npm install
```

## 开发环境运行

```bash
npm run dev
```

## 生产环境构建

```bash
npm run build
```

## 代码规范

```bash
npm run lint
```

## 功能特性

- 用户认证：登录、登出、权限控制
- API应用管理：增删改查、状态管理
- API模块管理：树形结构展示、模块管理
- API接口管理：接口信息管理、发布状态控制
- 统计分析：调用统计、性能分析
- 响应式设计：适配不同屏幕尺寸
- 组件化开发：可复用的UI组件

## 开发说明

1. 新增页面时，需要在 `src/router/index.js` 中配置路由
2. API接口统一放在 `src/api` 目录下管理
3. 公共组件放在 `src/components` 目录下
4. 全局样式在 `src/styles` 目录下统一管理
5. 使用 Pinia 进行状态管理，store 文件放在 `src/stores` 目录下

## 环境配置

开发环境会自动代理到后端服务（http://localhost:8080），如需修改请编辑 `vite.config.js` 中的 proxy 配置。

## 部署说明

1. 执行 `npm run build` 构建生产版本
2. 将 `dist` 目录下的文件部署到Web服务器
3. 配置Nginx等Web服务器，支持前端路由（history模式）

## 注意事项

- 本项目需要后端API支持，请确保后端服务正常运行
- 登录默认账号：admin/admin123
- 部署时需要根据实际环境修改API基础URL