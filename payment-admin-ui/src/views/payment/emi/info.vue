<template>
  <div v-loading="auditLoading" :element-loading-text="loadingText" class="app-container">
    <el-card v-loading="loading">
      <!-- 页面标题 -->
      <div slot="header" class="clearfix">
        <span class="card-title">
          <i :class="accountType === 'personal' ? 'el-icon-user' : 'el-icon-office-building'"></i>
          {{
            accountType === 'personal'
              ? $t('payment.emi.info.personalAccountInfo')
              : $t('payment.emi.info.companyAccountInfo')
          }}
          <el-tag v-if="accountInfo" :type="getStatusType(accountInfo.status)" size="small" style="margin-left: 12px">
            {{ getStatusName(accountInfo.status) }}
          </el-tag>
        </span>
        <!-- 审核状态按钮 -->
        <div style="float: right">
          <el-button
            v-if="accountInfo && accountInfo.status === 1"
            type="primary"
            size="small"
            @click="handleAudit"
            style="margin-right: 10px"
          >
            <i class="el-icon-s-check"></i>
            {{ $t('payment.emi.info.audit') }}
          </el-button>
          <el-button style="padding: 3px 0" type="text" @click="goBack">
            <i class="el-icon-back"></i>
            {{ $t('payment.emi.info.back') }}
          </el-button>
        </div>
      </div>

      <!-- 个人账户信息展示 -->
      <div v-if="accountType === 'personal' && accountInfo" class="account-info">
        <!-- 分隔线 -->
        <el-divider content-position="left">
          <i class="el-icon-user-solid"></i>
          {{ $t('payment.emi.info.basicInfo') }}
        </el-divider>

        <!-- 基础信息三列布局 -->
        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.idNumber') }}：</label>
              <span>{{ accountInfo.id }}</span>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.fullName') }}：</label>
              <span>{{ accountInfo.fullName }}</span>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.gender') }}：</label>
              <span>{{ getGenderName(accountInfo.gender) }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.birthDate') }}：</label>
              <span>{{ accountInfo.birthDate || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.nationality') }}：</label>
              <span>{{ getNationalityName(accountInfo.nationality) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.residenceCountry') }}：</label>
              <span>{{ getNationalityName(accountInfo.residenceCountry) }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.registrationDate') }}：</label>
              <span>{{ accountInfo.createdAt }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.email') }}：</label>
              <span>{{ accountInfo.email }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.fundSource') }}：</label>
              <span>{{ accountInfo.fundSource }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.bankAccount') }}：</label>
              <span>{{ accountInfo.cardNumber || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.bank') }}：</label>
              <span>{{ accountInfo.bank || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 联系信息 -->
        <el-divider content-position="left">
          <i class="el-icon-phone"></i>
          {{ $t('payment.emi.info.contactInfo') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.contactPhone') }}：</label>
              <span>{{ accountInfo.contactPhone }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.idType') }}：</label>
              <span>{{ getIdTypeName(accountInfo.idType) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.idNumberField') }}：</label>
              <span>{{ accountInfo.idNumber }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.provinceCity') }}：</label>
              <span>{{ accountInfo.residenceProvinceCity || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.postalCode') }}：</label>
              <span>{{ accountInfo.residencePostalCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.residenceAddress') }}：</label>
              <span>{{ accountInfo.residenceAddress }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 证件照片 -->
        <el-divider content-position="left">
          <i class="el-icon-picture"></i>
          {{ $t('payment.emi.info.idPhotos') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="24">
            <el-image
              v-if="accountInfo.idPhoto"
              :src="accountInfo.idPhoto"
              :preview-src-list="[accountInfo.idPhoto]"
              style="width: 200px; height: auto; cursor: pointer; border: 1px solid #dcdfe6; border-radius: 4px"
              fit="contain"
            >
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
            <span v-else class="no-data">{{ $t('payment.emi.info.noPhoto') }}</span>
          </el-col>
        </el-row>
      </div>

      <!-- 企业账户信息展示 -->
      <div v-if="accountType === 'company' && accountInfo" class="account-info">
        <!-- 分隔线 -->
        <el-divider content-position="left">
          <i class="el-icon-office-building"></i>
          {{ $t('payment.emi.info.companyBasicInfo') }}
        </el-divider>

        <!-- 基础信息三列布局 -->
        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.idNumber') }}：</label>
              <span>{{ accountInfo.id }}</span>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.companyNameChinese') }}：</label>
              <span>{{ accountInfo.companyNameChinese || '-' }}</span>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.companyNameEnglish') }}：</label>
              <span>{{ accountInfo.companyNameEnglish || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.businessRegistrationNumber') }}：</label>
              <span>{{ accountInfo.businessRegistrationNumber || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.companyRegistrationCode') }}：</label>
              <span>{{ accountInfo.companyRegistrationCode || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.registeredCountry') }}：</label>
              <span>{{ getNationalityName(accountInfo.registeredCountry) }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.establishmentDate') }}：</label>
              <span>{{ accountInfo.establishmentDate || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.expirationDate') }}：</label>
              <span>{{ accountInfo.expirationDate || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.employeeCount') }}：</label>
              <span>{{ accountInfo.employeeCount || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.industryType') }}：</label>
              <span>{{ accountInfo.industryType || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.website') }}：</label>
              <span>{{ accountInfo.website || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.statusLabel') }}：</label>
              <el-tag :type="getStatusType(accountInfo.status)" size="small">
                {{ getStatusName(accountInfo.status) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.createdAt') }}：</label>
              <span>{{ accountInfo.createdAt || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.updatedAt') }}：</label>
              <span>{{ accountInfo.updatedAt || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.userId') }}：</label>
              <span>{{ accountInfo.userId || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.bankAccount') }}：</label>
              <span>{{ accountInfo.cardNumber || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.bank') }}：</label>
              <span>{{ accountInfo.bank || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 地址信息 -->
        <el-divider content-position="left">
          <i class="el-icon-location"></i>
          {{ $t('payment.emi.info.addressInfo') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="12">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.registeredAddress') }}：</label>
              <span>{{ accountInfo.registeredAddress || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.actualOperationAddress') }}：</label>
              <span>{{ accountInfo.actualOperationAddress || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 出口信息 -->
        <el-divider content-position="left">
          <i class="el-icon-sell"></i>
          {{ $t('payment.emi.info.exportBusinessInfo') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.exportType') }}：</label>
              <span>{{ accountInfo.exportType || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.exportCountries') }}：</label>
              <span>{{ accountInfo.exportCountries || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.exportProductName') }}：</label>
              <span>{{ accountInfo.exportProductName || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="12">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.historicalAnnualExport') }}：</label>
              <span>{{ accountInfo.historicalAnnualExport || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.estimatedAnnualExport') }}：</label>
              <span>{{ accountInfo.estimatedAnnualExport || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 联系人信息 -->
        <el-divider content-position="left">
          <i class="el-icon-user-solid"></i>
          {{ $t('payment.emi.info.contactPersonInfo') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.fullName') }}：</label>
              <span>{{ accountInfo.fullName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.nationality') }}：</label>
              <span>{{ getNationalityName(accountInfo.nationality) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.email') }}：</label>
              <span>{{ accountInfo.email || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.idType') }}：</label>
              <span>{{ getIdTypeName(accountInfo.idType) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.info.idNumberField') }}：</label>
              <span>{{ accountInfo.idNumber || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 附件信息 -->
        <el-divider content-position="left">
          <i class="el-icon-picture"></i>
          {{ $t('payment.emi.info.attachmentInfo') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="12">
            <div class="info-item-vertical">
              <label>{{ $t('payment.emi.info.idPhoto') }}：</label>
              <div class="image-container">
                <el-image
                  v-if="accountInfo.idPhoto"
                  :src="accountInfo.idPhoto"
                  :preview-src-list="[accountInfo.idPhoto]"
                  class="document-image"
                  fit="contain"
                >
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
                <span v-else class="no-data">{{ $t('payment.emi.info.noPhoto') }}</span>
              </div>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="info-item-vertical">
              <label>{{ $t('payment.emi.info.businessRegistration') }}：</label>
              <div class="image-container">
                <el-image
                  v-if="accountInfo.businessRegistration"
                  :src="accountInfo.businessRegistration"
                  :preview-src-list="[accountInfo.businessRegistration]"
                  class="document-image"
                  fit="contain"
                >
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
                <span v-else class="no-data">{{ $t('payment.emi.info.noPhoto') }}</span>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="12">
            <div class="info-item-vertical">
              <label>{{ $t('payment.emi.info.companyRegistration') }}：</label>
              <div class="image-container">
                <el-image
                  v-if="accountInfo.companyRegistration"
                  :src="accountInfo.companyRegistration"
                  :preview-src-list="[accountInfo.companyRegistration]"
                  class="document-image"
                  fit="contain"
                >
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
                <span v-else class="no-data">{{ $t('payment.emi.info.noPhoto') }}</span>
              </div>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="info-item-vertical">
              <label>{{ $t('payment.emi.info.officeScene') }}：</label>
              <div class="image-container">
                <el-image
                  v-if="accountInfo.officeScene"
                  :src="accountInfo.officeScene"
                  :preview-src-list="[accountInfo.officeScene]"
                  class="document-image"
                  fit="contain"
                >
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
                <span v-else class="no-data">{{ $t('payment.emi.info.noPhoto') }}</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 无数据提示 -->
      <el-empty v-if="!accountInfo && !loading" :description="$t('payment.emi.info.noData')"></el-empty>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog
      :title="
        accountType === 'personal'
          ? $t('payment.emi.info.personalAuditTitle')
          : $t('payment.emi.info.companyAuditTitle')
      "
      :visible.sync="auditDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="auditForm" :model="auditForm" :rules="auditRules" label-width="100px">
        <el-form-item :label="$t('payment.emi.info.auditResult')" prop="status">
          <el-radio-group v-model="auditForm.status">
            <el-radio :label="3">{{ $t('payment.emi.info.auditPass') }}</el-radio>
            <el-radio :label="4">{{ $t('payment.emi.info.auditReject') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <!--        <el-form-item :label="$t('payment.emi.info.remark')">-->
        <!--          <el-input v-model="auditForm.remark" type="textarea" :rows="4" :placeholder="$t('payment.emi.info.remarkPlaceholder')" maxlength="500"-->
        <!--            show-word-limit />-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelAudit">{{ $t('payment.emi.info.cancelButton') }}</el-button>
        <el-button type="primary" @click="submitAudit">{{ $t('payment.emi.info.confirmButton') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { emiCompanyAudit, emiUserStatus, getCompanyInformation, getPersonalInformation } from '@/api/payment/emi'

export default {
  name: 'AccountInfo',
  data() {
    return {
      loading: false,
      auditLoading: false,
      loadingText: this.$t('payment.emi.info.processing'),
      accountType: '', // personal 或 company
      accountId: null,
      accountInfo: null,
      // 审核对话框
      auditDialogVisible: false,
      auditForm: {
        status: null,
        remark: ''
      },
      auditRules: {
        status: [{ required: true, message: this.$t('payment.emi.info.auditResult'), trigger: 'change' }]
      }
    }
  },
  created() {
    // 检查是否是 binderr 回调（有 code 和 state 参数）
    const code = this.$route.query.code
    const state = this.$route.query.state

    if (code && state) {
      // 从 state 参数中解析 id 和 type
      try {
        const stateData = JSON.parse(decodeURIComponent(state))
        this.accountId = stateData.id
        this.accountType = stateData.type

        if (this.accountType === 'company') {
          this.handleBinderrCallback(code)
          return
        }
      } catch (e) {
        console.error('解析 state 参数失败:', e)
        this.$message.error(this.$t('payment.emi.info.callbackParamError'))
        return
      }
    } else {
      // 正常访问，从路由参数获取 id 和 type
      this.accountId = this.$route.query.id //账号id
      this.accountType = this.$route.query.type //账号类型
    }

    if (!this.accountId || !this.accountType) {
      this.$message.error(this.$t('payment.emi.info.missingParams'))
      return
    }

    if (this.accountType !== 'personal' && this.accountType !== 'company') {
      this.$message.error(this.$t('payment.emi.info.invalidAccountType'))
      return
    }

    // 加载账户信息
    this.loadAccountInfo()
  },
  methods: {
    /** 加载账户信息 */
    loadAccountInfo() {
      this.loading = true

      // 根据账户类型调用不同的接口
      const apiMethod = this.accountType === 'personal' ? getPersonalInformation : getCompanyInformation

      return apiMethod(this.accountId)
        .then(response => {
          this.accountInfo = response.data
          this.loading = false
        })
        .catch(error => {
          this.loading = false
          this.$message.error(
            this.$t('payment.emi.info.fetchError') + '：' + (error.message || this.$t('payment.emi.info.unknown'))
          )
          throw error
        })
    },

    /** 获取国家名称 */
    getNationalityName(code) {
      if (!code) return '-'
      const country = this.$t(`payment.emi.info.countries.${code}`)
      return country === `payment.emi.info.countries.${code}` ? code : country
    },

    /** 获取证件类型名称 */
    getIdTypeName(type) {
      if (type === null || type === undefined) return '-'
      const typeMap = {
        0: this.$t('payment.emi.info.idCard'),
        1: this.$t('payment.emi.info.passport'),
        2: this.$t('payment.emi.info.driverLicense'),
        3: this.$t('payment.emi.info.militaryID'),
        4: this.$t('payment.emi.info.otherIdType')
      }
      return typeMap[type.toString()] || this.$t('payment.emi.info.unknown')
    },

    /** 获取性别名称 */
    getGenderName(gender) {
      if (!gender) return '-'
      const genderMap = {
        M: this.$t('payment.emi.info.male'),
        F: this.$t('payment.emi.info.female'),
        O: this.$t('payment.emi.info.other')
      }
      return genderMap[gender] || this.$t('payment.emi.info.unknown')
    },

    /** 获取状态名称 */
    getStatusName(status) {
      if (status === null || status === undefined) return '-'
      const statusMap = {
        0: this.$t('payment.emi.info.statusPendingSubmit'),
        1: this.$t('payment.emi.info.statusPendingReview'),
        2: this.$t('payment.emi.info.statusPendingBinnerReview'),
        3: this.$t('payment.emi.info.statusApproved'),
        4: this.$t('payment.emi.info.statusRejected')
      }
      return statusMap[status] || this.$t('payment.emi.info.unknown')
    },

    /** 获取状态标签类型 */
    getStatusType(status) {
      const typeMap = {
        0: 'info', // 待提交资料
        1: 'warning', // 待审核
        2: 'warning', // 待binner审核
        3: 'success', // 审核通过
        4: 'danger' // 审核失败
      }
      return typeMap[status] || 'info'
    },

    /** 返回上一页 */
    goBack() {
      this.$router.go(-1)
    },

    /** 打开审核对话框 */
    handleAudit() {
      this.auditDialogVisible = true
      this.auditForm = {
        status: null,
        remark: ''
      }
    },

    /** 提交审核 */
    submitAudit() {
      this.$refs.auditForm.validate(valid => {
        if (!valid) {
          return
        }

        // 确认对话框
        const statusText =
          this.auditForm.status === 3 ? this.$t('payment.emi.info.pass') : this.$t('payment.emi.info.reject')
        this.$confirm(
          this.$t('payment.emi.info.auditConfirm', { action: statusText }),
          this.$t('payment.emi.info.warning'),
          {
            confirmButtonText: this.$t('payment.emi.info.confirmButton'),
            cancelButtonText: this.$t('payment.emi.info.cancelButton'),
            type: 'warning'
          }
        )
          .then(() => {
            // 如果是企业账户且审核通过，跳转到 binderr 进行审核
            if (this.accountType === 'company' && this.auditForm.status === 3) {
              this.redirectToBinderr()
              return
            }

            // 个人账户或企业账户审核拒绝，直接调用后端接口
            this.performAudit()
          })
          .catch(() => {
            // 取消操作
          })
      })
    },

    /** 跳转到 binderr 进行企业审核 */
    redirectToBinderr() {
      // 获取当前页面基础 URL（不带查询参数）
      const baseUrl = window.location.origin + window.location.pathname

      // binderr OAuth2 认证地址
      const binderrUrl = process.env.VUE_APP_BINDERR_OAUTH_URL
      const clientId = process.env.VUE_APP_BINDERR_CLIENT_ID
      const redirectUri = encodeURIComponent(baseUrl)

      // 使用 state 参数传递 id 和 type 信息
      const state = encodeURIComponent(
        JSON.stringify({
          id: this.accountId,
          type: this.accountType
        })
      )

      // 关闭审核对话框
      this.auditDialogVisible = false

      // 将审核状态存储到 sessionStorage，回调时使用
      const backendStatus = this.auditForm.status === 3 ? 1 : 2
      sessionStorage.setItem('pendingAuditStatus', backendStatus.toString())
      sessionStorage.setItem('pendingAuditRemark', this.auditForm.remark || '')

      // 跳转到 binderr
      window.location.href = `${binderrUrl}?client_id=${clientId}&redirect_uri=${redirectUri}&state=${state}`
    },

    /** 处理 binderr 回调 */
    async handleBinderrCallback(code) {
      this.auditLoading = true
      this.loadingText = this.$t('payment.emi.info.submittingCompanyAudit')

      // 从 sessionStorage 获取之前保存的审核状态
      const status = parseInt(sessionStorage.getItem('pendingAuditStatus') || '1', 10)
      const remark = sessionStorage.getItem('pendingAuditRemark') || ''

      // 清除 sessionStorage
      sessionStorage.removeItem('pendingAuditStatus')
      sessionStorage.removeItem('pendingAuditRemark')

      // 准备提交的数据
      const submitData = {
        id: this.accountId,
        status: status,
        code: code, // 将 binderr 返回的 code 传递给后端
        remark: remark
      }

      const resetQuery = {
        id: this.accountId,
        type: this.accountType
      }

      let auditSucceeded = false
      try {
        await emiCompanyAudit(submitData)
        this.$message.success(this.$t('payment.emi.info.companyAuditSuccess'))
        auditSucceeded = true
      } catch (error) {
        this.$message.error(
          this.$t('payment.emi.info.companyAuditError') + '：' + (error.message || this.$t('payment.emi.info.unknown'))
        )
      }

      if (auditSucceeded) {
        try {
          await this.loadAccountInfo()
        } catch (error) {
          // 数据刷新失败时已在 loadAccountInfo 内部提示，这里无需重复提示
        }
      }

      this.auditLoading = false
      this.$router.replace({ query: resetQuery })
    },

    /** 执行审核操作（个人账户或企业拒绝） */
    async performAudit() {
      this.auditLoading = true
      this.loadingText = this.$t('payment.emi.info.submittingAudit')

      let auditSucceeded = false
      try {
        if (this.accountType === 'personal') {
          const submitData = {
            id: this.accountInfo.id,
            status: this.auditForm.status,
            remark: this.auditForm.remark
          }
          await emiUserStatus(submitData)
        } else {
          const submitData = {
            id: this.accountInfo.id,
            status: this.auditForm.status === 3 ? 1 : 2,
            remark: this.auditForm.remark
          }
          await emiCompanyAudit(submitData)
        }

        this.$message.success(this.$t('payment.emi.info.auditSuccess'))
        this.auditDialogVisible = false
        auditSucceeded = true
      } catch (error) {
        this.$message.error(
          this.$t('payment.emi.info.auditError') + '：' + (error.message || this.$t('payment.emi.info.unknown'))
        )
      }

      if (auditSucceeded) {
        try {
          await this.loadAccountInfo()
        } catch (error) {
          // 数据刷新失败时已在 loadAccountInfo 内部提示，这里无需重复提示
        }
      }

      this.auditLoading = false
    },

    /** 取消审核 */
    cancelAudit() {
      this.auditDialogVisible = false
      this.auditForm = {
        status: null,
        remark: ''
      }
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.card-title i {
  margin-right: 8px;
  font-size: 20px;
}

.account-info {
  padding: 10px 0;
}

/* 顶部标签信息栏 */
.info-tags {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  padding: 15px;
  background-color: #f9f9f9;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  margin-bottom: 20px;
}

.info-text {
  font-size: 13px;
  color: #606266;
  padding: 0 8px;
  white-space: nowrap;
}

/* 信息行 */
.info-row {
  margin-bottom: 16px;
}

/* 信息项 */
.info-item {
  display: flex;
  align-items: baseline;
  font-size: 14px;
  line-height: 28px;
  min-height: 28px;
}

.info-item label {
  color: #606266;
  font-weight: normal;
  flex-shrink: 0;
  margin-right: 8px;
  min-width: 80px;
}

.info-item span {
  color: #303133;
  flex: 1;
  word-break: break-all;
}

.no-data {
  color: #909399;
  font-size: 14px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}

::v-deep .el-card__header {
  padding: 18px 20px;
  border-bottom: 1px solid #ebeef5;
  background-color: #f5f7fa;
}

::v-deep .el-divider__text {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  background-color: #fff;
}

::v-deep .el-divider--horizontal {
  margin: 20px 0;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: '';
}

.clearfix:after {
  clear: both;
}

/* 垂直布局的信息项（用于附件） */
.info-item-vertical {
  display: flex;
  flex-direction: column;
  font-size: 14px;
  margin-bottom: 15px;
}

.info-item-vertical label {
  color: #606266;
  font-weight: normal;
  margin-bottom: 10px;
  min-width: 80px;
}

.image-container {
  display: flex;
  flex-direction: column;
}

.document-image {
  width: 100%;
  max-width: 400px;
  height: auto;
  cursor: pointer;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition: all 0.3s;
}

.document-image:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
