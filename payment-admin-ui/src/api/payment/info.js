import request from '@/utils/request'

// 查询IP地址列表
export function listInfo(query) {
  return request({
    url: '/payment/info/list',
    method: 'get',
    params: query
  })
}

// 查询IP地址详细
export function getInfo(id) {
  return request({
    url: '/payment/info/' + id,
    method: 'get'
  })
}

// 新增IP地址
export function addInfo(data) {
  return request({
    url: '/payment/info',
    method: 'post',
    data: data
  })
}

// 修改IP地址
export function updateInfo(data) {
  return request({
    url: '/payment/info',
    method: 'put',
    data: data
  })
}

// 删除IP地址
export function delInfo(id) {
  return request({
    url: '/payment/info/' + id,
    method: 'delete'
  })
}

// 导出IP地址
export function exportInfo(query) {
  return request({
    url: '/payment/info/export',
    method: 'get',
    params: query
  })
}