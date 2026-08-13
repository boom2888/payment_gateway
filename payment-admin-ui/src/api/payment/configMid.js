import request from '@/utils/request'

// 查询MID配置列表
export function listConfigMid(query) {
  return request({
    url: '/payment/configMid/list',
    method: 'get',
    params: query
  })
}

// 查询MID配置详细
export function getConfigMid(id) {
  return request({
    url: '/payment/configMid/' + id,
    method: 'get'
  })
}

// 新增MID配置
export function addConfigMid(data) {
  return request({
    url: '/payment/configMid',
    method: 'post',
    data: data
  })
}

// 修改MID配置
export function updateConfigMid(data) {
  return request({
    url: '/payment/configMid',
    method: 'put',
    data: data
  })
}

// 删除MID配置
export function delConfigMid(id) {
  return request({
    url: '/payment/configMid/' + id,
    method: 'delete'
  })
}

// 导出MID配置
export function exportConfigMid(query) {
  return request({
    url: '/payment/configMid/export',
    method: 'get',
    params: query
  })
}