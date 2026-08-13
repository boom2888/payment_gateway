<template>
  <div :class="{ 'app-container': true, 'full-screen': true }">
    <div class="header">
      <language-switch class="language-switch-btn" />
      <el-button type="text" class="logout-btn" @click="logout">
        <i class="el-icon-switch-button" />
        {{ $t('payment.emiAuth.logout') }}
      </el-button>
    </div>

    <div class="form-container">
      <!-- 状态 -->
      <div class="status-section">
        <span class="status-label">{{ $t('payment.emiAuth.reviewStatus') }}</span>
        <el-tag v-if="status === 0" type="info">{{ $t('payment.emiAuth.statusPendingSubmit') }}</el-tag>
        <el-tag v-else-if="status === 1" type="warning">{{ $t('payment.emiAuth.statusPendingReview') }}</el-tag>
        <el-tag v-else-if="status === 2" type="primary">{{ $t('payment.emiAuth.statusPendingDoubleCheck') }}</el-tag>
        <el-tag v-else-if="status === 3" type="success">{{ $t('payment.emiAuth.statusApproved') }}</el-tag>
        <el-tag v-else-if="status === 4" type="success">{{ $t('payment.emiAuth.statusRejected') }}</el-tag>
      </div>

      <!-- 账户类型选择 -->
      <el-form
        ref="accountTypeForm"
        :model="accountTypeFormData"
        :rules="accountTypeRules"
        :label-width="formLabelWidth"
      >
        <el-form-item :label="$t('payment.emiAuth.accountType')" prop="accountType">
          <el-button-group class="account-type-switch">
            <el-button
              v-if="!hasEchoData || (hasEchoData && userType === 1)"
              :type="accountType === 'personal' ? 'primary' : ''"
              :disabled="hasEchoData && userType === 1"
              @click="switchAccountType('personal')"
            >
              {{ $t('payment.emiAuth.personalAccount') }}
            </el-button>
            <el-button
              v-if="!hasEchoData || (hasEchoData && userType === 2)"
              :type="accountType === 'company' ? 'primary' : ''"
              :disabled="hasEchoData && userType === 2"
              @click="switchAccountType('company')"
            >
              {{ $t('payment.emiAuth.companyAccount') }}
            </el-button>
          </el-button-group>
        </el-form-item>
      </el-form>

      <!-- 个人账户表单 -->
      <el-form
        v-show="accountType === 'personal'"
        ref="personalForm"
        :model="personalForm"
        :rules="personalRules"
        :label-width="formLabelWidth"
      >
        <!-- 国籍 -->
        <el-form-item :label="$t('payment.emiAuth.nationality')" prop="nationality">
          <el-select
            v-model="personalForm.nationality"
            :placeholder="$t('payment.emiAuth.selectNationality')"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="country in countryOptions"
              :key="country.code"
              :label="country.name"
              :value="country.code"
            />
          </el-select>
        </el-form-item>

        <!-- 邮箱账号 -->
        <el-form-item :label="$t('payment.emiAuth.emailAccount')" prop="email">
          <el-input v-model="personalForm.email" :placeholder="$t('payment.emiAuth.enterEmail')" disabled />
        </el-form-item>

        <!-- 姓名 -->
        <el-form-item :label="$t('payment.emiAuth.fullName')" prop="fullName">
          <el-input v-model="personalForm.fullName" :placeholder="$t('payment.emiAuth.enterFullName')" />
        </el-form-item>

        <!-- 证件号 -->
        <el-form-item :label="$t('payment.emiAuth.idNumber')" prop="idNumber">
          <el-input v-model="personalForm.idNumber" :placeholder="$t('payment.emiAuth.enterIdNumber')" />
        </el-form-item>

        <!-- 性别 -->
        <el-form-item :label="$t('payment.emiAuth.gender')" prop="gender">
          <el-radio-group v-model="personalForm.gender">
            <el-radio v-for="item in sexOptions" :key="item.dictValue" :label="item.dictValue">
              {{ item.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 出生日期 -->
        <el-form-item :label="$t('payment.emiAuth.birthDate')" prop="birthDate">
          <el-date-picker
            v-model="personalForm.birthDate"
            type="date"
            :placeholder="$t('payment.emiAuth.selectBirthDate')"
            style="width: 100%"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <!-- 居住国家 -->
        <el-form-item :label="$t('payment.emiAuth.residenceCountry')" prop="residenceCountry">
          <el-select
            v-model="personalForm.residenceCountry"
            :placeholder="$t('payment.emiAuth.selectResidenceCountry')"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="country in countryOptions"
              :key="country.code"
              :label="country.name"
              :value="country.code"
            />
          </el-select>
        </el-form-item>

        <!-- 居住地址 -->
        <el-form-item :label="$t('payment.emiAuth.residenceAddress')" prop="residenceAddress">
          <el-input
            v-model="personalForm.residenceAddress"
            :placeholder="$t('payment.emiAuth.enterResidenceAddress')"
            @blur="handleResidenceAddressBlur"
          />
        </el-form-item>

        <!-- 居住邮编 -->
        <el-form-item :label="$t('payment.emiAuth.residencePostalCode')" prop="residencePostalCode">
          <el-input
            v-model="personalForm.residencePostalCode"
            :placeholder="$t('payment.emiAuth.enterResidencePostalCode')"
          />
        </el-form-item>

        <!-- 联系电话 -->
        <el-form-item :label="$t('payment.emiAuth.contactPhone')" prop="contactPhone">
          <el-input
            v-model="personalForm.contactPhone"
            :placeholder="$t('payment.emiAuth.enterContactPhone')"
            disabled
          />
        </el-form-item>

        <!-- 资金来源 -->
        <el-form-item :label="$t('payment.emiAuth.fundSource')" prop="fundSource">
          <el-input
            v-model="personalForm.fundSource"
            :placeholder="$t('payment.emiAuth.enterFundSource')"
            style="width: 100%"
          />
        </el-form-item>

        <!-- 证件类型 -->
        <el-form-item :label="$t('payment.emiAuth.idType')" prop="idType">
          <el-select
            v-model="personalForm.idType"
            :placeholder="$t('payment.emiAuth.selectIdType')"
            style="width: 100%"
          >
            <el-option
              v-for="item in documentType"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
        </el-form-item>

        <!-- 证件照片 -->
        <el-form-item :label="$t('payment.emiAuth.idPhoto')" prop="idPhoto">
          <el-upload
            class="id-photo-uploader"
            :action="uploadUrl"
            :headers="headers"
            :file-list="personalIdPhotoFileList"
            list-type="picture-card"
            :on-success="handlePersonalIdPhotoSuccess"
            :on-remove="handlePersonalIdPhotoRemove"
            :before-upload="beforeIdPhotoUpload"
            :limit="1"
            accept="image/*"
          >
            <i class="el-icon-plus" />
          </el-upload>
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item v-if="status === 0 || status === 1 || status === 4">
          <el-button
            type="primary"
            size="large"
            style="width: 100%; margin-top: 20px"
            :loading="personalSubmitting"
            :disabled="personalSubmitting"
            @click="submitPersonalForm"
          >
            {{ personalSubmitting ? $t('payment.emiAuth.submitting') : $t('payment.emiAuth.submit') }}
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 企业账户表单 -->
      <el-form
        v-show="accountType === 'company'"
        ref="companyFormRef"
        :model="companyForm"
        :rules="companyRules"
        :label-width="formLabelWidth"
      >
        <!-- 国籍 -->
        <el-form-item :label="$t('payment.emiAuth.nationality')" prop="nationality">
          <el-select
            v-model="companyForm.nationality"
            :placeholder="$t('payment.emiAuth.selectNationality')"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="country in countryOptions"
              :key="country.code"
              :label="country.name"
              :value="country.code"
            />
          </el-select>
        </el-form-item>

        <!-- 邮箱账号 -->
        <el-form-item :label="$t('payment.emiAuth.emailAccount')" prop="email">
          <el-input v-model="companyForm.email" :placeholder="$t('payment.emiAuth.enterEmail')" disabled />
        </el-form-item>

        <!-- 法人姓名 -->
        <el-form-item :label="$t('payment.emiAuth.legalPersonName')" prop="fullName">
          <el-input v-model="companyForm.fullName" :placeholder="$t('payment.emiAuth.enterLegalPersonName')" />
        </el-form-item>

        <!-- 证件号 -->
        <el-form-item :label="$t('payment.emiAuth.idNumber')" prop="idNumber">
          <el-input v-model="companyForm.idNumber" :placeholder="$t('payment.emiAuth.enterIdNumber')" />
        </el-form-item>

        <!-- 企业中文名称 -->
        <el-form-item :label="$t('payment.emiAuth.companyNameChinese')" prop="companyNameChinese">
          <el-input
            v-model="companyForm.companyNameChinese"
            :placeholder="$t('payment.emiAuth.enterCompanyNameChinese')"
          />
        </el-form-item>

        <!-- 企业英文名称 -->
        <el-form-item :label="$t('payment.emiAuth.companyNameEnglish')" prop="companyNameEnglish">
          <el-input
            v-model="companyForm.companyNameEnglish"
            :placeholder="$t('payment.emiAuth.enterCompanyNameEnglish')"
          />
        </el-form-item>

        <!-- 商业登记证编号 -->
        <el-form-item :label="$t('payment.emiAuth.businessRegistrationNumber')" prop="businessRegistrationNumber">
          <el-input
            v-model="companyForm.businessRegistrationNumber"
            :placeholder="$t('payment.emiAuth.enterBusinessRegistrationNumber')"
          />
        </el-form-item>

        <!-- 公司注册代码 -->
        <el-form-item :label="$t('payment.emiAuth.companyRegistrationCode')" prop="companyRegistrationCode">
          <el-input
            v-model="companyForm.companyRegistrationCode"
            :placeholder="$t('payment.emiAuth.enterCompanyRegistrationCode')"
          />
        </el-form-item>

        <!-- 企业注册国家 -->
        <el-form-item :label="$t('payment.emiAuth.registeredCountry')" prop="registeredCountry">
          <el-select
            v-model="companyForm.registeredCountry"
            :placeholder="$t('payment.emiAuth.selectRegisteredCountry')"
            filterable
            style="width: 100%"
            @change="handleRegistrationCountryChange"
          >
            <el-option
              v-for="country in countryOptions"
              :key="country.code"
              :label="country.name"
              :value="country.code"
            />
          </el-select>
        </el-form-item>

        <!-- 企业注册地址 -->
        <el-form-item :label="$t('payment.emiAuth.registeredAddress')" prop="registeredAddress">
          <el-input
            v-model="companyForm.registeredAddress"
            :placeholder="$t('payment.emiAuth.enterRegisteredAddress')"
          />
        </el-form-item>

        <!-- 实际经营地址 -->
        <el-form-item :label="$t('payment.emiAuth.actualOperationAddress')" prop="actualOperationAddress">
          <el-input
            v-model="companyForm.actualOperationAddress"
            :placeholder="$t('payment.emiAuth.enterActualOperationAddress')"
          />
        </el-form-item>

        <!-- 企业人数 -->
        <el-form-item :label="$t('payment.emiAuth.employeeCount')" prop="employeeCount">
          <el-input
            v-model="companyForm.employeeCount"
            :placeholder="$t('payment.emiAuth.enterEmployeeCount')"
            type="number"
            style="width: 100%"
          />
        </el-form-item>

        <!-- 企业成立日期 -->
        <el-form-item :label="$t('payment.emiAuth.establishmentDate')" prop="establishmentDate">
          <el-date-picker
            v-model="companyForm.establishmentDate"
            type="date"
            :placeholder="$t('payment.emiAuth.selectEstablishmentDate')"
            style="width: 100%"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <!-- 企业到期日期 -->
        <el-form-item :label="$t('payment.emiAuth.expirationDate')" prop="expirationDate">
          <div style="display: flex; align-items: center; gap: 10px">
            <el-date-picker
              v-model="companyForm.expirationDate"
              type="date"
              :placeholder="$t('payment.emiAuth.selectExpirationDate')"
              style="flex: 1"
              value-format="yyyy-MM-dd"
              :disabled="companyForm.longTermValid"
            />
          </div>
        </el-form-item>

        <!-- 行业类型 -->
        <el-form-item :label="$t('payment.emiAuth.industryType')" prop="industryType">
          <el-input v-model="companyForm.industryType" :placeholder="$t('payment.emiAuth.enterIndustryType')" />
        </el-form-item>

        <!-- 出口类型 -->
        <el-form-item :label="$t('payment.emiAuth.exportType')" prop="exportType">
          <el-input
            v-model="companyForm.exportType"
            :placeholder="$t('payment.emiAuth.enterExportType')"
            style="width: 100%"
          />
        </el-form-item>

        <!-- 出口国家 -->
        <el-form-item :label="$t('payment.emiAuth.exportCountries')" prop="exportCountries">
          <el-input v-model="companyForm.exportCountries" :placeholder="$t('payment.emiAuth.enterExportCountries')" />
        </el-form-item>

        <!-- 出口商品名称 -->
        <el-form-item :label="$t('payment.emiAuth.exportProductName')" prop="exportProductName">
          <el-input
            v-model="companyForm.exportProductName"
            :placeholder="$t('payment.emiAuth.enterExportProductName')"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <!-- 历史年出口额 -->
        <el-form-item :label="$t('payment.emiAuth.historicalAnnualExport')" prop="historicalAnnualExport">
          <el-input
            v-model="companyForm.historicalAnnualExport"
            :placeholder="$t('payment.emiAuth.enterHistoricalAnnualExport')"
          >
            <template slot="append">{{ $t('payment.emiAuth.amountUnit') }}</template>
          </el-input>
        </el-form-item>

        <!-- 预估年出口额 -->
        <el-form-item :label="$t('payment.emiAuth.estimatedAnnualExport')" prop="estimatedAnnualExport">
          <el-input
            v-model="companyForm.estimatedAnnualExport"
            :placeholder="$t('payment.emiAuth.enterEstimatedAnnualExport')"
          >
            <template slot="append">{{ $t('payment.emiAuth.amountUnit') }}</template>
          </el-input>
        </el-form-item>

        <!-- 企业网站 -->
        <el-form-item :label="$t('payment.emiAuth.website')" prop="website">
          <el-input v-model="companyForm.website" :placeholder="$t('payment.emiAuth.enterWebsite')" />
        </el-form-item>

        <!-- 证件类型 -->
        <el-form-item :label="$t('payment.emiAuth.idType')" prop="idType">
          <el-select v-model="companyForm.idType" :placeholder="$t('payment.emiAuth.selectIdType')" style="width: 100%">
            <el-option
              v-for="item in documentType"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
        </el-form-item>

        <!-- 证件照片 -->
        <el-form-item :label="$t('payment.emiAuth.idPhoto')" prop="idPhoto">
          <el-upload
            class="id-photo-uploader"
            :action="uploadUrl"
            :headers="headers"
            :file-list="companyIdPhotoFileList"
            list-type="picture-card"
            :on-success="handleCompanyIdPhotoSuccess"
            :on-remove="handleCompanyIdPhotoRemove"
            :before-upload="beforeIdPhotoUpload"
            :limit="1"
            accept="image/*"
          >
            <i class="el-icon-plus" />
          </el-upload>
        </el-form-item>

        <!-- 商业登记证书 -->
        <el-form-item :label="$t('payment.emiAuth.businessRegistration')" prop="businessRegistration">
          <el-upload
            class="id-photo-uploader"
            :action="uploadUrl"
            :headers="headers"
            :file-list="businessRegistrationFileList"
            list-type="picture-card"
            :on-success="handleBusinessRegistrationSuccess"
            :on-remove="handleBusinessRegistrationRemove"
            :before-upload="beforeIdPhotoUpload"
            :limit="1"
            accept="image/*"
          >
            <i class="el-icon-plus" />
          </el-upload>
        </el-form-item>

        <!-- 公司注册证书 -->
        <el-form-item :label="$t('payment.emiAuth.companyRegistration')" prop="companyRegistration">
          <el-upload
            class="id-photo-uploader"
            :action="uploadUrl"
            :headers="headers"
            :file-list="companyRegistrationFileList"
            list-type="picture-card"
            :on-success="handleCompanyRegistrationSuccess"
            :on-remove="handleCompanyRegistrationRemove"
            :before-upload="beforeIdPhotoUpload"
            :limit="1"
            accept="image/*"
          >
            <i class="el-icon-plus" />
          </el-upload>
        </el-form-item>

        <!-- 法团成立表格 -->
        <el-form-item :label="$t('payment.emiAuth.incorporation')" prop="incorporation">
          <el-upload
            class="id-photo-uploader"
            :action="uploadUrl"
            :headers="headers"
            :file-list="IncorporationFileList"
            list-type="picture-card"
            :on-success="handleIncorporationSuccess"
            :on-remove="handleIncorporationRemove"
            :before-upload="beforeIdPhotoUpload"
            :limit="1"
            accept="image/*"
          >
            <i class="el-icon-plus" />
          </el-upload>
        </el-form-item>

        <!-- 企业办公场景 -->
        <el-form-item :label="$t('payment.emiAuth.officeScene')" prop="officeScene">
          <el-upload
            class="id-photo-uploader"
            :action="uploadUrl"
            :headers="headers"
            :file-list="officeSceneFileList"
            list-type="picture-card"
            :on-success="handleOfficeSceneSuccess"
            :on-remove="handleOfficeSceneRemove"
            :before-upload="beforeIdPhotoUpload"
            :limit="1"
            accept="image/*"
          >
            <i class="el-icon-plus" />
          </el-upload>
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item v-if="status === 0 || status === 1 || status === 4">
          <el-button
            type="primary"
            size="large"
            style="width: 100%; margin-top: 20px"
            :loading="companySubmitting"
            :disabled="companySubmitting"
            @click="submitCompanyForm"
          >
            {{ companySubmitting ? $t('payment.emiAuth.submitting') : $t('payment.emiAuth.submit') }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { getCountryList } from '@/api/common/country'
import {
  companyAccountSubmit,
  getCompanyInformationUser,
  getPersonalInformation,
  getPersonalInformationUser,
  personalAccountSubmit
} from '@/api/payment/emi'
import { authListShop } from '@/api/payment/shop'
import LanguageSwitch from '@/components/LanguageSwitch'
import { getToken } from '@/utils/auth'

export default {
  name: 'EmiAuth',
  components: {
    LanguageSwitch
  },
  dicts: ['SHOP_STATUS', 'KYB_COMPANY_AUTH_TYPE', 'KYB_AUTH_TYPE', 'KYB_ADDRESS_TYPE'],
  data() {
    // 邮箱验证规则
    const validateEmail = (rule, value, callback) => {
      const emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
      if (!value) {
        callback(new Error(this.$t('payment.emiAuth.emailRequired')))
      } else if (!emailReg.test(value)) {
        callback(new Error(this.$t('payment.emiAuth.emailFormatError')))
      } else {
        callback()
      }
    }

    return {
      uploadUrl: process.env.VUE_APP_BASE_API + '/uploadFile',
      countryOptions: [], //国家列表
      documentType: [], // 证件类型选项
      sexOptions: [], // 性别选项
      accountType: 'personal', // 账户类型: personal-个人账户, company-企业账户
      // 账户类型表单数据
      accountTypeFormData: {
        accountType: 'personal'
      },
      // 账户类型验证规则
      accountTypeRules: {
        accountType: [{ required: true, message: this.$t('payment.emiAuth.accountTypeRequired'), trigger: 'change' }]
      },
      // 文件列表
      personalIdPhotoFileList: [],
      companyIdPhotoFileList: [],
      businessRegistrationFileList: [],
      companyRegistrationFileList: [],
      IncorporationFileList: [],
      officeSceneFileList: [],
      userType: '', // 用户类型,1:个人,2:企业
      status: 0, //状态,0-待提交资料，1-待审核，2: binderr审核中 3:审核通过
      hasEchoData: false, // 是否有回显数据
      personalSubmitting: false, // 个人账户提交中标志
      companySubmitting: false, // 企业账户提交中标志
      // 个人账户表单
      personalForm: {
        id: '',
        nationality: 'CN', // 国籍
        email: '', // 邮箱（必填）
        idType: '', // 证件类型（必填）
        idPhoto: '', // 证件照片存储路径
        fullName: '', // 姓名（必填）
        idNumber: '', // 证件号（必填）
        gender: 'M', // 性别
        birthDate: '', // 出生日期
        residenceCountry: 'CN', // 居住国家
        residenceAddress: '', // 居住地址
        residencePostalCode: '', // 居住邮编
        contactPhone: '', // 联系电话
        fundSource: '' // 资金来源
      },
      // 企业账户表单
      companyForm: {
        id: '',
        nationality: 'CN', // 国籍
        email: '', // 邮箱（示例）
        idType: '', // 证件类型
        idPhoto: '', // 证件照片
        fullName: '', // 法人姓名（必填）
        idNumber: '', // 证件号
        companyNameChinese: '', // 企业中文名称
        companyNameEnglish: '', // 企业英文名称
        businessRegistrationNumber: '', // 商业登记证编号
        companyRegistrationCode: '', // 公司注册代码
        registeredCountry: 'CN', // 企业注册国家
        registeredAddress: '', // 企业注册地址
        actualOperationAddress: '', // 实际经营地址
        employeeCount: '', // 企业人数
        establishmentDate: '', // 企业成立日期
        expirationDate: '', // 企业到期日期
        longTermValid: false, // 长期有效（前端使用）
        industryType: '', // 行业类型
        exportType: '', // 出口类型
        exportCountries: '', // 出口国家和地区
        exportProductName: '', // 出口商品名称
        historicalAnnualExport: '', // 历史年出口额
        estimatedAnnualExport: '', // 预估年出口额
        website: '', // 企业网站(选填)
        businessRegistration: '', // 商业登记证书
        companyRegistration: '', // 公司注册证书
        incorporation: '', // 法团成立表格
        officeScene: '' // 企业办公场景
      },
      // 个人账户验证规则
      personalRules: {
        email: [{ required: true, validator: validateEmail, trigger: 'blur' }],
        fullName: [{ required: true, message: this.$t('payment.emiAuth.fullNameRequired'), trigger: 'blur' }],
        idNumber: [
          { required: true, message: this.$t('payment.emiAuth.idNumberRequired'), trigger: 'blur' },
          {
            pattern: /^[A-Za-z0-9]+$/,
            message: this.$t('payment.emiAuth.idNumberFormatError'),
            trigger: 'blur'
          }
        ],
        nationality: [{ required: true, message: this.$t('payment.emiAuth.nationalityRequired'), trigger: 'change' }],
        residenceCountry: [
          { required: true, message: this.$t('payment.emiAuth.residenceCountryRequired'), trigger: 'change' }
        ],
        residenceAddress: [
          { required: true, message: this.$t('payment.emiAuth.residenceAddressRequired'), trigger: 'blur' }
        ],
        residencePostalCode: [
          { required: true, message: this.$t('payment.emiAuth.residencePostalCodeRequired'), trigger: 'blur' }
        ],
        gender: [{ required: true, message: this.$t('payment.emiAuth.genderRequired'), trigger: 'change' }],
        birthDate: [{ required: true, message: this.$t('payment.emiAuth.birthDateRequired'), trigger: 'change' }],
        contactPhone: [{ required: true, message: this.$t('payment.emiAuth.contactPhoneRequired'), trigger: 'blur' }],
        fundSource: [{ required: true, message: this.$t('payment.emiAuth.fundSourceRequired'), trigger: 'blur' }],
        idType: [{ required: true, message: this.$t('payment.emiAuth.idTypeRequired'), trigger: 'change' }],
        idPhoto: [{ required: true, message: this.$t('payment.emiAuth.idPhotoRequired'), trigger: 'change' }]
      },
      // 企业账户验证规则
      companyRules: {
        email: [{ required: true, validator: validateEmail, trigger: 'blur' }],
        companyNameChinese: [
          { required: true, message: this.$t('payment.emiAuth.companyNameChineseRequired'), trigger: 'blur' }
        ],
        companyNameEnglish: [
          { required: true, message: this.$t('payment.emiAuth.companyNameEnglishRequired'), trigger: 'blur' }
        ],
        businessRegistrationNumber: [
          { required: true, message: this.$t('payment.emiAuth.businessRegistrationNumberRequired'), trigger: 'blur' }
        ],
        companyRegistrationCode: [
          { required: true, message: this.$t('payment.emiAuth.companyRegistrationCodeRequired'), trigger: 'blur' }
        ],
        registeredCountry: [
          { required: true, message: this.$t('payment.emiAuth.registeredCountryRequired'), trigger: 'change' }
        ],
        registeredAddress: [
          { required: true, message: this.$t('payment.emiAuth.registeredAddressRequired'), trigger: 'blur' }
        ],
        actualOperationAddress: [
          { required: true, message: this.$t('payment.emiAuth.actualOperationAddressRequired'), trigger: 'blur' }
        ],
        employeeCount: [
          { required: true, message: this.$t('payment.emiAuth.employeeCountRequired'), trigger: 'blur' },
          { pattern: /^[0-9]+$/, message: this.$t('payment.emiAuth.employeeCountFormatError'), trigger: 'blur' }
        ],
        establishmentDate: [
          { required: true, message: this.$t('payment.emiAuth.establishmentDateRequired'), trigger: 'change' }
        ],
        fullName: [{ required: true, message: this.$t('payment.emiAuth.fullNameRequired'), trigger: 'blur' }],
        idNumber: [
          { required: true, message: this.$t('payment.emiAuth.idNumberRequired'), trigger: 'blur' },
          {
            pattern: /^[A-Za-z0-9]+$/,
            message: this.$t('payment.emiAuth.idNumberFormatError'),
            trigger: 'blur'
          }
        ],
        nationality: [{ required: true, message: this.$t('payment.emiAuth.nationalityRequired'), trigger: 'change' }],
        industryType: [{ required: true, message: this.$t('payment.emiAuth.industryTypeRequired'), trigger: 'blur' }],
        exportType: [{ required: true, message: this.$t('payment.emiAuth.exportTypeRequired'), trigger: 'blur' }],
        exportCountries: [
          { required: true, message: this.$t('payment.emiAuth.exportCountriesRequired'), trigger: 'blur' }
        ],
        exportProductName: [
          { required: true, message: this.$t('payment.emiAuth.exportProductNameRequired'), trigger: 'blur' },
          { max: 100, message: this.$t('payment.emiAuth.exportProductNameMaxLength'), trigger: 'blur' }
        ],
        historicalAnnualExport: [
          { required: true, message: this.$t('payment.emiAuth.historicalAnnualExportRequired'), trigger: 'blur' },
          {
            pattern: /^[0-9]+(\.[0-9]{1,2})?$/,
            message: this.$t('payment.emiAuth.historicalAnnualExportFormatError'),
            trigger: 'blur'
          }
        ],
        estimatedAnnualExport: [
          { required: true, message: this.$t('payment.emiAuth.estimatedAnnualExportRequired'), trigger: 'blur' },
          {
            pattern: /^[0-9]+(\.[0-9]{1,2})?$/,
            message: this.$t('payment.emiAuth.estimatedAnnualExportFormatError'),
            trigger: 'blur'
          }
        ],
        idType: [{ required: true, message: this.$t('payment.emiAuth.idTypeRequired'), trigger: 'change' }]
      }
    }
  },
  computed: {
    headers() {
      return {
        Authorization: 'Bearer ' + getToken()
      }
    },
    formLabelWidth() {
      // 中文使用 140px，英文使用 160px
      return this.$i18n.locale === 'zh' ? '140px' : '170px'
    }
  },

  created() {
    this.getDicts('IDCARD_TYPE').then(response => {
      //证件类型,
      this.documentType = response.data
    })
    //性别男女,也是需要通过接口获取sys_user_sex
    this.getDicts('sys_user_sex').then(response => {
      this.sexOptions = response.data
    })
    this.loadCountryList()
    this.getUserInfo()
    this.getShopInfo()
  },
  methods: {
    /** 获取用户信息并回显邮箱和手机号 */
    async getUserInfo() {
      try {
        const response = await this.$store.dispatch('GetInfo')
        if (response && response.user) {
          const userInfo = response.user
          // 提取邮箱和手机号码
          const email = userInfo.email || ''
          const phonenumber = userInfo.phonenumber || ''

          // 回显到个人账户表单
          this.personalForm.email = email
          this.personalForm.contactPhone = phonenumber

          // 回显到企业账户表单
          this.companyForm.email = email
        }
      } catch (error) {
        console.error(this.$t('payment.emiAuth.getUserInfoFailed'), error)
      }
    },

    /** 获取商户信息 */
    getShopInfo() {
      authListShop().then(response => {
        if (response.rows && response.rows.length > 0) {
          const shopInfo = response.rows[0]
          this.personalForm.id = shopInfo.id
          this.companyForm.id = shopInfo.id
          const userId = shopInfo.userId
          this.userType = shopInfo.userType //1:个人账户  2:企业账户
          this.status = shopInfo.status || 0
          // 根据用户类型进行数据回显
          if (this.userType === '1' || this.userType === 1) {
            // 个人账户，调用个人信息接口
            getPersonalInformationUser(userId)
              .then(response => {
                if (response.code === 200 && response.data) {
                  const data = response.data
                  // 将接口返回的数据赋值给 personalForm
                  this.personalForm = {
                    ...this.personalForm,
                    ...data
                  }
                  this.status = data.status || 0

                  // 设置账户类型为个人
                  this.accountType = 'personal'
                  this.accountTypeFormData.accountType = 'personal'

                  // 设置有回显数据标志
                  this.hasEchoData = true

                  // 回显证件照片
                  if (data.idPhoto) {
                    this.personalIdPhotoFileList = [
                      {
                        name: '证件照片',
                        url: data.idPhoto
                      }
                    ]
                  }
                }
              })
              .catch(error => {})
          } else if (this.userType === '2' || this.userType === 2) {
            // 企业账户，调用企业信息接口
            getCompanyInformationUser(userId)
              .then(response => {
                if (response.code === 200 && response.data) {
                  const data = response.data
                  this.status = data.status || 0 //这个是当前资料的提交状态,初次进入的时候,status是null,默认给0

                  // 将接口返回的数据赋值给 companyForm
                  this.companyForm = {
                    ...this.companyForm,
                    ...data
                  }

                  // 设置账户类型为企业
                  this.accountType = 'company'
                  this.accountTypeFormData.accountType = 'company'

                  // 设置有回显数据标志
                  this.hasEchoData = true

                  // 回显各类证件照片
                  if (data.idPhoto) {
                    this.companyIdPhotoFileList = [
                      {
                        name: '证件照片',
                        url: data.idPhoto
                      }
                    ]
                  }

                  if (data.businessRegistration) {
                    this.businessRegistrationFileList = [
                      {
                        name: '商业登记证书',
                        url: data.businessRegistration
                      }
                    ]
                  }

                  if (data.companyRegistration) {
                    this.companyRegistrationFileList = [
                      {
                        name: '公司注册证书',
                        url: data.companyRegistration
                      }
                    ]
                  }

                  if (data.incorporation) {
                    this.IncorporationFileList = [
                      {
                        name: '公司章程',
                        url: data.incorporation
                      }
                    ]
                  }

                  if (data.companyAddress) {
                    this.companyAddressFileList = [
                      {
                        name: '公司地址证明',
                        url: data.companyAddress
                      }
                    ]
                  }

                  if (data.officeScene) {
                    this.officeSceneFileList = [
                      {
                        name: '企业办公场景',
                        url: data.officeScene
                      }
                    ]
                  }
                }
              })
              .catch(error => {})
          } else {
            this.status = 0
          }
        } else {
        }
      })
    },

    /** 加载个人账户数据 */
    loadPersonalData(id) {
      getPersonalInformation(id)
        .then(response => {
          if (response.code === 200 && response.data) {
            const data = response.data
            // 将接口返回的数据赋值给 personalForm
            this.personalForm = {
              ...this.personalForm,
              ...data
            }

            // 设置账户类型为个人
            this.accountType = 'personal'

            // 回显证件照片
            if (data.idPhoto) {
              this.personalIdPhotoFileList = [
                {
                  name: '证件照片',
                  url: data.idPhoto
                }
              ]
            }
          }
        })
        .catch(error => {
          console.error('获取个人账户信息失败：', error)
        })
    },

    // 切换账户类型
    switchAccountType(type) {
      // 如果有回显数据，不允许切换账户类型
      if (this.hasEchoData) {
        this.$message.warning(this.$t('payment.emiAuth.cannotSwitchAccountType'))
        return
      }

      this.accountType = type
      this.accountTypeFormData.accountType = type
      // 清空表单验证
      this.$nextTick(() => {
        if (type === 'personal' && this.$refs.personalForm) {
          this.$refs.personalForm.clearValidate()
        } else if (type === 'company' && this.$refs.companyFormRef) {
          this.$refs.companyFormRef.clearValidate()
        }
      })
    },

    // 加载国家列表
    loadCountryList() {
      getCountryList()
        .then(response => {
          this.countryOptions = response.data || []
        })
        .catch(() => {
          this.$message.error(this.$t('payment.emiAuth.getCountryListFailed'))
        })
    },

    // 处理企业注册国家变更
    handleRegistrationCountryChange(value) {
      // 可以在这里添加其他逻辑
    },

    // 处理长期有效
    handleLongTermValid(value) {
      if (value) {
        this.companyForm.expiryDate = ''
      }
    },

    // 个人账户证件照片上传成功
    handlePersonalIdPhotoSuccess(response, file) {
      if (response.code === 200) {
        this.personalForm.idPhoto = response.url
        this.personalIdPhotoFileList = [{ name: file.name, url: response.url }]
        this.$message.success(this.$t('payment.emiAuth.uploadSuccess'))
      } else {
        this.$message.error(this.$t('payment.emiAuth.uploadFailed'))
      }
    },

    // 个人账户证件照片移除
    handlePersonalIdPhotoRemove(file, fileList) {
      this.personalForm.idPhoto = ''
      this.personalIdPhotoFileList = []
    },

    // 企业账户证件照片上传成功
    handleCompanyIdPhotoSuccess(response, file) {
      if (response.code === 200) {
        this.companyForm.idPhoto = response.url
        this.companyIdPhotoFileList = [{ name: file.name, url: response.url }]
        this.$message.success(this.$t('payment.emiAuth.uploadSuccess'))
      } else {
        this.$message.error(this.$t('payment.emiAuth.uploadFailed'))
      }
    },

    // 企业账户证件照片移除
    handleCompanyIdPhotoRemove(file, fileList) {
      this.companyForm.idPhoto = ''
      this.companyIdPhotoFileList = []
    },

    // 商业登记证书上传成功
    handleBusinessRegistrationSuccess(response, file) {
      if (response.code === 200) {
        this.companyForm.businessRegistration = response.url
        this.businessRegistrationFileList = [{ name: file.name, url: response.url }]
        this.$message.success(this.$t('payment.emiAuth.uploadSuccess'))
      } else {
        this.$message.error(this.$t('payment.emiAuth.uploadFailed'))
      }
    },

    // 商业登记证书移除
    handleBusinessRegistrationRemove(file, fileList) {
      this.companyForm.businessRegistration = ''
      this.businessRegistrationFileList = []
    },

    // 公司注册证书上传成功
    handleCompanyRegistrationSuccess(response, file) {
      if (response.code === 200) {
        this.companyForm.companyRegistration = response.url
        this.companyRegistrationFileList = [{ name: file.name, url: response.url }]
        this.$message.success(this.$t('payment.emiAuth.uploadSuccess'))
      } else {
        this.$message.error(this.$t('payment.emiAuth.uploadFailed'))
      }
    },

    // 公司注册证书移除
    handleCompanyRegistrationRemove(file, fileList) {
      this.companyForm.companyRegistration = ''
      this.companyRegistrationFileList = []
    },

    // 法团成立表格上传成功
    handleIncorporationSuccess(response, file) {
      if (response.code === 200) {
        this.companyForm.incorporation = response.url
        this.IncorporationFileList = [{ name: file.name, url: response.url }]
        this.$message.success(this.$t('payment.emiAuth.uploadSuccess'))
      } else {
        this.$message.error(this.$t('payment.emiAuth.uploadFailed'))
      }
    },

    // 法团成立表格移除
    handleIncorporationRemove(file, fileList) {
      this.companyForm.incorporation = ''
      this.IncorporationFileList = []
    },

    // 企业办公场景上传成功
    handleOfficeSceneSuccess(response, file) {
      if (response.code === 200) {
        this.companyForm.officeScene = response.url
        this.officeSceneFileList = [{ name: file.name, url: response.url }]
        this.$message.success(this.$t('payment.emiAuth.uploadSuccess'))
      } else {
        this.$message.error(this.$t('payment.emiAuth.uploadFailed'))
      }
    },

    // 企业办公场景移除
    handleOfficeSceneRemove(file, fileList) {
      this.companyForm.officeScene = ''
      this.officeSceneFileList = []
    },

    // 证件照片上传前校验
    beforeIdPhotoUpload(file) {
      const isImage = file.type.indexOf('image') !== -1
      const isLt5M = file.size / 1024 / 1024 < 5

      if (!isImage) {
        this.$message.error(this.$t('payment.emiAuth.onlyImageAllowed'))
        return false
      }
      if (!isLt5M) {
        this.$message.error(this.$t('payment.emiAuth.imageSizeLimit'))
        return false
      }
      return true
    },

    /** 提交个人账户表单 */
    submitPersonalForm() {
      // 防止重复提交
      if (this.personalSubmitting) {
        return
      }

      this.$refs.personalForm.validate(valid => {
        if (valid) {
          // 验证必填字段
          if (!this.personalForm.idPhoto) {
            this.$message.warning(this.$t('payment.emiAuth.idPhotoRequired'))
            return
          }

          const submitData = {
            ...this.personalForm,
            accountType: 'personal',
            // 如果当前状态是4(审核拒绝),再次提交时状态改为1(待审核)
            status: this.status === 4 ? 1 : this.personalForm.status
          }

          // 这里调用提交接口
          this.$confirm(this.$t('payment.emiAuth.confirmSubmitPersonal'), this.$t('payment.emiAuth.hint'), {
            confirmButtonText: this.$t('payment.emiAuth.confirm'),
            cancelButtonText: this.$t('payment.emiAuth.cancel'),
            type: 'warning'
          })
            .then(() => {
              // 设置提交中状态
              this.personalSubmitting = true
              // 调用接口:personalAccountSubmit
              personalAccountSubmit(submitData)
                .then(response => {
                  this.$message.success(this.$t('payment.emiAuth.personalSubmitSuccess'))
                  // 刷新页面
                  location.reload()
                })
                .catch(error => {
                  this.$message.error(
                    this.$t('payment.emiAuth.submitFailed') + (error.msg || this.$t('payment.emiAuth.pleaseRetryLater'))
                  )
                })
                .finally(() => {
                  // 无论成功或失败，都恢复提交状态
                  this.personalSubmitting = false
                })
            })
            .catch(() => {
              // 用户取消提交
            })
        } else {
          this.$message.warning(this.$t('payment.emiAuth.pleaseCompleteForm'))
          return false
        }
      })
    },

    /** 提交企业账户表单 */
    submitCompanyForm() {
      // 防止重复提交
      if (this.companySubmitting) {
        return
      }

      this.$refs.companyFormRef.validate(valid => {
        if (valid) {
          // 验证必填字段
          if (!this.companyForm.idPhoto) {
            this.$message.warning(this.$t('payment.emiAuth.idPhotoRequired'))
            return
          }

          // 提交时排除前端辅助字段 longTermValid
          const { longTermValid, ...restForm } = this.companyForm

          const submitData = {
            ...restForm,
            accountType: 'company',
            // 如果当前状态是4(审核拒绝),再次提交时状态改为1(待审核)
            status: this.status === 4 ? 1 : this.companyForm.status
          }

          // 这里调用提交接口
          this.$confirm(this.$t('payment.emiAuth.confirmSubmitCompany'), this.$t('payment.emiAuth.hint'), {
            confirmButtonText: this.$t('payment.emiAuth.confirm'),
            cancelButtonText: this.$t('payment.emiAuth.cancel'),
            type: 'warning'
          })
            .then(() => {
              // 设置提交中状态
              this.companySubmitting = true
              // 调用接口: companyAccountSubmit
              companyAccountSubmit(submitData)
                .then(response => {
                  this.$message.success(this.$t('payment.emiAuth.companySubmitSuccess'))
                  // 刷新页面
                  location.reload()
                })
                .catch(error => {
                  this.$message.error(
                    this.$t('payment.emiAuth.submitFailed') + (error.msg || this.$t('payment.emiAuth.pleaseRetryLater'))
                  )
                })
                .finally(() => {
                  // 无论成功或失败，都恢复提交状态
                  this.companySubmitting = false
                })
            })
            .catch(() => {
              // 用户取消提交
            })
        } else {
          this.$message.warning(this.$t('payment.emiAuth.pleaseCompleteForm'))
          return false
        }
      })
    },

    /** 重置个人表单 */
    resetPersonalForm() {
      if (this.$refs.personalForm) {
        this.$refs.personalForm.resetFields()
        this.personalForm.idPhoto = ''
        this.personalIdPhotoFileList = []
      }
    },

    /** 重置企业表单 */
    resetCompanyForm() {
      if (this.$refs.companyFormRef) {
        this.$refs.companyFormRef.resetFields()
        this.companyForm.idPhoto = ''
        this.companyIdPhotoFileList = []
        this.businessRegistrationFileList = []
        this.companyRegistrationFileList = []
        this.IncorporationFileList = []
        this.officeSceneFileList = []
      }
    },

    /** 退出登录 */
    logout() {
      this.$confirm(this.$t('payment.emiAuth.confirmLogout'), this.$t('payment.emiAuth.hint'), {
        confirmButtonText: this.$t('payment.emiAuth.confirm'),
        cancelButtonText: this.$t('payment.emiAuth.cancel'),
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index'
        })
      })
    },

    /** 处理居住地址失去焦点事件 */
    handleResidenceAddressBlur() {
      // 手动触发该字段的验证
      this.$refs.personalForm.validateField('residenceAddress')
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.full-screen {
  background-color: #f5f5f5;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 2000;
  overflow-y: auto;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 15px;
  margin-bottom: 30px;
}

.logout-btn {
  font-size: 14px;
}

.logout-btn i {
  margin-right: 5px;
}

.language-switch-btn {
  color: #606266;
}

.form-container {
  max-width: 60%;
  margin: 0 auto;
  background-color: #fff;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 状态显示区域 */
.status-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  align-items: center;
  gap: 10px;
}

.status-label {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

/* 账户类型切换按钮 */
.account-type-switch {
  display: inline-flex;
}

.account-type-switch .el-button {
  min-width: 120px;
  padding: 12px 20px;
}

/* 证件照片上传 */
.id-photo-uploader ::v-deep .el-upload--picture-card {
  width: 148px;
  height: 148px;
  line-height: 148px;
  border: 2px dashed #ffa940;
  background-color: #fffbe6;
}

.id-photo-uploader ::v-deep .el-upload--picture-card:hover {
  border-color: #ff9800;
  background-color: #fff7e6;
}

.id-photo-uploader ::v-deep .el-upload--picture-card i {
  font-size: 32px;
  color: #ffa940;
}

.id-photo-uploader ::v-deep .el-upload-list--picture-card .el-upload-list__item {
  width: 148px;
  height: 148px;
}

.fail-reason {
  color: #f56c6c;
  line-height: 1.5;
  padding: 8px 12px;
  background-color: #fef0f0;
  border-radius: 4px;
}

.el-divider {
  margin: 24px 0;
}

.el-upload__tip {
  color: #909399;
  font-size: 12px;
  margin-top: 7px;
  line-height: 1.4;
}

.el-alert {
  margin: 10px 0;
}

/* 表单样式优化 */
::v-deep .el-form-item__label {
  font-weight: 500;
  color: #303133;
}

::v-deep .el-input__inner,
::v-deep .el-textarea__inner,
::v-deep .el-select {
  border-radius: 4px;
}

::v-deep .el-input__inner:focus,
::v-deep .el-textarea__inner:focus {
  border-color: #409eff;
}

::v-deep .el-form-item {
  margin-bottom: 22px;
}

/* 姓名表单项样式 */
.name-form-item {
  margin-bottom: 22px;
  display: flex;
  align-items: flex-start;
}

.name-label {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  line-height: 40px;
  width: 140px;
  flex-shrink: 0;
  text-align: right;
  padding-right: 12px;
}

.name-form-item .el-row {
  flex: 1;
}

/* 姓名行内嵌套表单项样式 */
.name-form-item .el-row .el-col .el-form-item {
  margin-bottom: 0;
}

.name-form-item .el-row .el-col .el-form-item__content {
  margin-left: 0 !important;
}

/* 提交按钮样式 */
::v-deep .el-button--primary.el-button--large {
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  height: 48px;
  background-color: #409eff;
  border-color: #409eff;
}

::v-deep .el-button--primary.el-button--large:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

/* 添加上传组件和查看按钮的样式 */
.el-upload {
  display: inline-block;
  margin-right: 10px;
}

.el-button [class*='el-icon-'] {
  margin-right: 5px;
}

/* 限制上传列表的宽度，避免太长 */
.el-upload-list {
  max-width: 360px;
}

/* 上传文件名过长时显示省略号 */
.el-upload-list__item-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ubo-item {
  position: relative;
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 20px;
}

.remove-ubo-btn {
  position: absolute;
  top: 20px;
  right: 20px;
}

/* 表单内的复选框样式 */
.name-label .el-checkbox {
  display: inline-block;
  vertical-align: middle;
  font-size: 14px;
}

::v-deep .el-checkbox__label {
  font-size: 14px;
  color: #606266;
}

/* 地址同步和长期有效的样式 */
::v-deep .el-form-item__content > div {
  width: 100%;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .form-container {
    padding: 20px;
  }

  ::v-deep .el-form-item__label {
    line-height: 30px;
  }
}
</style>
