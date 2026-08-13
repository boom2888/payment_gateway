import request from '@/utils/request'

// 查询客户钱包列表
export function listCustomerWallet(query) {
  return request({
    url: '/payment/customerWallet/list',
    method: 'get',
    params: query
  })
}

// 查询客户钱包详细
export function getCustomerWallet(id) {
  return request({
    url: '/payment/customerWallet/' + id,
    method: 'get'
  })
}

// 新增客户钱包
export function addCustomerWallet(data) {
  return request({
    url: '/payment/customerWallet',
    method: 'post',
    data: data
  })
}

// 修改客户钱包
export function updateCustomerWallet(data) {
  return request({
    url: '/payment/customerWallet',
    method: 'put',
    data: data
  })
}

// 删除客户钱包
export function delCustomerWallet(id) {
  return request({
    url: '/payment/customerWallet/' + id,
    method: 'delete'
  })
}

// 导出客户钱包
export function exportCustomerWallet(query) {
  return request({
    url: '/payment/customerWallet/export',
    method: 'get',
    params: query
  })
}