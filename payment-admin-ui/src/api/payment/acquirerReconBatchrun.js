import request from '@/utils/request'

// 查询收单机构对账批处理列表
export function listAcquirerReconBatchrun(query) {
  return request({
    url: '/payment/acquirerReconBatchrun/list',
    method: 'get',
    params: query
  })
}

// 查询收单机构对账批处理详细
export function getAcquirerReconBatchrun(id) {
  return request({
    url: '/payment/acquirerReconBatchrun/' + id,
    method: 'get'
  })
}

// 新增收单机构对账批处理
export function addAcquirerReconBatchrun(data) {
  return request({
    url: '/payment/acquirerReconBatchrun',
    method: 'post',
    data: data
  })
}

// 修改收单机构对账批处理
export function updateAcquirerReconBatchrun(data) {
  return request({
    url: '/payment/acquirerReconBatchrun',
    method: 'put',
    data: data
  })
}

// 删除收单机构对账批处理
export function delAcquirerReconBatchrun(id) {
  return request({
    url: '/payment/acquirerReconBatchrun/' + id,
    method: 'delete'
  })
}

// 导出收单机构对账批处理
export function exportAcquirerReconBatchrun(query) {
  return request({
    url: '/payment/acquirerReconBatchrun/export',
    method: 'get',
    params: query
  })
}