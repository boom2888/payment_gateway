import request from '@/utils/request'

// 查询大洲配置列表
export function listConfigContinent(query) {
  return request({
    url: '/payment/configContinent/list',
    method: 'get',
    params: query
  })
}

// 查询大洲配置详细
export function getConfigContinent(code) {
  return request({
    url: '/payment/configContinent/' + code,
    method: 'get'
  })
}

// 新增大洲配置
export function addConfigContinent(data) {
  return request({
    url: '/payment/configContinent',
    method: 'post',
    data: data
  })
}

// 修改大洲配置
export function updateConfigContinent(data) {
  return request({
    url: '/payment/configContinent',
    method: 'put',
    data: data
  })
}

// 删除大洲配置
export function delConfigContinent(code) {
  return request({
    url: '/payment/configContinent/' + code,
    method: 'delete'
  })
}

// 导出大洲配置
export function exportConfigContinent(query) {
  return request({
    url: '/payment/configContinent/export',
    method: 'get',
    params: query
  })
}