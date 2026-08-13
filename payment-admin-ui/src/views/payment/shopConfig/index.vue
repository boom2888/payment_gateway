<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="220px">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.userName')" prop="userName">
            <el-input v-model="form.userName" :placeholder="$t('payment.shop.userName')" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.id')" prop="id">
            <el-input v-model="form.id" :placeholder="$t('payment.shop.id')" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.businessName')" prop="businessName">
            <el-input v-model="form.businessName" :placeholder="$t('payment.shop.businessName')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.businessNo')" prop="businessNo">
            <el-input v-model="form.businessNo" :placeholder="$t('payment.shop.businessNo')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.businessAddress')" prop="businessAddress">
            <el-input v-model="form.businessAddress" :placeholder="$t('payment.shop.businessAddress')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.country')" prop="country">
            <el-input v-model="form.country" :placeholder="$t('payment.shop.country')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.industry')" prop="industry">
            <el-input v-model="form.industry" :placeholder="$t('payment.shop.industry')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.sideIndustry')" prop="sideIndustry">
            <el-input v-model="form.sideIndustry" :placeholder="$t('payment.shop.sideIndustry')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.registrationDate')" prop="registrationDate">
            <el-input v-model="form.registrationDate" :placeholder="$t('payment.shop.registrationDate')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.registrationAddress')" prop="registrationAddress">
            <el-input v-model="form.registrationAddress" :placeholder="$t('payment.shop.registrationAddress')"
                      disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.shareholdingStructure')" prop="shareholdingStructure">
            <el-input v-model="form.shareholdingStructure" :placeholder="$t('payment.shop.shareholdingStructure')"
                      disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.turnoverYear')" prop="turnoverYear">
            <el-input v-model="form.turnoverYear" :placeholder="$t('payment.shop.turnoverYear')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.turnoverWeek')" prop="turnoverWeek">
            <el-input v-model="form.turnoverWeek" :placeholder="$t('payment.shop.turnoverWeek')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.station')" prop="station">
            <el-input v-model="form.station" :placeholder="$t('payment.shop.station')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.descriptor')" prop="descriptor">
            <el-input v-model="form.descriptor" :placeholder="$t('payment.shop.descriptor')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.website')" prop="website">
            <el-input v-model="form.website" :placeholder="$t('payment.shop.website')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.supplierName')" prop="supplierName">
            <el-input v-model="form.supplierName" :placeholder="$t('payment.shop.supplierName')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.supplierContracts')" prop="supplierContracts">
            <el-input v-model="form.supplierContracts" :placeholder="$t('payment.shop.supplierContracts')" disabled/>
          </el-form-item>
        </el-col>

        <!--        <el-col :span="12">-->
        <!--          <el-form-item :label="$t('payment.shop.saasUserCorporationId')" prop="saasUserCorporationId">-->
        <!--            <el-input v-model="form.saasUserCorporationId" :placeholder="$t('payment.shop.saasUserCorporationId')" disabled/>-->
        <!--          </el-form-item>-->
        <!--        </el-col>-->

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.requestCryptoTransactionEndpoint')"
                        prop="requestCryptoTransactionEndpoint">
            <el-input v-model="form.requestCryptoTransactionEndpoint"
                      :placeholder="$t('payment.shop.requestCryptoTransactionEndpoint')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.merchantHashSecretKey')" prop="merchantHashSecretKey">
            <el-input v-model="form.merchantHashSecretKey" :placeholder="$t('payment.shop.merchantHashSecretKey')"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.profitSharing')" prop="profitSharing">
            <el-input v-model="form.profitSharing" :placeholder="$t('payment.shop.profitSharing')" disabled/>
          </el-form-item>
        </el-col>
        <!--        <el-col :span="12">-->
        <!--          <el-form-item :label="$t('payment.shop.isSiftOn')" prop="isSiftOn">-->
        <!--            <el-select v-model="form.isSiftOn" :placeholder="$t('payment.shop.isSiftOn')" disabled filterable>-->
        <!--              <el-option-->
        <!--                v-for="dict in isSiftOnOptions"-->
        <!--                :key="dict.dictValue"-->
        <!--                :label="dict.dictLabel"-->
        <!--                :value="parseInt(dict.dictValue)"-->
        <!--              />-->
        <!--            </el-select>-->
        <!--          </el-form-item>-->
        <!--        </el-col>-->
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.cryptoDeliveryMethod')" prop="cryptoDeliveryMethod">
            <el-select v-model="form.cryptoDeliveryMethod" :placeholder="$t('payment.shop.cryptoDeliveryMethod')"
                       disabled filterable>
              <el-option
                  v-for="dict in cryptoDeliveryMethodOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.isHouseMerchant')" prop="isHouseMerchant">
            <el-select v-model="form.isHouseMerchant" :placeholder="$t('payment.shop.isHouseMerchant')" disabled
                       filterable>
              <el-option
                  v-for="dict in isHouseMerchantOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.reservePercentage')" prop="reservePercentage">
            <el-input v-model="form.reservePercentage" :placeholder="$t('payment.shop.reservePercentage')" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.reserveHoldingPeriod')" prop="reserveHoldingPeriod">
            <el-input v-model="form.reserveHoldingPeriod" :placeholder="$t('payment.shop.reserveHoldingPeriod')"
                      disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.settlementCurrency')" prop="settlementCurrency">
            <el-select v-model="form.settlementCurrency" :placeholder="$t('payment.shop.settlementCurrency')" disabled
                       filterable>
              <el-option
                  v-for="dict in settlementCurrencyOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <!--        <el-col :span="12">-->
        <!--          <el-form-item :label="$t('payment.shop.notifyOrderStatusEndpoint3')" prop="notifyOrderStatusEndpoint3">-->
        <!--            <el-input v-model="form.notifyOrderStatusEndpoint3" :placeholder="$t('payment.shop.notifyOrderStatusEndpoint3')" disabled/>-->
        <!--          </el-form-item>-->
        <!--        </el-col>-->

        <!--        <el-col :span="12">-->
        <!--          <el-form-item :label="$t('payment.shop.cryptoDeliveryDelayTime')" prop="cryptoDeliveryDelayTime">-->
        <!--            <el-input v-model="form.cryptoDeliveryDelayTime" :placeholder="$t('payment.shop.cryptoDeliveryDelayTime')" disabled/>-->
        <!--          </el-form-item>-->
        <!--        </el-col>-->

        <!--        <el-col :span="12">-->
        <!--          <el-form-item :label="$t('payment.shop.orderStatusChangeNotification')" prop="orderStatusChangeNotification">-->
        <!--            <el-input v-model="form.orderStatusChangeNotification" :placeholder="$t('payment.shop.orderStatusChangeNotification')"-->
        <!--                      disabled/>-->
        <!--          </el-form-item>-->
        <!--        </el-col>-->

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.onRampMin')" prop="onRampMin">
            <el-input v-model="form.onRampMin" :placeholder="$t('payment.shop.onRampMin')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.onRampMax')" prop="onRampMax">
            <el-input v-model="form.onRampMax" :placeholder="$t('payment.shop.onRampMax')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.offRampMin')" prop="offRampMin">
            <el-input v-model="form.offRampMin" :placeholder="$t('payment.shop.offRampMin')" disabled/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.offRampMax')" prop="offRampMax">
            <el-input v-model="form.offRampMax" :placeholder="$t('payment.shop.offRampMax')" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.businessDesc')" prop="businessDesc">
            <el-input v-model="form.businessDesc" :placeholder="$t('payment.shop.businessDesc')" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.is3dsOn')" prop="is3dsOn">
            <el-select v-model="form.is3dsOn" :placeholder="$t('payment.shop.is3dsOn')" filterable>
              <el-option
                  v-for="dict in is3dsOnOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.threeDSuccessUrl')" prop="threeDSuccessUrl">
            <el-input v-model="form.threeDSuccessUrl" :placeholder="$t('payment.shop.threeDSuccessUrl')"/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.threeDFailureUrl')" prop="threeDFailureUrl">
            <el-input v-model="form.threeDFailureUrl" :placeholder="$t('payment.shop.threeDFailureUrl')"/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.defaultRedirectSuccessUrl')" prop="defaultRedirectSuccessUrl">
            <el-input v-model="form.defaultRedirectSuccessUrl"
                      :placeholder="$t('payment.shop.defaultRedirectSuccessUrl')"/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.defaultRedirectFailureUrl')" prop="defaultRedirectFailureUrl">
            <el-input v-model="form.defaultRedirectFailureUrl"
                      :placeholder="$t('payment.shop.defaultRedirectFailureUrl')"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payment.shop.backOpen')" prop="backOpen">
            <el-select v-model="form.backOpen" :placeholder="$t('payment.shop.backOpen')" clearable size="small"
                       filterable>
              <el-option
                  v-for="dict in openList"
                  :key="dict.value"
                  :label="dict.key"
                  :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="form.backOpen === 'YES'" :span="12">
          <el-form-item :label="$t('payment.shop.notifyOrderStatusEndpoint')" prop="notifyOrderStatusEndpoint">
            <el-input v-model="form.notifyOrderStatusEndpoint"
                      :placeholder="$t('payment.shop.notifyOrderStatusEndpoint')"/>
          </el-form-item>
        </el-col>

      </el-row>

      <el-form-item>
        <el-button v-hasPermi="['payment:shopConfig:selfEdit']" type="primary" @click="submitForm">{{
            $t('common.save')
          }}
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>


<script>
import {listShop, updateShopConfig} from "@/api/payment/shopConfig"
import {getToken} from "@/utils/auth"

export default {
  name: "ShopStatusFail",
  dicts: ['SHOP_STATUS', 'KYB_COMPANY_AUTH_TYPE', 'KYB_AUTH_TYPE', 'KYB_ADDRESS_TYPE'],
  data() {
    return {
      // 状态数据字典
      statusOptions: [],
      // 新增KYB相关数据
      companyAuthTypeOptions: [],
      authTypeOptions: [],
      addressTypeOptions: [],
      uploadUrl: process.env.VUE_APP_BASE_API + "/uploadFile",
      companyDocList: [],
      orgChartList: [],
      idDocFrontList: [],
      idDocBackList: [],
      addressDocList: [],
      openList: [
        {key: "YES", value: "YES"},
        {key: "NO", value: "NO"}
      ],
      // 表单参数
      form: {
        id: undefined,
        userName: undefined,
        company: undefined,
        mobile: undefined,
        product: undefined,
        backUrl: undefined,
        authBackUrl: undefined,
        pubKey: undefined,
        status: 'FAIL',
        is3dsOn: undefined,
        threeDSuccessUrl: undefined,
        threeDFailureUrl: undefined,
        defaultRedirectSuccessUrl: undefined,
        defaultRedirectFailureUrl: undefined
      },
      // sift类型字典
      isSiftOnOptions: [],
      // 3ds开关字典
      is3dsOnOptions: [],
      // 加密货币交付方式字典
      cryptoDeliveryMethodOptions: [],
      // 直营户字典
      isHouseMerchantOptions: [],
      settlementCurrencyOptions: [],
      // 类型字典
      isWhitelistOnOptions: [],
      // 表单校验
      rules: {
        company: [
          {required: true, message: "公司名称不能为空", trigger: "blur"}
        ],
        mobile: [
          {required: true, trigger: 'blur', message: '请输入手机号'},
          {pattern: /^1[3-9]\d{9}$/, trigger: 'blur', message: '请输入正确的手机号'}
        ],
        pubKey: [
          {required: true, message: "公钥不能为空", trigger: "blur"}
        ],
        inviteOpenL1: [
          {required: true, message: '金额不能为空', trigger: 'blur'},
          {
            pattern: /^(0(\.\d+)?|[1-9]\d*(\.\d+)?)$/,
            message: '请输入正确的有效金额',
            trigger: 'blur'
          }
        ],
        inviteOpenL2: [
          {required: true, message: '金额不能为空', trigger: 'blur'},
          {
            pattern: /^(0(\.\d+)?|[1-9]\d*(\.\d+)?)$/,
            message: '请输入正确的有效金额',
            trigger: 'blur'
          }
        ],
        inviteTransaL1: [
          {required: true, message: '金额不能为空', trigger: 'blur'},
          {
            pattern: /^(0(\.\d+)?|[1-9]\d*(\.\d+)?)$/,
            message: '请输入正确的有效金额',
            trigger: 'blur'
          }
        ],
        inviteTransaL2: [
          {required: true, message: '金额不能为空', trigger: 'blur'},
          {
            pattern: /^(0(\.\d+)?|[1-9]\d*(\.\d+)?)$/,
            message: '请输入正确的有效金额',
            trigger: 'blur'
          }
        ],
        backOpen: [
          {required: true, message: '回调开关不能为空', trigger: 'blur'}
        ],
        backUrl: [
          {required: true, message: '回调地址不能为空', trigger: 'blur'}
        ],
        authOpen: [
          {required: true, message: '实名审批开关不能为空', trigger: 'blur'}
        ],
        apiOpen: [
          {required: true, message: 'API开关不能为空', trigger: 'blur'}
        ]
      },
      // 添加文件格式限制
      fileTypes: {
        doc: ['doc', 'docx', 'pdf'], // 文档类型
        image: ['jpg', 'jpeg', 'png'] // 图片类型
      },
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
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_IS_SIFT_ON').then(response => {
      this.isSiftOnOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_IS_3DS_ON').then(response => {
      this.is3dsOnOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_CRYPTO_DELIVERY_METHOD').then(response => {
      this.cryptoDeliveryMethodOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_IS_HOUSE_MERCHANT').then(response => {
      this.isHouseMerchantOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_IS_WHITELIST_ON').then(response => {
      this.isWhitelistOnOptions = response.data
    })
    this.getDicts('CURRENCY_ID').then(response => {
      this.settlementCurrencyOptions = response.data
    })
  },
  methods: {
    /** 获取商户信息 */
    getShopInfo() {
      listShop().then(response => {
        if (response.code === 200) {
          console.log("response", response)
          const shopInfo = response.data
          this.form = {...shopInfo}

          // 获取字典数据
          this.getDicts("SHOP_STATUS").then(response => {
            this.statusOptions = response.data
          })
        }
      })
    },
    /** 状态字典翻译 */
    statusFormat(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateShopConfig(this.form).then(() => {
            this.$message({
              message: '提交成功',
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
        company: undefined,
        mobile: undefined,
        product: undefined,
        backUrl: undefined,
        pubKey: undefined,
        status: 'FAIL',
        remark: undefined,
        ubos: [] // 确保重置时 ubos 是一个空数组
      }
      this.$refs["form"].resetFields()
    },
    /** 退出登录 */
    logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index'
        })
      })
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
    // 将验证方法移到 methods 中
    validateEmail(rule, value, callback) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (value && !emailRegex.test(value)) {
        callback(new Error('请输入正确的电子邮件格式'))
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
  text-align: right;
  margin-bottom: 20px;
}

.logout-btn {
  font-size: 14px;
}

.logout-btn i {
  margin-right: 5px;
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
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  margin-bottom: 20px;
}

.remove-ubo-btn {
  position: absolute;
  top: 20px;
  right: 20px;
}
</style>
