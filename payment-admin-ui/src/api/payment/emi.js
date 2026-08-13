import request from '@/utils/request'

//个人账户资料提交
export function personalAccountSubmit(data) {
  return request({
    url: '/payment/information',
    method: 'post',
    data: data
  })
}

//企业账户资料提交
export function companyAccountSubmit(data) {
  return request({
    url: '/payment/info',
    method: 'post',
    data: data
  })
}

//======================================================针对收款菜单的2个接口,分别是: 付款方列表,订单管理
//付款方列表接口: /payment/payer/list
export function reListPayer(query) {
  return request({
    url: '/payment/table/list',
    method: 'get',
    params: query
  })
}

//订单管理接口: /payment/order/list
export function reListOrder(query) {
  return request({
    url: '/payment/management/list',
    method: 'get',
    params: query
  })
}

//======================================================针对付款菜单的2个接口,分别是: 付款记录,收款方列表
//付款记录接口: /payment/record/list
export function paListRecord(query) {
  return request({
    url: '/payment/history/list',
    method: 'get',
    params: query
  })
}

//收款方列表接口: /payment/payee/list
export function paListPayee(query) {
  return request({
    url: '/payment/payee/list',
    method: 'get',
    params: query
  })
}

//获取账户信息详情
export function getAccountInfo(id) {
  return request({
    url: '/payment/account/info/' + id,
    method: 'get'
  })
}

//获取个人账户信息
export function getPersonalInformation(id) {
  return request({
    url: '/payment/information/' + id,
    method: 'get'
  })
}

//获取企业账户信息
export function getCompanyInformation(id) {
  return request({
    url: '/payment/info/' + id,
    method: 'get'
  })
}


//emi账户列表
export function emiUserList(query) {
  return request({
    url: '/payment/merchant/list',
    method: 'get',
    params: query
  })
}


//emi 个人账户启用禁用
export function emiUserEnable( data) {
  return request({
    url: '/payment/information',
    method: 'put',
    data: data
  })
}

//emi 个人账户删除
export function emiUserDelete(id) {
  return request({
    url: '/payment/information/' + id,
    method: 'delete'
  })
}

//emi 企业账户启用禁用
export function emiCompanyEnable(data) {
  return request({
    url: '/payment/info',
    method: 'put',
    data: data
  })
}

//emi 企业账户删除
export function emiCompanyDelete(id) {
  return request({
    url: '/payment/info/' + id,
    method: 'delete'
  })
}




//获取个人账户信息,资料提交界面,用于回显
export function getPersonalInformationUser(id) {
  return request({
    url: '/payment/information/user/' + id,
    method: 'get'
  })
}



//获取企业账户信息资料提交界面,用于回显
export function getCompanyInformationUser(id) {
  return request({
    url: '/payment/info/user/' + id,
    method: 'get'
  })
}




//个人账户审核
export function emiUserStatus( data) {
  return request({
    url: '/payment/information',
    method: 'put',
    data: data
  })
}

//企业账户审核
export function emiCompanyStatus( data) {
  return request({
    url: '/payment/info',
    method: 'put',
    data: data
  })
}

//emi 企业审核
export function emiCompanyAudit(data) {
  return request({
    url: '/payment/info/audit',
    method: 'post',
    data: data
  })
}
