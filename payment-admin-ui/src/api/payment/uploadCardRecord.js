import request from '@/utils/request'

// 查询上传卡记录列表
export function listUploadCardRecord(query) {
  return request({
    url: '/payment/uploadCardRecord/list',
    method: 'get',
    params: query
  })
}

// 查询上传卡记录详细
export function getUploadCardRecord(id) {
  return request({
    url: '/payment/uploadCardRecord/' + id,
    method: 'get'
  })
}

// 新增上传卡记录
export function addUploadCardRecord(data) {
  return request({
    url: '/payment/uploadCardRecord',
    method: 'post',
    data: data
  })
}

// 修改上传卡记录
export function updateUploadCardRecord(data) {
  return request({
    url: '/payment/uploadCardRecord',
    method: 'put',
    data: data
  })
}

// 删除上传卡记录
export function delUploadCardRecord(id) {
  return request({
    url: '/payment/uploadCardRecord/' + id,
    method: 'delete'
  })
}

// 导出上传卡记录
export function exportUploadCardRecord(query) {
  return request({
    url: '/payment/uploadCardRecord/export',
    method: 'get',
    params: query
  })
}