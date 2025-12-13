import request from '@/utils/request'

// 查询API模块列表
export function listModule(query) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        data: [
          {
            moduleId: 1,
            appId: 1,
            parentId: 0,
            moduleCode: 'user-module',
            moduleName: '用户模块',
            moduleDesc: '用户相关模块',
            orderNum: 1,
            children: [
              {
                moduleId: 2,
                appId: 1,
                parentId: 1,
                moduleCode: 'user-info',
                moduleName: '用户信息',
                moduleDesc: '用户信息子模块',
                orderNum: 1
              }
            ]
          },
          {
            moduleId: 3,
            appId: 1,
            parentId: 0,
            moduleCode: 'order-module',
            moduleName: '订单模块',
            moduleDesc: '订单相关模块',
            orderNum: 2
          }
        ]
      })
    }, 300)
  })
}

// 查询API模块树形结构
export function treeModule(appId) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        data: [
          {
            moduleId: 1,
            appId: appId,
            parentId: 0,
            moduleCode: 'user-module',
            moduleName: '用户模块',
            moduleDesc: '用户相关模块',
            orderNum: 1,
            children: [
              {
                moduleId: 2,
                appId: appId,
                parentId: 1,
                moduleCode: 'user-info',
                moduleName: '用户信息',
                moduleDesc: '用户信息子模块',
                orderNum: 1
              }
            ]
          },
          {
            moduleId: 3,
            appId: appId,
            parentId: 0,
            moduleCode: 'order-module',
            moduleName: '订单模块',
            moduleDesc: '订单相关模块',
            orderNum: 2
          }
        ]
      })
    }, 300)
  })
}

// 根据应用ID查询模块列表
export function getModulesByAppId(appId) {
  return request({
    url: '/api/module/app/' + appId,
    method: 'get'
  })
}

// 查询API模块详细
export function getModule(moduleId) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        data: {
          moduleId: moduleId,
          appId: 1,
          parentId: 0,
          moduleCode: 'user-module',
          moduleName: '用户模块',
          moduleDesc: '用户相关模块',
          orderNum: 1
        }
      })
    }, 300)
  })
}

// 新增API模块
export function addModule(data) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        msg: '新增成功'
      })
    }, 300)
  })
}

// 修改API模块
export function updateModule(data) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        msg: '修改成功'
      })
    }, 300)
  })
}

// 删除API模块
export function delModule(moduleIds) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        msg: '删除成功'
      })
    }, 300)
  })
}

// 查询子模块数量
export function getChildCount(parentId) {
  return request({
    url: '/api/module/childCount/' + parentId,
    method: 'get'
  })
}