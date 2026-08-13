import request from '@/utils/request'

// 查询欺诈报告列表
export function listReportFraudulent(query) {
  return request({
    url: '/payment/reportFraudulent/list',
    method: 'get',
    params: query
  })
}

// 查询欺诈报告详细
export function getReportFraudulent(id) {
  return request({
    url: '/payment/reportFraudulent/' + id,
    method: 'get'
  })
}

// 新增欺诈报告
export function addReportFraudulent(data) {
  return request({
    url: '/payment/reportFraudulent',
    method: 'post',
    data: data
  })
}

// 修改欺诈报告
export function updateReportFraudulent(data) {
  return request({
    url: '/payment/reportFraudulent',
    method: 'put',
    data: data
  })
}

// 删除欺诈报告
export function delReportFraudulent(id) {
  return request({
    url: '/payment/reportFraudulent/' + id,
    method: 'delete'
  })
}

// 导出欺诈报告
export function exportReportFraudulent(query) {
  return request({
    url: '/payment/reportFraudulent/export',
    method: 'get',
    params: query
  })
}