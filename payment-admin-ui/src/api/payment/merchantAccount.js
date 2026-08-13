import request from '@/utils/request'

// 查询商户账户列表
export function listMerchantAccount(query) {
  return request({
    url: '/payment/merchantAccount/list',
    method: 'get',
    params: query
  })
}

// 查询商户账户详细
export function getMerchantAccount(id) {
  return request({
    url: '/payment/merchantAccount/' + id,
    method: 'get'
  })
}

// 新增商户账户
export function addMerchantAccount(data) {
  return request({
    url: '/payment/merchantAccount',
    method: 'post',
    data: data
  })
}

// 修改商户账户
export function updateMerchantAccount(data) {
  return request({
    url: '/payment/merchantAccount',
    method: 'put',
    data: data
  })
}

// 删除商户账户
export function delMerchantAccount(id) {
  return request({
    url: '/payment/merchantAccount/' + id,
    method: 'delete'
  })
}

// 导出商户账户
export function exportMerchantAccount(query) {
  return request({
    url: '/payment/merchantAccount/export',
    method: 'get',
    params: query
  })
}