import request from '@/utils/request'

// 查询访问令牌列表
export function listToken(query) {
  return request({
    url: '/payment/token/list',
    method: 'get',
    params: query
  })
}

// 查询访问令牌详细
export function getToken(id) {
  return request({
    url: '/payment/token/' + id,
    method: 'get'
  })
}

// 新增访问令牌
export function addToken(data) {
  return request({
    url: '/payment/token',
    method: 'post',
    data: data
  })
}

// 修改访问令牌
export function updateToken(data) {
  return request({
    url: '/payment/token',
    method: 'put',
    data: data
  })
}

// 删除访问令牌
export function delToken(id) {
  return request({
    url: '/payment/token/' + id,
    method: 'delete'
  })
}

// 导出访问令牌
export function exportToken(query) {
  return request({
    url: '/payment/token/export',
    method: 'get',
    params: query
  })
}