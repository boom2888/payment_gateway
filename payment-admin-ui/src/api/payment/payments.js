import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listPayments(query) {
  return request({
    url: '/payment/payments/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getPayments(id) {
  return request({
    url: '/payment/payments/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addPayments(data) {
  return request({
    url: '/payment/payments',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updatePayments(data) {
  return request({
    url: '/payment/payments',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delPayments(id) {
  return request({
    url: '/payment/payments/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportPayments(query) {
  return request({
    url: '/payment/payments/export',
    method: 'get',
    params: query
  })
}