import hasPermi from './permission/hasPermi'

// 全局指令
export default function install(app) {
  app.directive('hasPermi', hasPermi)
}