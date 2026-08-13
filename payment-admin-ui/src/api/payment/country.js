import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listCountry(query) {
  return request({
    url: '/payment/country/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getCountry(id) {
  return request({
    url: '/payment/country/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addCountry(data) {
  return request({
    url: '/payment/country',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateCountry(data) {
  return request({
    url: '/payment/country',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delCountry(id) {
  return request({
    url: '/payment/country/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportCountry(query) {
  return request({
    url: '/payment/country/export',
    method: 'get',
    params: query
  })
}