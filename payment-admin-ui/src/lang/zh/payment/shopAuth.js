export default {
  // 页面标题和按钮
  logout: '退出登录',
  submitReview: '提交审核',
  reupload: '重新上传',
  addBeneficiary: '新增受益人',
  viewCurrentFile: '查看当前文件',

  // 状态提示信息
  fillMerchantInfo: '请按照以下内容填写商户入住信息',
  merchantStatusReviewing: '商户状态审核中',
  reviewPending: '您的资料正在审核中，请耐心等待',
  merchantStatusRejected: '商户状态未通过审核',
  modifyAndResubmit: '请修改以下信息并重新提交审核',
  merchantDisabled: '商户已被禁用',
  accountDisabled: '您的账户已被禁用,请联系管理员',

  // 表单标签
  merchantId: '商户号',
  privateKey: '私钥',
  enterPrivateKey: '请输入私钥',
  company: '公司',
  enterCompanyName: '请输入公司名称',
  phoneNumber: '手机号',
  enterPhoneNumber: '请输入手机号',
  businessNumber: '企业编号',
  enterRegistrationNumber: '请输入公司注册号',
  registrationCountry: '公司注册国家',
  selectCountry: '请选择国家',
  vatNumber: '增值税编号',
  enterVatNumber: '请输入增值税编号',
  companyEstablishDate: '公司成立日期',
  selectEstablishDate: '请选择公司成立日期',
  status: '状态',
  failureReason: '失败原因',
  noFailureReason: '暂无失败原因',

  // 公司认证信息
  companyAuthInfo: '公司认证信息',
  companyAuthType: '公司认证类型',
  selectCompanyAuthType: '请选择公司认证类型',
  companyAuthTypeFile: '公司认证类型文件',
  companyCharterDoc: '公司组织大纲和章程',
  companyOrgChart: '公司组织结构图',

  // 公司董事信息
  directorInfo: '公司董事信息',
  directorFullName: '董事完整姓名',
  enterDirectorName: '请输入董事完整姓名',
  directorEmail: '董事电子邮件',
  enterDirectorEmail: '请输入董事电子邮件',

  // 最终受益人信息
  ultimateBeneficiaryInfo: '最终受益人信息',
  beneficiaryNotice: '注意',
  beneficiaryNoticeDesc: '请填写持有超过25%股权或25%以上表决权的公司最终受益人信息',
  beneficiary: '受益人',
  beneficiaryEmail: '受益人电子邮件',
  enterBeneficiaryEmail: '请输入最终受益人电子邮件',

  // 受益人身份认证信息
  beneficiaryIdAuth: '受益人身份认证信息',
  idProofType: '身份证明类型',
  selectIdProofType: '请选择身份证明类型',
  passport: '护照',
  idDocumentFront: '身份证明文件(正面)',

  // 受益人地址认证信息
  beneficiaryAddressAuth: '受益人地址认证信息',
  addressProofType: '地址证明类型',
  selectAddressProofType: '请选择地址证明类型',
  addressProofDocument: '地址证明文件',

  // 文件上传提示
  supportedFormats: '支持 doc, docx, pdf 格式，不超过20MB',
  imageFormats: '支持 jpg, jpeg, png 格式，不超过20MB',

  // 验证信息
  companyNameRequired: '公司名称不能为空',
  phoneNumberRequired: '手机号不能为空',
  privateKeyRequired: '私钥不能为空',
  establishDateRequired: '公司成立日期不能为空',
  countryRequired: '公司注册国家代码不能为空',
  countryCodeLength: '国家代码长度必须为2个字符',
  vatNumberRequired: '增值税编号不能为空',
  registrationNumberRequired: '公司注册号不能为空',
  companyAuthTypeRequired: '请选择公司认证类型',
  idProofTypeRequired: '请选择身份证明类型',
  addressProofTypeRequired: '请选择地址证明类型',
  directorNameRequired: '董事姓名不能为空',
  directorNameMinLength: '长度不能小于2个字符',
  directorEmailRequired: '董事电子邮件不能为空',
  beneficiaryEmailRequired: '受益人电子邮件不能为空',
  emailFormatError: '请输入正确的电子邮件格式',
  uploadCompanyAuthFile: '请上传公司认证类型文件',
  uploadCompanyDoc: '请上传公司组织大纲和章程',
  uploadOrgChart: '请上传公司组织结构图',
  uploadIdDocument: '请上传身份证明文件',
  uploadAddressProof: '请上传地址证明文件',
  addAtLeastOneBeneficiary: '请添加至少一个受益人',

  // 操作确认
  confirmLogout: '确定注销并退出系统吗？',
  confirmDeleteBeneficiary: '确认删除该受益人信息吗？',
  hint: '提示',
  confirm: '确定',
  cancel: '取消',
  deleteSuccess: '删除成功',

  // 文件操作
  fileAddressNotExist: '文件地址不存在',
  fileSizeExceeded: '文件大小不能超过 20MB!',
  fileSizeExceeded1M: '文件大小不能超过 1MB!',
  fileFormatError: '文件格式不正确！只支持 {formats} 格式',
  fileRemoved: '文件已移除',
  fileUploadSuccess: '文件上传成功',
  fileUploadFailed: '文件上传失败',

  // 提交验证
  submitSuccess: '提交成功',
  addAtLeastOneBeneficiaryWarning: '请添加至少一个受益人',
  uploadCompanyDocWarning: '请上传公司组织大纲和章程',
  uploadCompanyAuthFileWarning: '请上传公司认证类型文件',
  uploadOrgChartWarning: '请上传公司组织结构图',
  countryCodeWarning: '国家代码长度必须为2个字符',

  // 文件名称
  companyAuthTypeFileName: '公司认证类型文件',
  companyCharterDocFileName: '公司组织大纲和章程',
  companyOrgChartFileName: '公司组织结构图',
  idDocumentFrontName: '身份证明文件(正面)',
  idDocumentBackName: '身份证明文件(背面)',
  addressProofDocumentName: '地址证明文件',

  // 错误信息
  getCountryListFailed: '获取国家列表失败',
  emailFormatIncorrect: '请输入正确的电子邮件格式',
  uploadFileRequired: '请上传文件',

  // 系统消息
  countryValidationError: '国家代码长度必须为2个字符',

  // 企业账户新增字段
  businessRegistrationNumber: '商业登记证号',
  enterBusinessRegistrationNumber: '一般为18/15位数字或者字母组合',
  companyRegistrationCode: '公司注册代码',
  enterCompanyRegistrationCode: '请输入公司注册代码',
  companyRegistrationCountry: '企业注册国家',
  selectRegistrationCountry: '请选择注册国家',
  companyRegistrationAddress: '企业注册地址',
  enterProvince: '请输入省',
  enterCity: '请输入市',
  enterDetailAddress: '请输入详细区县街道门牌号地址',
  actualBusinessAddress: '实际经营地址',
  sameAsRegistrationAddress: '与注册地址一致',
  companySize: '企业人数',
  selectCompanySize: '请选择企业人数',
  companySize_1_10: '1-10人',
  companySize_11_50: '11-50人',
  companySize_51_100: '51-100人',
  companySize_101_500: '101-500人',
  companySize_500plus: '500人以上',
  companyExpiryDate: '企业到期日期',
  selectExpiryDate: '请选择日期',
  longTermValid: '长期有效',

  // 验证信息
  businessRegistrationNumberRequired: '请输入商业登记证号',
  companyRegistrationCodeRequired: '请输入公司注册代码',
  registrationAddressRequired: '请输入企业注册地址',

  // 新增企业字段
  industryType: '行业类型',
  selectIndustryType: '请选择',
  industrySubType: '行业子类型',
  exportType: '出口类型',
  selectExportType: '请选择出口类型（多选）',
  exportCountries: '出口国家和地区',
  selectExportCountries: '请选择出口国家和地区（多选）',
  exportProductName: '出口商品名称',
  enterExportProductName: '请输入主要出口产品名称，使用全称信息，不超过100个字符',
  historicalExportAmount: '历史年出口额',
  enterHistoricalExportAmount: '请输入人民币金额，只填整数',
  estimatedExportAmount: '预估年出口额',
  enterEstimatedExportAmount: '请输入人民币金额，只填整数',
  companyWebsite: '企业网站(选填)',
  enterCompanyWebsite: '请输入企业网站或平台店铺地址，让我们了解您的产品',
  amountUnit: '万美元',

  // 验证信息
  industryTypeRequired: '请选择行业类型',
  exportTypeRequired: '请选择出口类型',
  exportCountriesRequired: '请选择出口国家和地区',
  exportProductNameRequired: '请输入出口商品名称',
  exportProductNameMaxLength: '不超过100个字符',
  historicalExportAmountRequired: '请输入历史年出口额',
  estimatedExportAmountRequired: '请输入预估年出口额'
}
