import request from '@/utils/request'

// 获取用户导出配置
export function getUserExportConfig(module) {
  return request({
    url: `/system/user/exportConfig/${module}`,
    method: 'get'
  })
}

// 保存用户导出配置
export function saveUserExportConfig(module, exportFields) {
  return request({
    url: '/system/user/exportConfig',
    method: 'post',
    params: {
      module,
      exportFields
    }
  })
}

