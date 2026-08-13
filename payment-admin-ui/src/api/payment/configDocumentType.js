import request from '@/utils/request'

// 查询证件类型配置列表
export function listConfigDocumentType(query) {
  return request({
    url: '/payment/configDocumentType/list',
    method: 'get',
    params: query
  })
}

// 查询证件类型配置详细
export function getConfigDocumentType(code) {
  return request({
    url: '/payment/configDocumentType/' + code,
    method: 'get'
  })
}

// 新增证件类型配置
export function addConfigDocumentType(data) {
  return request({
    url: '/payment/configDocumentType',
    method: 'post',
    data: data
  })
}

// 修改证件类型配置
export function updateConfigDocumentType(data) {
  return request({
    url: '/payment/configDocumentType',
    method: 'put',
    data: data
  })
}

// 删除证件类型配置
export function delConfigDocumentType(code) {
  return request({
    url: '/payment/configDocumentType/' + code,
    method: 'delete'
  })
}

// 导出证件类型配置
export function exportConfigDocumentType(query) {
  return request({
    url: '/payment/configDocumentType/export',
    method: 'get',
    params: query
  })
}