import request from '@/utils/request'

// 查询对账批处理作业文件列表
export function listReconBatchJobFile(query) {
  return request({
    url: '/payment/reconBatchJobFile/list',
    method: 'get',
    params: query
  })
}

// 查询对账批处理作业文件详细
export function getReconBatchJobFile(id) {
  return request({
    url: '/payment/reconBatchJobFile/' + id,
    method: 'get'
  })
}

// 新增对账批处理作业文件
export function addReconBatchJobFile(data) {
  return request({
    url: '/payment/reconBatchJobFile',
    method: 'post',
    data: data
  })
}

// 修改对账批处理作业文件
export function updateReconBatchJobFile(data) {
  return request({
    url: '/payment/reconBatchJobFile',
    method: 'put',
    data: data
  })
}

// 删除对账批处理作业文件
export function delReconBatchJobFile(id) {
  return request({
    url: '/payment/reconBatchJobFile/' + id,
    method: 'delete'
  })
}

// 导出对账批处理作业文件
export function exportReconBatchJobFile(query) {
  return request({
    url: '/payment/reconBatchJobFile/export',
    method: 'get',
    params: query
  })
}