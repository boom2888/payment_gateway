import request from '@/utils/request'

// 查询公司账户列表
export function listCompanyAccount(query) {
  return request({
    url: '/payment/companyAccount/list',
    method: 'get',
    params: query
  })
}

// 查询公司账户详细
export function getCompanyAccount(id) {
  return request({
    url: '/payment/companyAccount/' + id,
    method: 'get'
  })
}

// 新增公司账户
export function addCompanyAccount(data) {
  return request({
    url: '/payment/companyAccount',
    method: 'post',
    data: data
  })
}

// 修改公司账户
export function updateCompanyAccount(data) {
  return request({
    url: '/payment/companyAccount',
    method: 'put',
    data: data
  })
}

// 删除公司账户
export function delCompanyAccount(id) {
  return request({
    url: '/payment/companyAccount/' + id,
    method: 'delete'
  })
}

// 导出公司账户
export function exportCompanyAccount(query) {
  return request({
    url: '/payment/companyAccount/export',
    method: 'get',
    params: query
  })
}