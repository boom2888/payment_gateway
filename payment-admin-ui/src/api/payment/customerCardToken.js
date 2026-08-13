import request from '@/utils/request'

// 查询客户卡令牌列表
export function listCustomerCardToken(query) {
  return request({
    url: '/payment/customerCardToken/list',
    method: 'get',
    params: query
  })
}

// 查询客户卡令牌详细
export function getCustomerCardToken(id) {
  return request({
    url: '/payment/customerCardToken/' + id,
    method: 'get'
  })
}

// 新增客户卡令牌
export function addCustomerCardToken(data) {
  return request({
    url: '/payment/customerCardToken',
    method: 'post',
    data: data
  })
}

// 修改客户卡令牌
export function updateCustomerCardToken(data) {
  return request({
    url: '/payment/customerCardToken',
    method: 'put',
    data: data
  })
}

// 删除客户卡令牌
export function delCustomerCardToken(id) {
  return request({
    url: '/payment/customerCardToken/' + id,
    method: 'delete'
  })
}

// 导出客户卡令牌
export function exportCustomerCardToken(query) {
  return request({
    url: '/payment/customerCardToken/export',
    method: 'get',
    params: query
  })
}