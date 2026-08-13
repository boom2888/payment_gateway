import request from '@/utils/request'

// 查询支付到卡列表
export function listPayToCard(query) {
  return request({
    url: '/payment/payToCard/list',
    method: 'get',
    params: query
  })
}

// 查询支付到卡详细
export function getPayToCard(id) {
  return request({
    url: '/payment/payToCard/' + id,
    method: 'get'
  })
}

// 新增支付到卡
export function addPayToCard(data) {
  return request({
    url: '/payment/payToCard',
    method: 'post',
    data: data
  })
}

// 修改支付到卡
export function updatePayToCard(data) {
  return request({
    url: '/payment/payToCard',
    method: 'put',
    data: data
  })
}

// 删除支付到卡
export function delPayToCard(id) {
  return request({
    url: '/payment/payToCard/' + id,
    method: 'delete'
  })
}

// 导出支付到卡
export function exportPayToCard(query) {
  return request({
    url: '/payment/payToCard/export',
    method: 'get',
    params: query
  })
}