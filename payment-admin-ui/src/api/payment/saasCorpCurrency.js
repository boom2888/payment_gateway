import request from '@/utils/request'

// 查询SaaS用户公司支持货币列表
export function listSaasCorpCurrency(query) {
  return request({
    url: '/payment/saasCorpCurrency/list',
    method: 'get',
    params: query
  })
}

// 查询SaaS用户公司支持货币详细
export function getSaasCorpCurrency(id) {
  return request({
    url: '/payment/saasCorpCurrency/' + id,
    method: 'get'
  })
}

// 新增SaaS用户公司支持货币
export function addSaasCorpCurrency(data) {
  return request({
    url: '/payment/saasCorpCurrency',
    method: 'post',
    data: data
  })
}

// 修改SaaS用户公司支持货币
export function updateSaasCorpCurrency(data) {
  return request({
    url: '/payment/saasCorpCurrency',
    method: 'put',
    data: data
  })
}

// 删除SaaS用户公司支持货币
export function delSaasCorpCurrency(id) {
  return request({
    url: '/payment/saasCorpCurrency/' + id,
    method: 'delete'
  })
}

// 导出SaaS用户公司支持货币
export function exportSaasCorpCurrency(query) {
  return request({
    url: '/payment/saasCorpCurrency/export',
    method: 'get',
    params: query
  })
}