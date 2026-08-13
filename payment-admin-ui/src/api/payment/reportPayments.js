import request from '@/utils/request'

// 查询支付报告列表
export function listReportPayments(query) {
  return request({
    url: '/payment/reportPayments/list',
    method: 'get',
    params: query
  })
}

// 查询支付报告详细
export function getReportPayments(id) {
  return request({
    url: '/payment/reportPayments/' + id,
    method: 'get'
  })
}

// 新增支付报告
export function addReportPayments(data) {
  return request({
    url: '/payment/reportPayments',
    method: 'post',
    data: data
  })
}

// 修改支付报告
export function updateReportPayments(data) {
  return request({
    url: '/payment/reportPayments',
    method: 'put',
    data: data
  })
}

// 删除支付报告
export function delReportPayments(id) {
  return request({
    url: '/payment/reportPayments/' + id,
    method: 'delete'
  })
}

// 导出支付报告
export function exportReportPayments(query) {
  return request({
    url: '/payment/reportPayments/export',
    method: 'get',
    params: query
  })
}