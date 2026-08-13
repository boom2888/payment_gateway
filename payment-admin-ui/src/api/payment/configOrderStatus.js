import request from '@/utils/request'

// 查询订单状态配置列表
export function listConfigOrderStatus(query) {
  return request({
    url: '/payment/configOrderStatus/list',
    method: 'get',
    params: query
  })
}

// 查询订单状态配置详细
export function getConfigOrderStatus(code) {
  return request({
    url: '/payment/configOrderStatus/' + code,
    method: 'get'
  })
}

// 新增订单状态配置
export function addConfigOrderStatus(data) {
  return request({
    url: '/payment/configOrderStatus',
    method: 'post',
    data: data
  })
}

// 修改订单状态配置
export function updateConfigOrderStatus(data) {
  return request({
    url: '/payment/configOrderStatus',
    method: 'put',
    data: data
  })
}

// 删除订单状态配置
export function delConfigOrderStatus(code) {
  return request({
    url: '/payment/configOrderStatus/' + code,
    method: 'delete'
  })
}

// 导出订单状态配置
export function exportConfigOrderStatus(query) {
  return request({
    url: '/payment/configOrderStatus/export',
    method: 'get',
    params: query
  })
}