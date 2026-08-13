import request from '@/utils/request'

// 查询SaaS用户公司支持国家列表
export function listSaasCorpCountry(query) {
  return request({
    url: '/payment/saasCorpCountry/list',
    method: 'get',
    params: query
  })
}

// 查询SaaS用户公司支持国家详细
export function getSaasCorpCountry(id) {
  return request({
    url: '/payment/saasCorpCountry/' + id,
    method: 'get'
  })
}

// 新增SaaS用户公司支持国家
export function addSaasCorpCountry(data) {
  return request({
    url: '/payment/saasCorpCountry',
    method: 'post',
    data: data
  })
}

// 修改SaaS用户公司支持国家
export function updateSaasCorpCountry(data) {
  return request({
    url: '/payment/saasCorpCountry',
    method: 'put',
    data: data
  })
}

// 删除SaaS用户公司支持国家
export function delSaasCorpCountry(id) {
  return request({
    url: '/payment/saasCorpCountry/' + id,
    method: 'delete'
  })
}

// 导出SaaS用户公司支持国家
export function exportSaasCorpCountry(query) {
  return request({
    url: '/payment/saasCorpCountry/export',
    method: 'get',
    params: query
  })
}