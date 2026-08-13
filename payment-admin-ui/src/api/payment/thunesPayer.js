import request from '@/utils/request'

// 查询Thunes付款人列表
export function listThunesPayer(query) {
  return request({
    url: '/payment/thunesPayer/list',
    method: 'get',
    params: query
  })
}

// 查询Thunes付款人详细
export function getThunesPayer(id) {
  return request({
    url: '/payment/thunesPayer/' + id,
    method: 'get'
  })
}

// 新增Thunes付款人
export function addThunesPayer(data) {
  return request({
    url: '/payment/thunesPayer',
    method: 'post',
    data: data
  })
}

// 修改Thunes付款人
export function updateThunesPayer(data) {
  return request({
    url: '/payment/thunesPayer',
    method: 'put',
    data: data
  })
}

// 删除Thunes付款人
export function delThunesPayer(id) {
  return request({
    url: '/payment/thunesPayer/' + id,
    method: 'delete'
  })
}

// 导出Thunes付款人
export function exportThunesPayer(query) {
  return request({
    url: '/payment/thunesPayer/export',
    method: 'get',
    params: query
  })
}