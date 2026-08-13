import request from '@/utils/request'

// 查询费用类型列表
export function listFeeType(query) {
  return request({
    url: '/payment/feeType/list',
    method: 'get',
    params: query
  })
}

// 查询费用类型详细
export function getFeeType(id) {
  return request({
    url: '/payment/feeType/' + id,
    method: 'get'
  })
}

// 新增费用类型
export function addFeeType(data) {
  return request({
    url: '/payment/feeType',
    method: 'post',
    data: data
  })
}

// 修改费用类型
export function updateFeeType(data) {
  return request({
    url: '/payment/feeType',
    method: 'put',
    data: data
  })
}

// 删除费用类型
export function delFeeType(id) {
  return request({
    url: '/payment/feeType/' + id,
    method: 'delete'
  })
}

// 导出费用类型
export function exportFeeType(query) {
  return request({
    url: '/payment/feeType/export',
    method: 'get',
    params: query
  })
}