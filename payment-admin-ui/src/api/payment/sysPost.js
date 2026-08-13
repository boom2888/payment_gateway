import request from '@/utils/request'

// 查询岗位信息列表
export function listSysPost(query) {
  return request({
    url: '/payment/sysPost/list',
    method: 'get',
    params: query
  })
}

// 查询岗位信息详细
export function getSysPost(postId) {
  return request({
    url: '/payment/sysPost/' + postId,
    method: 'get'
  })
}

// 新增岗位信息
export function addSysPost(data) {
  return request({
    url: '/payment/sysPost',
    method: 'post',
    data: data
  })
}

// 修改岗位信息
export function updateSysPost(data) {
  return request({
    url: '/payment/sysPost',
    method: 'put',
    data: data
  })
}

// 删除岗位信息
export function delSysPost(postId) {
  return request({
    url: '/payment/sysPost/' + postId,
    method: 'delete'
  })
}

// 导出岗位信息
export function exportSysPost(query) {
  return request({
    url: '/payment/sysPost/export',
    method: 'get',
    params: query
  })
}