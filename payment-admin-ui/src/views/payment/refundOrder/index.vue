<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="160px" class="search-form">
      <el-form-item
        v-if="checkPermi(['payment:order:merchantName'])"
        :label="$t('payment.refundOrder.searchForm.merchantId')"
        prop="merchantId"
      >
        <el-select
          v-model="queryParams.merchantId"
          :placeholder="$t('payment.refundOrder.form.merchantIdPlaceholder')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in merchantIdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.refundOrder.searchForm.orderId')" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          :placeholder="$t('payment.refundOrder.form.orderIdPlaceholder')"
          clearable
          size="small"
          @input="(value) => validateInteger(value, 'orderId')"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.refundOrder.searchForm.refundSsoId')" prop="refundSsoId">
        <el-input
          v-model="queryParams.refundSsoId"
          :placeholder="$t('payment.refundOrder.form.refundSsoIdPlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.refundOrder.searchForm.transactionId')" prop="transactionId">
        <el-input
          v-model="queryParams.transactionId"
          :placeholder="$t('payment.refundOrder.form.transactionIdPlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.refundOrder.searchForm.refundAmount')" prop="refundAmount">
        <el-input
          v-model="queryParams.refundAmount"
          type="text"
          maxlength="20"
          :placeholder="$t('payment.refundOrder.form.refundAmountPlaceholder')"
          clearable
          size="small"
          @input="(value) => validateNumber(value, 'refundAmount')"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.refundOrder.searchForm.refundCurrencyId')" prop="refundCurrencyId">
        <el-select
          v-model="queryParams.refundCurrencyId"
          :placeholder="$t('payment.refundOrder.form.refundCurrencyIdPlaceholder')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in currencyIdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.refundOrder.searchForm.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('payment.refundOrder.form.statusPlaceholder')"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in refundStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('payment.refundOrder.searchForm.remainingRefundableAmount')"
        prop="remainingRefundableAmount"
      >
        <el-input
          v-model="queryParams.remainingRefundableAmount"
          type="text"
          maxlength="20"
          :placeholder="$t('payment.refundOrder.form.remainingRefundableAmountPlaceholder')"
          clearable
          size="small"
          @input="(value) => validateNumber(value, 'remainingRefundableAmount')"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.refundOrder.searchForm.createAt')" prop="createAt">
        <el-date-picker
          v-model="dateRange"
          clearable
          size="small"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          :start-placeholder="$t('common.startDate')"
          :end-placeholder="$t('common.endDate')"
          :placeholder="$t('common.createdAt')"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.refundOrder.searchForm.searchBtn') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('payment.refundOrder.searchForm.resetBtn') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- 屏蔽新增/编辑/删除按钮 -->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:refundOrder:export']"
        >
          {{ $t('payment.refundOrder.actions.export') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('payment.refundOrder.table.id')" align="center" prop="id" v-if="false" />
      <el-table-column v-if="checkPermi(['payment:order:merchantName'])" :label="$t('payment.refundOrder.table.merchantId')" align="center" prop="merchantId"/>
      <el-table-column v-if="checkPermi(['payment:order:merchantName'])" :label="$t('payment.refundOrder.table.merchant')" align="center" prop="merchantId" :formatter="merchantIdFormat" width="150" />
      <el-table-column :label="$t('payment.refundOrder.table.orderId')" align="center" prop="orderId" />
      <el-table-column :label="$t('payment.refundOrder.table.refundSsoId')" align="center" prop="refundSsoId">
        <template v-slot="scope">
          <div v-copy-hover>{{ scope.row.refundSsoId }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.refundOrder.table.transactionId')" align="center" prop="transactionId" >
        <template v-slot="scope">
          <div v-copy-hover> {{ scope.row.transactionId }} </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.refundOrder.table.fiatAmount')" align="center" prop="fiatAmount">
        <template v-slot="scope">
          <span>{{ scope.row.fiatAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.refundOrder.table.refundAmount')" align="center" prop="refundAmount">
        <template v-slot="scope">
          <span>{{ scope.row.refundAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.refundOrder.table.remainingRefundableAmount')"
        align="center"
        prop="remainingRefundableAmount"
        width="160"
      >
        <template v-slot="scope">
          <span>{{ scope.row.remainingRefundableAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.refundOrder.table.refundCurrencyId')"
        align="center"
        prop="refundCurrencyId"
        :formatter="currencyIdFormat"
      />
      <el-table-column :label="$t('payment.refundOrder.table.status')" align="center" prop="status" width="120">
        <template v-slot="scope">
          <div 
            v-if="scope.row.status !== 'SUCCESS' && scope.row.failReason"
            v-copy-hover="scope.row.failReason"
            class="status-with-fail-reason">
            {{ getRefundOrderStatus(scope.row.status) }}
          </div>
          <span v-else>{{ getRefundOrderStatus(scope.row.status) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.refundOrder.table.payTime')"
        align="center"
        prop="payTime"
        width="200">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.refundOrder.table.createAt')" align="center" prop="createAt" width="200">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createAt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <!-- 屏蔽每行编辑/删除操作列 -->
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改退款订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.refundOrder.searchForm.merchantId')" prop="merchantId">
              <el-input v-model="form.merchantId" :placeholder="$t('payment.refundOrder.form.merchantIdPlaceholder')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.refundOrder.searchForm.orderId')" prop="orderId">
              <el-input v-model="form.orderId" :placeholder="$t('payment.refundOrder.form.orderIdPlaceholder')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.refundOrder.searchForm.refundSsoId')" prop="refundSsoId">
              <el-input
                v-model="form.refundSsoId"
                :placeholder="$t('payment.refundOrder.form.refundSsoIdPlaceholder')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.refundOrder.searchForm.transactionId')" prop="transactionId">
              <el-input
                v-model="form.transactionId"
                :placeholder="$t('payment.refundOrder.form.transactionIdPlaceholder')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.refundOrder.searchForm.refundAmount')" prop="refundAmount">
              <el-input
                v-model="form.refundAmount"
                :placeholder="$t('payment.refundOrder.form.refundAmountPlaceholder')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.refundOrder.searchForm.refundCurrencyId')" prop="refundCurrencyId">
              <el-select
                v-model="form.refundCurrencyId"
                :placeholder="$t('payment.refundOrder.form.refundCurrencyIdPlaceholder')"
                filterable
              >
                <el-option
                  v-for="dict in currencyIdOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.refundOrder.searchForm.status')">
              <el-radio-group v-model="form.status">
                <el-option
                  v-for="dict in refundStatusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item
              :label="$t('payment.refundOrder.searchForm.remainingRefundableAmount')"
              prop="remainingRefundableAmount"
            >
              <el-input
                v-model="form.remainingRefundableAmount"
                :placeholder="$t('payment.refundOrder.form.remainingRefundableAmountPlaceholder')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.refundOrder.searchForm.createAt')" prop="createAt">
              <el-date-picker
                v-model="dateRange"
                clearable
                size="small"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                :start-placeholder="$t('common.startDate')"
                :end-placeholder="$t('common.endDate')"
                :placeholder="$t('common.createdAt')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('payment.refundOrder.table.failReason')" prop="failReason">
              <el-input
                v-model="form.failReason"
                type="textarea"
                :placeholder="$t('payment.refundOrder.form.failReasonPlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.refundOrder.buttons.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.refundOrder.buttons.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, delOrder, addOrder, updateOrder, exportOrder } from '@/api/payment/refundOrder'
import { checkPermi } from '@/utils/permission'

export default {
  name: 'RefundOrder',
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 【请填写功能名称】表格数据
      orderList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 日期范围（按天）
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        // 商户筛选
        merchantId: undefined,
        orderId: undefined,
        refundSsoId: undefined,
        transactionId: undefined,
        refundAmount: undefined,
        refundCurrencyId: undefined,
        status: undefined,
        remainingRefundableAmount: undefined,
        failReason: undefined
      },
      // 商户字典
      merchantIdOptions: [],
      // 法币字典
      currencyIdOptions: [],
      // 退款状态字典
      refundStatusOptions: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderId: [
          { required: true, message: this.$t('payment.refundOrder.validation.orderIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    // 商户下拉字典
    this.getDicts('MERCHANT_ID').then(response => {
      this.merchantIdOptions = response.data
    })
    this.getDicts('CURRENCY_ID').then(response => {
      this.currencyIdOptions = response.data
    })
    this.getDicts('REFUND_ORDER_STATUS').then(response => {
      this.refundStatusOptions = response.data
    })
  },
  methods: {
    checkPermi,
    // 商户字典翻译
    merchantIdFormat(row, column) {
      return this.selectDictLabel(this.merchantIdOptions, row.merchantId)
    },
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.beginTime = this.dateRange[0]
        params.endTime = this.dateRange[1]
      }
      listOrder(params).then(response => {
        this.orderList = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        merchantId: undefined,
        orderId: undefined,
        refundSsoId: undefined,
        transactionId: undefined,
        refundAmount: undefined,
        refundCurrencyId: undefined,
        status: 0,
        remainingRefundableAmount: undefined,
        createAt: undefined,
        failReason: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = this.$t('payment.refundOrder.dialog.addTitle')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.refundOrder.dialog.editTitle')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOrder(this.form).then(response => {
              this.msgSuccess(this.$t('payment.refundOrder.messages.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addOrder(this.form).then(response => {
              this.msgSuccess(this.$t('payment.refundOrder.messages.addSuccess'))
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$confirm(
        this.$t('payment.refundOrder.confirmDialog.deleteMessage', { ids }),
        this.$t('payment.refundOrder.confirmDialog.deleteTitle'),
        {
          confirmButtonText: this.$t('payment.refundOrder.confirmDialog.confirmText'),
          cancelButtonText: this.$t('payment.refundOrder.confirmDialog.cancelText'),
          type: 'warning'
        }
      )
        .then(function () {
          return delOrder(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.refundOrder.messages.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      let exportQuery = {}
      let confirmText = ''
      let exportIds

      if (this.ids && this.ids.length > 0) {
        exportIds = [...this.ids]
        confirmText = this.$t('payment.refundOrder.confirmDialog.confirmExportSelected', { count: this.ids.length })
      } else {
        exportQuery = this.collectExportConditions()
        confirmText = this.$t('payment.refundOrder.confirmDialog.confirmExportAll', { total: this.total })
      }

      this.$confirm(
        confirmText,
        this.$t('payment.refundOrder.confirmDialog.exportTitle'),
        {
          confirmButtonText: this.$t('payment.refundOrder.confirmDialog.confirmText'),
          cancelButtonText: this.$t('payment.refundOrder.confirmDialog.cancelText'),
          type: 'warning'
        }
      )
        .then(() => {
          return exportOrder(exportQuery, exportIds)
        })
        .then(response => {
          this.download(response.msg)
        })
    },
    collectExportConditions() {
      const query = {
        merchantId: this.queryParams.merchantId,
        orderId: this.queryParams.orderId,
        refundSsoId: this.queryParams.refundSsoId,
        transactionId: this.queryParams.transactionId,
        refundAmount: this.queryParams.refundAmount,
        refundCurrencyId: this.queryParams.refundCurrencyId,
        status: this.queryParams.status,
        remainingRefundableAmount: this.queryParams.remainingRefundableAmount,
        failReason: this.queryParams.failReason,
        pageNum: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize
      }

      if (this.dateRange && this.dateRange.length === 2) {
        query.beginTime = this.dateRange[0]
        query.endTime = this.dateRange[1]
      }

      const cleanedQuery = {}
      Object.keys(query).forEach(key => {
        const value = query[key]
        if (value !== undefined && value !== null && value !== '') {
          cleanedQuery[key] = value
        }
      })

      return cleanedQuery
    },
    // 法币字典翻译
    currencyIdFormat(row) {
      return this.selectDictLabel(this.currencyIdOptions, row.refundCurrencyId)
    },
    // 获取货币名称
    getCurrencyName(currencyId) {
      return this.selectDictLabel(this.currencyIdOptions, currencyId)
    },
    getRefundOrderStatus(status) {
      return this.selectDictLabel(this.refundStatusOptions, status)
    },
    // 验证整数输入
    validateInteger(value, field) {
      const newValue = value ? value.replace(/\D/g, '') : ''
      this.queryParams[field] = newValue
    },
    // 验证金额输入
    validateNumber(value, field) {
      // 只允许输入数字和小数点
      let newValue = value.replace(/[^\d.]/g, '')
      // 保证只有一个小数点
      if (newValue.split('.').length > 2) {
        newValue = newValue.replace(/\.$/g, '')
      }
      // 更新对应字段的值
      this.queryParams[field] = newValue
    }
  }
}
</script>

<style scoped>
/* 搜索表单三列布局与不换行标签 */
.search-form.el-form--inline .el-form-item {
  width: 33.33%;
  margin-right: 0;
}
.search-form .el-form-item__label {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
/* 让输入控件占满表单项宽度 */
.search-form .el-input,
.search-form .el-select,
.search-form .el-date-editor {
  width: 100%;
}

/* 退款订单状态带失败原因的样式 */
.status-with-fail-reason {
  display: inline-block;
  cursor: pointer;
  border-bottom: 1px dashed #f56c6c;
  color: #f56c6c;
  font-weight: 500;
}
</style>
