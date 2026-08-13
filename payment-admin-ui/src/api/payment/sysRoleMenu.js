import request from '@/utils/request'

// 查询角色和菜单关联列表
export function listSysRoleMenu(query) {
  return request({
    url: '/payment/sysRoleMenu/list',
    method: 'get',
    params: query
  })
}

// 查询角色和菜单关联详细
export function getSysRoleMenu(roleId) {
  return request({
    url: '/payment/sysRoleMenu/' + roleId,
    method: 'get'
  })
}

// 新增角色和菜单关联
export function addSysRoleMenu(data) {
  return request({
    url: '/payment/sysRoleMenu',
    method: 'post',
    data: data
  })
}

// 修改角色和菜单关联
export function updateSysRoleMenu(data) {
  return request({
    url: '/payment/sysRoleMenu',
    method: 'put',
    data: data
  })
}

// 删除角色和菜单关联
export function delSysRoleMenu(roleId) {
  return request({
    url: '/payment/sysRoleMenu/' + roleId,
    method: 'delete'
  })
}

// 导出角色和菜单关联
export function exportSysRoleMenu(query) {
  return request({
    url: '/payment/sysRoleMenu/export',
    method: 'get',
    params: query
  })
}