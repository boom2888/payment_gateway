import request from '@/utils/request'

// 查询汇率报告列表
export function listReportQuote(query) {
  return request({
    url: '/payment/reportQuote/list',
    method: 'get',
    params: query
  })
}

// 查询汇率报告详细
export function getReportQuote(id) {
  return request({
    url: '/payment/reportQuote/' + id,
    method: 'get'
  })
}

// 新增汇率报告
export function addReportQuote(data) {
  return request({
    url: '/payment/reportQuote',
    method: 'post',
    data: data
  })
}

// 修改汇率报告
export function updateReportQuote(data) {
  return request({
    url: '/payment/reportQuote',
    method: 'put',
    data: data
  })
}

// 删除汇率报告
export function delReportQuote(id) {
  return request({
    url: '/payment/reportQuote/' + id,
    method: 'delete'
  })
}

// 导出汇率报告
export function exportReportQuote(query) {
  return request({
    url: '/payment/reportQuote/export',
    method: 'get',
    params: query
  })
}