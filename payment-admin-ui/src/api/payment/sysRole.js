import request from '@/utils/request'

// 查询角色信息列表
export function listSysRole(query) {
  return request({
    url: '/payment/sysRole/list',
    method: 'get',
    params: query
  })
}

// 查询角色信息详细
export function getSysRole(roleId) {
  return request({
    url: '/payment/sysRole/' + roleId,
    method: 'get'
  })
}

// 新增角色信息
export function addSysRole(data) {
  return request({
    url: '/payment/sysRole',
    method: 'post',
    data: data
  })
}

// 修改角色信息
export function updateSysRole(data) {
  return request({
    url: '/payment/sysRole',
    method: 'put',
    data: data
  })
}

// 删除角色信息
export function delSysRole(roleId) {
  return request({
    url: '/payment/sysRole/' + roleId,
    method: 'delete'
  })
}

// 导出角色信息
export function exportSysRole(query) {
  return request({
    url: '/payment/sysRole/export',
    method: 'get',
    params: query
  })
}