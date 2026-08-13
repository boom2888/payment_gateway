import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listPayer(query) {
  return request({
    url: '/payment/payer/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getPayer(id) {
  return request({
    url: '/payment/payer/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addPayer(data) {
  return request({
    url: '/payment/payer',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updatePayer(data) {
  return request({
    url: '/payment/payer',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delPayer(id) {
  return request({
    url: '/payment/payer/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportPayer(query) {
  return request({
    url: '/payment/payer/export',
    method: 'get',
    params: query
  })
}