export default {
  // 页面标题和按钮
  logout: 'Logout',
  submitReview: 'Submit for Review',
  reupload: 'Re-upload',
  addBeneficiary: 'Add Beneficiary',
  viewCurrentFile: 'View Current File',

  // 状态提示信息
  fillMerchantInfo: 'Please fill in the merchant registration information according to the following content',
  merchantStatusReviewing: 'Merchant Status Under Review',
  reviewPending: 'Your information is under review, please wait patiently',
  merchantStatusRejected: 'Merchant Status Review Failed',
  modifyAndResubmit: 'Please modify the following information and resubmit for review',
  merchantDisabled: 'Merchant Account Disabled',
  accountDisabled: 'Your account has been disabled, please contact administrator',

  // 表单标签
  merchantId: 'Merchant ID',
  privateKey: 'Private Key',
  enterPrivateKey: 'Please enter private key',
  company: 'Company',
  enterCompanyName: 'Please enter company name',
  phoneNumber: 'Phone Number',
  enterPhoneNumber: 'Please enter phone number',
  businessNumber: 'Business Number',
  enterRegistrationNumber: 'Please enter company registration number',
  registrationCountry: 'Company Registration Country',
  selectCountry: 'Please select country',
  vatNumber: 'VAT Number',
  enterVatNumber: 'Please enter VAT number',
  companyEstablishDate: 'Company Establishment Date',
  selectEstablishDate: 'Please select company establishment date',
  status: 'Status',
  failureReason: 'Failure Reason',
  noFailureReason: 'No failure reason',

  // 公司认证信息
  companyAuthInfo: 'Company Authentication Information',
  companyAuthType: 'Company Authentication Type',
  selectCompanyAuthType: 'Please select company authentication type',
  companyAuthTypeFile: 'Company Authentication Type File',
  companyCharterDoc: 'Company Articles of Association',
  companyOrgChart: 'Company Organization Chart',

  // 公司董事信息
  directorInfo: 'Company Director Information',
  directorFullName: 'Director Full Name',
  enterDirectorName: 'Please enter director full name',
  directorEmail: 'Director Email',
  enterDirectorEmail: 'Please enter director email',

  // 最终受益人信息
  ultimateBeneficiaryInfo: 'Ultimate Beneficial Owner Information',
  beneficiaryNotice: 'Note',
  beneficiaryNoticeDesc:
    'Please fill in the information of ultimate beneficial owners who hold more than 25% equity or more than 25% voting rights in the company',
  beneficiary: 'Beneficiary',
  beneficiaryEmail: 'Beneficiary Email',
  enterBeneficiaryEmail: 'Please enter ultimate beneficiary email',

  // 受益人身份认证信息
  beneficiaryIdAuth: 'Beneficiary Identity Authentication Information',
  idProofType: 'Identity Proof Type',
  selectIdProofType: 'Please select identity proof type',
  passport: 'Passport',
  idDocumentFront: 'Identity Document (Front)',

  // 受益人地址认证信息
  beneficiaryAddressAuth: 'Beneficiary Address Authentication Information',
  addressProofType: 'Address Proof Type',
  selectAddressProofType: 'Please select address proof type',
  addressProofDocument: 'Address Proof Document',

  // 文件上传提示
  supportedFormats: 'Supports doc, docx, pdf formats, no more than 20MB',
  imageFormats: 'Supports jpg, jpeg, png formats, no more than 20MB',

  // 验证信息
  companyNameRequired: 'Company name cannot be empty',
  phoneNumberRequired: 'Phone number cannot be empty',
  privateKeyRequired: 'Private key cannot be empty',
  establishDateRequired: 'Company establishment date cannot be empty',
  countryRequired: 'Company registration country cannot be empty',
  countryCodeLength: 'Country code length must be 2 characters',
  vatNumberRequired: 'VAT number cannot be empty',
  registrationNumberRequired: 'Company registration number cannot be empty',
  companyAuthTypeRequired: 'Please select company authentication type',
  idProofTypeRequired: 'Please select identity proof type',
  addressProofTypeRequired: 'Please select address proof type',
  directorNameRequired: 'Director name cannot be empty',
  directorNameMinLength: 'Length cannot be less than 2 characters',
  directorEmailRequired: 'Director email cannot be empty',
  beneficiaryEmailRequired: 'Beneficiary email cannot be empty',
  emailFormatError: 'Please enter correct email format',
  uploadCompanyAuthFile: 'Please upload company authentication type file',
  uploadCompanyDoc: 'Please upload company articles of association',
  uploadOrgChart: 'Please upload company organization chart',
  uploadIdDocument: 'Please upload identity document',
  uploadAddressProof: 'Please upload address proof document',
  addAtLeastOneBeneficiary: 'Please add at least one beneficiary',

  // 操作确认
  confirmLogout: 'Are you sure to logout and exit the system?',
  confirmDeleteBeneficiary: 'Are you sure to delete this beneficiary information?',
  hint: 'Hint',
  confirm: 'Confirm',
  cancel: 'Cancel',
  deleteSuccess: 'Delete successful',

  // 文件操作
  fileAddressNotExist: 'File address does not exist',
  fileSizeExceeded: 'File size cannot exceed 20MB!',
  fileSizeExceeded1M: 'File size cannot exceed 1MB!',
  fileFormatError: 'File format is incorrect! Only supports {formats} formats',
  fileRemoved: 'File removed',
  fileUploadSuccess: 'File upload successful',
  fileUploadFailed: 'File upload failed',

  // 提交验证
  submitSuccess: 'Submit successful',
  addAtLeastOneBeneficiaryWarning: 'Please add at least one beneficiary',
  uploadCompanyDocWarning: 'Please upload company articles of association',
  uploadCompanyAuthFileWarning: 'Please upload company authentication type file',
  uploadOrgChartWarning: 'Please upload company organization chart',
  countryCodeWarning: 'Country code length must be 2 characters',

  // 文件名称
  companyAuthTypeFileName: 'Company Authentication Type File',
  companyCharterDocFileName: 'Company Articles of Association',
  companyOrgChartFileName: 'Company Organization Chart',
  idDocumentFrontName: 'Identity Document (Front)',
  idDocumentBackName: 'Identity Document (Back)',
  addressProofDocumentName: 'Address Proof Document',

  // 错误信息
  getCountryListFailed: 'Failed to get country list',
  emailFormatIncorrect: 'Please enter correct email format',
  uploadFileRequired: 'Please upload file',

  // 系统消息
  countryValidationError: 'Country code can only contain letters and must be 2 characters long',

  // 企业账户新增字段
  businessRegistrationNumber: 'Business Registration Number',
  enterBusinessRegistrationNumber: 'Generally 18/15 digits or letter combination',
  companyRegistrationCode: 'Company Registration Code',
  enterCompanyRegistrationCode: 'Please enter company registration code',
  companyRegistrationCountry: 'Company Registration Country',
  selectRegistrationCountry: 'Please select registration country',
  companyRegistrationAddress: 'Company Registration Address',
  enterProvince: 'Please enter province',
  enterCity: 'Please enter city',
  enterDetailAddress: 'Please enter detailed district, street and house number address',
  actualBusinessAddress: 'Actual Business Address',
  sameAsRegistrationAddress: 'Same as registration address',
  companySize: 'Company Size',
  selectCompanySize: 'Please select company size',
  companySize_1_10: '1-10 employees',
  companySize_11_50: '11-50 employees',
  companySize_51_100: '51-100 employees',
  companySize_101_500: '101-500 employees',
  companySize_500plus: '500+ employees',
  companyExpiryDate: 'Company Expiry Date',
  selectExpiryDate: 'Please select date',
  longTermValid: 'Long-term Valid',

  // 验证信息
  businessRegistrationNumberRequired: 'Please enter business registration number',
  companyRegistrationCodeRequired: 'Please enter company registration code',
  registrationAddressRequired: 'Please enter company registration address',

  // 新增企业字段
  industryType: 'Industry Type',
  selectIndustryType: 'Please select',
  industrySubType: 'Industry Sub Type',
  exportType: 'Export Type',
  selectExportType: 'Please select export type (multiple)',
  exportCountries: 'Export Countries and Regions',
  selectExportCountries: 'Please select export countries and regions (multiple)',
  exportProductName: 'Export Product Name',
  enterExportProductName: 'Please enter main export product name, use full name, no more than 100 characters',
  historicalExportAmount: 'Historical Annual Export Amount',
  enterHistoricalExportAmount: 'Please enter amount in RMB, integer only',
  estimatedExportAmount: 'Estimated Annual Export Amount',
  enterEstimatedExportAmount: 'Please enter amount in RMB, integer only',
  companyWebsite: 'Company Website (Optional)',
  enterCompanyWebsite: 'Please enter website or platform store address, let us understand your products',
  amountUnit: '10,000 USD',

  // 验证信息
  industryTypeRequired: 'Please select industry type',
  exportTypeRequired: 'Please select export type',
  exportCountriesRequired: 'Please select export countries and regions',
  exportProductNameRequired: 'Please enter export product name',
  exportProductNameMaxLength: 'No more than 100 characters',
  historicalExportAmountRequired: 'Please enter historical annual export amount',
  estimatedExportAmountRequired: 'Please enter estimated annual export amount'
}
