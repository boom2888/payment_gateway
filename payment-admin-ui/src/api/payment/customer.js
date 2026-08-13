import request from '@/utils/request'

// 查询客户列表
export function listCustomer(query) {
  return request({
    url: '/payment/customer/list',
    method: 'get',
    params: query
  })
}

// 查询客户详细
export function getCustomer(id) {
  return request({
    url: '/payment/customer/' + id,
    method: 'get'
  })
}

// 新增客户
export function addCustomer(data) {
  return request({
    url: '/payment/customer',
    method: 'post',
    data: data
  })
}

// 修改客户
export function updateCustomer(data) {
  return request({
    url: '/payment/customer',
    method: 'put',
    data: data
  })
}

// 删除客户
export function delCustomer(id) {
  return request({
    url: '/payment/customer/' + id,
    method: 'delete'
  })
}

// 导出客户
export function exportCustomer(query) {
  return request({
    url: '/payment/customer/export',
    method: 'get',
    params: query
  })
}