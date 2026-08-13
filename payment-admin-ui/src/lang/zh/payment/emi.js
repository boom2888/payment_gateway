export default {
  // 查询表单
  name: '名称',
  namePlaceholder: '请输入商户名称',
  status: '资料状态',
  statusPlaceholder: '请选择资料状态',
  enabledStatus: '启用状态',
  enabledStatusPlaceholder: '请选择启用状态',
  merchantType: '商户类型',
  merchantTypePlaceholder: '请选择商户类型',
  search: '搜索',
  reset: '重置',

  // 状态选项
  statusPendingSubmit: '待提交资料',
  statusPendingReview: '待审核',
  statusPendingBinnerReview: '待binner审核',
  statusApproved: '审核通过',
  statusRejected: '审核失败',

  // 启用状态
  enabled: '启用',
  disabled: '禁用',

  // 商户类型
  personal: '个人',
  company: '企业',

  // 表格列
  id: '编号',
  account: '账号',
  createdAt: '创建时间',
  operation: '操作',

  // 操作按钮
  add: '新增',
  delete: '删除',
  export: '导出',
  detail: '详情',

  // 提示信息
  deleteConfirm: '是否确认删除EMI商户编号为"{ids}"的数据项?',
  warning: '警告',
  confirm: '确定',
  cancel: '取消',
  deleteSuccess: '删除成功',
  deleteError: '删除失败',
  exportConfirm: '是否确认导出所有EMI商户数据项?',
  exportSuccess: '导出成功',
  addSuccess: '新增成功',
  updateSuccess: '修改成功',

  // 状态变更
  statusChangeConfirm: '确认要"{status}""{name}"商户吗?',
  enableSuccess: '启用成功',
  enableError: '启用失败',
  disableSuccess: '禁用成功',
  disableError: '禁用失败',

  // 弹窗标题
  addTitle: '添加EMI商户',
  updateTitle: '修改EMI商户',

  // 收款页面
  receive: {
    // 标签页
    payerListTab: '付款方列表',
    orderManageTab: '订单管理',

    // 筛选器
    all: '全部',
    startDate: '开始日期',
    endDate: '结束日期',
    addPayer: '添加付款人',

    // 状态
    completed: '已完成',
    partialPayment: '部分付款',
    pendingPayment: '待付款',

    // 表格列 - 付款方列表
    payerName: '付款人名称',
    paymentAccountName: '付款账户名称',
    remitArea: '汇出地区',
    paymentAccount: '付款账号',
    paymentBank: '付款银行',
    use: '用途',

    // 表格列 - 订单管理
    orderTime: '订单时间',
    orderNumber: '订单编号',
    orderAmount: '订单金额',
    remainingBalance: '剩余额度',
    nation: '国家',
    tradeMethod: '贸易方式',
    buyerName: '买家名称'
  },

  // 详情页面
  info: {
    // 页面标题
    personalAccountInfo: '个人账户信息',
    companyAccountInfo: '企业账户信息',

    // 按钮
    audit: '审核',
    back: '返回',

    // 分组标题
    basicInfo: '基础信息',
    contactInfo: '联系信息',
    idPhotos: '证件照片',
    companyBasicInfo: '公司基础信息',
    addressInfo: '地址信息',
    exportBusinessInfo: '出口业务信息',
    contactPersonInfo: '联系人信息',
    attachmentInfo: '附件信息',

    // 通用字段
    idNumber: '编号',
    fullName: '姓名',
    gender: '性别',
    birthDate: '出生日期',
    nationality: '国籍',
    residenceCountry: '所在国家',
    registrationDate: '注册日期',
    email: '邮箱',
    fundSource: '资金来源',
    bankAccount: '银行账户',
    bank: '开户银行',
    contactPhone: '联系电话',
    idType: '证件类型',
    idNumberField: '证件号码',
    provinceCity: '省/市',
    postalCode: '邮政编码',
    residenceAddress: '居住地址',

    // 企业字段
    companyNameChinese: '公司中文名称',
    companyNameEnglish: '公司英文名称',
    businessRegistrationNumber: '工商注册号',
    companyRegistrationCode: '公司注册代码',
    registeredCountry: '注册国家',
    establishmentDate: '成立日期',
    expirationDate: '证书到期日期',
    employeeCount: '员工人数',
    industryType: '行业类型',
    website: '公司网站',
    statusLabel: '状态',
    createdAt: '创建时间',
    updatedAt: '更新时间',
    userId: '用户ID',
    registeredAddress: '注册地址',
    actualOperationAddress: '实际经营地址',
    exportType: '出口类型',
    exportCountries: '出口国家',
    exportProductName: '出口产品名称',
    historicalAnnualExport: '历史年度出口额',
    estimatedAnnualExport: '预计年度出口额',

    // 附件
    idPhoto: '身份证照片',
    businessRegistration: '营业执照',
    companyRegistration: '公司注册证明',
    officeScene: '办公场景照片',

    // 性别
    male: '男',
    female: '女',
    other: '其他',

    // 证件类型
    idCard: '身份证',
    passport: '护照',
    driverLicense: '驾驶证',
    militaryID: '军官证',
    otherIdType: '其他',

    // 状态
    statusPendingSubmit: '待提交资料',
    statusPendingReview: '待审核',
    statusPendingBinnerReview: '待binner审核',
    statusApproved: '审核通过',
    statusRejected: '审核失败',

    // 国家
    countries: {
      CN: '中国',
      US: '美国',
      GB: '英国',
      JP: '日本',
      KR: '韩国',
      FR: '法国',
      DE: '德国',
      CA: '加拿大',
      AU: '澳大利亚',
      SG: '新加坡',
      HK: '香港',
      TW: '台湾',
      MO: '澳门',
      CL: '智利'
    },

    // 通用文本
    noData: '暂无数据',
    noPhoto: '暂无照片',
    unknown: '未知',

    // 审核对话框
    personalAuditTitle: '个人账户审核',
    companyAuditTitle: '企业账户审核',
    auditResult: '审核结果',
    auditPass: '审核通过',
    auditReject: '审核拒绝',
    remark: '备注',
    remarkPlaceholder: '请输入审核备注（可选）',
    confirmButton: '确 定',
    cancelButton: '取 消',

    // 提示信息
    missingParams: '缺少必要参数',
    invalidAccountType: '账户类型参数错误',
    fetchError: '获取账户信息失败',
    auditConfirm: '确定要{action}该账户的审核吗？',
    pass: '通过',
    reject: '拒绝',
    warning: '提示',
    companyAuditSuccess: '企业审核操作成功',
    companyAuditError: '企业审核操作失败',
    auditSuccess: '审核操作成功',
    auditError: '审核操作失败',
    callbackParamError: '回调参数错误',
    processing: '正在处理中...',
    submittingAudit: '正在提交审核...',
    submittingCompanyAudit: '正在提交企业审核...'
  },

  // 用户信息页面
  userInfo: {
    // 页面标题
    personalAccountInfo: '个人账户信息',
    companyAccountInfo: '企业账户信息',

    // 分组标题
    basicInfo: '基础信息',
    contactInfo: '联系信息',
    idPhotos: '证件照片',
    companyBasicInfo: '公司基础信息',
    addressInfo: '地址信息',
    exportBusinessInfo: '出口业务信息',
    contactPersonInfo: '联系人信息',
    attachmentInfo: '附件信息',

    // 通用字段
    idNumber: '编号',
    fullName: '姓名',
    gender: '性别',
    birthDate: '出生日期',
    nationality: '国籍',
    residenceCountry: '所在国家',
    registrationDate: '注册日期',
    email: '邮箱',
    fundSource: '资金来源',
    bankAccount: '银行账户',
    bank: '开户银行',
    contactPhone: '联系电话',
    idType: '证件类型',
    idNumberField: '证件号码',
    provinceCity: '省/市',
    postalCode: '邮政编码',
    residenceAddress: '居住地址',

    // 企业字段
    companyNameChinese: '公司中文名称',
    companyNameEnglish: '公司英文名称',
    businessRegistrationNumber: '工商注册号',
    companyRegistrationCode: '公司注册代码',
    registeredCountry: '注册国家',
    establishmentDate: '成立日期',
    expirationDate: '证书到期日期',
    employeeCount: '员工人数',
    industryType: '行业类型',
    website: '公司网站',
    statusLabel: '状态',
    createdAt: '创建时间',
    updatedAt: '更新时间',
    userId: '用户ID',
    registeredAddress: '注册地址',
    actualOperationAddress: '实际经营地址',
    exportType: '出口类型',
    exportCountries: '出口国家',
    exportProductName: '出口产品名称',
    historicalAnnualExport: '历史年度出口额',
    estimatedAnnualExport: '预计年度出口额',

    // 附件
    idPhoto: '身份证照片',
    businessRegistration: '营业执照',
    companyRegistration: '公司注册证明',
    officeScene: '办公场景照片',

    // 性别
    male: '男',
    female: '女',
    other: '其他',

    // 证件类型
    idCard: '身份证',
    passport: '护照',
    driverLicense: '驾驶证',
    militaryID: '军官证',
    otherIdType: '其他',

    // 状态
    statusPendingSubmit: '待提交资料',
    statusPendingReview: '待审核',
    statusPendingBinnerReview: '待binner审核',
    statusApproved: '审核通过',
    statusRejected: '审核失败',

    // 国家
    countries: {
      CN: '中国',
      US: '美国',
      GB: '英国',
      JP: '日本',
      KR: '韩国',
      FR: '法国',
      DE: '德国',
      CA: '加拿大',
      AU: '澳大利亚',
      SG: '新加坡',
      HK: '香港',
      TW: '台湾',
      MO: '澳门',
      CL: '智利'
    },

    // 通用文本
    noData: '暂无数据',
    noPhoto: '暂无照片',
    unknown: '未知',
    noEmiInfo: '当前登录账号暂无EMI信息',

    // 错误信息
    fetchPersonalError: '获取个人账户信息失败',
    fetchCompanyError: '获取企业账户信息失败',
    fetchShopError: '获取商户信息失败',
    unknownError: '未知错误'
  },

  // 付款页面
  pay: {
    // 标签页
    paymentRecordTab: '付款记录',
    payeeListTab: '收款方列表',

    // 筛选器 - 付款记录
    startDate: '开始日期',
    endDate: '结束日期',
    search: '搜索',

    // 筛选器 - 收款方列表
    accountLabel: '账号',
    accountPlaceholder: '请输入账号',
    statusLabel: '状态',
    statusPlaceholder: '请选择',
    accountTypeLabel: '账户类型',
    accountTypePlaceholder: '请选择',

    // 状态选项
    all: '全部',
    enabled: '启用',
    disabled: '停用',

    // 账户类型
    personal: '个人',
    company: '企业',

    // 表格列 - 付款记录
    payTime: '付款时间',
    withdrawalNumber: '提现编号',
    beneficiaryBank: '收款银行',
    payee: '收款人',
    paymentAccountNumber: '收款账号',
    paymentAmount: '付款金额',
    amountReceived: '到账金额',

    // 表格列 - 收款方列表
    account: '账号',
    bankOfDeposit: '开户银行',
    accountName: '开户名',
    accountType: '账户类型',
    status: '状态',
    use: '用途',

    // 提示信息
    fetchRecordError: '获取付款记录失败，请稍后重试',
    fetchPayeeError: '获取收款方列表失败，请稍后重试',
    exportPaymentRecord: '导出付款记录',
    createPayment: '打开创建付款单对话框',
    viewPaymentDetail: '查看付款详情：',
    cancelPaymentConfirm: '确认取消付款 "{number}" 吗？',
    warning: '提示',
    confirm: '确定',
    cancel: '取消',
    paymentCancelled: '付款已取消',
    addPayee: '打开添加收款人对话框',
    editPayee: '编辑收款人：',
    viewPayeeDetail: '查看收款人详情：',
    deletePayeeConfirm: '确认删除收款人 "{name}" 吗？',
    deleteSuccess: '删除成功'
  }
}
