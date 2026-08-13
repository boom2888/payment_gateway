import request from '@/utils/request'

// 查询重置密码链接列表
export function listResetPasswordLink(query) {
  return request({
    url: '/payment/resetPasswordLink/list',
    method: 'get',
    params: query
  })
}

// 查询重置密码链接详细
export function getResetPasswordLink(id) {
  return request({
    url: '/payment/resetPasswordLink/' + id,
    method: 'get'
  })
}

// 新增重置密码链接
export function addResetPasswordLink(data) {
  return request({
    url: '/payment/resetPasswordLink',
    method: 'post',
    data: data
  })
}

// 修改重置密码链接
export function updateResetPasswordLink(data) {
  return request({
    url: '/payment/resetPasswordLink',
    method: 'put',
    data: data
  })
}

// 删除重置密码链接
export function delResetPasswordLink(id) {
  return request({
    url: '/payment/resetPasswordLink/' + id,
    method: 'delete'
  })
}

// 导出重置密码链接
export function exportResetPasswordLink(query) {
  return request({
    url: '/payment/resetPasswordLink/export',
    method: 'get',
    params: query
  })
}