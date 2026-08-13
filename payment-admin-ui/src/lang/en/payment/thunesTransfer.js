export default {
  // Page title
  title: 'Thunes Transfer',

  // Steps
  step1: 'Get Payer Fields',
  step2: 'Fill Transfer Information',
  step3: 'Confirm Transfer',

  // Step 1: Get Payers
  countryCode: 'Country Code',
  currency: 'Currency Code',
  queryPayers: 'Query Payers',
  reset: 'Reset',
  payerList: 'Payer List',
  payerId: 'ID',
  payerName: 'Name',
  amountRange: 'Amount Range',
  supportedTransactionTypes: 'Supported Transaction Types',
  operation: 'Operation',
  select: 'Select',
  noPayersFound: 'No payers found',
  payersFound: 'Found {count} payers',
  payerSelected: 'Payer selected: {name}',

  // Step 2: Fill Transfer Information
  basicInfo: 'Basic Information',
  merchantId: 'Merchant ID',
  externalId: 'External Order ID',
  transactionType: 'Transaction Type',
  fieldCombination: 'Field Combination',
  selectFieldCombination: 'Please select the field combination you want to fill',
  requiredFieldCombination: 'Required Field Combination',
  combinationFormat: 'Combination {index}: Sender({sending}) | Receiver({receiving})',
  amount: 'Transfer Amount',
  transferCurrency: 'Transfer Currency',
  targetCountry: 'Target Country',
  targetCurrency: 'Target Currency',
  purposeOfRemittance: 'Purpose of Remittance',
  payerOnlySupports: 'This payer only supports the above purposes',
  callbackUrl: 'Callback URL',

  // Sender Information
  senderInfo: 'Sender Information',
  sendingBusinessInfo: 'Sending Business Information',
  firstName: 'First Name',
  lastName: 'Last Name',
  address: 'Address',
  city: 'City',
  postalCode: 'Postal Code',
  provinceState: 'Province/State',
  countryIsoCode: 'Country Code',
  email: 'Email',
  msisdn: 'Phone Number',
  registeredName: 'Registered Name',
  registrationNumber: 'Registration Number',
  code: 'Code',

  // Beneficiary Information
  beneficiaryInfo: 'Beneficiary Information',
  receivingBusinessInfo: 'Receiving Business Information',
  bankAccountNumber: 'Bank Account Number',
  abaRoutingNumber: 'ABA Routing Number',
  accountType: 'Account Type',
  accountTypeChecking: 'Checking Account (CHECKING)',
  accountTypeSavings: 'Savings Account (SAVINGS)',
  iban: 'IBAN',
  autoFilled: 'Auto Filled',

  // Field Descriptions (for placeholder)
  // Field Descriptions (for placeholder)
  desc: {
    // Sender Information
    'sender.firstName': "Sender's first name",
    'sender.lastName': "Sender's last name",
    'sender.middleName': "Sender's middle name (optional)",
    'sender.address': "Sender's address",
    'sender.city': "Sender's city",
    'sender.postalCode': "Sender's postal code",
    'sender.provinceState': "Sender's province/state",
    'sender.countryIsoCode': "Sender's country (ISO 3166-1 alpha-3 format)",
    'sender.email': "Sender's email address",
    'sender.msisdn': "Sender's phone number (international format)",
    // Sending Business Information
    'sendingBusiness.registeredName': 'Business registered name',
    'sendingBusiness.address': 'Business registered address',
    'sendingBusiness.city': 'Business city',
    'sendingBusiness.postalCode': 'Business postal code',
    'sendingBusiness.provinceState': 'Business province/state',
    'sendingBusiness.countryIsoCode': 'Business country (ISO 3166-1 alpha-3 format)',
    'sendingBusiness.registrationNumber': 'Business registration number',
    'sendingBusiness.code': 'Business identification code',
    'sendingBusiness.email': 'Business contact email',
    // Beneficiary Information
    'beneficiary.firstName': "Beneficiary's first name",
    'beneficiary.lastName': "Beneficiary's last name",
    'beneficiary.address': "Beneficiary's address",
    'beneficiary.city': "Beneficiary's city",
    'beneficiary.postalCode': "Beneficiary's postal code",
    'beneficiary.provinceState': "Beneficiary's province/state",
    'beneficiary.countryIsoCode': "Beneficiary's country (ISO 3166-1 alpha-3 format)",
    'beneficiary.bankAccountNumber': "Beneficiary's bank account number",
    'beneficiary.abaRoutingNumber': 'US Bank ABA routing number (9 digits)',
    'beneficiary.accountType': 'Bank account type (checking or savings)',
    'beneficiary.iban': 'International Bank Account Number (IBAN)',
    'beneficiary.msisdn': "Beneficiary's phone number (international format)",
    // Receiving Business Information
    'receivingBusiness.registeredName': 'Receiving business registered name',
    'receivingBusiness.address': 'Receiving business registered address',
    'receivingBusiness.city': 'Receiving business city',
    'receivingBusiness.postalCode': 'Receiving business postal code',
    'receivingBusiness.provinceState': 'Receiving business province/state',
    'receivingBusiness.countryIsoCode': 'Receiving business country (ISO 3166-1 alpha-3 format)',
    'receivingBusiness.registrationNumber': 'Receiving business registration number',
    'receivingBusiness.bankAccountNumber': "Receiving business's bank account number",
    'receivingBusiness.abaRoutingNumber': 'US Bank ABA routing number (9 digits)',
    'receivingBusiness.accountType': "Receiving business's bank account type"
  },

  status: 'Status',

  // Step 3: Confirm Transfer
  confirmTransfer: 'Transfer Information Confirmation',
  merchantName: 'Merchant Name',

  // Buttons
  previousStep: 'Previous',
  nextStep: 'Next',
  confirmAndSubmit: 'Confirm Transfer',

  // Transfer Result
  transferResult: 'Transfer Result',
  transferSuccess: 'Transfer Successful',
  transferFailed: 'Transfer Failed',
  transferRequestSubmitted: 'Transfer request submitted successfully',
  transferId: 'Transfer ID',
  thunesTransactionId: 'Thunes Transaction ID',
  exchangeRate: 'Exchange Rate',
  fee: 'Fee',
  targetAmount: 'Target Amount',
  close: 'Close',
  continueTransfer: 'Continue Transfer',
  viewTransferRecords: 'View Transfer Records',

  // Validation Messages
  validation: {
    selectMerchant: 'Please select a merchant',
    enterExternalId: 'Please enter external order ID',
    enterAmount: 'Please enter transfer amount',
    amountMustBeNumber: 'Transfer amount must be a number',
    amountTooSmall: 'Transfer amount cannot be less than {min}',
    amountTooLarge: 'Transfer amount cannot be greater than {max}',
    selectCurrency: 'Please select transfer currency',
    enterTargetCountry: 'Please enter target country code',
    selectTargetCurrency: 'Please select target currency code',
    selectTransactionType: 'Please select transaction type',
    selectPurpose: 'Please select purpose of remittance',
    enterField: 'Please enter {field}',
    completeTransferInfo: 'Please complete the transfer information'
  },

  // Purpose of Remittance
  purposes: {
    COMPUTER_SERVICES: 'Computer Service',
    FAMILY_SUPPORT: 'Family Support',
    EDUCATION: 'Education',
    GIFT_AND_DONATION: 'Gift and Donations',
    MEDICAL_TREATMENT: 'Medical Treatment',
    MAINTENANCE_EXPENSES: 'Maintenance Expenses',
    TRAVEL: 'Travel',
    SMALL_VALUE_REMITTANCE: 'Small Value Remittance',
    LIBERALIZED_REMITTANCE: 'Liberalized Remittance',
    CONSTRUCTION_EXPENSES: 'Construction Expenses',
    HOTEL_ACCOMMODATION: 'Hotel Accommodation',
    ADVERTISING_EXPENSES: 'Advertising Expenses',
    ADVISORY_FEES: 'Advisory Fees',
    BUSINESS_INSURANCE: 'Business Insurance',
    INSURANCE_CLAIMS: 'Insurance Claims',
    DELIVERY_FEES: 'Delivery Fees',
    EXPORTED_GOODS: 'Exported Goods Payment',
    SERVICE_CHARGES: 'Service Charges',
    LOAN_PAYMENT: 'Loan Payment',
    OFFICE_EXPENSES: 'Office Expenses',
    PROPERTY_PURCHASE: 'Property Purchase',
    PROPERTY_RENTAL: 'Property Rental',
    ROYALTY_FEES: 'Royalty Fees',
    SHARES_INVESTMENT: 'Shares Investment',
    FUND_INVESTMENT: 'Fund Investment',
    TAX_PAYMENT: 'Tax Payment',
    TRANSPORTATION_FEES: 'Transportation Fees',
    UTILITY_BILLS: 'Utility Bills',
    PERSONAL_TRANSFER: 'Personal Transfer',
    SALARY_PAYMENT: 'Salary Payment',
    REWARD_PAYMENT: 'Reward Payment',
    INFLUENCER_PAYMENT: 'Influencer Payment',
    OTHER_FEES: 'Other Fees',
    OTHER: 'Other'
  },

  // Tips
  tips: {
    iso3166Alpha3: 'ISO 3166-1 alpha-3 format (3 characters)',
    msisdn: 'International format, e.g.: 33712345678',
    abaRoutingNumber: 'US bank ABA routing number (9 digits)',
    iban: 'International Bank Account Number (IBAN)'
  }
}
