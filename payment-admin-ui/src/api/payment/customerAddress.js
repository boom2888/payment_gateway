import request from '@/utils/request'

// 查询客户地址列表
export function listCustomerAddress(query) {
  return request({
    url: '/payment/customerAddress/list',
    method: 'get',
    params: query
  })
}

// 查询客户地址详细
export function getCustomerAddress(id) {
  return request({
    url: '/payment/customerAddress/' + id,
    method: 'get'
  })
}

// 新增客户地址
export function addCustomerAddress(data) {
  return request({
    url: '/payment/customerAddress',
    method: 'post',
    data: data
  })
}

// 修改客户地址
export function updateCustomerAddress(data) {
  return request({
    url: '/payment/customerAddress',
    method: 'put',
    data: data
  })
}

// 删除客户地址
export function delCustomerAddress(id) {
  return request({
    url: '/payment/customerAddress/' + id,
    method: 'delete'
  })
}

// 导出客户地址
export function exportCustomerAddress(query) {
  return request({
    url: '/payment/customerAddress/export',
    method: 'get',
    params: query
  })
}