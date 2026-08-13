import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listFee(query) {
  return request({
    url: '/payment/fee/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getFee(id) {
  return request({
    url: '/payment/fee/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addFee(data) {
  return request({
    url: '/payment/fee',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateFee(data) {
  return request({
    url: '/payment/fee',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delFee(id) {
  return request({
    url: '/payment/fee/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportFee(query) {
  return request({
    url: '/payment/fee/export',
    method: 'get',
    params: query
  })
}