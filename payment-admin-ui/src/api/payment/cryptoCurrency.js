import request from '@/utils/request'

// 查询加密货币列表
export function listCryptoCurrency(query) {
  return request({
    url: '/payment/cryptoCurrency/list',
    method: 'get',
    params: query
  })
}

// 查询加密货币详细
export function getCryptoCurrency(id) {
  return request({
    url: '/payment/cryptoCurrency/' + id,
    method: 'get'
  })
}

// 新增加密货币
export function addCryptoCurrency(data) {
  return request({
    url: '/payment/cryptoCurrency',
    method: 'post',
    data: data
  })
}

// 修改加密货币
export function updateCryptoCurrency(data) {
  return request({
    url: '/payment/cryptoCurrency',
    method: 'put',
    data: data
  })
}

// 删除加密货币
export function delCryptoCurrency(id) {
  return request({
    url: '/payment/cryptoCurrency/' + id,
    method: 'delete'
  })
}

// 导出加密货币
export function exportCryptoCurrency(query) {
  return request({
    url: '/payment/cryptoCurrency/export',
    method: 'get',
    params: query
  })
}