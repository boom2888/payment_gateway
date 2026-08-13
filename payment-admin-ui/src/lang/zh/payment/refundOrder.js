export default {
  // 搜索表单
  searchForm: {
    merchantId: '商户',
    orderId: '订单ID',
    refundSsoId: '商户退款订单',
    transactionId: '银行交易流水号',
    refundAmount: '订单退款金额',
    refundCurrencyId: '订单退款币种',
    status: '退款状态',
    remainingRefundableAmount: '可退款金额',
    createAt: '退款时间',
    searchBtn: '搜索',
    resetBtn: '重置'
  },

  // 表单字段
  form: {
    merchantIdPlaceholder: '请选择商户名称',
    orderIdPlaceholder: '请输入订单ID',
    refundSsoIdPlaceholder: '请输入商户退款订单',
    transactionIdPlaceholder: '请输入银行交易流水号',
    refundAmountPlaceholder: '请输入订单退款金额',
    refundCurrencyIdPlaceholder: '请输入订单退款币种',
    statusPlaceholder: '请选择退款状态',
    remainingRefundableAmountPlaceholder: '请输入可退款金额',
    createAtPlaceholder: '选择创建时间',
    failReasonPlaceholder: '请输入内容'
  },

  // 表格列
  table: {
    id: '退款ID',
    merchant: '商户名称',
    merchantId: '商户ID',
    orderId: '订单ID',
    fiatAmount: '支付金额',
    refundSsoId: '商户退款订单',
    transactionId: '银行交易流水号',
    refundAmount: '订单退款金额',
    refundCurrencyId: '订单退款币种',
    status: '退款状态',
    remainingRefundableAmount: '剩余可退款金额',
    payTime: '支付时间',
    createAt: '退款时间',
    failReason: '失败原因',
    action: '操作'
  },

  // 操作按钮
  actions: {
    add: '新增',
    edit: '修改',
    delete: '删除',
    export: '导出'
  },

  // 退款状态
  statusOptions: {
    success: '成功',
    fail: '失败',
    processing: '处理中'
  },

  // 弹窗标题
  dialog: {
    addTitle: '添加退款订单',
    editTitle: '修改退款订单'
  },

  // 按钮文本
  buttons: {
    confirm: '确 定',
    cancel: '取 消'
  },

  // 表单验证消息
  validation: {
    orderIdRequired: '订单id不能为空'
  },

  // 操作消息
  messages: {
    addSuccess: '新增成功',
    editSuccess: '修改成功',
    deleteSuccess: '删除成功'
  },

  // 确认对话框
  confirmDialog: {
    deleteTitle: '警告',
    deleteMessage: '是否确认删除退款订单编号为"{ids}"的数据项?',
    exportTitle: '警告',
    exportMessage: '是否确认导出所有退款订单数据项?',
    confirmExportSelected: '是否确认导出所选的 {count} 条退款订单?',
    confirmExportAll: '是否确认导出当前筛选下的所有退款订单 {total} 条?',
    confirmText: '确定',
    cancelText: '取消'
  }
}
