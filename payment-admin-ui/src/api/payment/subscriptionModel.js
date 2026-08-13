import request from '@/utils/request'

// 查询订阅模型列表
export function listSubscriptionModel(query) {
  return request({
    url: '/payment/subscriptionModel/list',
    method: 'get',
    params: query
  })
}

// 查询订阅模型详细
export function getSubscriptionModel(id) {
  return request({
    url: '/payment/subscriptionModel/' + id,
    method: 'get'
  })
}

// 新增订阅模型
export function addSubscriptionModel(data) {
  return request({
    url: '/payment/subscriptionModel',
    method: 'post',
    data: data
  })
}

// 修改订阅模型
export function updateSubscriptionModel(data) {
  return request({
    url: '/payment/subscriptionModel',
    method: 'put',
    data: data
  })
}

// 删除订阅模型
export function delSubscriptionModel(id) {
  return request({
    url: '/payment/subscriptionModel/' + id,
    method: 'delete'
  })
}

// 导出订阅模型
export function exportSubscriptionModel(query) {
  return request({
    url: '/payment/subscriptionModel/export',
    method: 'get',
    params: query
  })
}