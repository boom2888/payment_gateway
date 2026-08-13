import request from '@/utils/request'

// 查询定时任务调度日志列表
export function listSysJobLog(query) {
  return request({
    url: '/payment/sysJobLog/list',
    method: 'get',
    params: query
  })
}

// 查询定时任务调度日志详细
export function getSysJobLog(jobLogId) {
  return request({
    url: '/payment/sysJobLog/' + jobLogId,
    method: 'get'
  })
}

// 新增定时任务调度日志
export function addSysJobLog(data) {
  return request({
    url: '/payment/sysJobLog',
    method: 'post',
    data: data
  })
}

// 修改定时任务调度日志
export function updateSysJobLog(data) {
  return request({
    url: '/payment/sysJobLog',
    method: 'put',
    data: data
  })
}

// 删除定时任务调度日志
export function delSysJobLog(jobLogId) {
  return request({
    url: '/payment/sysJobLog/' + jobLogId,
    method: 'delete'
  })
}

// 导出定时任务调度日志
export function exportSysJobLog(query) {
  return request({
    url: '/payment/sysJobLog/export',
    method: 'get',
    params: query
  })
}