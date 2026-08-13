import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listModel(query) {
  return request({
    url: '/payment/model/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getModel(id) {
  return request({
    url: '/payment/model/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addModel(data) {
  return request({
    url: '/payment/model',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateModel(data) {
  return request({
    url: '/payment/model',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delModel(id) {
  return request({
    url: '/payment/model/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportModel(query) {
  return request({
    url: '/payment/model/export',
    method: 'get',
    params: query
  })
}