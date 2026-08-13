import request from '@/utils/request'

// 查询平台钱包列表
export function listHouseWallet(query) {
  return request({
    url: '/payment/houseWallet/list',
    method: 'get',
    params: query
  })
}

// 查询平台钱包详细
export function getHouseWallet(id) {
  return request({
    url: '/payment/houseWallet/' + id,
    method: 'get'
  })
}

// 新增平台钱包
export function addHouseWallet(data) {
  return request({
    url: '/payment/houseWallet',
    method: 'post',
    data: data
  })
}

// 修改平台钱包
export function updateHouseWallet(data) {
  return request({
    url: '/payment/houseWallet',
    method: 'put',
    data: data
  })
}

// 删除平台钱包
export function delHouseWallet(id) {
  return request({
    url: '/payment/houseWallet/' + id,
    method: 'delete'
  })
}

// 导出平台钱包
export function exportHouseWallet(query) {
  return request({
    url: '/payment/houseWallet/export',
    method: 'get',
    params: query
  })
}