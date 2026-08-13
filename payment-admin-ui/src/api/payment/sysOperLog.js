import request from '@/utils/request'

// 查询操作日志记录列表
export function listSysOperLog(query) {
  return request({
    url: '/payment/sysOperLog/list',
    method: 'get',
    params: query
  })
}

// 查询操作日志记录详细
export function getSysOperLog(operId) {
  return request({
    url: '/payment/sysOperLog/' + operId,
    method: 'get'
  })
}

// 新增操作日志记录
export function addSysOperLog(data) {
  return request({
    url: '/payment/sysOperLog',
    method: 'post',
    data: data
  })
}

// 修改操作日志记录
export function updateSysOperLog(data) {
  return request({
    url: '/payment/sysOperLog',
    method: 'put',
    data: data
  })
}

// 删除操作日志记录
export function delSysOperLog(operId) {
  return request({
    url: '/payment/sysOperLog/' + operId,
    method: 'delete'
  })
}

// 导出操作日志记录
export function exportSysOperLog(query) {
  return request({
    url: '/payment/sysOperLog/export',
    method: 'get',
    params: query
  })
}