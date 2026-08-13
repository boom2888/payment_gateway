import request from '@/utils/request'

// 查询国家配置列表
export function listConfigCountry(query) {
  return request({
    url: '/payment/configCountry/list',
    method: 'get',
    params: query
  })
}

// 查询国家配置详细
export function getConfigCountry(code) {
  return request({
    url: '/payment/configCountry/' + code,
    method: 'get'
  })
}

// 新增国家配置
export function addConfigCountry(data) {
  return request({
    url: '/payment/configCountry',
    method: 'post',
    data: data
  })
}

// 修改国家配置
export function updateConfigCountry(data) {
  return request({
    url: '/payment/configCountry',
    method: 'put',
    data: data
  })
}

// 删除国家配置
export function delConfigCountry(code) {
  return request({
    url: '/payment/configCountry/' + code,
    method: 'delete'
  })
}

// 导出国家配置
export function exportConfigCountry(query) {
  return request({
    url: '/payment/configCountry/export',
    method: 'get',
    params: query
  })
}