import request from '@/utils/request'

// 查询支付信息列表
export function listPayoutInfo(query) {
  return request({
    url: '/payment/payoutInfo/list',
    method: 'get',
    params: query
  })
}

// 查询支付信息详细
export function getPayoutInfo(id) {
  return request({
    url: '/payment/payoutInfo/' + id,
    method: 'get'
  })
}

// 新增支付信息
export function addPayoutInfo(data) {
  return request({
    url: '/payment/payoutInfo',
    method: 'post',
    data: data
  })
}

// 修改支付信息
export function updatePayoutInfo(data) {
  return request({
    url: '/payment/payoutInfo',
    method: 'put',
    data: data
  })
}

// 删除支付信息
export function delPayoutInfo(id) {
  return request({
    url: '/payment/payoutInfo/' + id,
    method: 'delete'
  })
}

// 导出支付信息
export function exportPayoutInfo(query) {
  return request({
    url: '/payment/payoutInfo/export',
    method: 'get',
    params: query
  })
}