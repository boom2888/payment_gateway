import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listContinent(query) {
  return request({
    url: '/payment/continent/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getContinent(code) {
  return request({
    url: '/payment/continent/' + code,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addContinent(data) {
  return request({
    url: '/payment/continent',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateContinent(data) {
  return request({
    url: '/payment/continent',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delContinent(code) {
  return request({
    url: '/payment/continent/' + code,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportContinent(query) {
  return request({
    url: '/payment/continent/export',
    method: 'get',
    params: query
  })
}