export default {
  // 搜索表单
  searchForm: {
    merchantId: 'Merchant',
    orderId: 'Order ID',
    refundSsoId: 'Merchant Refund Order',
    transactionId: 'Bank Transaction ID',
    refundAmount: 'Refund Amount',
    refundCurrencyId: 'Refund Currency',
    status: 'Refund Status',
    remainingRefundableAmount: 'Remaining Refundable Amount',
    createAt: 'Refund Time',
    searchBtn: 'Search',
    resetBtn: 'Reset'
  },

  // 表单字段
  form: {
    merchantIdPlaceholder: 'Please select merchant',
    orderIdPlaceholder: 'Please enter order ID',
    refundSsoIdPlaceholder: 'Please enter merchant refund order',
    transactionIdPlaceholder: 'Please enter bank transaction ID',
    refundAmountPlaceholder: 'Please enter refund amount',
    refundCurrencyIdPlaceholder: 'Please enter refund currency',
    statusPlaceholder: 'Please select refund status',
    remainingRefundableAmountPlaceholder: 'Please enter remaining refundable amount',
    createAtPlaceholder: 'Select Refund Time',
    failReasonPlaceholder: 'Please enter content'
  },

  // 表格列
  table: {
    id: 'Refund ID',
    merchant: 'Merchant',
    merchantId: 'Merchant ID',
    orderId: 'Order ID',
    fiatAmount: 'Fiat Amount',
    refundSsoId: 'Merchant Refund Order',
    transactionId: 'Bank Transaction ID',
    refundAmount: 'Refund Amount',
    refundCurrencyId: 'Refund Currency',
    status: 'Refund Status',
    remainingRefundableAmount: 'Remaining Refundable Amount',
    payTime: 'Payment Time',
    createAt: 'Refund Time',
    failReason: 'Fail Reason',
    action: 'Action'
  },

  // 操作按钮
  actions: {
    add: 'Add',
    edit: 'Edit',
    delete: 'Delete',
    export: 'Export'
  },

  // 退款状态
  statusOptions: {
    success: 'Success',
    fail: 'Failed',
    processing: 'Processing'
  },

  // 弹窗标题
  dialog: {
    addTitle: 'Add Refund Order',
    editTitle: 'Edit Refund Order'
  },

  // 按钮文本
  buttons: {
    confirm: 'Confirm',
    cancel: 'Cancel'
  },

  // 表单验证消息
  validation: {
    orderIdRequired: 'Order ID cannot be empty'
  },

  // 操作消息
  messages: {
    addSuccess: 'Added successfully',
    editSuccess: 'Modified successfully',
    deleteSuccess: 'Deleted successfully'
  },

  // 确认对话框
  confirmDialog: {
    deleteTitle: 'Warning',
    deleteMessage: 'Are you sure to delete the refund order with ID "{ids}"?',
    exportTitle: 'Warning',
    exportMessage: 'Are you sure to export all refund order data items?',
    confirmExportSelected: 'Are you sure to export the selected {count} refund orders?',
    confirmExportAll: 'Are you sure to export all {total} filtered refund orders?',
    confirmText: 'Confirm',
    cancelText: 'Cancel'
  }
}
