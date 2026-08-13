import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listFile(query) {
  return request({
    url: '/payment/file/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getFile(id) {
  return request({
    url: '/payment/file/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addFile(data) {
  return request({
    url: '/payment/file',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateFile(data) {
  return request({
    url: '/payment/file',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delFile(id) {
  return request({
    url: '/payment/file/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportFile(query) {
  return request({
    url: '/payment/file/export',
    method: 'get',
    params: query
  })
}