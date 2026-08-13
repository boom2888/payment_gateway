import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listSetting(query) {
  return request({
    url: '/payment/setting/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getSetting(id) {
  return request({
    url: '/payment/setting/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addSetting(data) {
  return request({
    url: '/payment/setting',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateSetting(data) {
  return request({
    url: '/payment/setting',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delSetting(id) {
  return request({
    url: '/payment/setting/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportSetting(query) {
  return request({
    url: '/payment/setting/export',
    method: 'get',
    params: query
  })
}