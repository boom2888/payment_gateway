import request from '@/utils/request'

// 查询部门列表
export function listSysDept(query) {
  return request({
    url: '/payment/sysDept/list',
    method: 'get',
    params: query
  })
}

// 查询部门详细
export function getSysDept(deptId) {
  return request({
    url: '/payment/sysDept/' + deptId,
    method: 'get'
  })
}

// 新增部门
export function addSysDept(data) {
  return request({
    url: '/payment/sysDept',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateSysDept(data) {
  return request({
    url: '/payment/sysDept',
    method: 'put',
    data: data
  })
}

// 删除部门
export function delSysDept(deptId) {
  return request({
    url: '/payment/sysDept/' + deptId,
    method: 'delete'
  })
}

// 导出部门
export function exportSysDept(query) {
  return request({
    url: '/payment/sysDept/export',
    method: 'get',
    params: query
  })
}