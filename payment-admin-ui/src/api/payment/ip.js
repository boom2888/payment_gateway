import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listIp(query) {
  return request({
    url: '/payment/ip/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getIp(id) {
  return request({
    url: '/payment/ip/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addIp(data) {
  return request({
    url: '/payment/ip',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateIp(data) {
  return request({
    url: '/payment/ip',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delIp(id) {
  return request({
    url: '/payment/ip/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportIp(query) {
  return request({
    url: '/payment/ip/export',
    method: 'get',
    params: query
  })
}