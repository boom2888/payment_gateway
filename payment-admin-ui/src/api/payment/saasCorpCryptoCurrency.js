import request from '@/utils/request'

// 查询SaaS用户公司支持加密货币列表
export function listSaasCorpCryptoCurrency(query) {
  return request({
    url: '/payment/saasCorpCryptoCurrency/list',
    method: 'get',
    params: query
  })
}

// 查询SaaS用户公司支持加密货币详细
export function getSaasCorpCryptoCurrency(id) {
  return request({
    url: '/payment/saasCorpCryptoCurrency/' + id,
    method: 'get'
  })
}

// 新增SaaS用户公司支持加密货币
export function addSaasCorpCryptoCurrency(data) {
  return request({
    url: '/payment/saasCorpCryptoCurrency',
    method: 'post',
    data: data
  })
}

// 修改SaaS用户公司支持加密货币
export function updateSaasCorpCryptoCurrency(data) {
  return request({
    url: '/payment/saasCorpCryptoCurrency',
    method: 'put',
    data: data
  })
}

// 删除SaaS用户公司支持加密货币
export function delSaasCorpCryptoCurrency(id) {
  return request({
    url: '/payment/saasCorpCryptoCurrency/' + id,
    method: 'delete'
  })
}

// 导出SaaS用户公司支持加密货币
export function exportSaasCorpCryptoCurrency(query) {
  return request({
    url: '/payment/saasCorpCryptoCurrency/export',
    method: 'get',
    params: query
  })
}