import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listQuote(query) {
  return request({
    url: '/payment/quote/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getQuote(id) {
  return request({
    url: '/payment/quote/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addQuote(data) {
  return request({
    url: '/payment/quote',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateQuote(data) {
  return request({
    url: '/payment/quote',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delQuote(id) {
  return request({
    url: '/payment/quote/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportQuote(query) {
  return request({
    url: '/payment/quote/export',
    method: 'get',
    params: query
  })
}