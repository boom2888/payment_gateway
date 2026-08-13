import request from '@/utils/request'

// 查询接口日志列表
export function listApiLog(query) {
  return request({
    url: '/payment/apiLog/list',
    method: 'get',
    params: query
  })
}

// 查询接口日志详细
export function getApiLog(id) {
  return request({
    url: '/payment/apiLog/' + id,
    method: 'get'
  })
}

// 新增接口日志
export function addApiLog(data) {
  return request({
    url: '/payment/apiLog',
    method: 'post',
    data: data
  })
}

// 修改接口日志
export function updateApiLog(data) {
  return request({
    url: '/payment/apiLog',
    method: 'put',
    data: data
  })
}

// 删除接口日志
export function delApiLog(id) {
  return request({
    url: '/payment/apiLog/' + id,
    method: 'delete'
  })
}

// 导出接口日志
export function exportApiLog(query) {
  return request({
    url: '/payment/apiLog/export',
    method: 'get',
    params: query
  })
}