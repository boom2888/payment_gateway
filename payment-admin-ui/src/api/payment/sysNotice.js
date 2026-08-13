import request from '@/utils/request'

// 查询通知公告列表
export function listSysNotice(query) {
  return request({
    url: '/payment/sysNotice/list',
    method: 'get',
    params: query
  })
}

// 查询通知公告详细
export function getSysNotice(noticeId) {
  return request({
    url: '/payment/sysNotice/' + noticeId,
    method: 'get'
  })
}

// 新增通知公告
export function addSysNotice(data) {
  return request({
    url: '/payment/sysNotice',
    method: 'post',
    data: data
  })
}

// 修改通知公告
export function updateSysNotice(data) {
  return request({
    url: '/payment/sysNotice',
    method: 'put',
    data: data
  })
}

// 删除通知公告
export function delSysNotice(noticeId) {
  return request({
    url: '/payment/sysNotice/' + noticeId,
    method: 'delete'
  })
}

// 导出通知公告
export function exportSysNotice(query) {
  return request({
    url: '/payment/sysNotice/export',
    method: 'get',
    params: query
  })
}