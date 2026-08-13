import request from '@/utils/request'

// 查询注册码列表
export function listSignupCode(query) {
  return request({
    url: '/payment/signupCode/list',
    method: 'get',
    params: query
  })
}

// 查询注册码详细
export function getSignupCode(id) {
  return request({
    url: '/payment/signupCode/' + id,
    method: 'get'
  })
}

// 新增注册码
export function addSignupCode(data) {
  return request({
    url: '/payment/signupCode',
    method: 'post',
    data: data
  })
}

// 修改注册码
export function updateSignupCode(data) {
  return request({
    url: '/payment/signupCode',
    method: 'put',
    data: data
  })
}

// 删除注册码
export function delSignupCode(id) {
  return request({
    url: '/payment/signupCode/' + id,
    method: 'delete'
  })
}

// 导出注册码
export function exportSignupCode(query) {
  return request({
    url: '/payment/signupCode/export',
    method: 'get',
    params: query
  })
}