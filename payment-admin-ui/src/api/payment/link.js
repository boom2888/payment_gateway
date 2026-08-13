import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listLink(query) {
  return request({
    url: '/payment/link/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getLink(id) {
  return request({
    url: '/payment/link/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addLink(data) {
  return request({
    url: '/payment/link',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateLink(data) {
  return request({
    url: '/payment/link',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delLink(id) {
  return request({
    url: '/payment/link/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportLink(query) {
  return request({
    url: '/payment/link/export',
    method: 'get',
    params: query
  })
}