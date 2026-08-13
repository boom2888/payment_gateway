import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listMapping(query) {
  return request({
    url: '/payment/mapping/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getMapping(id) {
  return request({
    url: '/payment/mapping/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addMapping(data) {
  return request({
    url: '/payment/mapping',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateMapping(data) {
  return request({
    url: '/payment/mapping',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delMapping(id) {
  return request({
    url: '/payment/mapping/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportMapping(query) {
  return request({
    url: '/payment/mapping/export',
    method: 'get',
    params: query
  })
}