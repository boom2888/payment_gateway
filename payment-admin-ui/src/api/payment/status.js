import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listStatus(query) {
  return request({
    url: '/payment/status/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getStatus(code) {
  return request({
    url: '/payment/status/' + code,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addStatus(data) {
  return request({
    url: '/payment/status',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateStatus(data) {
  return request({
    url: '/payment/status',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delStatus(code) {
  return request({
    url: '/payment/status/' + code,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportStatus(query) {
  return request({
    url: '/payment/status/export',
    method: 'get',
    params: query
  })
}