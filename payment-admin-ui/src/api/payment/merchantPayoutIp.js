import request from '@/utils/request'

// 查询商户支付IP列表
export function listMerchantPayoutIp(query) {
  return request({
    url: '/payment/merchantPayoutIp/list',
    method: 'get',
    params: query
  })
}

// 查询商户支付IP详细
export function getMerchantPayoutIp(id) {
  return request({
    url: '/payment/merchantPayoutIp/' + id,
    method: 'get'
  })
}

// 新增商户支付IP
export function addMerchantPayoutIp(data) {
  return request({
    url: '/payment/merchantPayoutIp',
    method: 'post',
    data: data
  })
}

// 修改商户支付IP
export function updateMerchantPayoutIp(data) {
  return request({
    url: '/payment/merchantPayoutIp',
    method: 'put',
    data: data
  })
}

// 删除商户支付IP
export function delMerchantPayoutIp(id) {
  return request({
    url: '/payment/merchantPayoutIp/' + id,
    method: 'delete'
  })
}

// 导出商户支付IP
export function exportMerchantPayoutIp(query) {
  return request({
    url: '/payment/merchantPayoutIp/export',
    method: 'get',
    params: query
  })
}