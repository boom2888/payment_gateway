import request from '@/utils/request'

// 查询滚动准备金分类账列表
export function listRollingReserveLedger(query) {
  return request({
    url: '/payment/rollingReserveLedger/list',
    method: 'get',
    params: query
  })
}

// 查询滚动准备金分类账详细
export function getRollingReserveLedger(id) {
  return request({
    url: '/payment/rollingReserveLedger/' + id,
    method: 'get'
  })
}

// 新增滚动准备金分类账
export function addRollingReserveLedger(data) {
  return request({
    url: '/payment/rollingReserveLedger',
    method: 'post',
    data: data
  })
}

// 修改滚动准备金分类账
export function updateRollingReserveLedger(data) {
  return request({
    url: '/payment/rollingReserveLedger',
    method: 'put',
    data: data
  })
}

// 删除滚动准备金分类账
export function delRollingReserveLedger(id) {
  return request({
    url: '/payment/rollingReserveLedger/' + id,
    method: 'delete'
  })
}

// 导出滚动准备金分类账
export function exportRollingReserveLedger(query) {
  return request({
    url: '/payment/rollingReserveLedger/export',
    method: 'get',
    params: query
  })
}