import request from '@/utils/request'

// 查询Nuvei争议数据列表
export function listNuvieDisputeData(query) {
  return request({
    url: '/payment/nuvieDisputeData/list',
    method: 'get',
    params: query
  })
}

// 查询Nuvei争议数据详细
export function getNuvieDisputeData(id) {
  return request({
    url: '/payment/nuvieDisputeData/' + id,
    method: 'get'
  })
}

// 新增Nuvei争议数据
export function addNuvieDisputeData(data) {
  return request({
    url: '/payment/nuvieDisputeData',
    method: 'post',
    data: data
  })
}

// 修改Nuvei争议数据
export function updateNuvieDisputeData(data) {
  return request({
    url: '/payment/nuvieDisputeData',
    method: 'put',
    data: data
  })
}

// 删除Nuvei争议数据
export function delNuvieDisputeData(id) {
  return request({
    url: '/payment/nuvieDisputeData/' + id,
    method: 'delete'
  })
}

// 导出Nuvei争议数据
export function exportNuvieDisputeData(query) {
  return request({
    url: '/payment/nuvieDisputeData/export',
    method: 'get',
    params: query
  })
}