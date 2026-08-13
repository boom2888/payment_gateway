import request from '@/utils/request'

// 查询审计日志列表
export function listAuditLog(query) {
  return request({
    url: '/payment/auditLog/list',
    method: 'get',
    params: query
  })
}

// 查询审计日志详细
export function getAuditLog(id) {
  return request({
    url: '/payment/auditLog/' + id,
    method: 'get'
  })
}

// 新增审计日志
export function addAuditLog(data) {
  return request({
    url: '/payment/auditLog',
    method: 'post',
    data: data
  })
}

// 修改审计日志
export function updateAuditLog(data) {
  return request({
    url: '/payment/auditLog',
    method: 'put',
    data: data
  })
}

// 删除审计日志
export function delAuditLog(id) {
  return request({
    url: '/payment/auditLog/' + id,
    method: 'delete'
  })
}

// 导出审计日志
export function exportAuditLog(query) {
  return request({
    url: '/payment/auditLog/export',
    method: 'get',
    params: query
  })
}