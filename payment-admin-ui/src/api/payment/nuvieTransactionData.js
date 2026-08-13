import request from '@/utils/request'

// 查询Nuvei交易数据列表
export function listNuvieTransactionData(query) {
  return request({
    url: '/payment/nuvieTransactionData/list',
    method: 'get',
    params: query
  })
}

// 查询Nuvei交易数据详细
export function getNuvieTransactionData(id) {
  return request({
    url: '/payment/nuvieTransactionData/' + id,
    method: 'get'
  })
}

// 新增Nuvei交易数据
export function addNuvieTransactionData(data) {
  return request({
    url: '/payment/nuvieTransactionData',
    method: 'post',
    data: data
  })
}

// 修改Nuvei交易数据
export function updateNuvieTransactionData(data) {
  return request({
    url: '/payment/nuvieTransactionData',
    method: 'put',
    data: data
  })
}

// 删除Nuvei交易数据
export function delNuvieTransactionData(id) {
  return request({
    url: '/payment/nuvieTransactionData/' + id,
    method: 'delete'
  })
}

// 导出Nuvei交易数据
export function exportNuvieTransactionData(query) {
  return request({
    url: '/payment/nuvieTransactionData/export',
    method: 'get',
    params: query
  })
}