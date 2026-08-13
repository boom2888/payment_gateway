import request from '@/utils/request'

// 查询IP地址列表
export function listIpinfo(query) {
  return request({
    url: '/payment/ipinfo/list',
    method: 'get',
    params: query
  })
}

// 查询IP地址详细
export function getIpinfo(id) {
  return request({
    url: '/payment/ipinfo/' + id,
    method: 'get'
  })
}

// 新增IP地址
export function addIpinfo(data) {
  return request({
    url: '/payment/ipinfo',
    method: 'post',
    data: data
  })
}

// 修改IP地址
export function updateIpinfo(data) {
  return request({
    url: '/payment/ipinfo',
    method: 'put',
    data: data
  })
}

// 删除IP地址
export function delIpinfo(id) {
  return request({
    url: '/payment/ipinfo/' + id,
    method: 'delete'
  })
}

// 导出IP地址
export function exportIpinfo(query) {
  return request({
    url: '/payment/ipinfo/export',
    method: 'get',
    params: query
  })
}