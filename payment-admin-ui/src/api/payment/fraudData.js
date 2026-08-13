import request from '@/utils/request'

// 查询欺诈数据列表
export function listFraudData(query) {
  return request({
    url: '/payment/fraudData/list',
    method: 'get',
    params: query
  })
}

// 查询欺诈数据详细
export function getFraudData(id) {
  return request({
    url: '/payment/fraudData/' + id,
    method: 'get'
  })
}

// 新增欺诈数据
export function addFraudData(data) {
  return request({
    url: '/payment/fraudData',
    method: 'post',
    data: data
  })
}

// 修改欺诈数据
export function updateFraudData(data) {
  return request({
    url: '/payment/fraudData',
    method: 'put',
    data: data
  })
}

// 删除欺诈数据
export function delFraudData(id) {
  return request({
    url: '/payment/fraudData/' + id,
    method: 'delete'
  })
}

// 导出欺诈数据
export function exportFraudData(query) {
  return request({
    url: '/payment/fraudData/export',
    method: 'get',
    params: query
  })
}