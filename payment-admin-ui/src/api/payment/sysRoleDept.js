import request from '@/utils/request'

// 查询角色和部门关联列表
export function listSysRoleDept(query) {
  return request({
    url: '/payment/sysRoleDept/list',
    method: 'get',
    params: query
  })
}

// 查询角色和部门关联详细
export function getSysRoleDept(roleId) {
  return request({
    url: '/payment/sysRoleDept/' + roleId,
    method: 'get'
  })
}

// 新增角色和部门关联
export function addSysRoleDept(data) {
  return request({
    url: '/payment/sysRoleDept',
    method: 'post',
    data: data
  })
}

// 修改角色和部门关联
export function updateSysRoleDept(data) {
  return request({
    url: '/payment/sysRoleDept',
    method: 'put',
    data: data
  })
}

// 删除角色和部门关联
export function delSysRoleDept(roleId) {
  return request({
    url: '/payment/sysRoleDept/' + roleId,
    method: 'delete'
  })
}

// 导出角色和部门关联
export function exportSysRoleDept(query) {
  return request({
    url: '/payment/sysRoleDept/export',
    method: 'get',
    params: query
  })
}