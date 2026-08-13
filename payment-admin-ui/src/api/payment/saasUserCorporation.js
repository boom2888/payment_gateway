import request from '@/utils/request'

// 查询SaaS用户公司列表
export function listSaasUserCorporation(query) {
  return request({
    url: '/payment/saasUserCorporation/list',
    method: 'get',
    params: query
  })
}

// 查询SaaS用户公司详细
export function getSaasUserCorporation(id) {
  return request({
    url: '/payment/saasUserCorporation/' + id,
    method: 'get'
  })
}

// 新增SaaS用户公司
export function addSaasUserCorporation(data) {
  return request({
    url: '/payment/saasUserCorporation',
    method: 'post',
    data: data
  })
}

// 修改SaaS用户公司
export function updateSaasUserCorporation(data) {
  return request({
    url: '/payment/saasUserCorporation',
    method: 'put',
    data: data
  })
}

// 删除SaaS用户公司
export function delSaasUserCorporation(id) {
  return request({
    url: '/payment/saasUserCorporation/' + id,
    method: 'delete'
  })
}

// 导出SaaS用户公司
export function exportSaasUserCorporation(query) {
  return request({
    url: '/payment/saasUserCorporation/export',
    method: 'get',
    params: query
  })
}