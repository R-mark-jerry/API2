import request from '@/utils/request'

// 查询API应用列表
export function listApp(query) {
  return request({
    url: '/app/page',
    method: 'get',
    params: {
      pageNum: query.pageNum || 1,
      pageSize: query.pageSize || 10,
      appCode: query.appCode,
      appName: query.appName,
      ownerName: query.ownerName,
      status: query.status
    }
  })
}

// 分页查询API应用列表
export function pageApp(query) {
  return request({
    url: '/app/page',
    method: 'get',
    params: query
  })
}

// 查询API应用详细
export function getApp(appId) {
  return request({
    url: '/app/' + appId,
    method: 'get'
  })
}

// 新增API应用
export function addApp(data) {
  return request({
    url: '/app',
    method: 'post',
    data: data
  })
}

// 修改API应用
export function updateApp(data) {
  return request({
    url: '/app',
    method: 'put',
    data: data
  })
}

// 删除API应用
export function delApp(appIds) {
  return request({
    url: '/app/' + appIds,
    method: 'delete'
  })
}

// 修改应用状态
export function changeAppStatus(data) {
  return request({
    url: '/app/changeStatus',
    method: 'put',
    data: data
  })
}

// 导出API应用
export function exportApp(query) {
  return request({
    url: '/app/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 导入API应用
export function importApp(data) {
  return request({
    url: '/app/import',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 下载应用导入模板
export function importTemplate() {
  return request({
    url: '/app/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}