import request from '@/utils/request'

// 查询API应用列表
export function listApp(query) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        data: [
          {
            appId: 1,
            appCode: 'test-app',
            appName: '测试应用',
            appDesc: '这是一个测试应用',
            appVersion: '1.0.0',
            ownerId: 1,
            ownerName: '管理员',
            status: '0',
            createTime: new Date()
          },
          {
            appId: 2,
            appCode: 'demo-app',
            appName: '演示应用',
            appDesc: '这是一个演示应用',
            appVersion: '1.0.0',
            ownerId: 1,
            ownerName: '管理员',
            status: '0',
            createTime: new Date()
          }
        ]
      })
    }, 300)
  })
}

// 分页查询API应用列表
export function pageApp(query) {
  return request({
    url: '/api/app/page',
    method: 'get',
    params: query
  })
}

// 查询API应用详细
export function getApp(appId) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        data: {
          appId: appId,
          appCode: 'test-app',
          appName: '测试应用',
          appDesc: '这是一个测试应用',
          appVersion: '1.0.0',
          ownerId: 1,
          status: '0'
        }
      })
    }, 300)
  })
}

// 新增API应用
export function addApp(data) {
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

// 修改API应用
export function updateApp(data) {
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

// 删除API应用
export function delApp(appIds) {
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

// 修改应用状态
export function changeAppStatus(data) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        msg: '状态修改成功'
      })
    }, 300)
  })
}