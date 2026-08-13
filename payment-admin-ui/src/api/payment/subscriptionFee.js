import request from '@/utils/request'

// 查询订阅费用列表
export function listSubscriptionFee(query) {
  return request({
    url: '/payment/subscriptionFee/list',
    method: 'get',
    params: query
  })
}

// 查询订阅费用详细
export function getSubscriptionFee(id) {
  return request({
    url: '/payment/subscriptionFee/' + id,
    method: 'get'
  })
}

// 新增订阅费用
export function addSubscriptionFee(data) {
  return request({
    url: '/payment/subscriptionFee',
    method: 'post',
    data: data
  })
}

// 修改订阅费用
export function updateSubscriptionFee(data) {
  return request({
    url: '/payment/subscriptionFee',
    method: 'put',
    data: data
  })
}

// 删除订阅费用
export function delSubscriptionFee(id) {
  return request({
    url: '/payment/subscriptionFee/' + id,
    method: 'delete'
  })
}

// 导出订阅费用
export function exportSubscriptionFee(query) {
  return request({
    url: '/payment/subscriptionFee/export',
    method: 'get',
    params: query
  })
}