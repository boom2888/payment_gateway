import request from '@/utils/request'

// 查询黑名单信用卡列表
export function listCards(query) {
  return request({
    url: '/payment/cards/list',
    method: 'get',
    params: query
  })
}

// 查询黑名单信用卡详细
export function getCards(id) {
  return request({
    url: '/payment/cards/' + id,
    method: 'get'
  })
}

// 新增黑名单信用卡
export function addCards(data) {
  return request({
    url: '/payment/cards',
    method: 'post',
    data: data
  })
}

// 修改黑名单信用卡
export function updateCards(data) {
  return request({
    url: '/payment/cards',
    method: 'put',
    data: data
  })
}

// 删除黑名单信用卡
export function delCards(id) {
  return request({
    url: '/payment/cards/' + id,
    method: 'delete'
  })
}

// 导出黑名单信用卡
export function exportCards(query) {
  return request({
    url: '/payment/cards/export',
    method: 'get',
    params: query
  })
}