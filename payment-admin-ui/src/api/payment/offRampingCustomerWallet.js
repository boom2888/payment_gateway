import request from '@/utils/request'

// 查询出金客户钱包列表
export function listOffRampingCustomerWallet(query) {
  return request({
    url: '/payment/offRampingCustomerWallet/list',
    method: 'get',
    params: query
  })
}

// 查询出金客户钱包详细
export function getOffRampingCustomerWallet(id) {
  return request({
    url: '/payment/offRampingCustomerWallet/' + id,
    method: 'get'
  })
}

// 新增出金客户钱包
export function addOffRampingCustomerWallet(data) {
  return request({
    url: '/payment/offRampingCustomerWallet',
    method: 'post',
    data: data
  })
}

// 修改出金客户钱包
export function updateOffRampingCustomerWallet(data) {
  return request({
    url: '/payment/offRampingCustomerWallet',
    method: 'put',
    data: data
  })
}

// 删除出金客户钱包
export function delOffRampingCustomerWallet(id) {
  return request({
    url: '/payment/offRampingCustomerWallet/' + id,
    method: 'delete'
  })
}

// 导出出金客户钱包
export function exportOffRampingCustomerWallet(query) {
  return request({
    url: '/payment/offRampingCustomerWallet/export',
    method: 'get',
    params: query
  })
}