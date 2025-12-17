import request from '@/utils/request'

// 查询API接口列表
export function listInfo(query) {
  return request({
    url: '/info/page',
    method: 'get',
    params: {
      pageNum: query.pageNum || 1,
      pageSize: query.pageSize || 10,
      appId: query.appId,
      moduleId: query.moduleId,
      apiCode: query.apiCode,
      apiName: query.apiName,
      requestUrl: query.requestUrl,
      apiStatus: query.apiStatus,
      publishStatus: query.publishStatus
    }
  })
}

// 分页查询API接口列表
export function pageInfo(query) {
  return request({
    url: '/info/page',
    method: 'get',
    params: query
  })
}

// 根据应用ID查询接口列表
export function getApisByAppId(appId) {
  return request({
    url: '/info/app/' + appId,
    method: 'get'
  })
}

// 根据模块ID查询接口列表
export function getApisByModuleId(moduleId) {
  return request({
    url: '/info/module/' + moduleId,
    method: 'get'
  })
}

// 查询API接口详细
export function getInfo(apiId) {
  return request({
    url: '/info/' + apiId,
    method: 'get'
  })
}

// 新增API接口
export function addInfo(data) {
  return request({
    url: '/info',
    method: 'post',
    data: data
  })
}

// 修改API接口
export function updateInfo(data) {
  return request({
    url: '/info',
    method: 'put',
    data: data
  })
}

// 删除API接口
export function delInfo(apiIds) {
  return request({
    url: '/info/' + apiIds,
    method: 'delete'
  })
}

// 修改API状态
export function changeApiStatus(data) {
  return request({
    url: '/info/changeApiStatus',
    method: 'put',
    data: data
  })
}

// 修改发布状态
export function changePublishStatus(data) {
  return request({
    url: '/info/changePublishStatus',
    method: 'put',
    data: data
  })
}