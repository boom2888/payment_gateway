import request from '@/utils/request'

// 查询系统访问记录列表
export function listSysLogininfor(query) {
  return request({
    url: '/payment/sysLogininfor/list',
    method: 'get',
    params: query
  })
}

// 查询系统访问记录详细
export function getSysLogininfor(infoId) {
  return request({
    url: '/payment/sysLogininfor/' + infoId,
    method: 'get'
  })
}

// 新增系统访问记录
export function addSysLogininfor(data) {
  return request({
    url: '/payment/sysLogininfor',
    method: 'post',
    data: data
  })
}

// 修改系统访问记录
export function updateSysLogininfor(data) {
  return request({
    url: '/payment/sysLogininfor',
    method: 'put',
    data: data
  })
}

// 删除系统访问记录
export function delSysLogininfor(infoId) {
  return request({
    url: '/payment/sysLogininfor/' + infoId,
    method: 'delete'
  })
}

// 导出系统访问记录
export function exportSysLogininfor(query) {
  return request({
    url: '/payment/sysLogininfor/export',
    method: 'get',
    params: query
  })
}