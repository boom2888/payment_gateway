import request from '@/utils/request'

// 查询角色列表
export function listRole(query) {
  return request({
    url: '/payment/role/list',
    method: 'get',
    params: query
  })
}

// 查询角色详细
export function getRole(id) {
  return request({
    url: '/payment/role/' + id,
    method: 'get'
  })
}

// 新增角色
export function addRole(data) {
  return request({
    url: '/payment/role',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateRole(data) {
  return request({
    url: '/payment/role',
    method: 'put',
    data: data
  })
}

// 删除角色
export function delRole(id) {
  return request({
    url: '/payment/role/' + id,
    method: 'delete'
  })
}

// 导出角色
export function exportRole(query) {
  return request({
    url: '/payment/role/export',
    method: 'get',
    params: query
  })
}