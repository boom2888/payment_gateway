import request from '@/utils/request'

// 查询Checkout错误映射配置列表
export function listConfigCheckoutErrorMapping(query) {
  return request({
    url: '/payment/configCheckoutErrorMapping/list',
    method: 'get',
    params: query
  })
}

// 查询Checkout错误映射配置详细
export function getConfigCheckoutErrorMapping(id) {
  return request({
    url: '/payment/configCheckoutErrorMapping/' + id,
    method: 'get'
  })
}

// 新增Checkout错误映射配置
export function addConfigCheckoutErrorMapping(data) {
  return request({
    url: '/payment/configCheckoutErrorMapping',
    method: 'post',
    data: data
  })
}

// 修改Checkout错误映射配置
export function updateConfigCheckoutErrorMapping(data) {
  return request({
    url: '/payment/configCheckoutErrorMapping',
    method: 'put',
    data: data
  })
}

// 删除Checkout错误映射配置
export function delConfigCheckoutErrorMapping(id) {
  return request({
    url: '/payment/configCheckoutErrorMapping/' + id,
    method: 'delete'
  })
}

// 导出Checkout错误映射配置
export function exportConfigCheckoutErrorMapping(query) {
  return request({
    url: '/payment/configCheckoutErrorMapping/export',
    method: 'get',
    params: query
  })
}