import request from '@/utils/request'

// 查询交易列表
export function listTransaction(query) {
  return request({
    url: '/payment/transaction/list',
    method: 'get',
    params: query
  })
}

// 查询交易详细
export function getTransaction(id) {
  return request({
    url: '/payment/transaction/' + id,
    method: 'get'
  })
}

// 新增交易
export function addTransaction(data) {
  return request({
    url: '/payment/transaction',
    method: 'post',
    data: data
  })
}

// 修改交易
export function updateTransaction(data) {
  return request({
    url: '/payment/transaction',
    method: 'put',
    data: data
  })
}

// 删除交易
export function delTransaction(id) {
  return request({
    url: '/payment/transaction/' + id,
    method: 'delete'
  })
}

// 导出交易
export function exportTransaction(query) {
  return request({
    url: '/payment/transaction/export',
    method: 'get',
    params: query
  })
}