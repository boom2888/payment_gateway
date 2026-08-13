import request from '@/utils/request'

// 查询MFA代码列表
export function listMfaCode(query) {
  return request({
    url: '/payment/mfaCode/list',
    method: 'get',
    params: query
  })
}

// 查询MFA代码详细
export function getMfaCode(id) {
  return request({
    url: '/payment/mfaCode/' + id,
    method: 'get'
  })
}

// 新增MFA代码
export function addMfaCode(data) {
  return request({
    url: '/payment/mfaCode',
    method: 'post',
    data: data
  })
}

// 修改MFA代码
export function updateMfaCode(data) {
  return request({
    url: '/payment/mfaCode',
    method: 'put',
    data: data
  })
}

// 删除MFA代码
export function delMfaCode(id) {
  return request({
    url: '/payment/mfaCode/' + id,
    method: 'delete'
  })
}

// 导出MFA代码
export function exportMfaCode(query) {
  return request({
    url: '/payment/mfaCode/export',
    method: 'get',
    params: query
  })
}