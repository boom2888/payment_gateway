import request from '@/utils/request'

// 查询nuvei导入数据明细列表
export function listMovenmentData(query) {
  return request({
    url: '/payment/movenmentData/list',
    method: 'get',
    params: query
  })
}

// 查询nuvei导入数据明细详细
export function getMovenmentData(id) {
  return request({
    url: '/payment/movenmentData/' + id,
    method: 'get'
  })
}

// 新增nuvei导入数据明细
export function addMovenmentData(data) {
  return request({
    url: '/payment/movenmentData',
    method: 'post',
    data: data
  })
}

// 修改nuvei导入数据明细
export function updateMovenmentData(data) {
  return request({
    url: '/payment/movenmentData',
    method: 'put',
    data: data
  })
}

// 删除nuvei导入数据明细
export function delMovenmentData(id) {
  return request({
    url: '/payment/movenmentData/' + id,
    method: 'delete'
  })
}

// 导出nuvei导入数据明细
export function exportMovenmentData(query) {
  return request({
    url: '/payment/movenmentData/export',
    method: 'get',
    params: query
  })
}