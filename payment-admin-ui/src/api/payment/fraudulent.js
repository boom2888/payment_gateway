import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listFraudulent(query) {
  return request({
    url: '/payment/fraudulent/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getFraudulent(id) {
  return request({
    url: '/payment/fraudulent/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addFraudulent(data) {
  return request({
    url: '/payment/fraudulent',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateFraudulent(data) {
  return request({
    url: '/payment/fraudulent',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delFraudulent(id) {
  return request({
    url: '/payment/fraudulent/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportFraudulent(query) {
  return request({
    url: '/payment/fraudulent/export',
    method: 'get',
    params: query
  })
}