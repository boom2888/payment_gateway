import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listLedger(query) {
  return request({
    url: '/payment/ledger/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getLedger(id) {
  return request({
    url: '/payment/ledger/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addLedger(data) {
  return request({
    url: '/payment/ledger',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateLedger(data) {
  return request({
    url: '/payment/ledger',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delLedger(id) {
  return request({
    url: '/payment/ledger/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportLedger(query) {
  return request({
    url: '/payment/ledger/export',
    method: 'get',
    params: query
  })
}