import request from '@/utils/request'

// 查询支付设置列表
export function listPaymentSetting(query) {
  return request({
    url: '/payment/paymentSetting/list',
    method: 'get',
    params: query
  })
}

// 查询支付设置详细
export function getPaymentSetting(id) {
  return request({
    url: '/payment/paymentSetting/' + id,
    method: 'get'
  })
}

// 新增支付设置
export function addPaymentSetting(data) {
  return request({
    url: '/payment/paymentSetting',
    method: 'post',
    data: data
  })
}

// 修改支付设置
export function updatePaymentSetting(data) {
  return request({
    url: '/payment/paymentSetting',
    method: 'put',
    data: data
  })
}

// 删除支付设置
export function delPaymentSetting(id) {
  return request({
    url: '/payment/paymentSetting/' + id,
    method: 'delete'
  })
}

// 导出支付设置
export function exportPaymentSetting(query) {
  return request({
    url: '/payment/paymentSetting/export',
    method: 'get',
    params: query
  })
}