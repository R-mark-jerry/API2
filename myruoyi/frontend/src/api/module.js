import request from '@/utils/request'

// 查询API模块列表
export function listModule(query) {
  return request({
    url: '/api/module/list',
    method: 'get',
    params: query
  })
}

// 查询API模块树形结构
export function treeModule() {
  return request({
    url: '/api/module/tree',
    method: 'get'
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
  return request({
    url: '/api/module/' + moduleId,
    method: 'get'
  })
}

// 新增API模块
export function addModule(data) {
  return request({
    url: '/api/module',
    method: 'post',
    data: data
  })
}

// 修改API模块
export function updateModule(data) {
  return request({
    url: '/api/module',
    method: 'put',
    data: data
  })
}

// 删除API模块
export function delModule(moduleIds) {
  return request({
    url: '/api/module/' + moduleIds,
    method: 'delete'
  })
}

// 查询子模块数量
export function getChildCount(parentId) {
  return request({
    url: '/api/module/childCount/' + parentId,
    method: 'get'
  })
}