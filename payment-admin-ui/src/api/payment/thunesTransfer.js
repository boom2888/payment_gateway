import request from '@/utils/request'

// 获取国家列表
export function getCountries() {
  return request({
    url: '/payment/thunes/transfer/countries',
    method: 'get'
  })
}

// 获取已审核通过的商户列表
export function getMerchants() {
  return request({
    url: '/payment/thunes/transfer/merchants',
    method: 'get'
  })
}

// 获取收款方信息字段
export function getPayers(query) {
  return request({
    url: '/payment/thunes/transfer/payers',
    method: 'get',
    params: query
  })
}

// 执行转账
export function transfer(data) {
  return request({
    url: '/payment/thunes/transfer',
    method: 'post',
    data: data
  })
}
