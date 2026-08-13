import request from '@/utils/request'

// 查询字典类型列表
export function listSysDictType(query) {
  return request({
    url: '/payment/sysDictType/list',
    method: 'get',
    params: query
  })
}

// 查询字典类型详细
export function getSysDictType(dictId) {
  return request({
    url: '/payment/sysDictType/' + dictId,
    method: 'get'
  })
}

// 新增字典类型
export function addSysDictType(data) {
  return request({
    url: '/payment/sysDictType',
    method: 'post',
    data: data
  })
}

// 修改字典类型
export function updateSysDictType(data) {
  return request({
    url: '/payment/sysDictType',
    method: 'put',
    data: data
  })
}

// 删除字典类型
export function delSysDictType(dictId) {
  return request({
    url: '/payment/sysDictType/' + dictId,
    method: 'delete'
  })
}

// 导出字典类型
export function exportSysDictType(query) {
  return request({
    url: '/payment/sysDictType/export',
    method: 'get',
    params: query
  })
}