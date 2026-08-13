import request from '@/utils/request'

// 查询上传卡记录列表
export function listRecord(query) {
  return request({
    url: '/payment/record/list',
    method: 'get',
    params: query
  })
}

// 查询上传卡记录详细
export function getRecord(id) {
  return request({
    url: '/payment/record/' + id,
    method: 'get'
  })
}

// 新增上传卡记录
export function addRecord(data) {
  return request({
    url: '/payment/record',
    method: 'post',
    data: data
  })
}

// 修改上传卡记录
export function updateRecord(data) {
  return request({
    url: '/payment/record',
    method: 'put',
    data: data
  })
}

// 删除上传卡记录
export function delRecord(id) {
  return request({
    url: '/payment/record/' + id,
    method: 'delete'
  })
}

// 导出上传卡记录
export function exportRecord(query) {
  return request({
    url: '/payment/record/export',
    method: 'get',
    params: query
  })
}