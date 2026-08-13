export default {
  // 页面标题和按钮
  logout: '退出登录',
  submit: '提交',
  submitting: '提交中...',

  // 状态标签
  reviewStatus: '审核状态：',
  statusPendingSubmit: '待提交资料',
  statusPendingReview: '待审核',
  statusPendingDoubleCheck: '待复核',
  statusApproved: '审核通过',
  statusRejected: '审核拒绝',

  // 账户类型
  accountType: '账户类型',
  personalAccount: '个人账户',
  companyAccount: '企业账户',
  cannotSwitchAccountType: '已有提交记录，不能切换账户类型',

  // 个人账户 - 基础信息
  nationality: '国籍',
  selectNationality: '请选择国籍',
  emailAccount: '邮箱账号',
  enterEmail: '请输入邮箱账号',
  fullName: '姓名',
  enterFullName: '请输入姓名',
  idNumber: '证件号',
  enterIdNumber: '请填写真实证件号',
  gender: '性别',
  birthDate: '出生日期',
  selectBirthDate: '请选择出生日期',

  // 个人账户 - 居住信息
  residenceCountry: '居住国家',
  selectResidenceCountry: '请选择居住国家',
  residenceAddress: '居住地址',
  enterResidenceAddress: '请输入详细区县街道门牌号地址',
  residencePostalCode: '居住邮编',
  enterResidencePostalCode: '请填写邮政地址编码',
  contactPhone: '联系电话',
  enterContactPhone: '联系电话',
  fundSource: '资金来源',
  enterFundSource: '请输入资金来源',

  // 证件信息
  idType: '证件类型',
  selectIdType: '请选择证件类型',
  idPhoto: '证件照片',
  uploadIdPhoto: '请上传证件照片',

  // 企业账户 - 法人信息
  legalPersonName: '法人姓名',
  enterLegalPersonName: '请输入法人姓名',

  // 企业账户 - 企业基础信息
  companyNameChinese: '企业中文名称',
  enterCompanyNameChinese: '请输入企业中文名称',
  companyNameEnglish: '企业英文名称',
  enterCompanyNameEnglish: '请输入企业英文名称',
  businessRegistrationNumber: '商业登记证编号',
  enterBusinessRegistrationNumber: '一般为18/15位数字或者字母组合',
  companyRegistrationCode: '公司注册代码',
  enterCompanyRegistrationCode: '请输入公司注册代码',

  // 企业账户 - 注册信息
  registeredCountry: '企业注册国家',
  selectRegisteredCountry: '请选择注册国家',
  registeredAddress: '企业注册地址',
  enterRegisteredAddress: '请输入企业注册地址',
  actualOperationAddress: '实际经营地址',
  enterActualOperationAddress: '请输入实际经营地址',

  // 企业账户 - 企业详情
  employeeCount: '企业人数',
  enterEmployeeCount: '请输入企业人数',
  establishmentDate: '企业成立日期',
  selectEstablishmentDate: '请选择日期',
  expirationDate: '企业到期日期',
  selectExpirationDate: '请选择日期',
  longTermValid: '长期有效',

  // 企业账户 - 业务信息
  industryType: '行业类型',
  enterIndustryType: '请输入行业类型',
  exportType: '出口类型',
  enterExportType: '请输入出口类型',
  exportCountries: '出口国家',
  enterExportCountries: '请输入出口国家和地区',
  exportProductName: '出口商品名称',
  enterExportProductName: '请输入主要出口产品名称，使用全称信息，不超过100个字符',
  historicalAnnualExport: '历史年出口额',
  enterHistoricalAnnualExport: '请输入金额，只填整数',
  estimatedAnnualExport: '预估年出口额',
  enterEstimatedAnnualExport: '请输入金额，只填整数',
  amountUnit: '万美元',
  website: '企业网站',
  enterWebsite: '请输入企业网站（选填）',

  // 企业账户 - 证书文件
  businessRegistration: '商业登记证书：',
  companyRegistration: '公司注册证书：',
  incorporation: '法团成立表格',
  officeScene: '企业办公场景：',

  // 表单验证消息
  emailRequired: '请输入邮箱',
  emailFormatError: '请输入正确的邮箱格式',
  fullNameRequired: '请输入姓名',
  idNumberRequired: '请输入证件号',
  idNumberFormatError: '证件号只能包含字母或数字',
  nationalityRequired: '请选择国籍',
  residenceCountryRequired: '请选择居住国家',
  residenceAddressRequired: '请输入居住地址',
  residencePostalCodeRequired: '请输入居住邮编',
  genderRequired: '请选择性别',
  birthDateRequired: '请选择出生日期',
  contactPhoneRequired: '请输入联系电话',
  fundSourceRequired: '请输入资金来源',
  idTypeRequired: '请选择证件类型',
  idPhotoRequired: '请上传证件照片',

  // 企业表单验证消息
  companyNameChineseRequired: '请输入企业中文名称',
  companyNameEnglishRequired: '请输入企业英文名称',
  businessRegistrationNumberRequired: '请输入商业登记证号',
  companyRegistrationCodeRequired: '请输入公司注册代码',
  registeredCountryRequired: '请选择企业注册国家',
  registeredAddressRequired: '请输入企业注册地址',
  actualOperationAddressRequired: '请输入实际经营地址',
  employeeCountRequired: '请输入企业人数',
  employeeCountFormatError: '请输入有效的数字',
  establishmentDateRequired: '请选择企业成立日期',
  industryTypeRequired: '请输入行业类型',
  exportTypeRequired: '请输入出口类型',
  exportCountriesRequired: '请输入出口国家和地区',
  exportProductNameRequired: '请输入出口商品名称',
  exportProductNameMaxLength: '不超过100个字符',
  historicalAnnualExportRequired: '请输入历史年出口额',
  historicalAnnualExportFormatError: '请输入有效的数字（最多两位小数）',
  estimatedAnnualExportRequired: '请输入预估年出口额',
  estimatedAnnualExportFormatError: '请输入有效的数字（最多两位小数）',

  // 文件上传
  uploadSuccess: '上传成功',
  uploadFailed: '上传失败',
  onlyImageAllowed: '只能上传图片文件!',
  imageSizeLimit: '图片大小不能超过 5MB!',

  // 操作确认
  confirmSubmitPersonal: '确认提交个人账户注册资料？',
  confirmSubmitCompany: '确认提交企业账户注册资料？',
  confirmLogout: '确定注销并退出系统吗？',
  hint: '提示',
  confirm: '确定',
  cancel: '取消',

  // 提交结果
  personalSubmitSuccess: '个人账户资料提交成功',
  companySubmitSuccess: '企业账户资料提交成功',
  submitFailed: '提交失败：',
  pleaseRetryLater: '请稍后重试',
  pleaseCompleteForm: '请完善表单信息',

  // 其他消息
  getCountryListFailed: '获取国家列表失败',
  getUserInfoFailed: '获取用户信息失败：',
  accountTypeRequired: '请选择账户类型'
}
