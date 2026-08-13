import request from '@/utils/request'

// 查询结算分类账列表
export function listSettlementLedger(query) {
  return request({
    url: '/payment/settlementLedger/list',
    method: 'get',
    params: query
  })
}

// 查询结算分类账详细
export function getSettlementLedger(id) {
  return request({
    url: '/payment/settlementLedger/' + id,
    method: 'get'
  })
}

// 新增结算分类账
export function addSettlementLedger(data) {
  return request({
    url: '/payment/settlementLedger',
    method: 'post',
    data: data
  })
}

// 修改结算分类账
export function updateSettlementLedger(data) {
  return request({
    url: '/payment/settlementLedger',
    method: 'put',
    data: data
  })
}

// 删除结算分类账
export function delSettlementLedger(id) {
  return request({
    url: '/payment/settlementLedger/' + id,
    method: 'delete'
  })
}

// 导出结算分类账
export function exportSettlementLedger(query) {
  return request({
    url: '/payment/settlementLedger/export',
    method: 'get',
    params: query
  })
}