import request from '@/utils/request'

// 查询账户列表
export function listAccount(query) {
  return request({
    url: '/payment/account/list',
    method: 'get',
    params: query
  })
}

// 查询账户详细
export function getAccount(id) {
  return request({
    url: '/payment/account/' + id,
    method: 'get'
  })
}

// 新增账户
export function addAccount(data) {
  return request({
    url: '/payment/account',
    method: 'post',
    data: data
  })
}

// 修改账户
export function updateAccount(data) {
  return request({
    url: '/payment/account',
    method: 'put',
    data: data
  })
}

// 删除账户
export function delAccount(id) {
  return request({
    url: '/payment/account/' + id,
    method: 'delete'
  })
}

// 导出账户
export function exportAccount(query) {
  return request({
    url: '/payment/account/export',
    method: 'get',
    params: query
  })
}