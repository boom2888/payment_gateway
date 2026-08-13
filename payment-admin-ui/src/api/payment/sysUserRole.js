import request from '@/utils/request'

// 查询用户和角色关联列表
export function listSysUserRole(query) {
  return request({
    url: '/payment/sysUserRole/list',
    method: 'get',
    params: query
  })
}

// 查询用户和角色关联详细
export function getSysUserRole(userId) {
  return request({
    url: '/payment/sysUserRole/' + userId,
    method: 'get'
  })
}

// 新增用户和角色关联
export function addSysUserRole(data) {
  return request({
    url: '/payment/sysUserRole',
    method: 'post',
    data: data
  })
}

// 修改用户和角色关联
export function updateSysUserRole(data) {
  return request({
    url: '/payment/sysUserRole',
    method: 'put',
    data: data
  })
}

// 删除用户和角色关联
export function delSysUserRole(userId) {
  return request({
    url: '/payment/sysUserRole/' + userId,
    method: 'delete'
  })
}

// 导出用户和角色关联
export function exportSysUserRole(query) {
  return request({
    url: '/payment/sysUserRole/export',
    method: 'get',
    params: query
  })
}