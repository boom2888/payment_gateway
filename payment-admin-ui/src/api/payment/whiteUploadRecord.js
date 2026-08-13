import request from '@/utils/request'

// 查询白名单上传记录列表
export function listWhiteUploadRecord(query) {
  return request({
    url: '/payment/whiteUploadRecord/list',
    method: 'get',
    params: query
  })
}

// 查询白名单上传记录详细
export function getWhiteUploadRecord(id) {
  return request({
    url: '/payment/whiteUploadRecord/' + id,
    method: 'get'
  })
}

// 新增白名单上传记录
export function addWhiteUploadRecord(data) {
  return request({
    url: '/payment/whiteUploadRecord',
    method: 'post',
    data: data
  })
}

// 修改白名单上传记录
export function updateWhiteUploadRecord(data) {
  return request({
    url: '/payment/whiteUploadRecord',
    method: 'put',
    data: data
  })
}

// 删除白名单上传记录
export function delWhiteUploadRecord(id) {
  return request({
    url: '/payment/whiteUploadRecord/' + id,
    method: 'delete'
  })
}

// 导出白名单上传记录
export function exportWhiteUploadRecord(query) {
  return request({
    url: '/payment/whiteUploadRecord/export',
    method: 'get',
    params: query
  })
}