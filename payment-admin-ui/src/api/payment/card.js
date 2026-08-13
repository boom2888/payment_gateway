import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listCard(query) {
  return request({
    url: '/payment/card/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getCard(id) {
  return request({
    url: '/payment/card/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addCard(data) {
  return request({
    url: '/payment/card',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateCard(data) {
  return request({
    url: '/payment/card',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delCard(id) {
  return request({
    url: '/payment/card/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportCard(query) {
  return request({
    url: '/payment/card/export',
    method: 'get',
    params: query
  })
}