export default {
  // 页面标题和头部
  pageTitle: '公司账户管理',

  // 搜索表单标签
  searchForm: {
    currency: '账户货币',
    createdAt: '创建时间',
    searchBtn: '搜索',
    resetBtn: '重置'
  },

  // 表格标题
  table: {
    id: '主键ID',
    accountId: 'APM提供商账户ID',
    currency: '账户货币',
    createdAt: '创建时间',
    updatedAt: '更新时间',
    action: '操作'
  },

  // 操作按钮
  actions: {
    add: '新增',
    edit: '修改',
    delete: '删除',
    export: '导出'
  },

  // 对话框
  dialog: {
    addTitle: '添加公司账户',
    editTitle: '修改公司账户',
    confirmBtn: '确 定',
    cancelBtn: '取 消'
  },

  // 表单标签
  form: {
    accountId: 'APM提供商账户ID',
    currency: '账户货币',
    createdAt: '创建时间',
    updatedAt: '更新时间',
    accountIdPlaceholder: '请输入APM提供商账户ID',
    currencyPlaceholder: '请输入账户货币',
    createdAtPlaceholder: '选择创建时间',
    updatedAtPlaceholder: '选择更新时间'
  },

  // 验证消息
  validation: {
    accountIdRequired: 'APM提供商账户ID不能为空',
    currencyRequired: '账户货币不能为空',
    createdAtRequired: '创建时间不能为空',
    updatedAtRequired: '更新时间不能为空'
  },

  // 消息提示
  messages: {
    addSuccess: '新增成功',
    editSuccess: '修改成功',
    deleteSuccess: '删除成功',
    deleteConfirm: '是否确认删除公司账户编号为"{id}"的数据项?',
    exportConfirm: '是否确认导出所有公司账户数据项?',
    warning: '警告'
  }
}
