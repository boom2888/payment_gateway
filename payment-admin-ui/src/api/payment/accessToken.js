import request from '@/utils/request'

// 查询访问令牌列表
export function listAccessToken(query) {
  return request({
    url: '/payment/accessToken/list',
    method: 'get',
    params: query
  })
}

// 查询访问令牌详细
export function getAccessToken(id) {
  return request({
    url: '/payment/accessToken/' + id,
    method: 'get'
  })
}

// 新增访问令牌
export function addAccessToken(data) {
  return request({
    url: '/payment/accessToken',
    method: 'post',
    data: data
  })
}

// 修改访问令牌
export function updateAccessToken(data) {
  return request({
    url: '/payment/accessToken',
    method: 'put',
    data: data
  })
}

// 删除访问令牌
export function delAccessToken(id) {
  return request({
    url: '/payment/accessToken/' + id,
    method: 'delete'
  })
}

// 导出访问令牌
export function exportAccessToken(query) {
  return request({
    url: '/payment/accessToken/export',
    method: 'get',
    params: query
  })
}