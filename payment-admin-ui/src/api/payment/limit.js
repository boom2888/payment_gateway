import request from '@/utils/request'

// 查询商户限额列表
export function listLimit(query) {
  return request({
    url: '/payment/limit/list',
    method: 'get',
    params: query
  })
}

// 查询商户限额详细
export function getLimit(id) {
  return request({
    url: '/payment/limit/' + id,
    method: 'get'
  })
}

// 新增商户限额
export function addLimit(data) {
  return request({
    url: '/payment/limit',
    method: 'post',
    data: data
  })
}

// 修改商户限额
export function updateLimit(data) {
  return request({
    url: '/payment/limit',
    method: 'put',
    data: data
  })
}

// 删除商户限额
export function delLimit(id) {
  return request({
    url: '/payment/limit/' + id,
    method: 'delete'
  })
}

// 导出商户限额
export function exportLimit(query) {
  return request({
    url: '/payment/limit/export',
    method: 'get',
    params: query
  })
}