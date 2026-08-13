import request from '@/utils/request'

// 查询企业客户公司成员列表
export function listBusinessCustomerCorporationMem(query) {
  return request({
    url: '/payment/businessCustomerCorporationMem/list',
    method: 'get',
    params: query
  })
}

// 查询企业客户公司成员详细
export function getBusinessCustomerCorporationMem(id) {
  return request({
    url: '/payment/businessCustomerCorporationMem/' + id,
    method: 'get'
  })
}

// 新增企业客户公司成员
export function addBusinessCustomerCorporationMem(data) {
  return request({
    url: '/payment/businessCustomerCorporationMem',
    method: 'post',
    data: data
  })
}

// 修改企业客户公司成员
export function updateBusinessCustomerCorporationMem(data) {
  return request({
    url: '/payment/businessCustomerCorporationMem',
    method: 'put',
    data: data
  })
}

// 删除企业客户公司成员
export function delBusinessCustomerCorporationMem(id) {
  return request({
    url: '/payment/businessCustomerCorporationMem/' + id,
    method: 'delete'
  })
}

// 导出企业客户公司成员
export function exportBusinessCustomerCorporationMem(query) {
  return request({
    url: '/payment/businessCustomerCorporationMem/export',
    method: 'get',
    params: query
  })
}