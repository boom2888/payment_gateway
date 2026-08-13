import request from '@/utils/request'

// 查询字典数据列表
export function listSysDictData(query) {
  return request({
    url: '/payment/sysDictData/list',
    method: 'get',
    params: query
  })
}

// 查询字典数据详细
export function getSysDictData(dictCode) {
  return request({
    url: '/payment/sysDictData/' + dictCode,
    method: 'get'
  })
}

// 新增字典数据
export function addSysDictData(data) {
  return request({
    url: '/payment/sysDictData',
    method: 'post',
    data: data
  })
}

// 修改字典数据
export function updateSysDictData(data) {
  return request({
    url: '/payment/sysDictData',
    method: 'put',
    data: data
  })
}

// 删除字典数据
export function delSysDictData(dictCode) {
  return request({
    url: '/payment/sysDictData/' + dictCode,
    method: 'delete'
  })
}

// 导出字典数据
export function exportSysDictData(query) {
  return request({
    url: '/payment/sysDictData/export',
    method: 'get',
    params: query
  })
}