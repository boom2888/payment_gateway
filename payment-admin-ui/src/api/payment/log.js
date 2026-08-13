import request from '@/utils/request'

// 查询审计日志列表
export function listLog(query) {
  return request({
    url: '/payment/log/list',
    method: 'get',
    params: query
  })
}

// 查询审计日志详细
export function getLog(id) {
  return request({
    url: '/payment/log/' + id,
    method: 'get'
  })
}

// 新增审计日志
export function addLog(data) {
  return request({
    url: '/payment/log',
    method: 'post',
    data: data
  })
}

// 修改审计日志
export function updateLog(data) {
  return request({
    url: '/payment/log',
    method: 'put',
    data: data
  })
}

// 删除审计日志
export function delLog(id) {
  return request({
    url: '/payment/log/' + id,
    method: 'delete'
  })
}

// 导出审计日志
export function exportLog(query) {
  return request({
    url: '/payment/log/export',
    method: 'get',
    params: query
  })
}