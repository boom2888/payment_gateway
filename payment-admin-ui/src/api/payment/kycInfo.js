import request from '@/utils/request'

// 查询KYC信息列表
export function listKycInfo(query) {
  return request({
    url: '/payment/kycInfo/list',
    method: 'get',
    params: query
  })
}

// 查询KYC信息详细
export function getKycInfo(id) {
  return request({
    url: '/payment/kycInfo/' + id,
    method: 'get'
  })
}

// 新增KYC信息
export function addKycInfo(data) {
  return request({
    url: '/payment/kycInfo',
    method: 'post',
    data: data
  })
}

// 修改KYC信息
export function updateKycInfo(data) {
  return request({
    url: '/payment/kycInfo',
    method: 'put',
    data: data
  })
}

// 删除KYC信息
export function delKycInfo(id) {
  return request({
    url: '/payment/kycInfo/' + id,
    method: 'delete'
  })
}

// 导出KYC信息
export function exportKycInfo(query) {
  return request({
    url: '/payment/kycInfo/export',
    method: 'get',
    params: query
  })
}