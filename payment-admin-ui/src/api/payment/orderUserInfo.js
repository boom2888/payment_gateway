import request from '@/utils/request'

// 查询订单用户信息列表
export function listOrderUserInfo(query) {
  return request({
    url: '/payment/orderUserInfo/list',
    method: 'get',
    params: query
  })
}

// 查询订单用户信息详细
export function getOrderUserInfo(id) {
  return request({
    url: '/payment/orderUserInfo/' + id,
    method: 'get'
  })
}

// 新增订单用户信息
export function addOrderUserInfo(data) {
  return request({
    url: '/payment/orderUserInfo',
    method: 'post',
    data: data
  })
}

// 修改订单用户信息
export function updateOrderUserInfo(data) {
  return request({
    url: '/payment/orderUserInfo',
    method: 'put',
    data: data
  })
}

// 删除订单用户信息
export function delOrderUserInfo(id) {
  return request({
    url: '/payment/orderUserInfo/' + id,
    method: 'delete'
  })
}

// 导出订单用户信息
export function exportOrderUserInfo(query) {
  return request({
    url: '/payment/orderUserInfo/export',
    method: 'get',
    params: query
  })
}