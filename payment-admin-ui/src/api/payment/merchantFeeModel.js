import request from '@/utils/request'

// 查询商户收费模型列表
export function listMerchantFeeModel(query) {
  return request({
    url: '/payment/merchantFeeModel/list',
    method: 'get',
    params: query
  })
}

// 查询商户收费模型详细
export function getMerchantFeeModel(id) {
  return request({
    url: '/payment/merchantFeeModel/' + id,
    method: 'get'
  })
}

// 新增商户收费模型
export function addMerchantFeeModel(data) {
  return request({
    url: '/payment/merchantFeeModel',
    method: 'post',
    data: data
  })
}

// 修改商户收费模型
export function updateMerchantFeeModel(data) {
  return request({
    url: '/payment/merchantFeeModel',
    method: 'put',
    data: data
  })
}

// 删除商户收费模型
export function delMerchantFeeModel(id) {
  return request({
    url: '/payment/merchantFeeModel/' + id,
    method: 'delete'
  })
}

// 导出商户收费模型
export function exportMerchantFeeModel(query) {
  return request({
    url: '/payment/merchantFeeModel/export',
    method: 'get',
    params: query
  })
}