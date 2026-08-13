import request from '@/utils/request'

// 查询订阅级别列表
export function listSubscriptionLevel(query) {
  return request({
    url: '/payment/subscriptionLevel/list',
    method: 'get',
    params: query
  })
}

// 查询订阅级别详细
export function getSubscriptionLevel(id) {
  return request({
    url: '/payment/subscriptionLevel/' + id,
    method: 'get'
  })
}

// 新增订阅级别
export function addSubscriptionLevel(data) {
  return request({
    url: '/payment/subscriptionLevel',
    method: 'post',
    data: data
  })
}

// 修改订阅级别
export function updateSubscriptionLevel(data) {
  return request({
    url: '/payment/subscriptionLevel',
    method: 'put',
    data: data
  })
}

// 删除订阅级别
export function delSubscriptionLevel(id) {
  return request({
    url: '/payment/subscriptionLevel/' + id,
    method: 'delete'
  })
}

// 导出订阅级别
export function exportSubscriptionLevel(query) {
  return request({
    url: '/payment/subscriptionLevel/export',
    method: 'get',
    params: query
  })
}