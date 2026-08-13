import request from '@/utils/request'

// 查询文档列表
export function listDocuments(query) {
  return request({
    url: '/payment/documents/list',
    method: 'get',
    params: query
  })
}

// 查询文档详细
export function getDocuments(id) {
  return request({
    url: '/payment/documents/' + id,
    method: 'get'
  })
}

// 新增文档
export function addDocuments(data) {
  return request({
    url: '/payment/documents',
    method: 'post',
    data: data
  })
}

// 修改文档
export function updateDocuments(data) {
  return request({
    url: '/payment/documents',
    method: 'put',
    data: data
  })
}

// 删除文档
export function delDocuments(id) {
  return request({
    url: '/payment/documents/' + id,
    method: 'delete'
  })
}

// 导出文档
export function exportDocuments(query) {
  return request({
    url: '/payment/documents/export',
    method: 'get',
    params: query
  })
}