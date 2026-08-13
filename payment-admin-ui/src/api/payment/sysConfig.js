import request from '@/utils/request'

// 查询参数配置列表
export function listSysConfig(query) {
  return request({
    url: '/payment/sysConfig/list',
    method: 'get',
    params: query
  })
}

// 查询参数配置详细
export function getSysConfig(configId) {
  return request({
    url: '/payment/sysConfig/' + configId,
    method: 'get'
  })
}

// 新增参数配置
export function addSysConfig(data) {
  return request({
    url: '/payment/sysConfig',
    method: 'post',
    data: data
  })
}

// 修改参数配置
export function updateSysConfig(data) {
  return request({
    url: '/payment/sysConfig',
    method: 'put',
    data: data
  })
}

// 删除参数配置
export function delSysConfig(configId) {
  return request({
    url: '/payment/sysConfig/' + configId,
    method: 'delete'
  })
}

// 导出参数配置
export function exportSysConfig(query) {
  return request({
    url: '/payment/sysConfig/export',
    method: 'get',
    params: query
  })
}