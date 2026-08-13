import request from '@/utils/request'

// 查询nuvei导入数据明细列表
export function listMovementData(query) {
  return request({
    url: '/payment/movementData/list',
    method: 'get',
    params: query
  })
}

// 查询nuvei导入数据明细详细
export function getMovementData(id) {
  return request({
    url: '/payment/movementData/' + id,
    method: 'get'
  })
}

// 新增nuvei导入数据明细
export function addMovementData(data) {
  return request({
    url: '/payment/movementData',
    method: 'post',
    data: data
  })
}

// 修改nuvei导入数据明细
export function updateMovementData(data) {
  return request({
    url: '/payment/movementData',
    method: 'put',
    data: data
  })
}

// 删除nuvei导入数据明细
export function delMovementData(id) {
  return request({
    url: '/payment/movementData/' + id,
    method: 'delete'
  })
}

// 导出nuvei导入数据明细
export function exportMovementData(query) {
  return request({
    url: '/payment/movementData/export',
    method: 'get',
    params: query
  })
}