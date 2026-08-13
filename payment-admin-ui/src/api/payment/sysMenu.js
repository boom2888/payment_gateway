import request from '@/utils/request'

// 查询菜单权限列表
export function listSysMenu(query) {
  return request({
    url: '/payment/sysMenu/list',
    method: 'get',
    params: query
  })
}

// 查询菜单权限详细
export function getSysMenu(menuId) {
  return request({
    url: '/payment/sysMenu/' + menuId,
    method: 'get'
  })
}

// 新增菜单权限
export function addSysMenu(data) {
  return request({
    url: '/payment/sysMenu',
    method: 'post',
    data: data
  })
}

// 修改菜单权限
export function updateSysMenu(data) {
  return request({
    url: '/payment/sysMenu',
    method: 'put',
    data: data
  })
}

// 删除菜单权限
export function delSysMenu(menuId) {
  return request({
    url: '/payment/sysMenu/' + menuId,
    method: 'delete'
  })
}

// 导出菜单权限
export function exportSysMenu(query) {
  return request({
    url: '/payment/sysMenu/export',
    method: 'get',
    params: query
  })
}