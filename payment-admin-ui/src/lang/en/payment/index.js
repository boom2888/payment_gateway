import acquirerOrder from './acquirerOrder'
import apiLog from './apiLog'
import blacklistedCreditCards from './blacklistedCreditCards'
import businessCustomerCorporation from './businessCustomerCorporation'
import businessCustomerCorporationMem from './businessCustomerCorporationMem'
import companyAccount from './companyAccount'
import configAcquirer from './configAcquirer'
import configMid from './configMid'
import cryptoCurrency from './cryptoCurrency'
import customer from './customer'
import customerAddress from './customerAddress'
import customerWallet from './customerWallet'
import dashboard from './dashboard'
import emi from './emi'
import emiAuth from './emiAuth'
import ipinfo from './ipinfo'
import limit from './limit'
import merchantAccount from './merchantAccount'
import merchantFeeModel from './merchantFeeModel'
import movementData from './movementData'
import movementImport from './movementImport'
import offRampingCustomerWallet from './offRampingCustomerWallet'
import order from './order'
import refundOrder from './refundOrder'
import reportQuote from './reportQuote'
import shop from './shop'
import shopAuth from './shopAuth'
import thunesTransfer from './thunesTransfer'
import whitelistedCreditCards from './whitelistedCreditCards'


export default {
  order,
  refundOrder,
  shop,
  apiLog,
  merchantAccount,
  merchantFeeModel,
  movementData,
  movementImport,
  dashboard,
  companyAccount,
  acquirerOrder,
  businessCustomerCorporation,
  businessCustomerCorporationMem,
  configAcquirer,
  configMid,
  cryptoCurrency,
  customer,
  customerAddress,
  customerWallet,
  ipinfo,
  limit,
  reportQuote,
  blacklistedCreditCards,
  whitelistedCreditCards,
  offRampingCustomerWallet,
  shopAuth,
  emi,
  emiAuth,
  thunesTransfer
}
