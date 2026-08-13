import request from '@/utils/request'

// 查询收购单行列表
export function listAcquirer(query) {
  return request({
    url: '/payment/acquirer/list',
    method: 'get',
    params: query
  })
}

// 查询收购单行详细
export function getAcquirer(id) {
  return request({
    url: '/payment/acquirer/' + id,
    method: 'get'
  })
}

// 新增收购单行
export function addAcquirer(data) {
  return request({
    url: '/payment/acquirer',
    method: 'post',
    data: data
  })
}

// 修改收购单行
export function updateAcquirer(data) {
  return request({
    url: '/payment/acquirer',
    method: 'put',
    data: data
  })
}

// 删除收购单行
export function delAcquirer(id) {
  return request({
    url: '/payment/acquirer/' + id,
    method: 'delete'
  })
}

// 导出收购单行
export function exportAcquirer(query) {
  return request({
    url: '/payment/acquirer/export',
    method: 'get',
    params: query
  })
}