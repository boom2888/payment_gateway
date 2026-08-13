import request from '@/utils/request'

// 查询用户信息列表
export function listSysUser(query) {
  return request({
    url: '/payment/sysUser/list',
    method: 'get',
    params: query
  })
}

// 查询用户信息详细
export function getSysUser(userId) {
  return request({
    url: '/payment/sysUser/' + userId,
    method: 'get'
  })
}

// 新增用户信息
export function addSysUser(data) {
  return request({
    url: '/payment/sysUser',
    method: 'post',
    data: data
  })
}

// 修改用户信息
export function updateSysUser(data) {
  return request({
    url: '/payment/sysUser',
    method: 'put',
    data: data
  })
}

// 删除用户信息
export function delSysUser(userId) {
  return request({
    url: '/payment/sysUser/' + userId,
    method: 'delete'
  })
}

// 导出用户信息
export function exportSysUser(query) {
  return request({
    url: '/payment/sysUser/export',
    method: 'get',
    params: query
  })
}