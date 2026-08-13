<template>
  <div class="app-container">
    <el-card v-if="userType !== null" v-loading="loading">
      <!-- 页面标题 -->
      <div slot="header" class="clearfix">
        <span class="card-title">
          <i :class="accountType === 'personal' ? 'el-icon-user' : 'el-icon-office-building'"></i>
          {{
            accountType === 'personal' ? $t('payment.emi.userInfo.personalAccountInfo') : $t('payment.emi.userInfo.companyAccountInfo')
          }}
          <el-tag v-if="accountInfo" :type="getStatusType(accountInfo.status)" size="small" style="margin-left: 12px">
            {{ getStatusName(accountInfo.status) }}
          </el-tag>
        </span>
      </div>

      <!-- 个人账户信息展示 -->
      <div v-if="accountType === 'personal' && accountInfo" class="account-info">
        <!-- 分隔线 -->
        <el-divider content-position="left">
          <i class="el-icon-user-solid"></i>
          {{ $t('payment.emi.userInfo.basicInfo') }}
        </el-divider>

        <!-- 基础信息三列布局 -->
        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.idNumber') }}：</label>
              <span>{{ accountInfo.id }}</span>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.fullName') }}：</label>
              <span>{{ accountInfo.fullName }}</span>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.gender') }}：</label>
              <span>{{ getGenderName(accountInfo.gender) }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.birthDate') }}：</label>
              <span>{{ accountInfo.birthDate || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.nationality') }}：</label>
              <span>{{ getNationalityName(accountInfo.nationality) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.residenceCountry') }}：</label>
              <span>{{ getNationalityName(accountInfo.residenceCountry) }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.registrationDate') }}：</label>
              <span>{{ accountInfo.createdAt }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.email') }}：</label>
              <span>{{ accountInfo.email }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.fundSource') }}：</label>
              <span>{{ accountInfo.fundSource }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.bankAccount') }}：</label>
              <span>{{ accountInfo.cardNumber || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.bank') }}：</label>
              <span>{{ accountInfo.bank || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 联系信息 -->
        <el-divider content-position="left">
          <i class="el-icon-phone"></i>
          {{ $t('payment.emi.userInfo.contactInfo') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.contactPhone') }}：</label>
              <span>{{ accountInfo.contactPhone }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.idType') }}：</label>
              <span>{{ getIdTypeName(accountInfo.idType) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.idNumberField') }}：</label>
              <span>{{ accountInfo.idNumber }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.provinceCity') }}：</label>
              <span>{{ accountInfo.residenceProvinceCity || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.postalCode') }}：</label>
              <span>{{ accountInfo.residencePostalCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.residenceAddress') }}：</label>
              <span>{{ accountInfo.residenceAddress }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 证件照片 -->
        <el-divider content-position="left">
          <i class="el-icon-picture"></i>
          {{ $t('payment.emi.userInfo.idPhotos') }}
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
            <span v-else class="no-data">{{ $t('payment.emi.userInfo.noPhoto') }}</span>
          </el-col>
        </el-row>
      </div>

      <!-- 企业账户信息展示 -->
      <div v-if="accountType === 'company' && accountInfo" class="account-info">
        <!-- 分隔线 -->
        <el-divider content-position="left">
          <i class="el-icon-office-building"></i>
          {{ $t('payment.emi.userInfo.companyBasicInfo') }}
        </el-divider>

        <!-- 基础信息三列布局 -->
        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.idNumber') }}：</label>
              <span>{{ accountInfo.id }}</span>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.companyNameChinese') }}：</label>
              <span>{{ accountInfo.companyNameChinese || '-' }}</span>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.companyNameEnglish') }}：</label>
              <span>{{ accountInfo.companyNameEnglish || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.businessRegistrationNumber') }}：</label>
              <span>{{ accountInfo.businessRegistrationNumber || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.companyRegistrationCode') }}：</label>
              <span>{{ accountInfo.companyRegistrationCode || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.registeredCountry') }}：</label>
              <span>{{ getNationalityName(accountInfo.registeredCountry) }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.establishmentDate') }}：</label>
              <span>{{ accountInfo.establishmentDate || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.expirationDate') }}：</label>
              <span>{{ accountInfo.expirationDate || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.employeeCount') }}：</label>
              <span>{{ accountInfo.employeeCount || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.industryType') }}：</label>
              <span>{{ accountInfo.industryType || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.website') }}：</label>
              <span>{{ accountInfo.website || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.statusLabel') }}：</label>
              <el-tag :type="getStatusType(accountInfo.status)" size="small">
                {{ getStatusName(accountInfo.status) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.createdAt') }}：</label>
              <span>{{ accountInfo.createdAt || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.updatedAt') }}：</label>
              <span>{{ accountInfo.updatedAt || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.userId') }}：</label>
              <span>{{ accountInfo.userId || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.bankAccount') }}：</label>
              <span>{{ accountInfo.cardNumber || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.bank') }}：</label>
              <span>{{ accountInfo.bank || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 地址信息 -->
        <el-divider content-position="left">
          <i class="el-icon-location"></i>
          {{ $t('payment.emi.userInfo.addressInfo') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="12">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.registeredAddress') }}：</label>
              <span>{{ accountInfo.registeredAddress || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.actualOperationAddress') }}：</label>
              <span>{{ accountInfo.actualOperationAddress || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 出口信息 -->
        <el-divider content-position="left">
          <i class="el-icon-sell"></i>
          {{ $t('payment.emi.userInfo.exportBusinessInfo') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.exportType') }}：</label>
              <span>{{ accountInfo.exportType || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.exportCountries') }}：</label>
              <span>{{ accountInfo.exportCountries || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.exportProductName') }}：</label>
              <span>{{ accountInfo.exportProductName || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="12">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.historicalAnnualExport') }}：</label>
              <span>{{ accountInfo.historicalAnnualExport || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.estimatedAnnualExport') }}：</label>
              <span>{{ accountInfo.estimatedAnnualExport || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 联系人信息 -->
        <el-divider content-position="left">
          <i class="el-icon-user-solid"></i>
          {{ $t('payment.emi.userInfo.contactPersonInfo') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.fullName') }}：</label>
              <span>{{ accountInfo.fullName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.nationality') }}：</label>
              <span>{{ getNationalityName(accountInfo.nationality) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.email') }}：</label>
              <span>{{ accountInfo.email || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.idType') }}：</label>
              <span>{{ getIdTypeName(accountInfo.idType) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>{{ $t('payment.emi.userInfo.idNumberField') }}：</label>
              <span>{{ accountInfo.idNumber || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 附件信息 -->
        <el-divider content-position="left">
          <i class="el-icon-picture"></i>
          {{ $t('payment.emi.userInfo.attachmentInfo') }}
        </el-divider>

        <el-row :gutter="20" class="info-row">
          <el-col :span="12">
            <div class="info-item-vertical">
              <label>{{ $t('payment.emi.userInfo.idPhoto') }}：</label>
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
                <span v-else class="no-data">{{ $t('payment.emi.userInfo.noPhoto') }}</span>
              </div>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="info-item-vertical">
              <label>{{ $t('payment.emi.userInfo.businessRegistration') }}：</label>
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
                <span v-else class="no-data">{{ $t('payment.emi.userInfo.noPhoto') }}</span>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-row">
          <el-col :span="12">
            <div class="info-item-vertical">
              <label>{{ $t('payment.emi.userInfo.companyRegistration') }}：</label>
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
                <span v-else class="no-data">{{ $t('payment.emi.userInfo.noPhoto') }}</span>
              </div>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="info-item-vertical">
              <label>{{ $t('payment.emi.userInfo.officeScene') }}：</label>
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
                <span v-else class="no-data">{{ $t('payment.emi.userInfo.noPhoto') }}</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <!-- 暂无数据 -->
    <el-card v-else>
      <div style="text-align: center; padding: 60px 20px">
        <i class="el-icon-warning" style="font-size: 64px; color: #909399; margin-bottom: 20px"></i>
        <p style="font-size: 16px; color: #606266; margin: 0">{{ $t('payment.emi.userInfo.noEmiInfo') }}</p>
      </div>
    </el-card>
  </div>
</template>

<script>
import {getCompanyInformationUser, getPersonalInformationUser} from '@/api/payment/emi'
import {authListShop} from '@/api/payment/shop'

export default {
  name: 'EmiUserInfo',
  data() {
    return {
      loading: false,
      accountType: '', // personal 或 company
      accountId: null,
      accountInfo: null,
      userId: null, // 系统用户ID
      userType: null // 用户类型：1-个人账户 2-企业账户
    }
  },
  created() {
    // 获取商户信息
    this.getShopInfo()
  },
  methods: {
    /** 获取商户信息 */
    getShopInfo() {
      this.loading = true
      authListShop()
          .then(response => {
            if (response.rows && response.rows.length > 0) {
              const shopInfo = response.rows[0]
              this.userId = shopInfo.userId
              this.userType = shopInfo.userType // 1:个人账户  2:企业账户
              console.log('this.userId, this.userType')
              console.log(this.userId, this.userType)

              // 检查 userType 是否为 null 或 undefined，静默处理不显示错误
              if (this.userType === null || this.userType === undefined) {
                this.loading = false
                // 保持 userType 为 null，显示空状态
                return
              }

              // 根据用户类型进行数据加载
              if (this.userType === '1' || this.userType === 1) {
                // 个人账户，调用个人信息接口
                this.accountType = 'personal'
                getPersonalInformationUser(this.userId)
                    .then(response => {
                      if (response.code === 200 && response.data) {
                        const data = response.data
                        this.accountId = data.id
                        this.accountInfo = data
                        this.loading = false
                      } else {
                        this.loading = false
                      }
                    })
                    .catch(error => {
                      this.loading = false
                      this.$message.error(
                          this.$t('payment.emi.userInfo.fetchPersonalError') +
                          '：' +
                          (error.message || this.$t('payment.emi.userInfo.unknownError'))
                      )
                    })
              } else if (this.userType === '2' || this.userType === 2) {
                // 企业账户，调用企业信息接口
                this.accountType = 'company'
                getCompanyInformationUser(this.userId)
                    .then(response => {
                      if (response.code === 200 && response.data) {
                        const data = response.data
                        this.accountId = data.id
                        this.accountInfo = data
                        this.loading = false
                      } else {
                        this.loading = false
                      }
                    })
                    .catch(error => {
                      this.loading = false
                      this.$message.error(
                          this.$t('payment.emi.userInfo.fetchCompanyError') +
                          '：' +
                          (error.message || this.$t('payment.emi.userInfo.unknownError'))
                      )
                    })
              } else {
                // 未知账户类型，设置为 null 显示空状态
                this.userType = null
                this.loading = false
              }
            } else {
              // 没有商户信息，设置 userType 为 null 并静默处理
              this.userType = null
              this.loading = false
            }
          })
          .catch(error => {
            // 请求失败，设置 userType 为 null 并显示错误提示
            this.userType = null
            this.loading = false
            console.log("error", "获取authListShop接口失败")
            // this.$message.error(
            //     this.$t('payment.emi.userInfo.fetchShopError') + '：' + (error.message || this.$t('payment.emi.userInfo.unknownError'))
            // )
          })
    },

    /** 获取国家名称 */
    getNationalityName(code) {
      if (!code) return '-'
      const countryKey = `emi.userInfo.countries.${code}`
      const translated = this.$t(countryKey)
      // 如果翻译键不存在，返回原始代码
      return translated !== countryKey ? translated : code
    },

    /** 获取证件类型名称 */
    getIdTypeName(type) {
      if (type === null || type === undefined) return '-'
      const typeMap = {
        0: 'idCard',
        1: 'passport',
        2: 'driverLicense',
        3: 'militaryID',
        4: 'otherIdType'
      }
      const key = typeMap[type.toString()]
      return key ? this.$t(`emi.userInfo.${key}`) : this.$t('payment.emi.userInfo.unknown')
    },

    /** 获取性别名称 */
    getGenderName(gender) {
      if (!gender) return '-'
      const genderMap = {
        M: 'male',
        F: 'female',
        O: 'other'
      }
      const key = genderMap[gender]
      return key ? this.$t(`emi.userInfo.${key}`) : this.$t('payment.emi.userInfo.unknown')
    },

    /** 获取状态名称 */
    getStatusName(status) {
      if (status === null || status === undefined) return '-'
      const statusMap = {
        0: 'statusPendingSubmit',
        1: 'statusPendingReview',
        2: 'statusPendingBinnerReview',
        3: 'statusApproved',
        4: 'statusRejected'
      }
      const key = statusMap[status]
      return key ? this.$t(`emi.userInfo.${key}`) : this.$t('payment.emi.userInfo.unknown')
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
