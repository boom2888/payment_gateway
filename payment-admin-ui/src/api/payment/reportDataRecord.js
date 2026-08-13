import request from '@/utils/request'

// 查询报告数据记录列表
export function listReportDataRecord(query) {
  return request({
    url: '/payment/reportDataRecord/list',
    method: 'get',
    params: query
  })
}

// 查询报告数据记录详细
export function getReportDataRecord(id) {
  return request({
    url: '/payment/reportDataRecord/' + id,
    method: 'get'
  })
}

// 新增报告数据记录
export function addReportDataRecord(data) {
  return request({
    url: '/payment/reportDataRecord',
    method: 'post',
    data: data
  })
}

// 修改报告数据记录
export function updateReportDataRecord(data) {
  return request({
    url: '/payment/reportDataRecord',
    method: 'put',
    data: data
  })
}

// 删除报告数据记录
export function delReportDataRecord(id) {
  return request({
    url: '/payment/reportDataRecord/' + id,
    method: 'delete'
  })
}

// 导出报告数据记录
export function exportReportDataRecord(query) {
  return request({
    url: '/payment/reportDataRecord/export',
    method: 'get',
    params: query
  })
}