import request from '@/utils/request'

// 查询API应用列表
export function listApp(query) {
  return request({
    url: '/api/app/list',
    method: 'get',
    params: query
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
  return request({
    url: '/api/app/' + appId,
    method: 'get'
  })
}

// 新增API应用
export function addApp(data) {
  return request({
    url: '/api/app',
    method: 'post',
    data: data
  })
}

// 修改API应用
export function updateApp(data) {
  return request({
    url: '/api/app',
    method: 'put',
    data: data
  })
}

// 删除API应用
export function delApp(appIds) {
  return request({
    url: '/api/app/' + appIds,
    method: 'delete'
  })
}

// 修改应用状态
export function changeAppStatus(data) {
  return request({
    url: '/api/app/changeStatus',
    method: 'put',
    data: data
  })
}