import request from '@/utils/request'

// 查询订单列表
export function listOrder(query) {
  return request({
    url: '/payment/order/list',
    method: 'get',
    params: query
  })
}

// 查询订单详细
export function getOrder(id) {
  return request({
    url: '/payment/order/' + id,
    method: 'get'
  })
}

// 新增订单
export function addOrder(data) {
  return request({
    url: '/payment/order',
    method: 'post',
    data: data
  })
}

// 修改订单
export function updateOrder(data) {
  return request({
    url: '/payment/order',
    method: 'put',
    data: data
  })
}

// 删除订单
export function delOrder(id) {
  return request({
    url: '/payment/order/' + id,
    method: 'delete'
  })
}

// 导出订单,lang=en
export function exportOrder(query, lang) {
  const url = '/payment/order/export'

  // 处理查询参数
  const requestParams = {}

  // 添加lang参数到请求参数中
  if (lang) {
    requestParams.lang = lang
  }

  // 如果有ids，只传递ids和exportFields参数
  if (query.ids && query.ids.length > 0) {
    requestParams.ids = query.ids
    // 保留exportFields参数（如果存在）
    if (query.exportFields) {
      requestParams.exportFields = query.exportFields
    }
  } else {
    // 没有ids时，传递所有查询条件
    // 过滤掉空值的查询参数，确保只传递有效的过滤条件
    Object.keys(query).forEach(key => {
      if (query[key] !== undefined && query[key] !== null && query[key] !== '') {
        requestParams[key] = query[key]
      }
    })

    // 处理嵌套的params对象（日期范围等）
    if (query.params && typeof query.params === 'object') {
      Object.keys(query.params).forEach(key => {
        if (query.params[key] !== undefined && query.params[key] !== null && query.params[key] !== '') {
          requestParams[key] = query.params[key]
        }
      })
    }
  }

  return request({
    url: url,
    method: 'get',
    params: requestParams,
    timeout: 300000
  })
}

// 添加交易回调方法
export function tradeCallBack(data) {
  return request({
    url: '/payment/order/tradeCallBack',
    method: 'post',
    data: data
  })
}
