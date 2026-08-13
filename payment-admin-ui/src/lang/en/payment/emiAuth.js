export default {
  // Page title and buttons
  logout: 'Logout',
  submit: 'Submit',
  submitting: 'Submitting...',

  // Status labels
  reviewStatus: 'Review Status:',
  statusPendingSubmit: 'Pending Submission',
  statusPendingReview: 'Pending Review',
  statusPendingDoubleCheck: 'Pending Double Check',
  statusApproved: 'Approved',
  statusRejected: 'Rejected',

  // Account type
  accountType: 'Account Type',
  personalAccount: 'Personal Account',
  companyAccount: 'Company Account',
  cannotSwitchAccountType: 'Cannot switch account type with existing submission record',

  // Personal account - Basic information
  nationality: 'Nationality',
  selectNationality: 'Please select nationality',
  emailAccount: 'Email Account',
  enterEmail: 'Please enter email account',
  fullName: 'Full Name',
  enterFullName: 'Please enter full name',
  idNumber: 'ID Number',
  enterIdNumber: 'Please fill in real ID number',
  gender: 'Gender',
  birthDate: 'Birth Date',
  selectBirthDate: 'Please select birth date',

  // Personal account - Residence information
  residenceCountry: 'Residence Country',
  selectResidenceCountry: 'Please select residence country',
  residenceAddress: 'Residence Address',
  enterResidenceAddress: 'Please enter detailed district, street and house number address',
  residencePostalCode: 'Residence Postal Code',
  enterResidencePostalCode: 'Please fill in postal address code',
  contactPhone: 'Contact Phone',
  enterContactPhone: 'Contact Phone',
  fundSource: 'Fund Source',
  enterFundSource: 'Please enter fund source',

  // ID information
  idType: 'ID Type',
  selectIdType: 'Please select ID type',
  idPhoto: 'ID Photo',
  uploadIdPhoto: 'Please upload ID photo',

  // Company account - Legal person information
  legalPersonName: 'Legal Person Name',
  enterLegalPersonName: 'Please enter legal person name',

  // Company account - Company basic information
  companyNameChinese: 'Company Name (Chinese)',
  enterCompanyNameChinese: 'Please enter company name in Chinese',
  companyNameEnglish: 'Company Name (English)',
  enterCompanyNameEnglish: 'Please enter company name in English',
  businessRegistrationNumber: 'Business Registration Number',
  enterBusinessRegistrationNumber: 'Generally 18/15 digits or letter combination',
  companyRegistrationCode: 'Company Registration Code',
  enterCompanyRegistrationCode: 'Please enter company registration code',

  // Company account - Registration information
  registeredCountry: 'Registered Country',
  selectRegisteredCountry: 'Please select registered country',
  registeredAddress: 'Registered Address',
  enterRegisteredAddress: 'Please enter registered address',
  actualOperationAddress: 'Actual Operation Address',
  enterActualOperationAddress: 'Please enter actual operation address',

  // Company account - Company details
  employeeCount: 'Employee Count',
  enterEmployeeCount: 'Please enter employee count',
  establishmentDate: 'Establishment Date',
  selectEstablishmentDate: 'Please select date',
  expirationDate: 'Expiration Date',
  selectExpirationDate: 'Please select date',
  longTermValid: 'Long-term Valid',

  // Company account - Business information
  industryType: 'Industry Type',
  enterIndustryType: 'Please enter industry type',
  exportType: 'Export Type',
  enterExportType: 'Please enter export type',
  exportCountries: 'Export Countries',
  enterExportCountries: 'Please enter export countries and regions',
  exportProductName: 'Export Product Name',
  enterExportProductName: 'Please enter main export product name, use full name, no more than 100 characters',
  historicalAnnualExport: 'Historical Annual Export',
  enterHistoricalAnnualExport: 'Please enter amount, integer only',
  estimatedAnnualExport: 'Estimated Annual Export',
  enterEstimatedAnnualExport: 'Please enter amount, integer only',
  amountUnit: '10,000 USD',
  website: 'Company Website',
  enterWebsite: 'Please enter company website (optional)',

  // Company account - Certificate files
  businessRegistration: 'Business Registration:',
  companyRegistration: 'Company Registration:',
  incorporation: 'Incorporation Form',
  officeScene: 'Office Scene:',

  // Form validation messages
  emailRequired: 'Please enter email',
  emailFormatError: 'Please enter correct email format',
  fullNameRequired: 'Please enter full name',
  idNumberRequired: 'Please enter ID number',
  idNumberFormatError: 'ID number can only contain letters or numbers',
  nationalityRequired: 'Please select nationality',
  residenceCountryRequired: 'Please select residence country',
  residenceAddressRequired: 'Please enter residence address',
  residencePostalCodeRequired: 'Please enter residence postal code',
  genderRequired: 'Please select gender',
  birthDateRequired: 'Please select birth date',
  contactPhoneRequired: 'Please enter contact phone',
  fundSourceRequired: 'Please enter fund source',
  idTypeRequired: 'Please select ID type',
  idPhotoRequired: 'Please upload ID photo',

  // Company form validation messages
  companyNameChineseRequired: 'Please enter company name in Chinese',
  companyNameEnglishRequired: 'Please enter company name in English',
  businessRegistrationNumberRequired: 'Please enter business registration number',
  companyRegistrationCodeRequired: 'Please enter company registration code',
  registeredCountryRequired: 'Please select registered country',
  registeredAddressRequired: 'Please enter registered address',
  actualOperationAddressRequired: 'Please enter actual operation address',
  employeeCountRequired: 'Please enter employee count',
  employeeCountFormatError: 'Please enter valid number',
  establishmentDateRequired: 'Please select establishment date',
  industryTypeRequired: 'Please enter industry type',
  exportTypeRequired: 'Please enter export type',
  exportCountriesRequired: 'Please enter export countries and regions',
  exportProductNameRequired: 'Please enter export product name',
  exportProductNameMaxLength: 'No more than 100 characters',
  historicalAnnualExportRequired: 'Please enter historical annual export',
  historicalAnnualExportFormatError: 'Please enter valid number (up to 2 decimal places)',
  estimatedAnnualExportRequired: 'Please enter estimated annual export',
  estimatedAnnualExportFormatError: 'Please enter valid number (up to 2 decimal places)',

  // File upload
  uploadSuccess: 'Upload successful',
  uploadFailed: 'Upload failed',
  onlyImageAllowed: 'Only image files are allowed!',
  imageSizeLimit: 'Image size cannot exceed 5MB!',

  // Operation confirmation
  confirmSubmitPersonal: 'Confirm to submit personal account registration information?',
  confirmSubmitCompany: 'Confirm to submit company account registration information?',
  confirmLogout: 'Are you sure to logout and exit the system?',
  hint: 'Hint',
  confirm: 'Confirm',
  cancel: 'Cancel',

  // Submit results
  personalSubmitSuccess: 'Personal account information submitted successfully',
  companySubmitSuccess: 'Company account information submitted successfully',
  submitFailed: 'Submit failed: ',
  pleaseRetryLater: 'Please try again later',
  pleaseCompleteForm: 'Please complete the form information',

  // Other messages
  getCountryListFailed: 'Failed to get country list',
  getUserInfoFailed: 'Failed to get user information: ',
  accountTypeRequired: 'Please select account type'
}
