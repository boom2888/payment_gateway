import shopRequest from '@/utils/requests/shopRequest'
import request from '@/utils/request'
// 查询下游商户列表
export function listShop(query) {
  return shopRequest({
    url: '/card/shop/list',
    method: 'get',
    params: query
  })
}

// 查询商户认证数据
export function authListShop(query) {
  return request({
    url: '/payment/businessCustomerCorporation/authList',
    method: 'get',
    params: query
  })
}

// 查询下游商户详细
export function getShop(id) {
  return shopRequest({
    url: '/card/shop/' + id,
    method: 'get'
  })
}

// 新增下游商户
export function addShop(data) {
  return shopRequest({
    url: '/card/shop',
    method: 'post',
    data: data
  })
}

// 修改下游商户
export function updateShop(data) {
  return shopRequest({
    url: '/card/shop',
    method: 'put',
    data: data
  })
}

// 商户认证编辑
export function updateAuthShop(data) {
  return request({
    url: '/payment/businessCustomerCorporation/authEdit',
    method: 'put',
    data: data
  })
}

// 删除下游商户
export function delShop(id) {
  return shopRequest({
    url: '/card/shop/shop/' + id,
    method: 'delete'
  })
}

// 导出下游商户
export function exportShop(query) {
  return shopRequest({
    url: '/card/shop/export',
    method: 'get',
    params: query
  })
}

// 查询所有商户列表
export function allShopList() {
  return shopRequest({
    url: '/card/shop/allList',
    method: 'get'
  })
}

// 审核钱包记录
export function changeShopModel(data) {
  return shopRequest({
    url: '/card/shop/changeModel',
    method: 'post',
    data: data
  })
}
