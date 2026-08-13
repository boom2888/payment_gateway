import request from '@/utils/request'

// 查询企业客户公司成员列表
export function listMember(query) {
  return request({
    url: '/payment/member/list',
    method: 'get',
    params: query
  })
}

// 查询企业客户公司成员详细
export function getMember(id) {
  return request({
    url: '/payment/member/' + id,
    method: 'get'
  })
}

// 新增企业客户公司成员
export function addMember(data) {
  return request({
    url: '/payment/member',
    method: 'post',
    data: data
  })
}

// 修改企业客户公司成员
export function updateMember(data) {
  return request({
    url: '/payment/member',
    method: 'put',
    data: data
  })
}

// 删除企业客户公司成员
export function delMember(id) {
  return request({
    url: '/payment/member/' + id,
    method: 'delete'
  })
}

// 导出企业客户公司成员
export function exportMember(query) {
  return request({
    url: '/payment/member/export',
    method: 'get',
    params: query
  })
}