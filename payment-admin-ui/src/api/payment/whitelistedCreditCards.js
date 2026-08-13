import request from '@/utils/request'

// 查询白名单信用卡列表
export function listWhitelistedCreditCards(query) {
  return request({
    url: '/payment/whitelistedCreditCards/list',
    method: 'get',
    params: query
  })
}

// 查询白名单信用卡详细
export function getWhitelistedCreditCards(id) {
  return request({
    url: '/payment/whitelistedCreditCards/' + id,
    method: 'get'
  })
}

// 新增白名单信用卡
export function addWhitelistedCreditCards(data) {
  return request({
    url: '/payment/whitelistedCreditCards',
    method: 'post',
    data: data
  })
}

// 修改白名单信用卡
export function updateWhitelistedCreditCards(data) {
  return request({
    url: '/payment/whitelistedCreditCards',
    method: 'put',
    data: data
  })
}

// 删除白名单信用卡
export function delWhitelistedCreditCards(id) {
  return request({
    url: '/payment/whitelistedCreditCards/' + id,
    method: 'delete'
  })
}

// 导出白名单信用卡
export function exportWhitelistedCreditCards(query) {
  return request({
    url: '/payment/whitelistedCreditCards/export',
    method: 'get',
    params: query
  })
}

// 导入白名单信用卡
export function importWhitelistedCreditCards(data) {
  return request({
    url: '/payment/whitelistedCreditCards/import',
    method: 'post',
    data: data
  })
}
// 下载模板
export function importTemplate(lang) {
  return request({
    url: '/payment/whitelistedCreditCards/importTemplate',
    method: 'get',
    params: { lang }
  })
}
