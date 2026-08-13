import request from '@/utils/request'

// 查询定时任务调度列表
export function listSysJob(query) {
  return request({
    url: '/payment/sysJob/list',
    method: 'get',
    params: query
  })
}

// 查询定时任务调度详细
export function getSysJob(jobId) {
  return request({
    url: '/payment/sysJob/' + jobId,
    method: 'get'
  })
}

// 新增定时任务调度
export function addSysJob(data) {
  return request({
    url: '/payment/sysJob',
    method: 'post',
    data: data
  })
}

// 修改定时任务调度
export function updateSysJob(data) {
  return request({
    url: '/payment/sysJob',
    method: 'put',
    data: data
  })
}

// 删除定时任务调度
export function delSysJob(jobId) {
  return request({
    url: '/payment/sysJob/' + jobId,
    method: 'delete'
  })
}

// 导出定时任务调度
export function exportSysJob(query) {
  return request({
    url: '/payment/sysJob/export',
    method: 'get',
    params: query
  })
}