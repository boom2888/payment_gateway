import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listMid(query) {
  return request({
    url: '/payment/mid/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getMid(id) {
  return request({
    url: '/payment/mid/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addMid(data) {
  return request({
    url: '/payment/mid',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateMid(data) {
  return request({
    url: '/payment/mid',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delMid(id) {
  return request({
    url: '/payment/mid/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportMid(query) {
  return request({
    url: '/payment/mid/export',
    method: 'get',
    params: query
  })
}