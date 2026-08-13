import request from '@/utils/request'

// 查询黑名单信用卡列表
export function listBlacklistedCreditCards(query) {
  return request({
    url: '/payment/blacklistedCreditCards/list',
    method: 'get',
    params: query
  })
}

// 查询黑名单信用卡详细
export function getBlacklistedCreditCards(id) {
  return request({
    url: '/payment/blacklistedCreditCards/' + id,
    method: 'get'
  })
}

// 新增黑名单信用卡
export function addBlacklistedCreditCards(data) {
  return request({
    url: '/payment/blacklistedCreditCards',
    method: 'post',
    data: data
  })
}

// 修改黑名单信用卡
export function updateBlacklistedCreditCards(data) {
  return request({
    url: '/payment/blacklistedCreditCards',
    method: 'put',
    data: data
  })
}

// 删除黑名单信用卡
export function delBlacklistedCreditCards(id) {
  return request({
    url: '/payment/blacklistedCreditCards/' + id,
    method: 'delete'
  })
}

// 导出黑名单信用卡
export function exportBlacklistedCreditCards(query) {
  return request({
    url: '/payment/blacklistedCreditCards/export',
    method: 'get',
    params: query
  })
}