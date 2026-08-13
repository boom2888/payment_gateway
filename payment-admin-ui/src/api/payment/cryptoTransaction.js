import request from '@/utils/request'

// 查询加密货币交易列表
export function listCryptoTransaction(query) {
  return request({
    url: '/payment/cryptoTransaction/list',
    method: 'get',
    params: query
  })
}

// 查询加密货币交易详细
export function getCryptoTransaction(id) {
  return request({
    url: '/payment/cryptoTransaction/' + id,
    method: 'get'
  })
}

// 新增加密货币交易
export function addCryptoTransaction(data) {
  return request({
    url: '/payment/cryptoTransaction',
    method: 'post',
    data: data
  })
}

// 修改加密货币交易
export function updateCryptoTransaction(data) {
  return request({
    url: '/payment/cryptoTransaction',
    method: 'put',
    data: data
  })
}

// 删除加密货币交易
export function delCryptoTransaction(id) {
  return request({
    url: '/payment/cryptoTransaction/' + id,
    method: 'delete'
  })
}

// 导出加密货币交易
export function exportCryptoTransaction(query) {
  return request({
    url: '/payment/cryptoTransaction/export',
    method: 'get',
    params: query
  })
}