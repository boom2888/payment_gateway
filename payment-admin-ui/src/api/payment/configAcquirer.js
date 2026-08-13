import request from '@/utils/request'

// 查询收单机构配置列表
export function listConfigAcquirer(query) {
  return request({
    url: '/payment/configAcquirer/list',
    method: 'get',
    params: query
  })
}

// 查询收单机构配置详细
export function getConfigAcquirer(id) {
  return request({
    url: '/payment/configAcquirer/' + id,
    method: 'get'
  })
}

// 新增收单机构配置
export function addConfigAcquirer(data) {
  return request({
    url: '/payment/configAcquirer',
    method: 'post',
    data: data
  })
}

// 修改收单机构配置
export function updateConfigAcquirer(data) {
  return request({
    url: '/payment/configAcquirer',
    method: 'put',
    data: data
  })
}

// 删除收单机构配置
export function delConfigAcquirer(id) {
  return request({
    url: '/payment/configAcquirer/' + id,
    method: 'delete'
  })
}

// 导出收单机构配置
export function exportConfigAcquirer(query) {
  return request({
    url: '/payment/configAcquirer/export',
    method: 'get',
    params: query
  })
}