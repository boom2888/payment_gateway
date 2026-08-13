export default {
  // 页面标题
  title: 'Thunes 转账',

  // 步骤
  step1: '获取收款方字段',
  step2: '填写转账信息',
  step3: '确认转账',

  // 第一步：获取收款方
  countryCode: '国家代码',
  currency: '货币代码',
  queryPayers: '查询收款方',
  reset: '重置',
  payerList: '收款方列表',
  payerId: 'ID',
  payerName: '名称',
  amountRange: '金额范围',
  supportedTransactionTypes: '支持交易类型',
  operation: '操作',
  select: '选择',
  noPayersFound: '未找到符合条件的收款方',
  payersFound: '找到 {count} 个收款方',
  payerSelected: '已选择收款方: {name}',

  // 第二步：填写转账信息
  basicInfo: '基础信息',
  merchantId: '商户ID',
  externalId: '外部订单号',
  transactionType: '交易类型',
  fieldCombination: '字段组合',
  selectFieldCombination: '请选择您要填写的字段组合',
  requiredFieldCombination: '必填字段组合',
  combinationFormat: '组合{index}: 发送方({sending}) | 收款方({receiving})',
  amount: '转账金额',
  transferCurrency: '转账货币',
  targetCountry: '目标国家',
  targetCurrency: '目标货币',
  purposeOfRemittance: '汇款目的',
  payerOnlySupports: '该收款方仅支持以上汇款目的',
  callbackUrl: '回调URL',

  // 发送方信息
  senderInfo: '发送方信息',
  sendingBusinessInfo: '发送方企业信息',
  firstName: '名字',
  lastName: '姓氏',
  address: '地址',
  city: '城市',
  postalCode: '邮编',
  provinceState: '省/州',
  countryIsoCode: '国家代码',
  email: '邮箱',
  msisdn: '手机号',
  registeredName: '注册名称',
  registrationNumber: '注册号',
  code: '代码',

  // 收款方信息
  beneficiaryInfo: '收款方信息',
  receivingBusinessInfo: '收款方企业信息',
  bankAccountNumber: '银行账号',
  abaRoutingNumber: 'ABA路由号',
  accountType: '账户类型',
  accountTypeChecking: '支票账户 (CHECKING)',
  accountTypeSavings: '储蓄账户 (SAVINGS)',
  iban: 'IBAN',
  autoFilled: '自动填充',

  // 字段描述（用于placeholder）
  desc: {
    // 发送方个人信息
    'sender.firstName': '发送方的名字',
    'sender.lastName': '发送方的姓氏',
    'sender.middleName': '发送方的中间名（可选）',
    'sender.address': '发送方的地址',
    'sender.city': '发送方所在城市',
    'sender.postalCode': '发送方的邮政编码',
    'sender.provinceState': '发送方所在省/州',
    'sender.countryIsoCode': '发送方地址所在国家（ISO 3166-1 alpha-3格式）',
    'sender.email': '发送方的电子邮箱地址',
    'sender.msisdn': '发送方的手机号码（国际格式）',
    // 发送方企业信息
    'sendingBusiness.registeredName': '企业注册名称',
    'sendingBusiness.address': '企业注册地址',
    'sendingBusiness.city': '企业所在城市',
    'sendingBusiness.postalCode': '企业邮政编码',
    'sendingBusiness.provinceState': '企业所在省/州',
    'sendingBusiness.countryIsoCode': '企业所在国家（ISO 3166-1 alpha-3格式）',
    'sendingBusiness.registrationNumber': '企业注册号',
    'sendingBusiness.code': '企业识别代码',
    'sendingBusiness.email': '企业联系邮箱',
    // 收款方个人信息
    'beneficiary.firstName': '收款方的名字',
    'beneficiary.lastName': '收款方的姓氏',
    'beneficiary.address': '收款方的地址',
    'beneficiary.city': '收款方所在城市',
    'beneficiary.postalCode': '收款方的邮政编码',
    'beneficiary.provinceState': '收款方所在省/州',
    'beneficiary.countryIsoCode': '收款方地址所在国家（ISO 3166-1 alpha-3格式）',
    'beneficiary.bankAccountNumber': '收款方的银行账号',
    'beneficiary.abaRoutingNumber': '美国银行ABA路由号（9位数字）',
    'beneficiary.accountType': '银行账户类型（支票或储蓄账户）',
    'beneficiary.iban': '国际银行账号（IBAN）',
    'beneficiary.msisdn': '收款方的手机号码（国际格式）',
    // 收款方企业信息
    'receivingBusiness.registeredName': '收款企业注册名称',
    'receivingBusiness.address': '收款企业注册地址',
    'receivingBusiness.city': '收款企业所在城市',
    'receivingBusiness.postalCode': '收款企业邮政编码',
    'receivingBusiness.provinceState': '收款企业所在省/州',
    'receivingBusiness.countryIsoCode': '收款企业所在国家（ISO 3166-1 alpha-3格式）',
    'receivingBusiness.registrationNumber': '收款企业注册号',
    'receivingBusiness.bankAccountNumber': '收款企业的银行账号',
    'receivingBusiness.abaRoutingNumber': '美国银行ABA路由号（9位数字）',
    'receivingBusiness.accountType': '银行账户类型（支票或储蓄账户）'
  },

  status: '状态',

  // 第三步：确认转账
  confirmTransfer: '转账信息确认',
  merchantName: '商户名称',

  // 按钮
  previousStep: '上一步',
  nextStep: '下一步',
  confirmAndSubmit: '确认转账',

  // 转账结果
  transferResult: '转账结果',
  transferSuccess: '转账成功',
  transferFailed: '转账失败',
  transferRequestSubmitted: '转账请求已提交成功',
  transferId: '转账ID',
  thunesTransactionId: 'Thunes交易ID',
  exchangeRate: '汇率',
  fee: '手续费',
  targetAmount: '目标金额',
  close: '关闭',
  continueTransfer: '继续转账',
  viewTransferRecords: '查看转账记录',

  // 验证消息
  validation: {
    selectMerchant: '请选择商户',
    enterExternalId: '请输入外部订单号',
    enterAmount: '请输入转账金额',
    amountMustBeNumber: '转账金额必须为数字',
    amountTooSmall: '转账金额不能小于 {min}',
    amountTooLarge: '转账金额不能大于 {max}',
    selectCurrency: '请选择转账货币',
    enterTargetCountry: '请输入目标国家代码',
    selectTargetCurrency: '请选择目标货币代码',
    selectTransactionType: '请选择交易类型',
    selectPurpose: '请选择汇款目的',
    enterField: '请输入{field}',
    completeTransferInfo: '请填写完整的转账信息'
  },

  // 汇款目的
  purposes: {
    COMPUTER_SERVICES: '计算机服务',
    FAMILY_SUPPORT: '家庭支持',
    EDUCATION: '教育',
    GIFT_AND_DONATION: '礼物和捐赠',
    MEDICAL_TREATMENT: '医疗',
    MAINTENANCE_EXPENSES: '维护费用',
    TRAVEL: '旅行',
    SMALL_VALUE_REMITTANCE: '小额汇款',
    LIBERALIZED_REMITTANCE: '自由汇款',
    CONSTRUCTION_EXPENSES: '建筑费用',
    HOTEL_ACCOMMODATION: '酒店住宿',
    ADVERTISING_EXPENSES: '广告费用',
    ADVISORY_FEES: '咨询服务费',
    BUSINESS_INSURANCE: '商业保险',
    INSURANCE_CLAIMS: '保险理赔',
    DELIVERY_FEES: '配送费',
    EXPORTED_GOODS: '出口商品付款',
    SERVICE_CHARGES: '服务费',
    LOAN_PAYMENT: '贷款还款',
    OFFICE_EXPENSES: '办公费用',
    PROPERTY_PURCHASE: '房产购买',
    PROPERTY_RENTAL: '房产租赁',
    ROYALTY_FEES: '版权费',
    SHARES_INVESTMENT: '股票投资',
    FUND_INVESTMENT: '基金投资',
    TAX_PAYMENT: '税款支付',
    TRANSPORTATION_FEES: '运输费',
    UTILITY_BILLS: '水电费',
    PERSONAL_TRANSFER: '个人转账',
    SALARY_PAYMENT: '工资支付',
    REWARD_PAYMENT: '奖励支付',
    INFLUENCER_PAYMENT: '网红支付',
    OTHER_FEES: '其他费用',
    OTHER: '其他'
  },

  // 提示
  tips: {
    iso3166Alpha3: 'ISO 3166-1 alpha-3格式（3位字母）',
    msisdn: '国际格式，例如：33712345678',
    abaRoutingNumber: '美国银行ABA路由号（9位数字）',
    iban: '国际银行账号（IBAN）'
  }
}
