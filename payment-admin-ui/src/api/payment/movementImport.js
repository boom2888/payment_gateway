import request from '@/utils/request'

// 查询nuvei报告导入记录列表
export function listMovementImport(query) {
  return request({
    url: '/payment/movementImport/list',
    method: 'get',
    params: query
  })
}

// 查询nuvei报告导入记录详细
export function getMovementImport(id) {
  return request({
    url: '/payment/movementImport/' + id,
    method: 'get'
  })
}

// 新增nuvei报告导入记录
export function addMovementImport(data) {
  return request({
    url: '/payment/movementImport',
    method: 'post',
    data: data
  })
}

// 修改nuvei报告导入记录
export function updateMovementImport(data) {
  return request({
    url: '/payment/movementImport',
    method: 'put',
    data: data
  })
}

// 删除nuvei报告导入记录
export function delMovementImport(id) {
  return request({
    url: '/payment/movementImport/' + id,
    method: 'delete'
  })
}

export function exportDailySummary(ids) {
  return request({
    url: '/payment/movementImport/exportDailySummary/' + ids,
    method: 'get'
  })
}

export function invalidMovementImport(id) {
  return request({
    url: '/payment/movementImport/invalid/' + id,
    method: 'post'
  })
}

export function regenerate(id) {
  return request({
    url: '/payment/movementImport/regenerate/' + id,
    method: 'post'
  })
}

// 导入 Nuvie 报告
export function importNuvieReport(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/payment/movementImport/importNuvieReport',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    timeout: 120000 // 文件上传设置 60 秒超时
  })
}
