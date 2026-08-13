import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listOrder(query) {
  return request({
    url: '/payment/refundOrder/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getOrder(id) {
  return request({
    url: '/payment/refundOrder/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addOrder(data) {
  return request({
    url: '/payment/refundOrder',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateOrder(data) {
  return request({
    url: '/payment/refundOrder',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delOrder(id) {
  return request({
    url: '/payment/refundOrder/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportOrder(query, ids) {
  const params = {
    ...query,
    ids: ids && ids.length ? ids.join(',') : undefined
  }
  if (!params.beginTime) delete params.beginTime
  if (!params.endTime) delete params.endTime
  return request({
    url: '/payment/refundOrder/export',
    method: 'get',
    params: params
  })
}
