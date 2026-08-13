import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listData(query) {
  return request({
    url: '/payment/data/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getData(id) {
  return request({
    url: '/payment/data/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addData(data) {
  return request({
    url: '/payment/data',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateData(data) {
  return request({
    url: '/payment/data',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delData(id) {
  return request({
    url: '/payment/data/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportData(query) {
  return request({
    url: '/payment/data/export',
    method: 'get',
    params: query
  })
}