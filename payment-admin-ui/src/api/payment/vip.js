import request from '@/utils/request'

// 查询VIP列表
export function listVip(query) {
  return request({
    url: '/payment/vip/list',
    method: 'get',
    params: query
  })
}

// 查询VIP详细
export function getVip(id) {
  return request({
    url: '/payment/vip/' + id,
    method: 'get'
  })
}

// 新增VIP
export function addVip(data) {
  return request({
    url: '/payment/vip',
    method: 'post',
    data: data
  })
}

// 修改VIP
export function updateVip(data) {
  return request({
    url: '/payment/vip',
    method: 'put',
    data: data
  })
}

// 删除VIP
export function delVip(id) {
  return request({
    url: '/payment/vip/' + id,
    method: 'delete'
  })
}

// 导出VIP
export function exportVip(query) {
  return request({
    url: '/payment/vip/export',
    method: 'get',
    params: query
  })
}