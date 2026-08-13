import request from '@/utils/request'

// 查询订单对账列表
export function listOrderRecon(query) {
  return request({
    url: '/payment/orderRecon/list',
    method: 'get',
    params: query
  })
}

// 查询订单对账详细
export function getOrderRecon(id) {
  return request({
    url: '/payment/orderRecon/' + id,
    method: 'get'
  })
}

// 新增订单对账
export function addOrderRecon(data) {
  return request({
    url: '/payment/orderRecon',
    method: 'post',
    data: data
  })
}

// 修改订单对账
export function updateOrderRecon(data) {
  return request({
    url: '/payment/orderRecon',
    method: 'put',
    data: data
  })
}

// 删除订单对账
export function delOrderRecon(id) {
  return request({
    url: '/payment/orderRecon/' + id,
    method: 'delete'
  })
}

// 导出订单对账
export function exportOrderRecon(query) {
  return request({
    url: '/payment/orderRecon/export',
    method: 'get',
    params: query
  })
}