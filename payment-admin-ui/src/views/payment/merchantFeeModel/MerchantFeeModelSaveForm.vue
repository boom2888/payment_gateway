<template>
  <el-dialog :title="title" :visible.sync="visible" width="1200px" append-to-body custom-class="merchant-fee-model-dialog" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="230px">
      <el-card class="box-card" style="margin-bottom: 10px;">
        <div slot="header" class="clearfix">
          <span>{{ $t('payment.merchantFeeModel.basicInfoTitle') }}</span>
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantFeeModel.modelName')" prop="name">
              <el-input v-model="formData.name" :placeholder="$t('payment.merchantFeeModel.modelName')"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('common.remark')" prop="remark">
              <el-input v-model="formData.remark" :placeholder="$t('common.remark')"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="box-card gateway-fee-settings" style="margin-bottom: 10px;">
        <div slot="header" class="clearfix">
          <span>{{ $t('payment.merchantFeeModel.gatewayFeeSettings') }}</span>
        </div>
        <el-row>
          <el-col :span="8">
            <el-form-item :label="$t('payment.merchantFeeModel.gatewayFeeThreshold')" prop="gatewayFeeThreshold">
              <el-input v-model="formData.gatewayFeeThreshold" type="number" :placeholder="$t('payment.merchantFeeModel.gatewayFeeThreshold')" >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('payment.merchantFeeModel.gatewayFeeBelowThreshold')" prop="gatewayFeeBelowThreshold">
              <el-input v-model="formData.gatewayFeeBelowThreshold" type="number" :placeholder="$t('payment.merchantFeeModel.gatewayFeeBelowThreshold')" style="width: 100%">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('payment.merchantFeeModel.gatewayFeeAboveThreshold')" prop="gatewayFeeAboveThreshold">
              <el-input v-model="formData.gatewayFeeAboveThreshold" type="number" :placeholder="$t('payment.merchantFeeModel.gatewayFeeAboveThreshold')" style="width: 100%">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="box-card" style="margin-bottom: 10px;">
        <div slot="header" class="clearfix">
          <span>{{ $t('payment.merchantFeeModel.processFeeSettings') }}</span>
        </div>
        <el-table :data="processFeeTableDataWithRules" border style="width: 100%" class="process-fee-table">
          <el-table-column prop="region" :label="$t('payment.merchantFeeModel.region')" width="120" align="center">
            <template slot-scope="scope">
              <span class="region-text">{{ $t(scope.row.regionKey) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="currency" :label="$t('payment.merchantFeeModel.currency')" width="120" align="center">
            <template slot-scope="scope">
              <span class="currency-text">{{ $t(scope.row.currencyKey) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="feeRatio" :label="$t('payment.merchantFeeModel.feeRatio')" align="center">
            <template slot-scope="scope">
              <el-form-item :prop="scope.row.prop" :rules="scope.row.rules" style="margin-bottom: 0;">
                <el-input
                  v-model="formData[scope.row.prop]"
                  type="number"
                  :placeholder="$t(scope.row.placeholderKey)"
                  class="fee-input"
                />
              </el-form-item>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card class="box-card" style="margin-bottom: 10px;">
        <div slot="header" class="clearfix">
          <span>{{ $t('payment.merchantFeeModel.rollingReserveSettings') }}</span>
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantFeeModel.rollingReserveRate')" prop="rollingReserveRate">
              <el-input v-model="formData.rollingReserveRate" type="number" :placeholder="$t('payment.merchantFeeModel.rollingReserveRate')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantFeeModel.rollingReserveReleaseDays')" prop="rollingReserveReleaseDays">
              <el-input v-model="formData.rollingReserveReleaseDays" type="number" :placeholder="$t('payment.merchantFeeModel.rollingReserveReleaseDays')"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="box-card" style="margin-bottom: 10px;">
        <div slot="header" class="clearfix">
          <span>{{ $t('payment.merchantFeeModel.refundFeeSettings') }}</span>
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantFeeModel.refundFee')" prop="refundFee">
              <el-input v-model="formData.refundFee" type="number" :placeholder="$t('payment.merchantFeeModel.refundFee')"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="box-card" style="margin-bottom: 10px;">
        <div slot="header" class="clearfix">
          <span>{{ $t('payment.merchantFeeModel.disputeTransactionFeeSettings') }}</span>
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantFeeModel.rdrFee')" prop="rdrFee">
              <el-input v-model="formData.rdrFee" type="number" :placeholder="$t('payment.merchantFeeModel.rdrFee')"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="box-card" style="margin-bottom: 10px;">
        <div slot="header" class="clearfix">
          <span>{{ $t('payment.merchantFeeModel.chargebackFeeSettings') }}</span>
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantFeeModel.cbFee')" prop="cbFee">
              <el-input v-model="formData.cbFee" type="number" :placeholder="$t('payment.merchantFeeModel.cbFee')"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="box-card" style="margin-bottom: 10px;">
        <div slot="header" class="clearfix">
          <span>{{ $t('payment.merchantFeeModel.exchangeRateMarkupSettings') }}</span>
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantFeeModel.exchangeRateBps')" prop="exchangeRateBps">
              <el-input v-model="formData.exchangeRateBps" type="number" :placeholder="$t('payment.merchantFeeModel.exchangeRateBps')"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">{{ $t('common.confirm') }}</el-button>
      <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "MerchantFeeModelSaveForm",
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ""
    },
    formData: {
      type: Object,
      default: () => ({
        id: undefined,
        name: undefined,
        processFeeEuUsd: undefined,
        processFeeEuNonUsd: undefined,
        processFeeNonEuUsd: undefined,
        processFeeNonEuNonUsd: undefined,
        gatewayFeeUnit: 'USD',
        gatewayFeeThreshold: undefined,
        gatewayFeeBelowThreshold: undefined,
        gatewayFeeAboveThreshold: undefined,
        rollingReserveRate: undefined,
        rollingReserveReleaseDays: undefined,
        exchangeRateBps: undefined,
        refundFee: undefined,
        rdrFee: undefined,
        cbFee: undefined,
        remark: undefined
      })
    }
  },
  data() {
    return {
      processFeeTableData: [
        {
          regionKey: 'payment.merchantFeeModel.europe',
          currencyKey: 'payment.merchantFeeModel.eur',
          prop: 'processFeeEuUsd',
          placeholderKey: 'payment.merchantFeeModel.pleaseEnterEuEurFeeRate'
        },
        {
          regionKey: 'payment.merchantFeeModel.europe',
          currencyKey: 'payment.merchantFeeModel.nonEur',
          prop: 'processFeeEuNonUsd',
          placeholderKey: 'payment.merchantFeeModel.pleaseEnterEuNonEurFeeRate'
        },
        {
          regionKey: 'payment.merchantFeeModel.nonEurope',
          currencyKey: 'payment.merchantFeeModel.eur',
          prop: 'processFeeNonEuUsd',
          placeholderKey: 'payment.merchantFeeModel.pleaseEnterNonEuEurFeeRate'
        },
        {
          regionKey: 'payment.merchantFeeModel.nonEurope',
          currencyKey: 'payment.merchantFeeModel.nonEur',
          prop: 'processFeeNonEuNonUsd',
          placeholderKey: 'payment.merchantFeeModel.pleaseEnterNonEuNonEurFeeRate'
        }
      ],
      rules: {
        name: [
          { required: true, message: this.$t('payment.merchantFeeModel.modelNameNotEmpty'), trigger: "blur" }
        ],
        gatewayFeeThreshold: [
          { required: true, message: this.$t('payment.merchantFeeModel.gatewayFeeThresholdNotEmpty'), trigger: "blur" }
        ],
        gatewayFeeBelowThreshold: [
          { required: true, message: this.$t('payment.merchantFeeModel.gatewayFeeBelowThresholdNotEmpty'), trigger: "blur" }
        ],
        gatewayFeeAboveThreshold: [
          { required: true, message: this.$t('payment.merchantFeeModel.gatewayFeeAboveThresholdNotEmpty'), trigger: "blur" }
        ],
        rollingReserveRate: [
          { required: true, message: this.$t('payment.merchantFeeModel.rollingReserveRateNotEmpty'), trigger: "blur" }
        ],
        rollingReserveReleaseDays: [
          { required: true, message: this.$t('payment.merchantFeeModel.rollingReserveReleaseDaysNotEmpty'), trigger: "blur" }
        ],
        exchangeRateBps: [
          { required: true, message: this.$t('payment.merchantFeeModel.exchangeRateBpsNotEmpty'), trigger: "blur" }
        ],
        refundFee: [
          { required: true, message: this.$t('payment.merchantFeeModel.refundFeeNotEmpty'), trigger: "blur" }
        ],
        rdrFee: [
          { required: true, message: this.$t('payment.merchantFeeModel.rdrFeeNotEmpty'), trigger: "blur" }
        ],
        cbFee: [
          { required: true, message: this.$t('payment.merchantFeeModel.cbFeeNotEmpty'), trigger: "blur" }
        ],
      }
    };
  },
  computed: {
    processFeeTableDataWithRules() {
      return this.processFeeTableData.map(item => ({
        ...item,
        rules: [{
          required: true,
          message: this.getValidationMessage(item.prop),
          trigger: "blur"
        }]
      }));
    }
  },
  watch: {
    visible(newVal) {
      // 当弹窗关闭时，清除表单验证状态
      if (!newVal) {
        this.$nextTick(() => {
          this.$refs.form.clearValidate();
        });
      }
    }
  },
  methods: {
    getValidationMessage(prop) {
      const messageMap = {
        'processFeeEuUsd': this.$t('payment.merchantFeeModel.euEurFeeRateNotEmpty'),
        'processFeeEuNonUsd': this.$t('payment.merchantFeeModel.euNonEurFeeRateNotEmpty'),
        'processFeeNonEuUsd': this.$t('payment.merchantFeeModel.nonEuEurFeeRateNotEmpty'),
        'processFeeNonEuNonUsd': this.$t('payment.merchantFeeModel.nonEuNonEurFeeRateNotEmpty')
      };
      return messageMap[prop] || this.$t('common.fieldRequired');
    },
    handleClose() {
      this.$emit('update:visible', false);
      this.$emit('close');
    },
    cancel() {
      this.handleClose();
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 检查所有必填字段
          const requiredFields = [
            { field: 'name', nameKey: 'payment.merchantFeeModel.modelName' },
            { field: 'processFeeEuUsd', nameKey: 'payment.merchantFeeModel.processFeeEuUsd' },
            { field: 'processFeeEuNonUsd', nameKey: 'payment.merchantFeeModel.processFeeEuNonUsd' },
            { field: 'processFeeNonEuUsd', nameKey: 'payment.merchantFeeModel.processFeeNonEuUsd' },
            { field: 'processFeeNonEuNonUsd', nameKey: 'payment.merchantFeeModel.processFeeNonEuNonUsd' },
            { field: 'gatewayFeeThreshold', nameKey: 'payment.merchantFeeModel.gatewayFeeThreshold' },
            { field: 'gatewayFeeBelowThreshold', nameKey: 'payment.merchantFeeModel.gatewayFeeBelowThreshold' },
            { field: 'gatewayFeeAboveThreshold', nameKey: 'payment.merchantFeeModel.gatewayFeeAboveThreshold' },
            { field: 'rollingReserveRate', nameKey: 'payment.merchantFeeModel.rollingReserveRate' },
            { field: 'rollingReserveReleaseDays', nameKey: 'payment.merchantFeeModel.rollingReserveReleaseDays' },
            { field: 'exchangeRateBps', nameKey: 'payment.merchantFeeModel.exchangeRateBps' },
            { field: 'refundFee', nameKey: 'payment.merchantFeeModel.refundFee' },
            { field: 'rdrFee', nameKey: 'payment.merchantFeeModel.rdrFee' },
            { field: 'cbFee', nameKey: 'payment.merchantFeeModel.cbFee' }
          ];

          const missingFields = requiredFields.filter(item => {
            const value = this.formData[item.field];
            return !value || value === '' || value === null || value === undefined ||
                   (typeof value === 'string' && value.trim() === '') ||
                   (typeof value === 'number' && isNaN(value));
          });

          if (missingFields.length > 0) {
            const fieldNames = missingFields.map(item => this.$t(item.nameKey)).join('、');
            this.$message.error(this.$t('payment.merchantFeeModel.missingFieldsError', { fields: fieldNames }));
            return false;
          }

          // 验证数值范围
          const numericFields = [
            { field: 'processFeeEuUsd', nameKey: 'payment.merchantFeeModel.processFeeEuUsd', min: 0, max: 100 },
            { field: 'processFeeEuNonUsd', nameKey: 'payment.merchantFeeModel.processFeeEuNonUsd', min: 0, max: 100 },
            { field: 'processFeeNonEuUsd', nameKey: 'payment.merchantFeeModel.processFeeNonEuUsd', min: 0, max: 100 },
            { field: 'processFeeNonEuNonUsd', nameKey: 'payment.merchantFeeModel.processFeeNonEuNonUsd', min: 0, max: 100 },
            { field: 'rollingReserveRate', nameKey: 'payment.merchantFeeModel.rollingReserveRate', min: 0, max: 100 },
            { field: 'rollingReserveReleaseDays', nameKey: 'payment.merchantFeeModel.rollingReserveReleaseDays', min: 1, max: 365 }
          ];

          const invalidFields = numericFields.filter(item => {
            const value = parseFloat(this.formData[item.field]);
            return isNaN(value) || value < item.min || value > item.max;
          });

          if (invalidFields.length > 0) {
            const fieldNames = invalidFields.map(item => `${this.$t(item.nameKey)}(${item.min}-${item.max})`).join('、');
            this.$message.error(this.$t('payment.merchantFeeModel.invalidRangeError', { fields: fieldNames }));
            return false;
          }

          this.$emit('submit', this.formData);
        } else {
          this.$message.error(this.$t('payment.merchantFeeModel.pleaseCompleteAllFields'));
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
.process-fee-table {
  border-radius: 8px;
  overflow: hidden;
}

.process-fee-table .el-table__header {
  background-color: #f5f7fa;
}

.process-fee-table .el-table__header th {
  background-color: #f5f7fa !important;
  color: #606266;
  font-weight: 600;
}

.region-text, .currency-text {
  font-weight: 500;
  color: #303133;
}

.fee-input {
  width: 200px;
}

.fee-input .el-input__inner {
  text-align: center;
  border-radius: 4px;
}

.process-fee-table .el-table__body tr:hover > td {
  background-color: #f5f7fa !important;
}

.process-fee-table .el-table__body td {
  padding: 12px 0;
}

.merchant-fee-model-dialog {
  max-height: 80vh;
}

.merchant-fee-model-dialog .el-dialog__body {
  max-height: calc(80vh - 110px);
  overflow-y: auto;
}

/* 网关费设置样式优化 */
.gateway-fee-settings .el-form-item {
  margin-bottom: 18px;
}

.gateway-fee-settings .el-input {
  width: 100%;
}

.gateway-fee-settings .el-input__inner {
  padding: 8px 12px;
  font-size: 14px;
  min-width: 180px;
  width: 100%;
}

.gateway-fee-settings .el-input-group__append {
  background-color: #f5f7fa;
  border-color: #dcdfe6;
  color: #606266;
  font-weight: 500;
  min-width: 40px;
  max-width: 50px;
  text-align: center;
  padding: 0 8px;
  font-size: 13px;
}

.gateway-fee-settings .el-select {
  width: 100%;
}

.gateway-fee-settings .el-select .el-input__inner {
  min-width: 120px;
  width: 100%;
}

/* 确保数字输入框有足够空间 */
.gateway-fee-settings .el-input-number {
  width: 100%;
}

.gateway-fee-settings .el-input-number .el-input__inner {
  text-align: left;
  padding-right: 30px;
}

/* 优化表单项标签 */
.gateway-fee-settings .el-form-item__label {
  font-size: 13px;
  line-height: 1.5;
  padding-right: 8px;
  white-space: nowrap;
}
</style>
