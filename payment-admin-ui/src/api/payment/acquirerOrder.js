import request from '@/utils/request'

// 查询收单机构订单列表
export function listAcquirerOrder(query) {
  return request({
    url: '/payment/acquirerOrder/list',
    method: 'get',
    params: query
  })
}

// 查询收单机构订单详细
export function getAcquirerOrder(id) {
  return request({
    url: '/payment/acquirerOrder/' + id,
    method: 'get'
  })
}

// 新增收单机构订单
export function addAcquirerOrder(data) {
  return request({
    url: '/payment/acquirerOrder',
    method: 'post',
    data: data
  })
}

// 修改收单机构订单
export function updateAcquirerOrder(data) {
  return request({
    url: '/payment/acquirerOrder',
    method: 'put',
    data: data
  })
}

// 删除收单机构订单
export function delAcquirerOrder(id) {
  return request({
    url: '/payment/acquirerOrder/' + id,
    method: 'delete'
  })
}

// 导出收单机构订单
export function exportAcquirerOrder(query) {
  return request({
    url: '/payment/acquirerOrder/export',
    method: 'get',
    params: query
  })
}