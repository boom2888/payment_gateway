import request from '@/utils/request'

// 查询卡项目列表
export function listShop(query) {
  return request({
    url: '/payment/businessCustomerCorporation/shopConfig',
    method: 'get',
    params: query
  })
}

// 查询卡项目详细
export function updateShopConfig(data) {
  return request({
    url: '/payment/businessCustomerCorporation/selfEdit',
    method: 'put',
    data: data
  })
}
