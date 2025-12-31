import request from '@/utils/request'

// 查询API权限列表
export function listPermission(query) {
  return request({
    url: '/permission/list',
    method: 'get',
    params: query
  })
}

// 分页查询API权限列表
export function pagePermission(query) {
  return request({
    url: '/permission/page',
    method: 'get',
    params: {
      pageNum: query.pageNum || 1,
      pageSize: query.pageSize || 10,
      appName: query.appName,
      permissionType: query.permissionType,
      status: query.status
    }
  })
}

// 查询API权限详细
export function getPermission(permissionId) {
  return request({
    url: '/permission/' + permissionId,
    method: 'get'
  })
}

// 新增API权限
export function addPermission(data) {
  return request({
    url: '/permission',
    method: 'post',
    data: data
  })
}

// 修改API权限
export function updatePermission(data) {
  return request({
    url: '/permission',
    method: 'put',
    data: data
  })
}

// 删除API权限
export function delPermission(permissionIds) {
  // 如果是数组，需要特殊处理
  if (Array.isArray(permissionIds)) {
    // 对于数组，我们可以逐个删除或者使用逗号分隔的字符串
    // 这里使用逗号分隔的方式
    const idsStr = permissionIds.join(',')
    return request({
      url: '/permission/' + idsStr,
      method: 'delete'
    })
  } else {
    // 单个ID直接使用
    return request({
      url: '/permission/' + permissionIds,
      method: 'delete'
    })
  }
}

// 修改权限状态
export function changePermissionStatus(data) {
  return request({
    url: '/permission/changeStatus',
    method: 'put',
    data: data
  })
}