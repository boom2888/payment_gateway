import request from '@/utils/request'

// 查询风险等级配置列表
export function listConfigRiskLevel(query) {
  return request({
    url: '/payment/configRiskLevel/list',
    method: 'get',
    params: query
  })
}

// 查询风险等级配置详细
export function getConfigRiskLevel(id) {
  return request({
    url: '/payment/configRiskLevel/' + id,
    method: 'get'
  })
}

// 新增风险等级配置
export function addConfigRiskLevel(data) {
  return request({
    url: '/payment/configRiskLevel',
    method: 'post',
    data: data
  })
}

// 修改风险等级配置
export function updateConfigRiskLevel(data) {
  return request({
    url: '/payment/configRiskLevel',
    method: 'put',
    data: data
  })
}

// 删除风险等级配置
export function delConfigRiskLevel(id) {
  return request({
    url: '/payment/configRiskLevel/' + id,
    method: 'delete'
  })
}

// 导出风险等级配置
export function exportConfigRiskLevel(query) {
  return request({
    url: '/payment/configRiskLevel/export',
    method: 'get',
    params: query
  })
}