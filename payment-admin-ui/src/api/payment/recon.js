import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listRecon(query) {
  return request({
    url: '/payment/recon/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getRecon(id) {
  return request({
    url: '/payment/recon/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addRecon(data) {
  return request({
    url: '/payment/recon',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateRecon(data) {
  return request({
    url: '/payment/recon',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delRecon(id) {
  return request({
    url: '/payment/recon/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportRecon(query) {
  return request({
    url: '/payment/recon/export',
    method: 'get',
    params: query
  })
}