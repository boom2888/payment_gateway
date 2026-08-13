export default {
  // Query form
  name: 'Name',
  namePlaceholder: 'Please enter merchant name',
  status: 'Document Status',
  statusPlaceholder: 'Please select document status',
  enabledStatus: 'Enabled Status',
  enabledStatusPlaceholder: 'Please select enabled status',
  merchantType: 'Merchant Type',
  merchantTypePlaceholder: 'Please select merchant type',
  search: 'Search',
  reset: 'Reset',

  // Status options
  statusPendingSubmit: 'Pending Submit',
  statusPendingReview: 'Pending Review',
  statusPendingBinnerReview: 'Pending Binner Review',
  statusApproved: 'Approved',
  statusRejected: 'Rejected',

  // Enabled status
  enabled: 'Enabled',
  disabled: 'Disabled',

  // Merchant type
  personal: 'Personal',
  company: 'Company',

  // Table columns
  id: 'ID',
  account: 'Account',
  createdAt: 'Created At',
  operation: 'Operation',

  // Action buttons
  add: 'Add',
  delete: 'Delete',
  export: 'Export',
  detail: 'Detail',

  // Prompt messages
  deleteConfirm: 'Are you sure to delete EMI merchant with ID "{ids}"?',
  warning: 'Warning',
  confirm: 'Confirm',
  cancel: 'Cancel',
  deleteSuccess: 'Deleted successfully',
  deleteError: 'Delete failed',
  exportConfirm: 'Are you sure to export all EMI merchant data?',
  exportSuccess: 'Exported successfully',
  addSuccess: 'Added successfully',
  updateSuccess: 'Updated successfully',

  // Status change
  statusChangeConfirm: 'Are you sure to "{status}" merchant "{name}"?',
  enableSuccess: 'Enabled successfully',
  enableError: 'Enable failed',
  disableSuccess: 'Disabled successfully',
  disableError: 'Disable failed',

  // Dialog title
  addTitle: 'Add EMI Merchant',
  updateTitle: 'Update EMI Merchant',

  // Receive page
  receive: {
    // Tabs
    payerListTab: 'Payer List',
    orderManageTab: 'Order Management',

    // Filters
    all: 'All',
    startDate: 'Start Date',
    endDate: 'End Date',
    addPayer: 'Add Payer',

    // Status
    completed: 'Completed',
    partialPayment: 'Partial Payment',
    pendingPayment: 'Pending Payment',

    // Table columns - Payer List
    payerName: 'Payer Name',
    paymentAccountName: 'Payment Account Name',
    remitArea: 'Remittance Area',
    paymentAccount: 'Payment Account',
    paymentBank: 'Payment Bank',
    use: 'Purpose',

    // Table columns - Order Management
    orderTime: 'Order Time',
    orderNumber: 'Order Number',
    orderAmount: 'Order Amount',
    remainingBalance: 'Remaining Balance',
    nation: 'Country',
    tradeMethod: 'Trade Method',
    buyerName: 'Buyer Name'
  },

  // Info page
  info: {
    // Page title
    personalAccountInfo: 'Personal Account Information',
    companyAccountInfo: 'Company Account Information',

    // Buttons
    audit: 'Audit',
    back: 'Back',

    // Section titles
    basicInfo: 'Basic Information',
    contactInfo: 'Contact Information',
    idPhotos: 'ID Photos',
    companyBasicInfo: 'Company Basic Information',
    addressInfo: 'Address Information',
    exportBusinessInfo: 'Export Business Information',
    contactPersonInfo: 'Contact Person Information',
    attachmentInfo: 'Attachment Information',

    // Common fields
    idNumber: 'ID',
    fullName: 'Full Name',
    gender: 'Gender',
    birthDate: 'Birth Date',
    nationality: 'Nationality',
    residenceCountry: 'Residence Country',
    registrationDate: 'Registration Date',
    email: 'Email',
    fundSource: 'Fund Source',
    bankAccount: 'Bank Account',
    bank: 'Bank',
    contactPhone: 'Contact Phone',
    idType: 'ID Type',
    idNumberField: 'ID Number',
    provinceCity: 'Province/City',
    postalCode: 'Postal Code',
    residenceAddress: 'Residence Address',

    // Company fields
    companyNameChinese: 'Company Name (Chinese)',
    companyNameEnglish: 'Company Name (English)',
    businessRegistrationNumber: 'Business Registration Number',
    companyRegistrationCode: 'Company Registration Code',
    registeredCountry: 'Registered Country',
    establishmentDate: 'Establishment Date',
    expirationDate: 'Certificate Expiration Date',
    employeeCount: 'Employee Count',
    industryType: 'Industry Type',
    website: 'Website',
    statusLabel: 'Status',
    createdAt: 'Created At',
    updatedAt: 'Updated At',
    userId: 'User ID',
    registeredAddress: 'Registered Address',
    actualOperationAddress: 'Actual Operation Address',
    exportType: 'Export Type',
    exportCountries: 'Export Countries',
    exportProductName: 'Export Product Name',
    historicalAnnualExport: 'Historical Annual Export',
    estimatedAnnualExport: 'Estimated Annual Export',

    // Attachments
    idPhoto: 'ID Photo',
    businessRegistration: 'Business License',
    companyRegistration: 'Company Registration Certificate',
    officeScene: 'Office Scene Photo',

    // Gender
    male: 'Male',
    female: 'Female',
    other: 'Other',

    // ID Types
    idCard: 'ID Card',
    passport: 'Passport',
    driverLicense: 'Driver License',
    militaryID: 'Military ID',
    otherIdType: 'Other',

    // Status
    statusPendingSubmit: 'Pending Submit',
    statusPendingReview: 'Pending Review',
    statusPendingBinnerReview: 'Pending Binner Review',
    statusApproved: 'Approved',
    statusRejected: 'Rejected',

    // Countries
    countries: {
      CN: 'China',
      US: 'United States',
      GB: 'United Kingdom',
      JP: 'Japan',
      KR: 'South Korea',
      FR: 'France',
      DE: 'Germany',
      CA: 'Canada',
      AU: 'Australia',
      SG: 'Singapore',
      HK: 'Hong Kong',
      TW: 'Taiwan',
      MO: 'Macau',
      CL: 'Chile'
    },

    // Common texts
    noData: 'No data',
    noPhoto: 'No photo',
    unknown: 'Unknown',

    // Audit dialog
    personalAuditTitle: 'Personal Account Audit',
    companyAuditTitle: 'Company Account Audit',
    auditResult: 'Audit Result',
    auditPass: 'Pass',
    auditReject: 'Reject',
    remark: 'Remark',
    remarkPlaceholder: 'Please enter audit remark (optional)',
    confirmButton: 'Confirm',
    cancelButton: 'Cancel',

    // Messages
    missingParams: 'Missing required parameters',
    invalidAccountType: 'Invalid account type parameter',
    fetchError: 'Failed to fetch account information',
    auditConfirm: 'Are you sure to {action} this account audit?',
    pass: 'pass',
    reject: 'reject',
    warning: 'Warning',
    companyAuditSuccess: 'Company audit operation successful',
    companyAuditError: 'Company audit operation failed',
    auditSuccess: 'Audit operation successful',
    auditError: 'Audit operation failed',
    callbackParamError: 'Callback parameter error',
    processing: 'Processing...',
    submittingAudit: 'Submitting audit...',
    submittingCompanyAudit: 'Submitting company audit...'
  },

  // User info page
  userInfo: {
    // Page title
    personalAccountInfo: 'Personal Account Information',
    companyAccountInfo: 'Company Account Information',

    // Section titles
    basicInfo: 'Basic Information',
    contactInfo: 'Contact Information',
    idPhotos: 'ID Photos',
    companyBasicInfo: 'Company Basic Information',
    addressInfo: 'Address Information',
    exportBusinessInfo: 'Export Business Information',
    contactPersonInfo: 'Contact Person Information',
    attachmentInfo: 'Attachment Information',

    // Common fields
    idNumber: 'ID',
    fullName: 'Full Name',
    gender: 'Gender',
    birthDate: 'Birth Date',
    nationality: 'Nationality',
    residenceCountry: 'Residence Country',
    registrationDate: 'Registration Date',
    email: 'Email',
    fundSource: 'Fund Source',
    bankAccount: 'Bank Account',
    bank: 'Bank',
    contactPhone: 'Contact Phone',
    idType: 'ID Type',
    idNumberField: 'ID Number',
    provinceCity: 'Province/City',
    postalCode: 'Postal Code',
    residenceAddress: 'Residence Address',

    // Company fields
    companyNameChinese: 'Company Name (Chinese)',
    companyNameEnglish: 'Company Name (English)',
    businessRegistrationNumber: 'Business Registration Number',
    companyRegistrationCode: 'Company Registration Code',
    registeredCountry: 'Registered Country',
    establishmentDate: 'Establishment Date',
    expirationDate: 'Certificate Expiration Date',
    employeeCount: 'Employee Count',
    industryType: 'Industry Type',
    website: 'Website',
    statusLabel: 'Status',
    createdAt: 'Created At',
    updatedAt: 'Updated At',
    userId: 'User ID',
    registeredAddress: 'Registered Address',
    actualOperationAddress: 'Actual Operation Address',
    exportType: 'Export Type',
    exportCountries: 'Export Countries',
    exportProductName: 'Export Product Name',
    historicalAnnualExport: 'Historical Annual Export',
    estimatedAnnualExport: 'Estimated Annual Export',

    // Attachments
    idPhoto: 'ID Photo',
    businessRegistration: 'Business License',
    companyRegistration: 'Company Registration Certificate',
    officeScene: 'Office Scene Photo',

    // Gender
    male: 'Male',
    female: 'Female',
    other: 'Other',

    // ID Types
    idCard: 'ID Card',
    passport: 'Passport',
    driverLicense: 'Driver License',
    militaryID: 'Military ID',
    otherIdType: 'Other',

    // Status
    statusPendingSubmit: 'Pending Submit',
    statusPendingReview: 'Pending Review',
    statusPendingBinnerReview: 'Pending Binner Review',
    statusApproved: 'Approved',
    statusRejected: 'Rejected',

    // Countries
    countries: {
      CN: 'China',
      US: 'United States',
      GB: 'United Kingdom',
      JP: 'Japan',
      KR: 'South Korea',
      FR: 'France',
      DE: 'Germany',
      CA: 'Canada',
      AU: 'Australia',
      SG: 'Singapore',
      HK: 'Hong Kong',
      TW: 'Taiwan',
      MO: 'Macau',
      CL: 'Chile'
    },

    // Common texts
    noData: 'No data',
    noPhoto: 'No photo',
    unknown: 'Unknown',
    noEmiInfo: 'Current login account has no EMI information',

    // Error messages
    fetchPersonalError: 'Failed to fetch personal account information',
    fetchCompanyError: 'Failed to fetch company account information',
    fetchShopError: 'Failed to fetch merchant information',
    unknownError: 'Unknown error'
  },

  // Pay page
  pay: {
    // Tabs
    paymentRecordTab: 'Payment Record',
    payeeListTab: 'Payee List',

    // Filters - Payment Record
    startDate: 'Start Date',
    endDate: 'End Date',
    search: 'Search',

    // Filters - Payee List
    accountLabel: 'Account',
    accountPlaceholder: 'Please enter account',
    statusLabel: 'Status',
    statusPlaceholder: 'Please select',
    accountTypeLabel: 'Account Type',
    accountTypePlaceholder: 'Please select',

    // Status options
    all: 'All',
    enabled: 'Enabled',
    disabled: 'Disabled',

    // Account type
    personal: 'Personal',
    company: 'Company',

    // Table columns - Payment Record
    payTime: 'Payment Time',
    withdrawalNumber: 'Withdrawal Number',
    beneficiaryBank: 'Beneficiary Bank',
    payee: 'Payee',
    paymentAccountNumber: 'Payment Account Number',
    paymentAmount: 'Payment Amount',
    amountReceived: 'Amount Received',

    // Table columns - Payee List
    account: 'Account',
    bankOfDeposit: 'Bank of Deposit',
    accountName: 'Account Name',
    accountType: 'Account Type',
    status: 'Status',
    use: 'Purpose',

    // Messages
    fetchRecordError: 'Failed to fetch payment records, please try again later',
    fetchPayeeError: 'Failed to fetch payee list, please try again later',
    exportPaymentRecord: 'Export payment record',
    createPayment: 'Open create payment dialog',
    viewPaymentDetail: 'View payment detail: ',
    cancelPaymentConfirm: 'Are you sure to cancel payment "{number}"?',
    warning: 'Warning',
    confirm: 'Confirm',
    cancel: 'Cancel',
    paymentCancelled: 'Payment has been cancelled',
    addPayee: 'Open add payee dialog',
    editPayee: 'Edit payee: ',
    viewPayeeDetail: 'View payee detail: ',
    deletePayeeConfirm: 'Are you sure to delete payee "{name}"?',
    deleteSuccess: 'Deleted successfully'
  }
}
