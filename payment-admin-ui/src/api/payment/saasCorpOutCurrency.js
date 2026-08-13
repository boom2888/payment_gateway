import request from '@/utils/request'

// 查询SaaS用户公司出金支持货币列表
export function listSaasCorpOutCurrency(query) {
  return request({
    url: '/payment/saasCorpOutCurrency/list',
    method: 'get',
    params: query
  })
}

// 查询SaaS用户公司出金支持货币详细
export function getSaasCorpOutCurrency(id) {
  return request({
    url: '/payment/saasCorpOutCurrency/' + id,
    method: 'get'
  })
}

// 新增SaaS用户公司出金支持货币
export function addSaasCorpOutCurrency(data) {
  return request({
    url: '/payment/saasCorpOutCurrency',
    method: 'post',
    data: data
  })
}

// 修改SaaS用户公司出金支持货币
export function updateSaasCorpOutCurrency(data) {
  return request({
    url: '/payment/saasCorpOutCurrency',
    method: 'put',
    data: data
  })
}

// 删除SaaS用户公司出金支持货币
export function delSaasCorpOutCurrency(id) {
  return request({
    url: '/payment/saasCorpOutCurrency/' + id,
    method: 'delete'
  })
}

// 导出SaaS用户公司出金支持货币
export function exportSaasCorpOutCurrency(query) {
  return request({
    url: '/payment/saasCorpOutCurrency/export',
    method: 'get',
    params: query
  })
}