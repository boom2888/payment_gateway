import request from '@/utils/request'

// 查询收单机构对账批处理列表
export function listBatchrun(query) {
  return request({
    url: '/payment/batchrun/list',
    method: 'get',
    params: query
  })
}

// 查询收单机构对账批处理详细
export function getBatchrun(id) {
  return request({
    url: '/payment/batchrun/' + id,
    method: 'get'
  })
}

// 新增收单机构对账批处理
export function addBatchrun(data) {
  return request({
    url: '/payment/batchrun',
    method: 'post',
    data: data
  })
}

// 修改收单机构对账批处理
export function updateBatchrun(data) {
  return request({
    url: '/payment/batchrun',
    method: 'put',
    data: data
  })
}

// 删除收单机构对账批处理
export function delBatchrun(id) {
  return request({
    url: '/payment/batchrun/' + id,
    method: 'delete'
  })
}

// 导出收单机构对账批处理
export function exportBatchrun(query) {
  return request({
    url: '/payment/batchrun/export',
    method: 'get',
    params: query
  })
}