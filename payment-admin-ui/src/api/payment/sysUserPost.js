import request from '@/utils/request'

// 查询用户与岗位关联列表
export function listSysUserPost(query) {
  return request({
    url: '/payment/sysUserPost/list',
    method: 'get',
    params: query
  })
}

// 查询用户与岗位关联详细
export function getSysUserPost(userId) {
  return request({
    url: '/payment/sysUserPost/' + userId,
    method: 'get'
  })
}

// 新增用户与岗位关联
export function addSysUserPost(data) {
  return request({
    url: '/payment/sysUserPost',
    method: 'post',
    data: data
  })
}

// 修改用户与岗位关联
export function updateSysUserPost(data) {
  return request({
    url: '/payment/sysUserPost',
    method: 'put',
    data: data
  })
}

// 删除用户与岗位关联
export function delSysUserPost(userId) {
  return request({
    url: '/payment/sysUserPost/' + userId,
    method: 'delete'
  })
}

// 导出用户与岗位关联
export function exportSysUserPost(query) {
  return request({
    url: '/payment/sysUserPost/export',
    method: 'get',
    params: query
  })
}