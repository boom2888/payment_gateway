<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('payment.thunesTransfer.title') }}</span>
      </div>

      <!-- Step Bar -->
      <el-steps :active="activeStep" finish-status="success" align-center style="margin-bottom: 30px">
        <el-step :title="$t('payment.thunesTransfer.step1')" />
        <el-step :title="$t('payment.thunesTransfer.step2')" />
        <el-step :title="$t('payment.thunesTransfer.step3')" />
      </el-steps>

      <!-- Step 1: Get Payer Fields -->
      <div v-show="activeStep === 0">
        <el-form ref="payerQueryForm" :model="payerQuery" label-width="120px">
          <el-form-item :label="$t('payment.thunesTransfer.countryCode')" prop="countryCode">
            <el-select
              v-model="payerQuery.countryCode"
              filterable
              clearable
              :placeholder="$t('payment.thunesTransfer.countryCode')"
              style="width: 100%"
            >
              <el-option
                v-for="country in countryList"
                :key="country.code"
                :label="`${country.flag} ${country.nameEn} (${country.code})`"
                :value="country.code"
              >
                <span style="float: left">
                  <span style="font-size: 18px; margin-right: 8px">{{ country.flag }}</span>
                  <span>{{ country.nameEn }}</span>
                </span>
                <span style="float: right; color: #8492a6; font-size: 13px">
                  {{ country.code }}
                </span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('payment.thunesTransfer.currency')" prop="currency">
            <el-select
              v-model="payerQuery.currency"
              filterable
              clearable
              :placeholder="$t('payment.thunesTransfer.currency')"
              style="width: 100%"
            >
              <el-option
                v-for="item in dict.type.thunes_transfer_currency"
                :key="item.dictValue"
                :label="`${item.dictLabel} (${item.dictValue})`"
                :value="item.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleGetPayers">{{ $t('payment.thunesTransfer.queryPayers') }}</el-button>
            <el-button @click="resetPayerQuery">{{ $t('payment.thunesTransfer.reset') }}</el-button>
          </el-form-item>
        </el-form>

        <!-- Payer List -->
        <div v-if="payerList.length > 0">
          <el-divider content-position="left">{{ $t('payment.thunesTransfer.payerList') }}</el-divider>
          <el-table :data="payerList" border style="width: 100%">
            <el-table-column prop="id" :label="$t('payment.thunesTransfer.payerId')" width="80" />
            <el-table-column prop="name" :label="$t('payment.thunesTransfer.payerName')" min-width="150" />
            <el-table-column prop="currency" :label="$t('payment.thunesTransfer.currency')" width="80" />
            <el-table-column prop="countryIsoCode" :label="$t('payment.thunesTransfer.countryCode')" width="80" />
            <el-table-column :label="$t('payment.thunesTransfer.amountRange')" width="150">
              <template slot-scope="scope">
                {{ scope.row.minAmount }} - {{ scope.row.maxAmount }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('payment.thunesTransfer.supportedTransactionTypes')" min-width="150">
              <template slot-scope="scope">
                <el-tag
                  v-for="(value, key) in scope.row.transactionTypes"
                  :key="key"
                  size="small"
                  style="margin-right: 5px"
                >
                  {{ key }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column :label="$t('payment.thunesTransfer.operation')" width="150" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleSelectPayer(scope.row)">
                  {{ $t('payment.thunesTransfer.select') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- Step 2: Fill Transfer Information -->
      <div v-show="activeStep === 1">
        <el-form ref="transferForm" :model="transferForm" :rules="transferRules" label-width="150px">
          <!-- Basic Information -->
          <el-divider content-position="left">{{ $t('payment.thunesTransfer.basicInfo') }}</el-divider>
          <el-form-item :label="$t('payment.thunesTransfer.merchantId')" prop="merchantId">
            <el-select
              v-model="transferForm.merchantId"
              filterable
              clearable
              :placeholder="$t('payment.thunesTransfer.merchantId')"
              style="width: 100%"
            >
              <el-option
                v-for="merchant in merchantList"
                :key="merchant.id"
                :label="`${merchant.businessName} (${merchant.userName})`"
                :value="merchant.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('payment.thunesTransfer.externalId')" prop="externalId">
            <el-input v-model="transferForm.externalId" :placeholder="$t('payment.thunesTransfer.externalId')" />
          </el-form-item>
          <el-form-item :label="$t('payment.thunesTransfer.transactionType')" prop="transactionType">
            <el-select v-model="transferForm.transactionType" :placeholder="$t('payment.thunesTransfer.transactionType')" @change="handleTransactionTypeChange">
              <el-option
                v-for="type in availableTransactionTypes"
                :key="type"
                :label="type"
                :value="type"
              />
            </el-select>
          </el-form-item>

          <!-- Field Combination Selection -->
          <el-form-item v-if="fieldCombinations.length > 0" :label="$t('payment.thunesTransfer.fieldCombination')">
            <el-select v-model="transferForm.selectedFieldsIndex" :placeholder="$t('payment.thunesTransfer.fieldCombination')" @change="updateDynamicRules">
              <el-option
                v-for="(combo, index) in fieldCombinations"
                :key="index"
                :label="$t('payment.thunesTransfer.combinationFormat', { index: index + 1, sending: combo.sendingDescription, receiving: combo.receivingDescription })"
                :value="index"
              />
            </el-select>
            <span v-if="fieldCombinations.length > 1" style="color: #909399; font-size: 12px; display: block; margin-top: 5px">
              {{ $t('payment.thunesTransfer.selectFieldCombination') }}
            </span>
            <span v-else style="color: #909399; font-size: 12px; display: block; margin-top: 5px">
              {{ $t('payment.thunesTransfer.requiredFieldCombination') }}
            </span>
          </el-form-item>
          <el-form-item :label="$t('payment.thunesTransfer.amount')" prop="amount">
            <el-input v-model="transferForm.amount" :placeholder="$t('payment.thunesTransfer.amount')" type="text" @input="handleAmountInput" @blur="handleAmountBlur">
              <template slot="append">{{ transferForm.currency }}</template>
            </el-input>
            <span v-if="amountHint" style="color: #909399; font-size: 12px">{{ amountHint }}</span>
          </el-form-item>
          <el-form-item :label="$t('payment.thunesTransfer.transferCurrency')" prop="currency">
            <el-select
              v-model="transferForm.currency"
              filterable
              clearable
              :placeholder="$t('payment.thunesTransfer.transferCurrency')"
              style="width: 100%"
            >
              <el-option
                v-for="item in dict.type.thunes_transfer_currency"
                :key="item.dictValue"
                :label="`${item.dictLabel} (${item.dictValue})`"
                :value="item.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('payment.thunesTransfer.targetCountry')" prop="targetCountry">
            <el-input v-model="transferForm.targetCountry" :placeholder="$t('payment.thunesTransfer.targetCountry')" />
          </el-form-item>
          <el-form-item :label="$t('payment.thunesTransfer.targetCurrency')" prop="targetCurrency">
            <el-select
              v-model="transferForm.targetCurrency"
              filterable
              clearable
              :placeholder="$t('payment.thunesTransfer.targetCurrency')"
              style="width: 100%"
            >
              <el-option
                v-for="item in dict.type.thunes_transfer_currency"
                :key="item.dictValue"
                :label="`${item.dictLabel} (${item.dictValue})`"
                :value="item.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('payment.thunesTransfer.purposeOfRemittance')" prop="purposeOfRemittance">
            <el-select v-model="transferForm.purposeOfRemittance" :placeholder="$t('payment.thunesTransfer.purposeOfRemittance')">
              <el-option
                v-for="purpose in (availablePurposes.length > 0 ? availablePurposes : allPurposes)"
                :key="purpose.value"
                :label="purpose.label"
                :value="purpose.value"
              />
            </el-select>
            <span v-if="availablePurposes.length > 0" style="color: #909399; font-size: 12px">
              {{ $t('payment.thunesTransfer.payerOnlySupports') }}
            </span>
          </el-form-item>
          <el-form-item :label="$t('payment.thunesTransfer.callbackUrl')" prop="callbackUrl">
            <el-input v-model="transferForm.callbackUrl" :placeholder="$t('payment.thunesTransfer.callbackUrl')" />
          </el-form-item>

          <!-- Sender info (C2C/C2B) -->
          <div v-if="showSenderIndividual">
            <el-divider content-position="left">{{ $t('payment.thunesTransfer.senderInfo') }}</el-divider>
            <el-form-item :label="$t('payment.thunesTransfer.firstName')" prop="sender.firstName" :required="isFieldRequired('sender.firstName')">
              <el-input v-model="transferForm.sender.firstName" :placeholder="getFieldPlaceholder('sender.firstName')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.lastName')" prop="sender.lastName" :required="isFieldRequired('sender.lastName')">
              <el-input v-model="transferForm.sender.lastName" :placeholder="getFieldPlaceholder('sender.lastName')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.address')" prop="sender.address" :required="isFieldRequired('sender.address')">
              <el-input v-model="transferForm.sender.address" :placeholder="getFieldPlaceholder('sender.address')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.countryIsoCode')" prop="sender.countryIsoCode" :required="isFieldRequired('sender.countryIsoCode')">
              <el-select
                v-model="transferForm.sender.countryIsoCode"
                filterable
                clearable
                :placeholder="getFieldPlaceholder('sender.countryIsoCode')"
                style="width: 100%"
                @change="handleCountryChange('sender')"
              >
                <el-option
                  v-for="country in countryList"
                  :key="country.code"
                  :label="`${country.flag} ${country.nameEn} (${country.code})`"
                  :value="country.code"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.provinceState')" prop="sender.provinceState" :required="isFieldRequired('sender.provinceState')">
              <el-select
                v-model="transferForm.sender.provinceState"
                filterable
                clearable
                allow-create
                default-first-option
                :placeholder="getFieldPlaceholder('sender.provinceState')"
                style="width: 100%"
                @change="handleStateChange('sender')"
              >
                <el-option
                  v-for="state in senderStates"
                  :key="state.name"
                  :label="`${state.name} (${state.code})`"
                  :value="state.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.city')" prop="sender.city" :required="isFieldRequired('sender.city')">
              <el-select
                v-model="transferForm.sender.city"
                filterable
                clearable
                allow-create
                default-first-option
                :placeholder="getFieldPlaceholder('sender.city')"
                style="width: 100%"
              >
                <el-option
                  v-for="city in senderCities"
                  :key="city"
                  :label="city"
                  :value="city"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.email')" prop="sender.email" :required="isFieldRequired('sender.email')">
              <el-input v-model="transferForm.sender.email" :placeholder="getFieldPlaceholder('sender.email')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.postalCode')" prop="sender.postalCode"
                          :required="isFieldRequired('sender.postalCode')">
              <el-input v-model="transferForm.sender.postalCode"
                        :placeholder="getFieldPlaceholder('sender.postalCode')"/>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.msisdn')" prop="sender.msisdn" :required="isFieldRequired('sender.msisdn')">
              <el-input v-model="transferForm.sender.msisdn" :placeholder="getFieldPlaceholder('sender.msisdn')">
                <template slot="prepend">
                  <el-tooltip :content="$t('payment.thunesTransfer.tips.msisdn')" placement="top">
                    <i class="el-icon-question" style="cursor: help"></i>
                  </el-tooltip>
                </template>
              </el-input>
            </el-form-item>
          </div>

          <!-- Sending business info (B2C/B2B) -->
          <div v-if="showSendingBusiness">
            <el-divider content-position="left">{{ $t('payment.thunesTransfer.sendingBusinessInfo') }}</el-divider>
            <el-form-item :label="$t('payment.thunesTransfer.registeredName')" prop="sendingBusiness.registeredName" :required="isFieldRequired('sendingBusiness.registeredName')">
              <el-input v-model="transferForm.sendingBusiness.registeredName" :placeholder="getFieldPlaceholder('sendingBusiness.registeredName')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.address')" prop="sendingBusiness.address" :required="isFieldRequired('sendingBusiness.address')">
              <el-input v-model="transferForm.sendingBusiness.address" :placeholder="getFieldPlaceholder('sendingBusiness.address')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.countryIsoCode')" prop="sendingBusiness.countryIsoCode" :required="isFieldRequired('sendingBusiness.countryIsoCode')">
              <el-select
                v-model="transferForm.sendingBusiness.countryIsoCode"
                filterable
                clearable
                :placeholder="getFieldPlaceholder('sendingBusiness.countryIsoCode')"
                style="width: 100%"
                @change="handleCountryChange('sendingBusiness')"
              >
                <el-option
                  v-for="country in countryList"
                  :key="country.code"
                  :label="`${country.flag} ${country.nameEn} (${country.code})`"
                  :value="country.code"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.provinceState')" prop="sendingBusiness.provinceState" :required="isFieldRequired('sendingBusiness.provinceState')">
              <el-select
                v-model="transferForm.sendingBusiness.provinceState"
                filterable
                clearable
                allow-create
                default-first-option
                :placeholder="getFieldPlaceholder('sendingBusiness.provinceState')"
                style="width: 100%"
                @change="handleStateChange('sendingBusiness')"
              >
                <el-option
                  v-for="state in sendingBusinessStates"
                  :key="state.name"
                  :label="`${state.name} (${state.code})`"
                  :value="state.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.city')" prop="sendingBusiness.city" :required="isFieldRequired('sendingBusiness.city')">
              <el-select
                v-model="transferForm.sendingBusiness.city"
                filterable
                clearable
                allow-create
                default-first-option
                :placeholder="getFieldPlaceholder('sendingBusiness.city')"
                style="width: 100%"
              >
                <el-option
                  v-for="city in sendingBusinessCities"
                  :key="city"
                  :label="city"
                  :value="city"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.registrationNumber')" prop="sendingBusiness.registrationNumber" :required="isFieldRequired('sendingBusiness.registrationNumber')">
              <el-input v-model="transferForm.sendingBusiness.registrationNumber" :placeholder="getFieldPlaceholder('sendingBusiness.registrationNumber')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.code')" prop="sendingBusiness.code" :required="isFieldRequired('sendingBusiness.code')">
              <el-input v-model="transferForm.sendingBusiness.code" :placeholder="getFieldPlaceholder('sendingBusiness.code')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.postalCode')" prop="sendingBusiness.postalCode"
                          :required="isFieldRequired('sendingBusiness.postalCode')">
              <el-input v-model="transferForm.sendingBusiness.postalCode"
                        :placeholder="getFieldPlaceholder('sendingBusiness.postalCode')"/>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.email')" prop="sendingBusiness.email" :required="isFieldRequired('sendingBusiness.email')">
              <el-input v-model="transferForm.sendingBusiness.email" :placeholder="getFieldPlaceholder('sendingBusiness.email')" />
            </el-form-item>
          </div>

          <!-- Beneficiary info (C2C/B2C) -->
          <div v-if="showBeneficiaryIndividual">
            <el-divider content-position="left">{{ $t('payment.thunesTransfer.beneficiaryInfo') }}</el-divider>
            <el-form-item :label="$t('payment.thunesTransfer.firstName')" prop="beneficiary.firstName" :required="isFieldRequired('beneficiary.firstName')">
              <el-input v-model="transferForm.beneficiary.firstName" :placeholder="getFieldPlaceholder('beneficiary.firstName')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.lastName')" prop="beneficiary.lastName" :required="isFieldRequired('beneficiary.lastName')">
              <el-input v-model="transferForm.beneficiary.lastName" :placeholder="getFieldPlaceholder('beneficiary.lastName')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.address')" prop="beneficiary.address" :required="isFieldRequired('beneficiary.address')">
              <el-input v-model="transferForm.beneficiary.address" :placeholder="getFieldPlaceholder('beneficiary.address')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.provinceState')" prop="beneficiary.provinceState" :required="isFieldRequired('beneficiary.provinceState')">
              <el-select
                v-model="transferForm.beneficiary.provinceState"
                filterable
                clearable
                allow-create
                default-first-option
                :placeholder="getFieldPlaceholder('beneficiary.provinceState')"
                style="width: 100%"
                @change="handleStateChange('beneficiary')"
              >
                <el-option
                  v-for="state in beneficiaryStates"
                  :key="state.name"
                  :label="`${state.name} (${state.code})`"
                  :value="state.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.city')" prop="beneficiary.city" :required="isFieldRequired('beneficiary.city')">
              <el-select
                v-model="transferForm.beneficiary.city"
                filterable
                clearable
                allow-create
                default-first-option
                :placeholder="getFieldPlaceholder('beneficiary.city')"
                style="width: 100%"
              >
                <el-option
                  v-for="city in beneficiaryCities"
                  :key="city"
                  :label="city"
                  :value="city"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.countryIsoCode')" prop="beneficiary.countryIsoCode" :required="isFieldRequired('beneficiary.countryIsoCode')">
              <el-select
                v-model="transferForm.beneficiary.countryIsoCode"
                filterable
                clearable
                :placeholder="getFieldPlaceholder('beneficiary.countryIsoCode')"
                style="width: 100%"
                @change="handleCountryChange('beneficiary')"
              >
                <el-option
                  v-for="country in countryList"
                  :key="country.code"
                  :label="`${country.flag} ${country.nameEn} (${country.code})`"
                  :value="country.code"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.bankAccountNumber')" prop="beneficiary.bankAccountNumber" required>
              <el-input v-model="transferForm.beneficiary.bankAccountNumber" :placeholder="getFieldPlaceholder('beneficiary.bankAccountNumber')" />
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.abaRoutingNumber')" :label="$t('payment.thunesTransfer.abaRoutingNumber')" prop="beneficiary.abaRoutingNumber" required>
              <el-input v-model="transferForm.beneficiary.abaRoutingNumber" :placeholder="getFieldPlaceholder('beneficiary.abaRoutingNumber')">
                <template slot="prepend">
                  <el-tooltip :content="$t('payment.thunesTransfer.tips.abaRoutingNumber')" placement="top">
                    <i class="el-icon-question" style="cursor: help"></i>
                  </el-tooltip>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.accountType')" :label="$t('payment.thunesTransfer.accountType')" prop="beneficiary.accountType" required>
              <el-select v-model="transferForm.beneficiary.accountType" :placeholder="$t('payment.thunesTransfer.accountType')" style="width: 100%">
                <el-option :label="$t('payment.thunesTransfer.accountTypeChecking')" value="CHECKING" />
                <el-option :label="$t('payment.thunesTransfer.accountTypeSavings')" value="SAVINGS" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.iban')" prop="beneficiary.iban" :required="isFieldRequired('beneficiary.iban')">
              <el-input v-model="transferForm.beneficiary.iban" :placeholder="getFieldPlaceholder('beneficiary.iban')">
                <template slot="prepend">
                  <el-tooltip :content="$t('payment.thunesTransfer.tips.iban')" placement="top">
                    <i class="el-icon-question" style="cursor: help"></i>
                  </el-tooltip>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.clabe')" :label="$t('payment.thunesTransfer.clabe')"
                          prop="beneficiary.clabe" required>
              <el-input v-model="transferForm.beneficiary.clabe"
                        :placeholder="getFieldPlaceholder('beneficiary.clabe')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.cbu')" :label="$t('payment.thunesTransfer.cbu')"
                          prop="beneficiary.cbu" required>
              <el-input v-model="transferForm.beneficiary.cbu" :placeholder="getFieldPlaceholder('beneficiary.cbu')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.cbuAlias')" :label="$t('payment.thunesTransfer.cbuAlias')"
                          prop="beneficiary.cbuAlias" required>
              <el-input v-model="transferForm.beneficiary.cbuAlias"
                        :placeholder="getFieldPlaceholder('beneficiary.cbuAlias')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.swiftBicCode')"
                          :label="$t('payment.thunesTransfer.swiftBicCode')" prop="beneficiary.swiftBicCode" required>
              <el-input v-model="transferForm.beneficiary.swiftBicCode"
                        :placeholder="getFieldPlaceholder('beneficiary.swiftBicCode')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.bikCode')" :label="$t('payment.thunesTransfer.bikCode')"
                          prop="beneficiary.bikCode" required>
              <el-input v-model="transferForm.beneficiary.bikCode"
                        :placeholder="getFieldPlaceholder('beneficiary.bikCode')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.ifsCode')" :label="$t('payment.thunesTransfer.ifsCode')"
                          prop="beneficiary.ifsCode" required>
              <el-input v-model="transferForm.beneficiary.ifsCode"
                        :placeholder="getFieldPlaceholder('beneficiary.ifsCode')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.sortCode')" :label="$t('payment.thunesTransfer.sortCode')"
                          prop="beneficiary.sortCode" required>
              <el-input v-model="transferForm.beneficiary.sortCode"
                        :placeholder="getFieldPlaceholder('beneficiary.sortCode')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.bsbNumber')"
                          :label="$t('payment.thunesTransfer.bsbNumber')" prop="beneficiary.bsbNumber" required>
              <el-input v-model="transferForm.beneficiary.bsbNumber"
                        :placeholder="getFieldPlaceholder('beneficiary.bsbNumber')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.branchNumber')"
                          :label="$t('payment.thunesTransfer.branchNumber')" prop="beneficiary.branchNumber" required>
              <el-input v-model="transferForm.beneficiary.branchNumber"
                        :placeholder="getFieldPlaceholder('beneficiary.branchNumber')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.routingCode')"
                          :label="$t('payment.thunesTransfer.routingCode')" prop="beneficiary.routingCode" required>
              <el-input v-model="transferForm.beneficiary.routingCode"
                        :placeholder="getFieldPlaceholder('beneficiary.routingCode')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.entityTtId')"
                          :label="$t('payment.thunesTransfer.entityTtId')" prop="beneficiary.entityTtId" required>
              <el-input v-model="transferForm.beneficiary.entityTtId"
                        :placeholder="getFieldPlaceholder('beneficiary.entityTtId')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.accountNumber')"
                          :label="$t('payment.thunesTransfer.accountNumber')" prop="beneficiary.accountNumber" required>
              <el-input v-model="transferForm.beneficiary.accountNumber"
                        :placeholder="getFieldPlaceholder('beneficiary.accountNumber')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.cardAccountNumber')"
                          :label="$t('payment.thunesTransfer.cardAccountNumber')" prop="beneficiary.cardAccountNumber"
                          required>
              <el-input v-model="transferForm.beneficiary.cardAccountNumber"
                        :placeholder="getFieldPlaceholder('beneficiary.cardAccountNumber')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('beneficiary.cardNumber')"
                          :label="$t('payment.thunesTransfer.cardNumber')" prop="beneficiary.cardNumber" required>
              <el-input v-model="transferForm.beneficiary.cardNumber"
                        :placeholder="getFieldPlaceholder('beneficiary.cardNumber')"/>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.postalCode')" prop="beneficiary.postalCode"
                          :required="isFieldRequired('beneficiary.postalCode')">
              <el-input v-model="transferForm.beneficiary.postalCode"
                        :placeholder="getFieldPlaceholder('beneficiary.postalCode')"/>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.email')" prop="beneficiary.email"
                          :required="isFieldRequired('beneficiary.email')">
              <el-input v-model="transferForm.beneficiary.email"
                        :placeholder="getFieldPlaceholder('beneficiary.email')"/>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.msisdn')" prop="beneficiary.msisdn" :required="isFieldRequired('beneficiary.msisdn')">
              <el-input v-model="transferForm.beneficiary.msisdn" :placeholder="getFieldPlaceholder('beneficiary.msisdn')">
                <template slot="prepend">
                  <el-tooltip :content="$t('payment.thunesTransfer.tips.msisdn')" placement="top">
                    <i class="el-icon-question" style="cursor: help"></i>
                  </el-tooltip>
                </template>
              </el-input>
            </el-form-item>
          </div>

          <!-- Receiving business info (C2B/B2B) -->
          <div v-if="showReceivingBusiness">
            <el-divider content-position="left">{{ $t('payment.thunesTransfer.receivingBusinessInfo') }}</el-divider>
            <el-form-item :label="$t('payment.thunesTransfer.registeredName')" prop="receivingBusiness.registeredName" :required="isFieldRequired('receivingBusiness.registeredName')">
              <el-input v-model="transferForm.receivingBusiness.registeredName" :placeholder="getFieldPlaceholder('receivingBusiness.registeredName')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.address')" prop="receivingBusiness.address" :required="isFieldRequired('receivingBusiness.address')">
              <el-input v-model="transferForm.receivingBusiness.address" :placeholder="getFieldPlaceholder('receivingBusiness.address')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.provinceState')" prop="receivingBusiness.provinceState" :required="isFieldRequired('receivingBusiness.provinceState')">
              <el-select
                v-model="transferForm.receivingBusiness.provinceState"
                filterable
                clearable
                allow-create
                default-first-option
                :placeholder="getFieldPlaceholder('receivingBusiness.provinceState')"
                style="width: 100%"
                @change="handleStateChange('receivingBusiness')"
              >
                <el-option
                  v-for="state in receivingBusinessStates"
                  :key="state.name"
                  :label="`${state.name} (${state.code})`"
                  :value="state.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.city')" prop="receivingBusiness.city" :required="isFieldRequired('receivingBusiness.city')">
              <el-select
                v-model="transferForm.receivingBusiness.city"
                filterable
                clearable
                allow-create
                default-first-option
                :placeholder="getFieldPlaceholder('receivingBusiness.city')"
                style="width: 100%"
              >
                <el-option
                  v-for="city in receivingBusinessCities"
                  :key="city"
                  :label="city"
                  :value="city"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.countryIsoCode')" prop="receivingBusiness.countryIsoCode" :required="isFieldRequired('receivingBusiness.countryIsoCode')">
              <el-select
                v-model="transferForm.receivingBusiness.countryIsoCode"
                filterable
                clearable
                :placeholder="getFieldPlaceholder('receivingBusiness.countryIsoCode')"
                style="width: 100%"
                @change="handleCountryChange('receivingBusiness')"
              >
                <el-option
                  v-for="country in countryList"
                  :key="country.code"
                  :label="`${country.flag} ${country.nameEn} (${country.code})`"
                  :value="country.code"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.postalCode')" prop="receivingBusiness.postalCode"
                          :required="isFieldRequired('receivingBusiness.postalCode')">
              <el-input v-model="transferForm.receivingBusiness.postalCode"
                        :placeholder="getFieldPlaceholder('receivingBusiness.postalCode')"/>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.email')" prop="receivingBusiness.email"
                          :required="isFieldRequired('receivingBusiness.email')">
              <el-input v-model="transferForm.receivingBusiness.email"
                        :placeholder="getFieldPlaceholder('receivingBusiness.email')"/>
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.registrationNumber')" prop="receivingBusiness.registrationNumber" :required="isFieldRequired('receivingBusiness.registrationNumber')">
              <el-input v-model="transferForm.receivingBusiness.registrationNumber" :placeholder="getFieldPlaceholder('receivingBusiness.registrationNumber')" />
            </el-form-item>
            <el-form-item :label="$t('payment.thunesTransfer.bankAccountNumber')" prop="receivingBusiness.bankAccountNumber" required>
              <el-input v-model="transferForm.receivingBusiness.bankAccountNumber" :placeholder="getFieldPlaceholder('receivingBusiness.bankAccountNumber')" />
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.abaRoutingNumber')" :label="$t('payment.thunesTransfer.abaRoutingNumber')" prop="receivingBusiness.abaRoutingNumber" required>
              <el-input v-model="transferForm.receivingBusiness.abaRoutingNumber" :placeholder="getFieldPlaceholder('receivingBusiness.abaRoutingNumber')">
                <template slot="prepend">
                  <el-tooltip :content="$t('payment.thunesTransfer.tips.abaRoutingNumber')" placement="top">
                    <i class="el-icon-question" style="cursor: help"></i>
                  </el-tooltip>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.accountType')" :label="$t('payment.thunesTransfer.accountType')" prop="receivingBusiness.accountType" required>
              <el-select v-model="transferForm.receivingBusiness.accountType" :placeholder="$t('payment.thunesTransfer.accountType')" style="width: 100%">
                <el-option :label="$t('payment.thunesTransfer.accountTypeChecking')" value="CHECKING" />
                <el-option :label="$t('payment.thunesTransfer.accountTypeSavings')" value="SAVINGS" />
              </el-select>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.iban')" :label="$t('payment.thunesTransfer.iban')"
                          prop="receivingBusiness.iban" required>
              <el-input v-model="transferForm.receivingBusiness.iban"
                        :placeholder="getFieldPlaceholder('receivingBusiness.iban')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.clabe')" :label="$t('payment.thunesTransfer.clabe')"
                          prop="receivingBusiness.clabe" required>
              <el-input v-model="transferForm.receivingBusiness.clabe"
                        :placeholder="getFieldPlaceholder('receivingBusiness.clabe')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.cbu')" :label="$t('payment.thunesTransfer.cbu')"
                          prop="receivingBusiness.cbu" required>
              <el-input v-model="transferForm.receivingBusiness.cbu"
                        :placeholder="getFieldPlaceholder('receivingBusiness.cbu')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.cbuAlias')"
                          :label="$t('payment.thunesTransfer.cbuAlias')" prop="receivingBusiness.cbuAlias" required>
              <el-input v-model="transferForm.receivingBusiness.cbuAlias"
                        :placeholder="getFieldPlaceholder('receivingBusiness.cbuAlias')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.swiftBicCode')"
                          :label="$t('payment.thunesTransfer.swiftBicCode')" prop="receivingBusiness.swiftBicCode"
                          required>
              <el-input v-model="transferForm.receivingBusiness.swiftBicCode"
                        :placeholder="getFieldPlaceholder('receivingBusiness.swiftBicCode')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.bikCode')"
                          :label="$t('payment.thunesTransfer.bikCode')" prop="receivingBusiness.bikCode" required>
              <el-input v-model="transferForm.receivingBusiness.bikCode"
                        :placeholder="getFieldPlaceholder('receivingBusiness.bikCode')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.ifsCode')"
                          :label="$t('payment.thunesTransfer.ifsCode')" prop="receivingBusiness.ifsCode" required>
              <el-input v-model="transferForm.receivingBusiness.ifsCode"
                        :placeholder="getFieldPlaceholder('receivingBusiness.ifsCode')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.sortCode')"
                          :label="$t('payment.thunesTransfer.sortCode')" prop="receivingBusiness.sortCode" required>
              <el-input v-model="transferForm.receivingBusiness.sortCode"
                        :placeholder="getFieldPlaceholder('receivingBusiness.sortCode')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.bsbNumber')"
                          :label="$t('payment.thunesTransfer.bsbNumber')" prop="receivingBusiness.bsbNumber" required>
              <el-input v-model="transferForm.receivingBusiness.bsbNumber"
                        :placeholder="getFieldPlaceholder('receivingBusiness.bsbNumber')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.branchNumber')"
                          :label="$t('payment.thunesTransfer.branchNumber')" prop="receivingBusiness.branchNumber"
                          required>
              <el-input v-model="transferForm.receivingBusiness.branchNumber"
                        :placeholder="getFieldPlaceholder('receivingBusiness.branchNumber')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.routingCode')"
                          :label="$t('payment.thunesTransfer.routingCode')" prop="receivingBusiness.routingCode"
                          required>
              <el-input v-model="transferForm.receivingBusiness.routingCode"
                        :placeholder="getFieldPlaceholder('receivingBusiness.routingCode')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.entityTtId')"
                          :label="$t('payment.thunesTransfer.entityTtId')" prop="receivingBusiness.entityTtId" required>
              <el-input v-model="transferForm.receivingBusiness.entityTtId"
                        :placeholder="getFieldPlaceholder('receivingBusiness.entityTtId')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.accountNumber')"
                          :label="$t('payment.thunesTransfer.accountNumber')" prop="receivingBusiness.accountNumber"
                          required>
              <el-input v-model="transferForm.receivingBusiness.accountNumber"
                        :placeholder="getFieldPlaceholder('receivingBusiness.accountNumber')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.cardAccountNumber')"
                          :label="$t('payment.thunesTransfer.cardAccountNumber')"
                          prop="receivingBusiness.cardAccountNumber" required>
              <el-input v-model="transferForm.receivingBusiness.cardAccountNumber"
                        :placeholder="getFieldPlaceholder('receivingBusiness.cardAccountNumber')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.cardNumber')"
                          :label="$t('payment.thunesTransfer.cardNumber')" prop="receivingBusiness.cardNumber" required>
              <el-input v-model="transferForm.receivingBusiness.cardNumber"
                        :placeholder="getFieldPlaceholder('receivingBusiness.cardNumber')"/>
            </el-form-item>
            <el-form-item v-if="isFieldRequired('receivingBusiness.msisdn')"
                          :label="$t('payment.thunesTransfer.msisdn')" prop="receivingBusiness.msisdn" required>
              <el-input v-model="transferForm.receivingBusiness.msisdn"
                        :placeholder="getFieldPlaceholder('receivingBusiness.msisdn')"/>
            </el-form-item>
          </div>

          <el-form-item>
            <el-button @click="activeStep = 0">{{ $t('payment.thunesTransfer.previousStep') }}</el-button>
            <el-button type="primary" @click="handleNextStep">{{ $t('payment.thunesTransfer.nextStep') }}</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- Step 3: Confirm transfer -->
      <div v-show="activeStep === 2" class="confirm-container">
        <div class="confirm-section">
          <div class="confirm-title">{{ $t('payment.thunesTransfer.confirmTransfer') }}</div>
          <el-row :gutter="20">
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.merchantId') }}: </span>
              <span class="detail-value">{{ transferForm.merchantId }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.merchantName') }}: </span>
              <span class="detail-value">{{ getSelectedMerchantName() }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.externalId') }}: </span>
              <span class="detail-value">{{ transferForm.externalId }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.transactionType') }}: </span>
              <span class="detail-value">{{ transferForm.transactionType }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.amount') }}: </span>
              <span class="detail-value">{{ transferForm.amount }} {{ transferForm.currency }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.targetCountry') }}: </span>
              <span class="detail-value">{{ transferForm.targetCountry }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.targetCurrency') }}: </span>
              <span class="detail-value">{{ transferForm.targetCurrency }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.purposeOfRemittance') }}: </span>
              <span class="detail-value">{{ getPurposeLabel(transferForm.purposeOfRemittance) }}</span>
            </el-col>
          </el-row>
        </div>

        <!-- Sender info -->
        <div v-if="showSenderIndividual" class="confirm-section shadow-box">
          <div class="confirm-title">{{ $t('payment.thunesTransfer.senderInfo') }}</div>
          <el-row :gutter="20">
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.firstName') }}: </span>
              <span class="detail-value">{{ transferForm.sender.firstName }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.lastName') }}: </span>
              <span class="detail-value">{{ transferForm.sender.lastName }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.countryIsoCode') }}: </span>
              <span class="detail-value">{{ transferForm.sender.countryIsoCode }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.provinceState') }}: </span>
              <span class="detail-value">{{ transferForm.sender.provinceState || '-' }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.city') }}: </span>
              <span class="detail-value">{{ transferForm.sender.city }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.address') }}: </span>
              <span class="detail-value">{{ transferForm.sender.address }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.postalCode') }}: </span>
              <span class="detail-value">{{ transferForm.sender.postalCode }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.email') }}: </span>
              <span class="detail-value">{{ transferForm.sender.email || '-' }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.msisdn') }}: </span>
              <span class="detail-value">{{ transferForm.sender.msisdn || '-' }}</span>
            </el-col>
          </el-row>
        </div>

        <!-- Sending business info -->
        <div v-if="showSendingBusiness" class="confirm-section shadow-box">
          <div class="confirm-title">{{ $t('payment.thunesTransfer.sendingBusinessInfo') }}</div>
          <el-row :gutter="20">
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.registeredName') }}: </span>
              <span class="detail-value">{{ transferForm.sendingBusiness.registeredName }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.countryIsoCode') }}: </span>
              <span class="detail-value">{{ transferForm.sendingBusiness.countryIsoCode }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.provinceState') }}: </span>
              <span class="detail-value">{{ transferForm.sendingBusiness.provinceState || '-' }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.city') }}: </span>
              <span class="detail-value">{{ transferForm.sendingBusiness.city }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.address') }}: </span>
              <span class="detail-value">{{ transferForm.sendingBusiness.address }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.postalCode') }}: </span>
              <span class="detail-value">{{ transferForm.sendingBusiness.postalCode }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.registrationNumber') }}: </span>
              <span class="detail-value">{{ transferForm.sendingBusiness.registrationNumber || '-' }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.code') }}: </span>
              <span class="detail-value">{{ transferForm.sendingBusiness.code || '-' }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.email') }}: </span>
              <span class="detail-value">{{ transferForm.sendingBusiness.email || '-' }}</span>
            </el-col>
          </el-row>
        </div>

        <!-- Beneficiary info -->
        <div v-if="showBeneficiaryIndividual" class="confirm-section shadow-box">
          <div class="confirm-title">{{ $t('payment.thunesTransfer.beneficiaryInfo') }}</div>
          <el-row :gutter="20">
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.firstName') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.firstName }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.lastName') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.lastName }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.countryIsoCode') }}: </span>
              <span class="detail-value">{{
                  transferForm.beneficiary.countryIsoCode || transferForm.targetCountry
                }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.provinceState') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.provinceState || '-' }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.city') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.city }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.address') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.address }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.city') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.city }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.postalCode') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.postalCode }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.email" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.email') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.email }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.msisdn" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.msisdn') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.msisdn }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.bankAccountNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.bankAccountNumber') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.bankAccountNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.abaRoutingNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.abaRoutingNumber') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.abaRoutingNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.accountType" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.accountType') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.accountType }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.iban" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.iban') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.iban }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.clabe" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.clabe') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.clabe }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.cbu" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.cbu') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.cbu }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.cbuAlias" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.cbuAlias') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.cbuAlias }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.swiftBicCode" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.swiftBicCode') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.swiftBicCode }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.bikCode" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.bikCode') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.bikCode }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.ifsCode" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.ifsCode') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.ifsCode }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.sortCode" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.sortCode') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.sortCode }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.bsbNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.bsbNumber') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.bsbNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.branchNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.branchNumber') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.branchNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.routingCode" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.routingCode') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.routingCode }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.entityTtId" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.entityTtId') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.entityTtId }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.accountNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.accountNumber') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.accountNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.cardAccountNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.cardAccountNumber') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.cardAccountNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.beneficiary.cardNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.cardNumber') }}: </span>
              <span class="detail-value">{{ transferForm.beneficiary.cardNumber }}</span>
            </el-col>
          </el-row>
        </div>

        <!-- Receiving business info -->
        <div v-if="showReceivingBusiness" class="confirm-section shadow-box">
          <div class="confirm-title">{{ $t('payment.thunesTransfer.receivingBusinessInfo') }}</div>
          <el-row :gutter="20">
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.registeredName') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.registeredName }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.countryIsoCode') }}: </span>
              <span class="detail-value">{{
                  transferForm.receivingBusiness.countryIsoCode || transferForm.targetCountry
                }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.provinceState') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.provinceState || '-' }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.city') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.city }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.address') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.address }}</span>
            </el-col>
            <el-col :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.postalCode') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.postalCode }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.email" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.email') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.email }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.msisdn" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.msisdn') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.msisdn }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.bankAccountNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.bankAccountNumber') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.bankAccountNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.abaRoutingNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.abaRoutingNumber') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.abaRoutingNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.accountType" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.accountType') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.accountType }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.iban" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.iban') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.iban }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.clabe" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.clabe') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.clabe }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.cbu" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.cbu') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.cbu }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.cbuAlias" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.cbuAlias') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.cbuAlias }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.swiftBicCode" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.swiftBicCode') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.swiftBicCode }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.bikCode" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.bikCode') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.bikCode }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.ifsCode" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.ifsCode') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.ifsCode }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.sortCode" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.sortCode') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.sortCode }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.bsbNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.bsbNumber') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.bsbNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.branchNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.branchNumber') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.branchNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.routingCode" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.routingCode') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.routingCode }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.entityTtId" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.entityTtId') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.entityTtId }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.accountNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.accountNumber') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.accountNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.cardAccountNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.cardAccountNumber') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.cardAccountNumber }}</span>
            </el-col>
            <el-col v-if="transferForm.receivingBusiness.cardNumber" :span="12" class="detail-item">
              <span class="detail-label">{{ $t('payment.thunesTransfer.cardNumber') }}: </span>
              <span class="detail-value">{{ transferForm.receivingBusiness.cardNumber }}</span>
            </el-col>
          </el-row>
        </div>

        <div style="margin-top: 30px; text-align: center">
          <el-button @click="activeStep = 1">{{ $t('payment.thunesTransfer.previousStep') }}</el-button>
          <el-button type="primary" :loading="submitting" @click="handleSubmitTransfer">{{ $t('payment.thunesTransfer.confirmAndSubmit') }}</el-button>
        </div>
      </div>

      <!-- Transfer Result -->
      <el-dialog :title="$t('payment.thunesTransfer.transferResult')" :visible.sync="resultDialogVisible" width="600px"
                 append-to-body>
        <div class="result-container">
          <div class="result-icon">
            <i v-if="transferResult.success" class="el-icon-success" style="color: #67C23A; font-size: 64px"></i>
            <i v-else class="el-icon-error" style="color: #F56C6C; font-size: 64px"></i>
          </div>
          <div class="result-title">
            {{
              transferResult.success ? $t('payment.thunesTransfer.transferSuccess') : $t('payment.thunesTransfer.transferFailed')
            }}
          </div>
          <div class="result-subtitle" v-if="transferResult.message">
            {{ transferResult.message }}
          </div>

          <div v-if="transferResult.success && transferResult.data" class="result-detail-wrapper">
            <div class="result-detail">
              <div class="detail-item">
                <span class="detail-label">{{ $t('payment.thunesTransfer.transferId') }}: </span>
                <span class="detail-value">{{ transferResult.data.transferId }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">{{ $t('payment.thunesTransfer.externalId') }}: </span>
                <span class="detail-value">{{ transferResult.data.externalId }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">{{ $t('payment.thunesTransfer.status') }}: </span>
                <span class="detail-value">{{ transferResult.data.status }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">{{ $t('payment.thunesTransfer.thunesTransactionId') }}: </span>
                <span class="detail-value">{{ transferResult.data.thunesTransactionId }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">{{ $t('payment.thunesTransfer.exchangeRate') }}: </span>
                <span class="detail-value">{{ transferResult.data.exchangeRate }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">{{ $t('payment.thunesTransfer.fee') }}: </span>
                <span class="detail-value">{{ transferResult.data.fee }} {{ transferResult.data.feeCurrency }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">{{ $t('payment.thunesTransfer.targetAmount') }}: </span>
                <span class="detail-value">{{ transferResult.data.targetAmount }} {{
                    transferResult.data.targetCurrency
                  }}</span>
              </div>
            </div>
          </div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="resultDialogVisible = false">{{ $t('payment.thunesTransfer.close') }}</el-button>
          <el-button v-if="transferResult.success" type="success" @click="handleViewTransferRecord">
            {{ $t('payment.thunesTransfer.viewTransferRecords') }}
          </el-button>
          <el-button v-if="transferResult.success" type="primary" @click="handleResetForm">{{ $t('payment.thunesTransfer.continueTransfer') }}</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import {getCountries, getMerchants, getPayers, transfer} from '@/api/payment/thunesTransfer'
import {getCities, getStates} from '@/api/payment/location'

// API field name -> Frontend field name mapping
const apiToFrontendMap = {
  'firstname': 'firstName',
  'lastname': 'lastName',
  'aba_routing_number': 'abaRoutingNumber',
  'account_type': 'accountType',
  'bank_account_number': 'bankAccountNumber',
  'country_iso_code': 'countryIsoCode',
  'postal_code': 'postalCode',
  'province_state': 'provinceState',
  'registered_name': 'registeredName',
  'registration_number': 'registrationNumber',
  'clabe': 'clabe',
  'cbu': 'cbu',
  'cbu_alias': 'cbuAlias',
  'swift_bic_code': 'swiftBicCode',
  'bik_code': 'bikCode',
  'ifs_code': 'ifsCode',
  'sort_code': 'sortCode',
  'bsb_number': 'bsbNumber',
  'branch_number': 'branchNumber',
  'routing_code': 'routingCode',
  'entity_tt_id': 'entityTtId',
  'account_number': 'accountNumber',
  'card_account_number': 'cardAccountNumber',
  'card_number': 'cardNumber',
  'msisdn': 'msisdn',
  'email': 'email'
}

export default {
  name: 'ThunesTransfer',
  data() {
    return {
      activeStep: 0,
      // Dictionary data
      dict: {
        type: {
          thunes_transfer_currency: []
        }
      },
      payerQuery: {
        countryCode: '',
        currency: ''
      },
      countryList: [],
      merchantList: [], // Merchant list
      payerList: [],
      selectedPayer: null,
      availableTransactionTypes: [],
      // Geography lists
      senderStates: [],
      senderCities: [],
      sendingBusinessStates: [],
      sendingBusinessCities: [],
      beneficiaryStates: [],
      beneficiaryCities: [],
      receivingBusinessStates: [],
      receivingBusinessCities: [],
      transferForm: {
        merchantId: null,
        signature: 'test_signature',
        externalId: '',
        amount: null,
        currency: 'USD',
        targetCountry: '',
        targetCurrency: '',
        transactionType: '',
        purposeOfRemittance: 'FAMILY_SUPPORT',
        callbackUrl: '',
        // Selected field combination index (unified)
        selectedFieldsIndex: 0,
        sender: {
          firstName: '',
          lastName: '',
          address: '',
          city: '',
          email: '',
          msisdn: '',
          postalCode: '',
          provinceState: '',
          countryIsoCode: 'MLT'
        },
        sendingBusiness: {
          registeredName: '',
          address: '',
          city: '',
          email: '',
          postalCode: '',
          provinceState: '',
          countryIsoCode: 'MLT',
          registrationNumber: '',
          code: ''
        },
        beneficiary: {
          firstName: '',
          lastName: '',
          bankAccountNumber: '',
          abaRoutingNumber: '',
          accountType: '',
          iban: '',
          clabe: '',
          cbu: '',
          cbuAlias: '',
          swiftBicCode: '',
          bikCode: '',
          ifsCode: '',
          sortCode: '',
          bsbNumber: '',
          branchNumber: '',
          routingCode: '',
          entityTtId: '',
          accountNumber: '',
          cardAccountNumber: '',
          cardNumber: '',
          msisdn: '',
          email: '',
          address: '',
          city: '',
          postalCode: '',
          provinceState: '',
          countryIsoCode: ''
        },
        receivingBusiness: {
          registeredName: '',
          address: '',
          city: '',
          email: '',
          bankAccountNumber: '',
          abaRoutingNumber: '',
          accountType: '',
          iban: '',
          clabe: '',
          cbu: '',
          cbuAlias: '',
          swiftBicCode: '',
          bikCode: '',
          ifsCode: '',
          sortCode: '',
          bsbNumber: '',
          branchNumber: '',
          routingCode: '',
          entityTtId: '',
          accountNumber: '',
          cardAccountNumber: '',
          cardNumber: '',
          postalCode: '',
          provinceState: '',
          countryIsoCode: '',
          registrationNumber: '',
          msisdn: ''
        }
      },
      transferRules: {},
      submitting: false,
      resultDialogVisible: false,
      transferResult: {
        success: false,
        message: '',
        data: null
      }
    }
  },
  computed: {
    showSenderIndividual() {
      return ['C2C', 'C2B'].includes(this.transferForm.transactionType)
    },
    showSendingBusiness() {
      return ['B2C', 'B2B'].includes(this.transferForm.transactionType)
    },
    showBeneficiaryIndividual() {
      return ['C2C', 'B2C'].includes(this.transferForm.transactionType)
    },
    showReceivingBusiness() {
      return ['C2B', 'B2B'].includes(this.transferForm.transactionType)
    },
    // All supported remittance purposes (internationalized)
    allPurposes() {
      return [
        { value: 'COMPUTER_SERVICES', label: this.$t('payment.thunesTransfer.purposes.COMPUTER_SERVICES') },
        { value: 'FAMILY_SUPPORT', label: this.$t('payment.thunesTransfer.purposes.FAMILY_SUPPORT') },
        { value: 'EDUCATION', label: this.$t('payment.thunesTransfer.purposes.EDUCATION') },
        { value: 'GIFT_AND_DONATION', label: this.$t('payment.thunesTransfer.purposes.GIFT_AND_DONATION') },
        { value: 'MEDICAL_TREATMENT', label: this.$t('payment.thunesTransfer.purposes.MEDICAL_TREATMENT') },
        { value: 'MAINTENANCE_EXPENSES', label: this.$t('payment.thunesTransfer.purposes.MAINTENANCE_EXPENSES') },
        { value: 'TRAVEL', label: this.$t('payment.thunesTransfer.purposes.TRAVEL') },
        { value: 'SMALL_VALUE_REMITTANCE', label: this.$t('payment.thunesTransfer.purposes.SMALL_VALUE_REMITTANCE') },
        { value: 'LIBERALIZED_REMITTANCE', label: this.$t('payment.thunesTransfer.purposes.LIBERALIZED_REMITTANCE') },
        { value: 'CONSTRUCTION_EXPENSES', label: this.$t('payment.thunesTransfer.purposes.CONSTRUCTION_EXPENSES') },
        { value: 'HOTEL_ACCOMMODATION', label: this.$t('payment.thunesTransfer.purposes.HOTEL_ACCOMMODATION') },
        { value: 'ADVERTISING_EXPENSES', label: this.$t('payment.thunesTransfer.purposes.ADVERTISING_EXPENSES') },
        { value: 'ADVISORY_FEES', label: this.$t('payment.thunesTransfer.purposes.ADVISORY_FEES') },
        { value: 'BUSINESS_INSURANCE', label: this.$t('payment.thunesTransfer.purposes.BUSINESS_INSURANCE') },
        { value: 'INSURANCE_CLAIMS', label: this.$t('payment.thunesTransfer.purposes.INSURANCE_CLAIMS') },
        { value: 'DELIVERY_FEES', label: this.$t('payment.thunesTransfer.purposes.DELIVERY_FEES') },
        { value: 'EXPORTED_GOODS', label: this.$t('payment.thunesTransfer.purposes.EXPORTED_GOODS') },
        { value: 'SERVICE_CHARGES', label: this.$t('payment.thunesTransfer.purposes.SERVICE_CHARGES') },
        { value: 'LOAN_PAYMENT', label: this.$t('payment.thunesTransfer.purposes.LOAN_PAYMENT') },
        { value: 'OFFICE_EXPENSES', label: this.$t('payment.thunesTransfer.purposes.OFFICE_EXPENSES') },
        { value: 'PROPERTY_PURCHASE', label: this.$t('payment.thunesTransfer.purposes.PROPERTY_PURCHASE') },
        { value: 'PROPERTY_RENTAL', label: this.$t('payment.thunesTransfer.purposes.PROPERTY_RENTAL') },
        { value: 'ROYALTY_FEES', label: this.$t('payment.thunesTransfer.purposes.ROYALTY_FEES') },
        { value: 'SHARES_INVESTMENT', label: this.$t('payment.thunesTransfer.purposes.SHARES_INVESTMENT') },
        { value: 'FUND_INVESTMENT', label: this.$t('payment.thunesTransfer.purposes.FUND_INVESTMENT') },
        { value: 'TAX_PAYMENT', label: this.$t('payment.thunesTransfer.purposes.TAX_PAYMENT') },
        { value: 'TRANSPORTATION_FEES', label: this.$t('payment.thunesTransfer.purposes.TRANSPORTATION_FEES') },
        { value: 'UTILITY_BILLS', label: this.$t('payment.thunesTransfer.purposes.UTILITY_BILLS') },
        { value: 'PERSONAL_TRANSFER', label: this.$t('payment.thunesTransfer.purposes.PERSONAL_TRANSFER') },
        { value: 'SALARY_PAYMENT', label: this.$t('payment.thunesTransfer.purposes.SALARY_PAYMENT') },
        { value: 'REWARD_PAYMENT', label: this.$t('payment.thunesTransfer.purposes.REWARD_PAYMENT') },
        { value: 'INFLUENCER_PAYMENT', label: this.$t('payment.thunesTransfer.purposes.INFLUENCER_PAYMENT') },
        { value: 'OTHER_FEES', label: this.$t('payment.thunesTransfer.purposes.OTHER_FEES') },
        { value: 'OTHER', label: this.$t('payment.thunesTransfer.purposes.OTHER') }
      ]
    },
    // Get supported remittance purposes list
    availablePurposes() {
      if (!this.selectedPayer || !this.transferForm.transactionType) {
        return []
      }
      const config = this.selectedPayer.transactionTypes[this.transferForm.transactionType]
      if (!config || !config.purposeOfRemittanceValuesAccepted) {
        return []
      }
      // Empty array means all values supported
      if (config.purposeOfRemittanceValuesAccepted.length === 0) {
        return []
      }
      // Map API values to options
      return config.purposeOfRemittanceValuesAccepted
        .map(value => {
          const purpose = this.allPurposes.find(p => p.value === value)
          return purpose || { value, label: value }
        })
    },
    // Get min transfer amount
    minTransactionAmount() {
      if (!this.selectedPayer || !this.transferForm.transactionType) {
        return null
      }
      const config = this.selectedPayer.transactionTypes[this.transferForm.transactionType]
      if (!config || !config.minimumTransactionAmount) {
        return null
      }
      const min = parseFloat(config.minimumTransactionAmount)
      return isNaN(min) ? null : min
    },
    // Get max transfer amount
    maxTransactionAmount() {
      if (!this.selectedPayer || !this.transferForm.transactionType) {
        return null
      }
      const config = this.selectedPayer.transactionTypes[this.transferForm.transactionType]
      if (!config || config.maximumTransactionAmount === null || config.maximumTransactionAmount === undefined) {
        return null
      }
      const max = parseFloat(config.maximumTransactionAmount)
      return isNaN(max) ? null : max
    },
    // Amount hint
    amountHint() {
      const min = this.minTransactionAmount
      const max = this.maxTransactionAmount

      if (min === null && max === null) {
        return ''
      }

      const minText = min !== null && min > 0 ? min.toString() : '0'
      const maxText = max !== null ? max.toString() : this.$t('common.unlimited') || 'Unlimited'

      return this.$t('payment.thunesTransfer.amountRange', { min: minText, max: maxText })
    },
    // Get unified field combination list
    fieldCombinations() {
      if (!this.selectedPayer || !this.transferForm.transactionType) {
        return []
      }
      const config = this.selectedPayer.transactionTypes[this.transferForm.transactionType]
      if (!config) {
        return []
      }

      const sendingFields = config.requiredSendingEntityFields || []
      const receivingFields = config.requiredReceivingEntityFields || []

      // Build combination descriptions
      const maxLength = Math.max(sendingFields.length, receivingFields.length)
      const combinations = []

      for (let i = 0; i < maxLength; i++) {
        const sendingGroup = i < sendingFields.length ? sendingFields[i] : []
        const receivingGroup = i < receivingFields.length ? receivingFields[i] : []

        const sendingFieldsList = Array.isArray(sendingGroup) ? sendingGroup : [sendingGroup]
        const receivingFieldsList = Array.isArray(receivingGroup) ? receivingGroup : [receivingGroup]

        const sendingLabels = sendingFieldsList.filter(f => f).map(f => this.getFieldLabel(apiToFrontendMap[f] || f)).join(',')
        const receivingLabels = receivingFieldsList.filter(f => f).map(f => this.getFieldLabel(apiToFrontendMap[f] || f)).join(',')

        combinations.push({
          index: i,
          sendingFields: sendingFieldsList,
          receivingFields: receivingFieldsList,
          sendingDescription: sendingLabels || this.$t('common.none') || 'None',
          receivingDescription: receivingLabels || this.$t('common.none') || 'None'
        })
      }

      return combinations
    }
  },
  created() {
    console.log('ThunesTransfer component created')
    this.loadDicts()
    this.loadCountries()
    this.loadMerchants()

    // Load initial states for default countries
    this.handleCountryChange('sender')
    this.handleCountryChange('sendingBusiness')
  },
  methods: {
    // Load dict data
    loadDicts() {
      this.getDicts('thunes_transfer_currency').then(response => {
        this.dict.type.thunes_transfer_currency = response.data
        console.log('Currency dict loaded:', this.dict.type.thunes_transfer_currency.length)
      }).catch(error => {
        console.error('Failed to load currency dict:', error)
        this.$message.error(this.$t('common.loadFailed') || 'Load failed')
      })
    },
    // Handle amount input, limit to 4 decimal places
    handleAmountInput(value) {
      // Allow only numbers and one decimal point
      let newValue = value.replace(/[^\d.]/g, '')

      // Keep only first decimal point
      const parts = newValue.split('.')
      if (parts.length > 2) {
        newValue = parts[0] + '.' + parts.slice(1).join('')
      }

      // Limit to 4 decimal places
      if (parts.length === 2 && parts[1].length > 4) {
        newValue = parts[0] + '.' + parts[1].substring(0, 4)
      }

      // Update as string
      this.transferForm.amount = newValue
    },
    // Handle amount blur, convert to number and trigger validation
    handleAmountBlur() {
      if (this.transferForm.amount) {
        // Convert to number type
        const numValue = parseFloat(this.transferForm.amount)
        if (!isNaN(numValue)) {
          this.transferForm.amount = numValue
        }
      }
      // Trigger validation
      this.$nextTick(() => {
        if (this.$refs.transferForm) {
          this.$refs.transferForm.validateField('amount')
        }
      })
    },
    loadCountries() {
      console.log('Loading countries...')
      getCountries().then(response => {
        console.log('Countries response:', response)
        this.countryList = response.data
        console.log('Country list loaded:', this.countryList.length)
      }).catch(error => {
        console.error('Failed to load countries:', error)
        this.$message.error((this.$t('common.loadFailed') || 'Load failed') + ': ' + (error.message || 'Unknown error'))
      })
    },
    loadMerchants() {
      console.log('Loading merchants...')
      getMerchants().then(response => {
        console.log('Merchants response:', response)
        this.merchantList = response.data
        console.log('Merchant list loaded:', this.merchantList.length)
      }).catch(error => {
        console.error('Failed to load merchants:', error)
        this.$message.error((this.$t('common.loadFailed') || 'Load failed') + ': ' + (error.message || 'Unknown error'))
      })
    },
    handleGetPayers() {
      getPayers(this.payerQuery).then(response => {
        this.payerList = response.data
        if (this.payerList.length === 0) {
          this.$message.warning(this.$t('payment.thunesTransfer.noPayersFound'))
        } else {
          this.$message.success(this.$t('payment.thunesTransfer.payersFound', { count: this.payerList.length }))
        }
      }).catch(error => {
        this.$message.error((this.$t('common.operationFailed') || 'Operation failed') + ': ' + error.message)
      })
    },
    resetPayerQuery() {
      this.payerQuery = {
        countryCode: '',
        currency: ''
      }
      this.payerList = []
    },
    handleSelectPayer(payer) {
      this.selectedPayer = payer
      this.transferForm.targetCountry = payer.countryIsoCode
      this.transferForm.targetCurrency = payer.currency

      // Auto-fill beneficiary country code
      this.transferForm.beneficiary.countryIsoCode = payer.countryIsoCode
      this.transferForm.receivingBusiness.countryIsoCode = payer.countryIsoCode

      this.availableTransactionTypes = Object.keys(payer.transactionTypes || {})

      // Load states for destination countries
      this.handleCountryChange('beneficiary')
      this.handleCountryChange('receivingBusiness')

      this.activeStep = 1
      this.$message.success(this.$t('payment.thunesTransfer.payerSelected', { name: payer.name }))
    },
    handleTransactionTypeChange() {
      // Reset field combination index
      this.transferForm.selectedFieldsIndex = 0

      // Clear related form data
      this.transferForm.sender = {
        firstName: '',
        lastName: '',
        address: '',
        city: '',
        email: '',
        msisdn: '',
        postalCode: '',
        provinceState: '',
        countryIsoCode: 'MLT'
      }
      this.transferForm.sendingBusiness = {
        registeredName: '',
        address: '',
        city: '',
        email: '',
        postalCode: '',
        provinceState: '',
        countryIsoCode: 'MLT',
        registrationNumber: '',
        code: ''
      }
      this.transferForm.beneficiary = {
        firstName: '',
        lastName: '',
        bankAccountNumber: '',
        abaRoutingNumber: '',
        accountType: '',
        iban: '',
        msisdn: '',
        email: '',
        address: '',
        city: '',
        postalCode: '',
        provinceState: '',
        countryIsoCode: ''
      }
      this.transferForm.receivingBusiness = {
        registeredName: '',
        address: '',
        city: '',
        email: '',
        bankAccountNumber: '',
        abaRoutingNumber: '',
        accountType: '',
        postalCode: '',
        provinceState: '',
        countryIsoCode: '',
        registrationNumber: ''
      }

      // Re-fill beneficiary country code
      if (this.selectedPayer) {
        this.transferForm.beneficiary.countryIsoCode = this.selectedPayer.countryIsoCode
        this.transferForm.receivingBusiness.countryIsoCode = this.selectedPayer.countryIsoCode
      }

      // Update dynamic validation rules
      this.updateDynamicRules()
    },
    // Update dynamic validation rules
    updateDynamicRules() {
      if (!this.selectedPayer || !this.transferForm.transactionType) {
        return
      }

      const config = this.selectedPayer.transactionTypes[this.transferForm.transactionType]
      if (!config) {
        return
      }

      // Clear old dynamic rules
      const baseRules = {
        merchantId: [{ required: true, message: this.$t('payment.thunesTransfer.validation.selectMerchant'), trigger: 'change' }],
        externalId: [{ required: true, message: this.$t('payment.thunesTransfer.validation.enterExternalId'), trigger: 'blur' }],
        amount: [
          { required: true, message: this.$t('payment.thunesTransfer.validation.enterAmount'), trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              // Allow string or number
              const numValue = typeof value === 'string' ? parseFloat(value) : value

              if (value === null || value === undefined || value === '') {
                callback(new Error(this.$t('payment.thunesTransfer.validation.enterAmount')))
                return
              }

              if (isNaN(numValue)) {
                callback(new Error(this.$t('payment.thunesTransfer.validation.amountMustBeNumber')))
                return
              }

              callback()
            },
            trigger: 'blur'
          }
        ],
        currency: [{ required: true, message: this.$t('payment.thunesTransfer.validation.selectCurrency'), trigger: 'change' }],
        targetCountry: [{ required: true, message: this.$t('payment.thunesTransfer.validation.enterTargetCountry'), trigger: 'blur' }],
        targetCurrency: [{ required: true, message: this.$t('payment.thunesTransfer.validation.selectTargetCurrency'), trigger: 'change' }],
        transactionType: [{ required: true, message: this.$t('payment.thunesTransfer.validation.selectTransactionType'), trigger: 'change' }],
        purposeOfRemittance: [{ required: true, message: this.$t('payment.thunesTransfer.validation.selectPurpose'), trigger: 'change' }]
      }

      // Add amount range validation
      const minAmount = this.minTransactionAmount
      const maxAmount = this.maxTransactionAmount

      if (minAmount !== null || maxAmount !== null) {
        baseRules.amount.push({
          validator: (rule, value, callback) => {
            // Convert to number
            const numValue = typeof value === 'string' ? parseFloat(value) : value

            if (value === null || value === undefined || value === '') {
              callback()
              return
            }

            if (isNaN(numValue)) {
              callback()
              return
            }

            if (minAmount !== null && numValue < minAmount) {
              callback(new Error(this.$t('payment.thunesTransfer.validation.amountTooSmall', { min: minAmount })))
              return
            }
            if (maxAmount !== null && numValue > maxAmount) {
              callback(new Error(this.$t('payment.thunesTransfer.validation.amountTooLarge', { max: maxAmount })))
              return
            }
            callback()
          },
          trigger: 'blur'
        })
      }

      // Get unified field combination index
      const selectedIndex = this.transferForm.selectedFieldsIndex || 0

      // Add sender required field rules (only for the selected combination)
      if (config.requiredSendingEntityFields && config.requiredSendingEntityFields.length > 0) {
        if (selectedIndex < config.requiredSendingEntityFields.length) {
          const selectedGroup = config.requiredSendingEntityFields[selectedIndex]
          const fieldGroup = Array.isArray(selectedGroup) ? selectedGroup : [selectedGroup]

          fieldGroup.forEach(apiField => {
            const frontendField = apiToFrontendMap[apiField] || apiField
            const fieldPath = this.showSenderIndividual ? `sender.${frontendField}` : `sendingBusiness.${frontendField}`
            baseRules[fieldPath] = [{ required: true, message: this.$t('payment.thunesTransfer.validation.enterField', { field: this.getFieldLabel(frontendField) }), trigger: 'blur' }]
          })
        }
      }

      // Add beneficiary required field rules（只添加选中组合的字段）
      if (config.requiredReceivingEntityFields && config.requiredReceivingEntityFields.length > 0) {
        if (selectedIndex < config.requiredReceivingEntityFields.length) {
          const selectedGroup = config.requiredReceivingEntityFields[selectedIndex]
          const fieldGroup = Array.isArray(selectedGroup) ? selectedGroup : [selectedGroup]

          fieldGroup.forEach(apiField => {
            const frontendField = apiToFrontendMap[apiField] || apiField
            const fieldPath = this.showBeneficiaryIndividual ? `beneficiary.${frontendField}` : `receivingBusiness.${frontendField}`
            baseRules[fieldPath] = [{ required: true, message: this.$t('payment.thunesTransfer.validation.enterField', { field: this.getFieldLabel(frontendField) }), trigger: 'blur' }]
          })
        }
      }

      // Add credit_party_identifiers required field rules (bank account info)
      if (config.creditPartyIdentifiersAccepted && config.creditPartyIdentifiersAccepted.length > 0) {
        config.creditPartyIdentifiersAccepted.forEach(fieldGroup => {
          if (Array.isArray(fieldGroup)) {
            fieldGroup.forEach(apiField => {
              const frontendField = apiToFrontendMap[apiField] || apiField
              const fieldPath = this.showBeneficiaryIndividual ? `beneficiary.${frontendField}` : `receivingBusiness.${frontendField}`
              if (!baseRules[fieldPath]) {
                baseRules[fieldPath] = [{ required: true, message: this.$t('payment.thunesTransfer.validation.enterField', { field: this.getFieldLabel(frontendField) }), trigger: 'blur' }]
              }
            })
          }
        })
      }

      this.transferRules = baseRules

      // Clear form validation status to avoid unnecessary validation hints
      this.$nextTick(() => {
        if (this.$refs.transferForm) {
          this.$refs.transferForm.clearValidate()
        }
      })
    },
    // Get field label from i18n
    getFieldLabel(field) {
      const labelKey = `payment.thunesTransfer.${field}`
      const label = this.$t(labelKey)
      return label && label !== labelKey ? label : field
    },
    // Get field placeholder for inputs
    getFieldPlaceholder(fieldPath) {
      // Try to get i18n description
      const i18nKey = `payment.thunesTransfer.desc.${fieldPath}`
      const description = this.$t(i18nKey)

      if (description && description !== i18nKey) {
        return `${this.$t('common.pleaseEnter') || 'Please enter'}: ${description}`
      }

      // Fallback to field label
      const fieldName = fieldPath.split('.').pop()
      const label = this.getFieldLabel(fieldName)
      return `${this.$t('common.pleaseEnter') || 'Please enter'} ${label}`
    },
    // Check if field is required
    isFieldRequired(fieldPath) {
      if (!this.selectedPayer || !this.transferForm.transactionType) {
        return false
      }

      const config = this.selectedPayer.transactionTypes[this.transferForm.transactionType]
      if (!config) {
        return false
      }

      // 提取字段名（去掉前缀如 sender. 或 beneficiary.）
      const fieldName = fieldPath.split('.').pop()
      const prefix = fieldPath.split('.')[0] // sender, beneficiary, sendingBusiness, receivingBusiness

      // 字段名映射：前端字段名 -> API字段名
      const fieldNameMap = {
        'firstName': 'firstname',
        'lastName': 'lastname',
        'abaRoutingNumber': 'aba_routing_number',
        'accountType': 'account_type',
        'bankAccountNumber': 'bank_account_number',
        'countryIsoCode': 'country_iso_code',
        'postalCode': 'postal_code',
        'provinceState': 'province_state',
        'registrationNumber': 'registration_number',
        'clabe': 'clabe',
        'cbu': 'cbu',
        'cbuAlias': 'cbu_alias',
        'swiftBicCode': 'swift_bic_code',
        'bikCode': 'bik_code',
        'ifsCode': 'ifs_code',
        'sortCode': 'sort_code',
        'bsbNumber': 'bsb_number',
        'branchNumber': 'branch_number',
        'routingCode': 'routing_code',
        'entityTtId': 'entity_tt_id',
        'accountNumber': 'account_number',
        'cardAccountNumber': 'card_account_number',
        'cardNumber': 'card_number',
        'msisdn': 'msisdn',
        'email': 'email'
      }

      // 转换为API字段名
      const apiFieldName = fieldNameMap[fieldName] || fieldName.toLowerCase()

      // Get unified field combination index
      const selectedIndex = this.transferForm.selectedFieldsIndex || 0

      // 检查发送方字段
      if (prefix === 'sender' || prefix === 'sendingBusiness') {
        const requiredSending = config.requiredSendingEntityFields || []
        if (requiredSending.length === 0 || selectedIndex >= requiredSending.length) {
          return false
        }

        // 只检查选中组合的字段
        const selectedGroup = requiredSending[selectedIndex]
        return Array.isArray(selectedGroup) ? selectedGroup.includes(apiFieldName) : selectedGroup === apiFieldName
      }

      // 检查收款方字段
      if (prefix === 'beneficiary' || prefix === 'receivingBusiness') {
        // 先检查 requiredReceivingEntityFields
        const requiredReceiving = config.requiredReceivingEntityFields || []
        if (requiredReceiving.length > 0 && selectedIndex < requiredReceiving.length) {
          const selectedGroup = requiredReceiving[selectedIndex]
          const isInReceivingFields = Array.isArray(selectedGroup) ? selectedGroup.includes(apiFieldName) : selectedGroup === apiFieldName
          if (isInReceivingFields) {
            return true
          }
        }

        // 再检查 credit_party_identifiers_accepted（银行账户信息）
        const creditPartyIdentifiers = config.creditPartyIdentifiersAccepted || []
        if (creditPartyIdentifiers.length > 0) {
          // 遍历所有组合，检查是否包含该字段
          for (const identifierGroup of creditPartyIdentifiers) {
            if (Array.isArray(identifierGroup)) {
              if (identifierGroup.includes(apiFieldName)) {
                return true
              }
            } else if (identifierGroup === apiFieldName) {
              return true
            }
          }
        }

        return false
      }

      return false
    },
    handleNextStep() {
      // 先进行表单验证
      this.$refs.transferForm.validate(valid => {
        if (!valid) {
          this.$message.error(this.$t('payment.thunesTransfer.validation.completeTransferInfo') || 'Please complete the transfer information')
          return false
        }

        // 额外检查所有必填字段是否都有值
        const missingFields = []

        // 检查基础字段
        if (!this.transferForm.merchantId) missingFields.push(this.$t('payment.thunesTransfer.merchantId'))
        if (!this.transferForm.externalId) missingFields.push(this.$t('payment.thunesTransfer.externalId'))
        if (!this.transferForm.amount) missingFields.push(this.$t('payment.thunesTransfer.amount'))
        if (!this.transferForm.currency) missingFields.push(this.$t('payment.thunesTransfer.transferCurrency'))
        if (!this.transferForm.targetCountry) missingFields.push(this.$t('payment.thunesTransfer.targetCountry'))
        if (!this.transferForm.targetCurrency) missingFields.push(this.$t('payment.thunesTransfer.targetCurrency'))
        if (!this.transferForm.transactionType) missingFields.push(this.$t('payment.thunesTransfer.transactionType'))
        if (!this.transferForm.purposeOfRemittance) missingFields.push(this.$t('payment.thunesTransfer.purposeOfRemittance'))

        // 检查发送方个人信息（C2C/C2B）
        if (this.showSenderIndividual) {
          if (this.isFieldRequired('sender.firstName') && !this.transferForm.sender.firstName) {
            missingFields.push(`${this.$t('payment.thunesTransfer.senderInfo')} - ${this.$t('payment.thunesTransfer.firstName')}`)
          }
          if (this.isFieldRequired('sender.lastName') && !this.transferForm.sender.lastName) {
            missingFields.push(`${this.$t('payment.thunesTransfer.senderInfo')} - ${this.$t('payment.thunesTransfer.lastName')}`)
          }
          if (this.isFieldRequired('sender.address') && !this.transferForm.sender.address) {
            missingFields.push(`${this.$t('payment.thunesTransfer.senderInfo')} - ${this.$t('payment.thunesTransfer.address')}`)
          }
          if (this.isFieldRequired('sender.city') && !this.transferForm.sender.city) {
            missingFields.push(`${this.$t('payment.thunesTransfer.senderInfo')} - ${this.$t('payment.thunesTransfer.city')}`)
          }
          if (this.isFieldRequired('sender.postalCode') && !this.transferForm.sender.postalCode) {
            missingFields.push(`${this.$t('payment.thunesTransfer.senderInfo')} - ${this.$t('payment.thunesTransfer.postalCode')}`)
          }
          if (this.isFieldRequired('sender.provinceState') && !this.transferForm.sender.provinceState) {
            missingFields.push(`${this.$t('payment.thunesTransfer.senderInfo')} - ${this.$t('payment.thunesTransfer.provinceState')}`)
          }
          if (this.isFieldRequired('sender.countryIsoCode') && !this.transferForm.sender.countryIsoCode) {
            missingFields.push(`${this.$t('payment.thunesTransfer.senderInfo')} - ${this.$t('payment.thunesTransfer.countryIsoCode')}`)
          }
          if (this.isFieldRequired('sender.email') && !this.transferForm.sender.email) {
            missingFields.push(`${this.$t('payment.thunesTransfer.senderInfo')} - ${this.$t('payment.thunesTransfer.email')}`)
          }
          if (this.isFieldRequired('sender.msisdn') && !this.transferForm.sender.msisdn) {
            missingFields.push(`${this.$t('payment.thunesTransfer.senderInfo')} - ${this.$t('payment.thunesTransfer.msisdn')}`)
          }
        }

        // 检查发送方企业信息（B2C/B2B）
        if (this.showSendingBusiness) {
          if (this.isFieldRequired('sendingBusiness.registeredName') && !this.transferForm.sendingBusiness.registeredName) {
            missingFields.push(`${this.$t('payment.thunesTransfer.sendingBusinessInfo')} - ${this.$t('payment.thunesTransfer.registeredName')}`)
          }
          if (this.isFieldRequired('sendingBusiness.address') && !this.transferForm.sendingBusiness.address) {
            missingFields.push(`${this.$t('payment.thunesTransfer.sendingBusinessInfo')} - ${this.$t('payment.thunesTransfer.address')}`)
          }
          if (this.isFieldRequired('sendingBusiness.city') && !this.transferForm.sendingBusiness.city) {
            missingFields.push(`${this.$t('payment.thunesTransfer.sendingBusinessInfo')} - ${this.$t('payment.thunesTransfer.city')}`)
          }
          if (this.isFieldRequired('sendingBusiness.postalCode') && !this.transferForm.sendingBusiness.postalCode) {
            missingFields.push(`${this.$t('payment.thunesTransfer.sendingBusinessInfo')} - ${this.$t('payment.thunesTransfer.postalCode')}`)
          }
          if (this.isFieldRequired('sendingBusiness.provinceState') && !this.transferForm.sendingBusiness.provinceState) {
            missingFields.push(`${this.$t('payment.thunesTransfer.sendingBusinessInfo')} - ${this.$t('payment.thunesTransfer.provinceState')}`)
          }
          if (this.isFieldRequired('sendingBusiness.countryIsoCode') && !this.transferForm.sendingBusiness.countryIsoCode) {
            missingFields.push(`${this.$t('payment.thunesTransfer.sendingBusinessInfo')} - ${this.$t('payment.thunesTransfer.countryIsoCode')}`)
          }
          if (this.isFieldRequired('sendingBusiness.registrationNumber') && !this.transferForm.sendingBusiness.registrationNumber) {
            missingFields.push(`${this.$t('payment.thunesTransfer.sendingBusinessInfo')} - ${this.$t('payment.thunesTransfer.registrationNumber')}`)
          }
          if (this.isFieldRequired('sendingBusiness.code') && !this.transferForm.sendingBusiness.code) {
            missingFields.push(`${this.$t('payment.thunesTransfer.sendingBusinessInfo')} - ${this.$t('payment.thunesTransfer.code')}`)
          }
          if (this.isFieldRequired('sendingBusiness.email') && !this.transferForm.sendingBusiness.email) {
            missingFields.push(`${this.$t('payment.thunesTransfer.sendingBusinessInfo')} - ${this.$t('payment.thunesTransfer.email')}`)
          }
        }

        // 检查收款方个人信息（C2C/B2C）
        if (this.showBeneficiaryIndividual) {
          if (this.isFieldRequired('beneficiary.firstName') && !this.transferForm.beneficiary.firstName) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.firstName')}`)
          }
          if (this.isFieldRequired('beneficiary.lastName') && !this.transferForm.beneficiary.lastName) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.lastName')}`)
          }
          if (this.isFieldRequired('beneficiary.address') && !this.transferForm.beneficiary.address) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.address')}`)
          }
          if (this.isFieldRequired('beneficiary.city') && !this.transferForm.beneficiary.city) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.city')}`)
          }
          if (this.isFieldRequired('beneficiary.postalCode') && !this.transferForm.beneficiary.postalCode) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.postalCode')}`)
          }
          if (this.isFieldRequired('beneficiary.provinceState') && !this.transferForm.beneficiary.provinceState) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.provinceState')}`)
          }
          if (this.isFieldRequired('beneficiary.countryIsoCode') && !this.transferForm.beneficiary.countryIsoCode) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.countryIsoCode')}`)
          }
          // 银行账号始终必填
          if (!this.transferForm.beneficiary.bankAccountNumber) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.bankAccountNumber')}`)
          }
          // 只检查显示的美国特有字段
          if (this.isFieldRequired('beneficiary.abaRoutingNumber') && !this.transferForm.beneficiary.abaRoutingNumber) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.abaRoutingNumber')}`)
          }
          if (this.isFieldRequired('beneficiary.accountType') && !this.transferForm.beneficiary.accountType) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.accountType')}`)
          }
          if (this.isFieldRequired('beneficiary.iban') && !this.transferForm.beneficiary.iban) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.iban')}`)
          }
          if (this.isFieldRequired('beneficiary.email') && !this.transferForm.beneficiary.email) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.email')}`)
          }
          if (this.isFieldRequired('beneficiary.msisdn') && !this.transferForm.beneficiary.msisdn) {
            missingFields.push(`${this.$t('payment.thunesTransfer.beneficiaryInfo')} - ${this.$t('payment.thunesTransfer.msisdn')}`)
          }
        }

        // 检查收款方企业信息（C2B/B2B）
        if (this.showReceivingBusiness) {
          if (this.isFieldRequired('receivingBusiness.registeredName') && !this.transferForm.receivingBusiness.registeredName) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.registeredName')}`)
          }
          if (this.isFieldRequired('receivingBusiness.address') && !this.transferForm.receivingBusiness.address) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.address')}`)
          }
          if (this.isFieldRequired('receivingBusiness.city') && !this.transferForm.receivingBusiness.city) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.city')}`)
          }
          if (this.isFieldRequired('receivingBusiness.postalCode') && !this.transferForm.receivingBusiness.postalCode) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.postalCode')}`)
          }
          if (this.isFieldRequired('receivingBusiness.provinceState') && !this.transferForm.receivingBusiness.provinceState) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.provinceState')}`)
          }
          if (this.isFieldRequired('receivingBusiness.countryIsoCode') && !this.transferForm.receivingBusiness.countryIsoCode) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.countryIsoCode')}`)
          }
          if (this.isFieldRequired('receivingBusiness.registrationNumber') && !this.transferForm.receivingBusiness.registrationNumber) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.registrationNumber')}`)
          }
          if (this.isFieldRequired('receivingBusiness.email') && !this.transferForm.receivingBusiness.email) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.email')}`)
          }
          // 银行账号始终必填
          if (!this.transferForm.receivingBusiness.bankAccountNumber) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.bankAccountNumber')}`)
          }
          // 只检查显示的美国特有字段
          if (this.isFieldRequired('receivingBusiness.abaRoutingNumber') && !this.transferForm.receivingBusiness.abaRoutingNumber) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.abaRoutingNumber')}`)
          }
          if (this.isFieldRequired('receivingBusiness.accountType') && !this.transferForm.receivingBusiness.accountType) {
            missingFields.push(`${this.$t('payment.thunesTransfer.receivingBusinessInfo')} - ${this.$t('payment.thunesTransfer.accountType')}`)
          }
        }

        // 如果有缺失字段，显示详细提示
        if (missingFields.length > 0) {
          const separator = this.$t('common.separator') || ','
          const fieldList = missingFields.join(separator)
          this.$message.error(`${this.$t('payment.thunesTransfer.validation.enterField', { field: '' })}${fieldList}`)
          return false
        }

        // 所有验证通过，进入下一步
        this.activeStep = 2
      })
    },
    handleSubmitTransfer() {
      this.submitting = true

      // 根据交易类型自动填充 documentReferenceNumber
      const transactionType = this.transferForm.transactionType
      let documentReferenceNumber = ''

      switch (transactionType) {
        case 'B2B':
          // B2B: 使用发送方企业注册号
          documentReferenceNumber = this.transferForm.sendingBusiness.registrationNumber || ''
          break
        case 'B2C':
          // B2C: 使用发送方企业注册号
          documentReferenceNumber = this.transferForm.sendingBusiness.registrationNumber || ''
          break
        case 'C2B':
          // C2B: 使用收款方企业注册号
          documentReferenceNumber = this.transferForm.receivingBusiness.registrationNumber || ''
          break
        case 'C2C':
          // C2C: 不需要 documentReferenceNumber
          break
      }

      // 构建提交数据，添加 documentReferenceNumber
      const submitData = {
        ...this.transferForm,
        documentReferenceNumber: documentReferenceNumber
      }

      transfer(submitData).then(response => {
        console.log('Thunes transfer success response:', response)
        this.transferResult = {
          success: true,
          message: this.$t('payment.thunesTransfer.transferRequestSubmitted') || 'Transfer request submitted successfully',
          data: response.data || response
        }
        this.resultDialogVisible = true
      }).catch(error => {
        console.error('Thunes transfer error:', error)
        this.transferResult = {
          success: false,
          message: error.message || this.$t('payment.thunesTransfer.transferFailed'),
          data: null
        }
        this.resultDialogVisible = true
      }).finally(() => {
        this.submitting = false
      })
    },
    handleResetForm() {
      this.resultDialogVisible = false
      this.activeStep = 0
      this.selectedPayer = null
      this.payerList = []
      this.resetPayerQuery()
      this.$refs.transferForm.resetFields()
    },
    handleViewTransferRecord() {
      this.resultDialogVisible = false
      // Match the actual path shown in the browser: /order_manager/transferRecord
      this.$router.push({path: '/order_manager/transferRecord'})
    },
    // Get selected merchant name
    getSelectedMerchantName() {
      if (!this.transferForm.merchantId || !this.merchantList.length) {
        return '-'
      }
      const merchant = this.merchantList.find(m => m.id === this.transferForm.merchantId)
      return merchant ? `${merchant.businessName} (${merchant.userName})` : '-'
    },
    // Get purpose label
    getPurposeLabel(value) {
      const purpose = this.allPurposes.find(p => p.value === value)
      return purpose ? purpose.label : value
    },
    // Location methods
    handleCountryChange(type) {
      if (!this.transferForm[type]) return

      const countryCode = this.transferForm[type].countryIsoCode
      // Reset state and city
      this.transferForm[type].provinceState = ''
      this.transferForm[type].city = ''
      this[type + 'States'] = []
      this[type + 'Cities'] = []

      if (countryCode) {
        getStates(countryCode).then(response => {
          this[type + 'States'] = response.data || []
        }).catch(err => {
          console.error('Failed to load states for ' + countryCode, err)
        })
      }
    },
    handleStateChange(type) {
      if (!this.transferForm[type]) return

      const countryCode = this.transferForm[type].countryIsoCode
      const stateName = this.transferForm[type].provinceState

      // Reset city
      this.transferForm[type].city = ''
      this[type + 'Cities'] = []

      if (countryCode && stateName) {
        getCities(countryCode, stateName).then(response => {
          this[type + 'Cities'] = response.data || []
        }).catch(err => {
          console.error('Failed to load cities for ' + stateName, err)
        })
      }
    }
  }
}
</script>

<style scoped>
.box-card {
  margin: 20px;
}

.confirm-container {
  padding: 10px;
}

.confirm-section {
  margin-bottom: 25px;
  padding: 15px;
  background-color: #fff;
  border-radius: 4px;
}

.shadow-box {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: 1px solid #EBEEF5;
}

.confirm-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
}

.detail-item {
  margin-bottom: 15px;
  line-height: 24px;
}

.detail-label {
  font-weight: 500;
  color: #909399;
  font-size: 14px;
}

.detail-value {
  color: #303133;
  font-size: 14px;
  word-break: break-all;
  font-weight: 500;
}

.result-detail {
  padding: 10px;
  background-color: #fafafa;
  border-radius: 4px;
  border: 1px solid #ebeef5;
}

.result-detail .detail-item {
  margin-bottom: 8px;
  display: flex;
}

.result-detail .detail-label {
  width: 150px;
}

.result-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}

.result-icon {
  margin-bottom: 15px;
}

.result-title {
  font-size: 24px;
  color: #303133;
  font-weight: 500;
  margin-bottom: 10px;
}

.result-subtitle {
  font-size: 14px;
  color: #606266;
  margin-bottom: 20px;
  text-align: center;
}

.result-detail-wrapper {
  width: 100%;
  margin-top: 10px;
}
</style>
