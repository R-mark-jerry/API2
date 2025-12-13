import request from '@/utils/request'

// 查询API接口列表
export function listInfo(query) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        data: [
          {
            apiId: 1,
            appId: 1,
            moduleId: 1,
            appName: '测试应用',
            moduleName: '用户模块',
            apiCode: 'user-list',
            apiName: '获取用户列表',
            apiDesc: '获取用户列表接口',
            requestMethod: 'GET',
            requestUrl: '/api/user/list',
            contentType: 'application/json',
            requestParams: '{"page": 1, "size": 10}',
            responseParams: '{"total": 100, "list": []}',
            requestExample: '{"page": 1, "size": 10}',
            responseExample: '{"code": 200, "data": {"total": 100, "list": []}}',
            apiStatus: '2',
            publishStatus: '1',
            createTime: new Date()
          },
          {
            apiId: 2,
            appId: 1,
            moduleId: 1,
            appName: '测试应用',
            moduleName: '用户模块',
            apiCode: 'user-add',
            apiName: '新增用户',
            apiDesc: '新增用户接口',
            requestMethod: 'POST',
            requestUrl: '/api/user/add',
            contentType: 'application/json',
            requestParams: '{"username": "", "password": ""}',
            responseParams: '{"code": 200, "msg": "成功"}',
            requestExample: '{"username": "test", "password": "123456"}',
            responseExample: '{"code": 200, "msg": "新增成功"}',
            apiStatus: '2',
            publishStatus: '1',
            createTime: new Date()
          }
        ]
      })
    }, 300)
  })
}

// 分页查询API接口列表
export function pageInfo(query) {
  return request({
    url: '/api/info/page',
    method: 'get',
    params: query
  })
}

// 根据应用ID查询接口列表
export function getApisByAppId(appId) {
  return request({
    url: '/api/info/app/' + appId,
    method: 'get'
  })
}

// 根据模块ID查询接口列表
export function getApisByModuleId(moduleId) {
  return request({
    url: '/api/info/module/' + moduleId,
    method: 'get'
  })
}

// 查询API接口详细
export function getInfo(apiId) {
  // 模拟数据，实际应该调用后端API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        data: {
          apiId: apiId,
          appId: 1,
          moduleId: 1,
          appName: '测试应用',
          moduleName: '用户模块',
          apiCode: 'user-list',
          apiName: '获取用户列表',
          apiDesc: '获取用户列表接口',
          requestMethod: 'GET',
          requestUrl: '/api/user/list',
          contentType: 'application/json',
          requestParams: '{"page": 1, "size": 10}',
          responseParams: '{"total": 100, "list": []}',
          requestExample: '{"page": 1, "size": 10}',
          responseExample: '{"code": 200, "data": {"total": 100, "list": []}}',
          apiStatus: '2',
          publishStatus: '1'
        }
      })
    }, 300)
  })
}

// 新增API接口
export function addInfo(data) {
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

// 修改API接口
export function updateInfo(data) {
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

// 删除API接口
export function delInfo(apiIds) {
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

// 修改API状态
export function changeApiStatus(data) {
  return request({
    url: '/api/info/changeApiStatus',
    method: 'put',
    data: data
  })
}

// 修改发布状态
export function changePublishStatus(data) {
  return request({
    url: '/api/info/changePublishStatus',
    method: 'put',
    data: data
  })
}