<template>
  <div :class="{ 'app-container': true, 'full-screen': form.businessProfileStatus !== 1 }">
    <div v-if="form.businessProfileStatus !== 1" class="header">
      <language-switch class="language-switch-btn" />
      <el-button type="text" class="logout-btn" @click="logout">
        <i class="el-icon-switch-button" />
        {{ $t('payment.shopAuth.logout') }}
      </el-button>
    </div>

    <el-alert
      v-if="form.businessProfileStatus === -1"
      :title="$t('payment.shopAuth.fillMerchantInfo')"
      type="info"
      show-icon
      :closable="false"
      style="margin-bottom: 20px; font-size: 18px; font-weight: bold; color: #ff0000"
    />

    <el-alert
      v-if="form.businessProfileStatus === 0"
      :title="$t('payment.shopAuth.merchantStatusReviewing')"
      type="info"
      :description="$t('payment.shopAuth.reviewPending')"
      show-icon
      :closable="false"
      style="margin-bottom: 20px"
    />
    <el-alert
      v-if="form.businessProfileStatus === 2"
      :title="$t('payment.shopAuth.merchantStatusRejected')"
      type="error"
      :description="$t('payment.shopAuth.modifyAndResubmit')"
      show-icon
      :closable="false"
      style="margin-bottom: 20px"
    />
    <el-alert
      v-if="form.deleted === 1"
      :title="$t('payment.shopAuth.merchantDisabled')"
      type="warning"
      :description="$t('payment.shopAuth.accountDisabled')"
      show-icon
      :closable="false"
      style="margin-bottom: 20px"
    />

    <el-form ref="form" :model="form" :rules="rules" label-width="240px">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.merchantId')">
            <el-input v-model="form.id" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.privateKey')" prop="merchantHashSecretKey">
            <el-input
              v-model="form.merchantHashSecretKey"
              :placeholder="$t('payment.shopAuth.enterPrivateKey')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.company')" prop="businessName">
            <el-input v-model="form.businessName" :placeholder="$t('payment.shopAuth.enterCompanyName')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.phoneNumber')" prop="phonenumber">
            <el-input v-model="form.phonenumber" :placeholder="$t('payment.shopAuth.enterPhoneNumber')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.businessNumber')" prop="businessNo">
            <el-input v-model="form.businessNo" :placeholder="$t('payment.shopAuth.enterRegistrationNumber')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.registrationCountry')" prop="country">
            <el-select v-model="form.country" :placeholder="$t('payment.shopAuth.selectCountry')" filterable clearable>
              <el-option
                v-for="country in countryOptions"
                :key="country.code"
                :label="country.name"
                :value="country.code"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.vatNumber')" prop="vatNumber">
            <el-input v-model="form.vatNumber" :placeholder="$t('payment.shopAuth.enterVatNumber')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.companyEstablishDate')" prop="registrationDate">
            <el-date-picker
              v-model="form.registrationDate"
              type="date"
              :placeholder="$t('payment.shopAuth.selectEstablishDate')"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <!--        <el-col :span="12">-->
        <!--          <el-form-item label="回调地址" prop="notifyOrderStatusEndpoint">-->
        <!--            <el-input v-model="form.notifyOrderStatusEndpoint" placeholder="请输入回调地址" />-->
        <!--          </el-form-item>-->
        <!--        </el-col>-->
        <el-col :span="24">
          <el-form-item :label="$t('payment.shopAuth.status')">
            <el-tag :type="form.businessProfileStatus === 1 ? 'success' : 'danger'">{{ statusFormat(form) }}</el-tag>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-if="form.businessProfileStatus === 2">
        <el-col :span="24">
          <el-form-item :label="$t('payment.shopAuth.failureReason')" label-width="260px">
            <div class="fail-reason">{{ form.remark || $t('payment.shopAuth.noFailureReason') }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">{{ $t('payment.shopAuth.companyAuthInfo') }}</el-divider>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.companyAuthType')" prop="companyAuthType" label-width="260px">
            <el-select
              v-model="form.companyAuthType"
              :placeholder="$t('payment.shopAuth.selectCompanyAuthType')"
              filterable
            >
              <el-option
                v-for="dict in companyAuthTypeOptions"
                :key="dict.dictValue"
                :label="$i18n.locale === 'en' ? dict.dictLabel : dict.dictLabelZh"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            :label="$t('payment.shopAuth.companyAuthTypeFile')"
            prop="companyAuthTypeUrl"
            label-width="260px"
          >
            <el-upload
              :action="uploadUrl"
              :headers="headers"
              :before-upload="handleDocBeforeUpload"
              :on-success="handleCompanyAuthTypeUrlSuccess"
              :file-list="companyAuthTypeList"
              :on-remove="handleCompanyAuthTypeUrlRemove"
              accept=".doc,.docx,.pdf"
            >
              <el-button type="primary">{{ $t('payment.shopAuth.reupload') }}</el-button>
              <div slot="tip" class="el-upload__tip">{{ $t('payment.shopAuth.supportedFormats') }}</div>
            </el-upload>
            <div v-if="form.companyAuthTypeUrl" style="margin-top: 10px">
              <el-button
                type="text"
                icon="el-icon-document"
                @click="handleFileDownload(form.companyAuthTypeUrl, $t('payment.shopAuth.companyAuthTypeFile'))"
              >
                {{ $t('payment.shopAuth.viewCurrentFile') }}
              </el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('payment.shopAuth.companyCharterDoc')" prop="companyDocUrl" label-width="260px">
            <el-upload
              :action="uploadUrl"
              :headers="headers"
              :before-upload="handleDocBeforeUpload"
              :on-success="handleCompanyDocSuccess"
              :file-list="companyDocList"
              :on-remove="handleCompanyDocRemove"
              accept=".doc,.docx,.pdf"
            >
              <el-button type="primary">{{ $t('payment.shopAuth.reupload') }}</el-button>
              <div slot="tip" class="el-upload__tip">{{ $t('payment.shopAuth.supportedFormats') }}</div>
            </el-upload>
            <div v-if="form.companyDocUrl" style="margin-top: 10px">
              <el-button
                type="text"
                icon="el-icon-document"
                @click="handleFileDownload(form.companyDocUrl, $t('payment.shopAuth.companyCharterDocFileName'))"
              >
                {{ $t('payment.shopAuth.viewCurrentFile') }}
              </el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item
            :label="$t('payment.shopAuth.companyOrgChart')"
            prop="shareholdingStructure"
            label-width="260px"
          >
            <el-upload
              :action="uploadUrl"
              :headers="headers"
              :before-upload="handleDocBeforeUpload"
              :on-success="handleOrgChartSuccess"
              :file-list="orgChartList"
              :on-remove="handleOrgChartRemove"
              accept=".doc,.docx,.pdf"
            >
              <el-button type="primary">{{ $t('payment.shopAuth.reupload') }}</el-button>
              <div slot="tip" class="el-upload__tip">{{ $t('payment.shopAuth.supportedFormats') }}</div>
            </el-upload>
            <div v-if="form.shareholdingStructure" style="margin-top: 10px">
              <el-button
                type="text"
                icon="el-icon-document"
                @click="handleFileDownload(form.shareholdingStructure, $t('payment.shopAuth.companyOrgChartFileName'))"
              >
                {{ $t('payment.shopAuth.viewCurrentFile') }}
              </el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">{{ $t('payment.shopAuth.directorInfo') }}</el-divider>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.directorFullName')" prop="directorName" label-width="260px">
            <el-input v-model="form.directorName" :placeholder="$t('payment.shopAuth.enterDirectorName')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shopAuth.directorEmail')" prop="directorEmail">
            <el-input v-model="form.directorEmail" :placeholder="$t('payment.shopAuth.enterDirectorEmail')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">{{ $t('payment.shopAuth.ultimateBeneficiaryInfo') }}</el-divider>
      <el-alert
        :title="$t('payment.shopAuth.beneficiaryNotice')"
        type="info"
        :description="$t('payment.shopAuth.beneficiaryNoticeDesc')"
        show-icon
        :closable="false"
        style="margin-bottom: 20px"
      />

      <el-button type="primary" style="margin-bottom: 20px" @click="addUbo">
        {{ $t('payment.shopAuth.addBeneficiary') }}
      </el-button>

      <div v-for="(ubo, index) in form.ubos" :key="index" class="ubo-item">
        <el-divider>{{ $t('payment.shopAuth.beneficiary') }} {{ index + 1 }}</el-divider>

        <el-button
          type="danger"
          icon="el-icon-delete"
          circle
          size="mini"
          class="remove-ubo-btn"
          @click="removeUbo(index)"
        />

        <el-row>
          <el-col :span="12">
            <el-form-item
              :label="$t('payment.shopAuth.beneficiaryEmail')"
              :prop="'ubos.' + index + '.uboEmail'"
              :rules="[
                { required: true, message: $t('payment.shopAuth.beneficiaryEmailRequired'), trigger: 'blur' },
                { validator: validateEmail, trigger: 'blur' }
              ]"
            >
              <el-input v-model="ubo.uboEmail" :placeholder="$t('payment.shopAuth.enterBeneficiaryEmail')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">{{ $t('payment.shopAuth.beneficiaryIdAuth') }}</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item
              :label="$t('payment.shopAuth.idProofType')"
              :prop="'ubos.' + index + '.authType'"
              :rules="[{ required: true, message: $t('payment.shopAuth.idProofTypeRequired'), trigger: 'change' }]"
            >
              <el-select v-model="ubo.authType" :placeholder="$t('payment.shopAuth.selectIdProofType')" filterable>
                <el-option
                  v-for="dict in authTypeOptions"
                  :key="dict.dictValue"
                  :label="$i18n.locale === 'en' ? dict.dictLabel : dict.dictLabelZh"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item
              :label="
                ubo.authType === 'PASSPORT' ? $t('payment.shopAuth.passport') : $t('payment.shopAuth.idDocumentFront')
              "
              :prop="'ubos.' + index + '.idDocFrontUrl'"
              :rules="[{ required: true, message: $t('payment.shopAuth.uploadIdDocument'), trigger: 'change' }]"
            >
              <el-upload
                :action="uploadUrl"
                :headers="headers"
                :before-upload="handleImageBeforeUpload"
                :on-success="response => handleIdDocFrontSuccess(response, index)"
                :file-list="getIdDocFrontList(index)"
                :on-remove="() => handleIdDocFrontRemove(index)"
                list-type="picture-card"
                :limit="1"
                accept=".jpg,.jpeg,.png"
              >
                <i class="el-icon-plus" />
                <div slot="tip" class="el-upload__tip">{{ $t('payment.shopAuth.imageFormats') }}</div>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">{{ $t('payment.shopAuth.beneficiaryAddressAuth') }}</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item
              :label="$t('payment.shopAuth.addressProofType')"
              :prop="'ubos.' + index + '.addressType'"
              :rules="[{ required: true, message: $t('payment.shopAuth.addressProofTypeRequired'), trigger: 'change' }]"
            >
              <el-select
                v-model="ubo.addressType"
                :placeholder="$t('payment.shopAuth.selectAddressProofType')"
                filterable
              >
                <el-option
                  v-for="dict in addressTypeOptions"
                  :key="dict.dictValue"
                  :label="$i18n.locale === 'en' ? dict.dictLabel : dict.dictLabelZh"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item
              :label="$t('payment.shopAuth.addressProofDocument')"
              :prop="'ubos.' + index + '.addressDocUrl'"
              :rules="[{ required: true, validator: validateFiles, trigger: 'change' }]"
            >
              <el-upload
                :action="uploadUrl"
                :headers="headers"
                :before-upload="handleDocBeforeUpload"
                :on-success="response => handleAddressDocSuccess(response, index)"
                :file-list="getAddressDocList(index)"
                :on-remove="() => handleAddressDocRemove(index)"
                accept=".doc,.docx,.pdf"
              >
                <el-button type="primary">{{ $t('payment.shopAuth.reupload') }}</el-button>
                <div slot="tip" class="el-upload__tip">{{ $t('payment.shopAuth.supportedFormats') }}</div>
              </el-upload>
              <div v-if="ubo.addressDocUrl" style="margin-top: 10px">
                <el-button
                  type="text"
                  icon="el-icon-document"
                  @click="handleFileDownload(ubo.addressDocUrl, $t('payment.shopAuth.addressProofDocumentName'))"
                >
                  {{ $t('payment.shopAuth.viewCurrentFile') }}
                </el-button>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </div>

      <el-form-item>
        <el-button type="primary" @click="submitForm">{{ $t('payment.shopAuth.submitReview') }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getCountryList } from '@/api/common/country'
import { authListShop, updateAuthShop } from '@/api/payment/shop'
import LanguageSwitch from '@/components/LanguageSwitch'
import { getToken } from '@/utils/auth'

export default {
  name: 'ShopStatusFail',
  components: {
    LanguageSwitch
  },
  dicts: ['SHOP_STATUS', 'KYB_COMPANY_AUTH_TYPE', 'KYB_AUTH_TYPE', 'KYB_ADDRESS_TYPE'],
  data() {
    return {
      // 状态数据字典
      statusOptions: [],
      // 新增KYB相关数据
      companyAuthTypeOptions: [],
      authTypeOptions: [],
      addressTypeOptions: [],
      // 国家选项
      countryOptions: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/uploadFile',
      companyDocList: [],
      companyAuthTypeList: [],
      orgChartList: [],
      idDocFrontList: [],
      idDocBackList: [],
      addressDocList: [],
      // 表单参数
      form: {
        id: undefined,
        businessName: undefined,
        phonenumber: undefined,
        product: undefined,
        notifyOrderStatusEndpoint: undefined,
        merchantHashSecretKey: undefined,
        businessProfileStatus: 2,
        remark: undefined,
        // 新增公司信息字段
        registrationDate: undefined,
        country: undefined,
        vatNumber: undefined,
        businessNo: undefined,
        companyAuthType: undefined,
        authType: undefined,
        addressType: undefined,
        companyDocUrl: undefined,
        companyAuthTypeUrl: undefined,
        shareholdingStructure: undefined,
        idDocFrontUrl: undefined,
        idDocBackUrl: undefined,
        addressDocUrl: undefined,
        directorName: undefined,
        directorEmail: undefined,
        ubos: []
      },
      // 表单校验
      rules: {
        businessName: [{ required: true, message: this.$t('payment.shopAuth.companyNameRequired'), trigger: 'blur' }],
        phonenumber: [{ required: true, message: this.$t('payment.shopAuth.phoneNumberRequired'), trigger: 'blur' }],
        merchantHashSecretKey: [
          { required: true, message: this.$t('payment.shopAuth.privateKeyRequired'), trigger: 'blur' }
        ],
        registrationDate: [
          { required: true, message: this.$t('payment.shopAuth.establishDateRequired'), trigger: 'change' }
        ],
        country: [
          { required: true, message: this.$t('payment.shopAuth.countryRequired'), trigger: 'blur' },
          { min: 2, max: 2, message: this.$t('payment.shopAuth.countryCodeLength'), trigger: 'blur' }
        ],
        vatNumber: [{ required: true, message: this.$t('payment.shopAuth.vatNumberRequired'), trigger: 'blur' }],
        businessNo: [
          { required: true, message: this.$t('payment.shopAuth.registrationNumberRequired'), trigger: 'blur' }
        ],
        companyAuthType: [
          { required: true, message: this.$t('payment.shopAuth.companyAuthTypeRequired'), trigger: 'change' }
        ],
        authType: [{ required: true, message: this.$t('payment.shopAuth.idProofTypeRequired'), trigger: 'change' }],
        addressType: [
          { required: true, message: this.$t('payment.shopAuth.addressProofTypeRequired'), trigger: 'change' }
        ],
        directorName: [
          { required: true, message: this.$t('payment.shopAuth.directorNameRequired'), trigger: 'blur' },
          { min: 2, message: this.$t('payment.shopAuth.directorNameMinLength'), trigger: 'blur' }
        ],
        directorEmail: [
          { required: true, message: this.$t('payment.shopAuth.directorEmailRequired'), trigger: 'blur' },
          { validator: this.validateEmail, trigger: 'blur' }
        ],
        ubos: [{ required: true, message: this.$t('payment.shopAuth.addAtLeastOneBeneficiary'), trigger: 'change' }],
        companyAuthTypeUrl: [
          { required: true, message: this.$t('payment.shopAuth.uploadCompanyAuthFile'), trigger: 'change' }
        ],
        companyDocUrl: [{ required: true, message: this.$t('payment.shopAuth.uploadCompanyDoc'), trigger: 'change' }],
        shareholdingStructure: [
          { required: true, message: this.$t('payment.shopAuth.uploadOrgChart'), trigger: 'change' }
        ],
        idDocFrontUrl: undefined,
        idDocBackUrl: undefined,
        addressDocUrl: undefined
      },
      // 添加文件格式限制
      fileTypes: {
        doc: ['doc', 'docx', 'pdf'], // 文档类型
        image: ['jpg', 'jpeg', 'png'] // 图片类型
      },
      // 文件大小限制（MB）
      maxFileSize: 20,
      // 添加新的数据属性来存储受益人文件列表
      uboFileList: [] // 用于存储所有受益人的文件列表
    }
  },
  computed: {
    headers() {
      return {
        Authorization: 'Bearer ' + getToken()
      }
    }
  },
  watch: {
    // 监听身份证明类型变化
    'form.authType'(newVal) {
      this.handleAuthTypeChange()
    }
  },
  created() {
    this.getShopInfo()
    this.getCountryList()
    this.getDicts('KYB_COMPANY_AUTH_TYPE').then(response => {
      this.companyAuthTypeOptions = response.data
    })
    this.getDicts('KYB_AUTH_TYPE').then(response => {
      this.authTypeOptions = response.data
    })
    this.getDicts('KYB_ADDRESS_TYPE').then(response => {
      this.addressTypeOptions = response.data
    })
  },
  methods: {
    /** 获取国家列表 */
    getCountryList() {
      getCountryList()
        .then(response => {
          this.countryOptions = response.data || []
        })
        .catch(error => {
          console.error('获取国家列表失败:', error)
          this.$message.error(this.$t('payment.shopAuth.getCountryListFailed'))
        })
    },
    /** 获取商户信息 */
    getShopInfo() {
      authListShop().then(response => {
        if (response.rows && response.rows.length > 0) {
          const shopInfo = response.rows[0]
          // 确保 ubos 是一个数组
          shopInfo.ubos = shopInfo.ubos || []
          this.form = { ...shopInfo }
          // 需要判断当前的状态status,如果=1,需要直接把用户登出,

          // 初始化受益人文件列表
          this.uboFileList = shopInfo.ubos.map(ubo => ({
            idDocFrontList: ubo.idDocFrontUrl
              ? [
                {
                  name: this.$t('payment.shopAuth.idDocumentFrontName'),
                  url: ubo.idDocFrontUrl
                }
              ]
              : [],
            addressDocList: ubo.addressDocUrl
              ? [
                {
                  name: this.$t('payment.shopAuth.addressProofDocumentName'),
                  url: ubo.addressDocUrl
                }
              ]
              : []
          }))

          // 初始化文件列表
          if (this.form.companyAuthTypeUrl) {
            this.companyAuthTypeList = [
              {
                name: this.$t('payment.shopAuth.companyAuthTypeFileName'),
                url: this.form.companyAuthTypeUrl
              }
            ]
          }
          if (this.form.companyDocUrl) {
            this.companyDocList = [
              {
                name: this.$t('payment.shopAuth.companyCharterDocFileName'),
                url: this.form.companyDocUrl
              }
            ]
          }
          if (this.form.shareholdingStructure) {
            this.orgChartList = [
              {
                name: this.$t('payment.shopAuth.companyOrgChartFileName'),
                url: this.form.shareholdingStructure
              }
            ]
          }
          if (this.form.idDocFrontUrl) {
            this.idDocFrontList = [
              {
                name: this.$t('payment.shopAuth.idDocumentFrontName'),
                url: this.form.idDocFrontUrl
              }
            ]
          }
          if (this.form.idDocBackUrl) {
            this.idDocBackList = [
              {
                name: this.$t('payment.shopAuth.idDocumentBackName'),
                url: this.form.idDocBackUrl
              }
            ]
          }
          if (this.form.addressDocUrl) {
            this.addressDocList = [
              {
                name: this.$t('payment.shopAuth.addressProofDocumentName'),
                url: this.form.addressDocUrl
              }
            ]
          }

          // 获取字典数据
          this.getDicts('BUSINESS_CUSTOMER_CORPORATION_BUSINESS_PROFILE_STATUS').then(response => {
            this.statusOptions = response.data
          })
        } else {
          // 如果没有数据，也要确保 ubos 是一个空数组
          this.form.ubos = []
        }
      })
    },
    /** 状态字典翻译 */
    statusFormat(row) {
      return this.selectDictLabel(this.statusOptions, row.businessProfileStatus)
    },
    /** 提交按钮 */
    submitForm() {
      console.log(this.form)
      this.$refs['form'].validate(valid => {
        if (valid) {
          // 验证受益人信息
          if (!this.form.ubos || this.form.ubos.length === 0) {
            this.$message.warning(this.$t('payment.shopAuth.addAtLeastOneBeneficiaryWarning'))
            return
          }

          // 验证公司相关文件
          if (!this.form.companyDocUrl) {
            this.$message.warning(this.$t('payment.shopAuth.uploadCompanyDocWarning'))
            return
          }
          if (!this.form.companyAuthTypeUrl) {
            this.$message.warning(this.$t('payment.shopAuth.uploadCompanyAuthFileWarning'))
            return
          }
          if (!this.form.shareholdingStructure) {
            this.$message.warning(this.$t('payment.shopAuth.uploadOrgChartWarning'))
            return
          }
          // 对国家country进行校验,只能是字母,长度为2
          if (!/^[a-zA-Z]{2}$/.test(this.form.country)) {
            this.$message.warning(this.$t('payment.shopAuth.countryCodeWarning'))
            return
          }
          // 同时将国家country转换为大写
          this.form.country = this.form.country.toUpperCase()

          updateAuthShop(this.form).then(() => {
            this.$message({
              message: this.$t('payment.shopAuth.submitSuccess'),
              type: 'success',
              duration: 1500,
              onClose: () => {
                location.reload()
              }
            })
          })
        }
      })
    },
    /** 重置按钮操作 */
    reset() {
      this.form = {
        id: undefined,
        businessName: undefined,
        phonenumber: undefined,
        product: undefined,
        notifyOrderStatusEndpoint: undefined,
        merchantHashSecretKey: undefined,
        businessProfileStatus: 2,
        remark: undefined,
        // 重置新增的公司信息字段
        registrationDate: undefined,
        country: undefined,
        vatNumber: undefined,
        businessNo: undefined,
        ubos: [] // 确保重置时 ubos 是一个空数组
      }
      this.$refs['form'].resetFields()
    },
    /** 退出登录 */
    logout() {
      this.$confirm(this.$t('payment.shopAuth.confirmLogout'), this.$t('payment.shopAuth.hint'), {
        confirmButtonText: this.$t('payment.shopAuth.confirm'),
        cancelButtonText: this.$t('payment.shopAuth.cancel'),
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index'
        })
      })
    },
    // 修改文件上传前的处理方法,文件大小限制
    beforeUpload(file, fileType) {
      const fileSizeInMB = file.size / 1024 / 1024
      const isLtMaxSize = fileSizeInMB < this.maxFileSize
      if (!isLtMaxSize) {
        this.$message.error(this.$t('payment.shopAuth.fileSizeExceeded', { size: this.maxFileSize }))
        return false
      }
      // 文件格式验证
      const fileName = file.name.toLowerCase()
      const extension = fileName.substring(fileName.lastIndexOf('.') + 1)
      const allowedTypes = this.fileTypes[fileType]

      if (!allowedTypes.includes(extension)) {
        this.$message.error(this.$t('payment.shopAuth.fileFormatError', { formats: allowedTypes.join(', ') }))
        return false
      }

      return true
    },

    // 修改各个上传组件的beforeUpload绑定
    handleDocBeforeUpload(file) {
      return this.beforeUpload(file, 'doc')
    },

    handleImageBeforeUpload(file) {
      return this.beforeUpload(file, 'image')
    },

    // 各类文件上传成功的处理方法
    handleCompanyDocSuccess(response, file) {
      if (response.code === 200) {
        // 假设后端返回code 200表示成功
        this.form.companyDocUrl = response.url // 更新form中的URL
        this.companyDocList = [
          {
            name: file.name, // 使用实际文件名而不是固定值
            url: response.url
          }
        ]
        this.$message.success(this.$t('payment.shopAuth.fileUploadSuccess'))
      } else {
        this.$message.error(response.msg || this.$t('payment.shopAuth.fileUploadFailed'))
        // 上传失败时清空
        this.form.companyDocUrl = undefined
        this.companyDocList = []
      }
    },

    // 各类文件上传成功的处理方法
    handleCompanyAuthTypeUrlSuccess(response, file) {
      if (response.code === 200) {
        // 假设后端返回code 200表示成功
        this.form.companyAuthTypeUrl = response.url // 更新form中的URL
        this.companyAuthTypeList = [
          {
            name: file.name, // 使用实际文件名而不是固定值
            url: response.url
          }
        ]
        this.$refs.form.validateField('companyAuthTypeUrl') // ✅ 手动触发校验
        this.$message.success(this.$t('payment.shopAuth.fileUploadSuccess'))
      } else {
        this.$message.error(response.msg || this.$t('payment.shopAuth.fileUploadFailed'))
        // 上传失败时清空
        this.form.companyAuthTypeUrl = undefined
        this.companyAuthTypeList = []
      }
    },
    handleOrgChartSuccess(response, file) {
      if (response.code === 200) {
        this.form.shareholdingStructure = response.url
        this.orgChartList = [
          {
            name: file.name, // 使用实际文件名而不是固定值
            url: response.url
          }
        ]
        this.$message.success(this.$t('payment.shopAuth.fileUploadSuccess'))
      } else {
        this.$message.error(response.msg || this.$t('payment.shopAuth.fileUploadFailed'))
        this.form.shareholdingStructure = undefined
        this.orgChartList = []
      }
    },
    handleIdDocFrontSuccess(response, index) {
      if (response.code === 200) {
        this.$set(this.form.ubos[index], 'idDocFrontUrl', response.url)
        this.$set(this.uboFileList[index], 'idDocFrontList', [
          {
            name: this.$t('payment.shopAuth.idDocumentFrontName'),
            url: response.url
          }
        ])
        this.$message.success(this.$t('payment.shopAuth.fileUploadSuccess'))
      } else {
        this.$message.error(response.msg || this.$t('payment.shopAuth.fileUploadFailed'))
      }
    },
    handleIdDocBackSuccess(response) {
      this.form.idDocBackUrl = response.url
      this.idDocBackList = [
        {
          name: this.$t('payment.shopAuth.idDocumentBackName'),
          url: response.url
        }
      ]
    },
    handleAddressDocSuccess(response, index) {
      if (response.code === 200) {
        this.$set(this.form.ubos[index], 'addressDocUrl', response.url)
        this.$set(this.uboFileList[index], 'addressDocList', [
          {
            name: this.$t('payment.shopAuth.addressProofDocumentName'),
            url: response.url
          }
        ])
        this.$message.success(this.$t('payment.shopAuth.fileUploadSuccess'))
      } else {
        this.$message.error(response.msg || this.$t('payment.shopAuth.fileUploadFailed'))
      }
    },

    // 文件移除处理方法
    handleCompanyAuthTypeUrlRemove(file) {
      this.form.companyAuthTypeUrl = undefined
      this.companyAuthTypeList = []
      if (file && file.size && file.size / 1024 / 1024 >= this.maxFileSize) {
        this.$message.error(this.$t('payment.shopAuth.fileSizeExceeded', { size: this.maxFileSize }))
        return
      }
      this.$message.info(this.$t('payment.shopAuth.fileRemoved'))
    },
    handleCompanyDocRemove(file) {
      this.form.companyDocUrl = undefined
      this.companyDocList = []
      if (file && file.size && file.size / 1024 / 1024 >= this.maxFileSize) {
        this.$message.error(this.$t('payment.shopAuth.fileSizeExceeded', { size: this.maxFileSize }))
        return
      }
      this.$message.info(this.$t('payment.shopAuth.fileRemoved'))
    },
    handleOrgChartRemove(file) {
      this.form.shareholdingStructure = undefined
      this.orgChartList = []
      if (file && file.size && file.size / 1024 / 1024 >= this.maxFileSize) {
        this.$message.error(this.$t('payment.shopAuth.fileSizeExceeded', { size: this.maxFileSize }))
        return
      }
      this.$message.info(this.$t('payment.shopAuth.fileRemoved'))
    },
    handleIdDocFrontRemove(index) {
      this.$set(this.form.ubos[index], 'idDocFrontUrl', undefined)
      this.$set(this.uboFileList[index], 'idDocFrontList', [])
    },
    handleIdDocBackRemove() {
      this.form.idDocBackUrl = undefined
      this.idDocBackList = []
    },
    handleAddressDocRemove(index) {
      this.$set(this.form.ubos[index], 'addressDocUrl', undefined)
      this.$set(this.uboFileList[index], 'addressDocList', [])
    },

    /** 文件下载处理 */
    handleFileDownload(url, fileName) {
      if (!url) {
        this.$message.error(this.$t('payment.shopAuth.fileAddressNotExist'))
        return
      }

      // 从url中获取文件名
      const originalFileName = url.substring(url.lastIndexOf('/') + 1)
      // 创建下载链接
      const link = document.createElement('a')

      link.href = process.env.VUE_APP_BASE_API + '/common/downloadUrl?fileUrl=' + encodeURIComponent(url)
      link.download = fileName || originalFileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    },
    // 修改身份证明类型变化的处理
    handleAuthTypeChange() {
      // 当切换到护照类型时，清空背面图片并移除验证规则
      if (this.form.authType === 'PASSPORT') {
        this.form.idDocBackUrl = undefined
        this.idDocBackList = []
        this.$refs.form.clearValidate('idDocBackUrl')
      }
    },
    // 新增受益人
    addUbo() {
      this.form.ubos.push({
        authType: undefined,
        idDocFrontUrl: undefined,
        addressType: undefined,
        addressDocUrl: undefined,
        uboEmail: undefined
      })
      // 同时添加对应的文件列表
      this.uboFileList.push({
        idDocFrontList: [],
        addressDocList: []
      })
    },

    // 删除受益人
    removeUbo(index) {
      this.$confirm(this.$t('payment.shopAuth.confirmDeleteBeneficiary'), this.$t('payment.shopAuth.hint'), {
        confirmButtonText: this.$t('payment.shopAuth.confirm'),
        cancelButtonText: this.$t('payment.shopAuth.cancel'),
        type: 'warning'
      }).then(() => {
        this.form.ubos.splice(index, 1)
        this.uboFileList.splice(index, 1)
        this.$message.success(this.$t('payment.shopAuth.deleteSuccess'))
      })
    },

    // 获取指定索引的证件照片列表
    getIdDocFrontList(index) {
      return this.uboFileList[index]?.idDocFrontList || []
    },

    // 获取指定索引的地址证明文件列表
    getAddressDocList(index) {
      return this.uboFileList[index]?.addressDocList || []
    },

    // 将验证方法移到 methods 中
    validateEmail(rule, value, callback) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (value && !emailRegex.test(value)) {
        callback(new Error(this.$t('payment.shopAuth.emailFormatIncorrect')))
      } else {
        callback()
      }
    },

    validateFiles(rule, value, callback) {
      if (!value) {
        callback(new Error(this.$t('payment.shopAuth.uploadFileRequired')))
      } else {
        callback()
      }
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.full-screen {
  background-color: #fff;
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
  margin-bottom: 20px;
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

/* 添加图片上传样式 */
.el-upload--picture-card {
  width: 148px;
  height: 148px;
  line-height: 146px;
}

.el-upload-list--picture-card .el-upload-list__item {
  width: 148px;
  height: 148px;
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
</style>
