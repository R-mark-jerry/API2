@echo off
echo 正在启动API全生命周期管理平台前端...
echo.

REM 检查是否安装了依赖
if not exist "node_modules" (
    echo 首次运行，正在安装依赖...
    npm install
    echo.
)

REM 启动开发服务器
echo 启动开发服务器...
npm run dev

pause