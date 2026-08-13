import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listCode(query) {
  return request({
    url: '/payment/code/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getCode(id) {
  return request({
    url: '/payment/code/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addCode(data) {
  return request({
    url: '/payment/code',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateCode(data) {
  return request({
    url: '/payment/code',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delCode(id) {
  return request({
    url: '/payment/code/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportCode(query) {
  return request({
    url: '/payment/code/export',
    method: 'get',
    params: query
  })
}