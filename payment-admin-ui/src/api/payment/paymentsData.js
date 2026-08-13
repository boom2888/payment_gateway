import request from '@/utils/request'

// 查询支付数据列表
export function listPaymentsData(query) {
  return request({
    url: '/payment/paymentsData/list',
    method: 'get',
    params: query
  })
}

// 查询支付数据详细
export function getPaymentsData(id) {
  return request({
    url: '/payment/paymentsData/' + id,
    method: 'get'
  })
}

// 新增支付数据
export function addPaymentsData(data) {
  return request({
    url: '/payment/paymentsData',
    method: 'post',
    data: data
  })
}

// 修改支付数据
export function updatePaymentsData(data) {
  return request({
    url: '/payment/paymentsData',
    method: 'put',
    data: data
  })
}

// 删除支付数据
export function delPaymentsData(id) {
  return request({
    url: '/payment/paymentsData/' + id,
    method: 'delete'
  })
}

// 导出支付数据
export function exportPaymentsData(query) {
  return request({
    url: '/payment/paymentsData/export',
    method: 'get',
    params: query
  })
}