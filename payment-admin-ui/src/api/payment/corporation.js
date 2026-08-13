import request from '@/utils/request'

// 查询企业客户公司列表
export function listCorporation(query) {
  return request({
    url: '/payment/corporation/list',
    method: 'get',
    params: query
  })
}

// 查询企业客户公司详细
export function getCorporation(id) {
  return request({
    url: '/payment/corporation/' + id,
    method: 'get'
  })
}

// 新增企业客户公司
export function addCorporation(data) {
  return request({
    url: '/payment/corporation',
    method: 'post',
    data: data
  })
}

// 修改企业客户公司
export function updateCorporation(data) {
  return request({
    url: '/payment/corporation',
    method: 'put',
    data: data
  })
}

// 删除企业客户公司
export function delCorporation(id) {
  return request({
    url: '/payment/corporation/' + id,
    method: 'delete'
  })
}

// 导出企业客户公司
export function exportCorporation(query) {
  return request({
    url: '/payment/corporation/export',
    method: 'get',
    params: query
  })
}