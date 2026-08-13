import request from '@/utils/request'

// 查询SaaS用户列表
export function listSaasUser(query) {
  return request({
    url: '/payment/saasUser/list',
    method: 'get',
    params: query
  })
}

// 查询SaaS用户详细
export function getSaasUser(id) {
  return request({
    url: '/payment/saasUser/' + id,
    method: 'get'
  })
}

// 新增SaaS用户
export function addSaasUser(data) {
  return request({
    url: '/payment/saasUser',
    method: 'post',
    data: data
  })
}

// 修改SaaS用户
export function updateSaasUser(data) {
  return request({
    url: '/payment/saasUser',
    method: 'put',
    data: data
  })
}

// 删除SaaS用户
export function delSaasUser(id) {
  return request({
    url: '/payment/saasUser/' + id,
    method: 'delete'
  })
}

// 导出SaaS用户
export function exportSaasUser(query) {
  return request({
    url: '/payment/saasUser/export',
    method: 'get',
    params: query
  })
}