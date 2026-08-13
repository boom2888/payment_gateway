export default {
  // Page title and headers
  pageTitle: 'Company Account Management',

  // Search form labels
  searchForm: {
    currency: 'Account Currency',
    createdAt: 'Created Time',
    searchBtn: 'Search',
    resetBtn: 'Reset'
  },

  // Table headers
  table: {
    id: 'Primary Key ID',
    accountId: 'APM Provider Account ID',
    currency: 'Account Currency',
    createdAt: 'Created Time',
    updatedAt: 'Updated Time',
    action: 'Actions'
  },

  // Action buttons
  actions: {
    add: 'Add',
    edit: 'Edit',
    delete: 'Delete',
    export: 'Export'
  },

  // Dialog
  dialog: {
    addTitle: 'Add Company Account',
    editTitle: 'Edit Company Account',
    confirmBtn: 'Confirm',
    cancelBtn: 'Cancel'
  },

  // Form labels
  form: {
    accountId: 'APM Provider Account ID',
    currency: 'Account Currency',
    createdAt: 'Created Time',
    updatedAt: 'Updated Time',
    accountIdPlaceholder: 'Please enter APM Provider Account ID',
    currencyPlaceholder: 'Please enter account currency',
    createdAtPlaceholder: 'Select created time',
    updatedAtPlaceholder: 'Select updated time'
  },

  // Validation messages
  validation: {
    accountIdRequired: 'APM Provider Account ID cannot be empty',
    currencyRequired: 'Account Currency cannot be empty',
    createdAtRequired: 'Created Time cannot be empty',
    updatedAtRequired: 'Updated Time cannot be empty'
  },

  // Messages
  messages: {
    addSuccess: 'Added successfully',
    editSuccess: 'Modified successfully',
    deleteSuccess: 'Deleted successfully',
    deleteConfirm: 'Are you sure to delete the company account with ID "{id}"?',
    exportConfirm: 'Are you sure to export all company account data?',
    warning: 'Warning'
  }
}
