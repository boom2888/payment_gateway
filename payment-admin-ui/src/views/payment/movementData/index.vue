<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('payment.movementData.report')" prop="status">
        <el-select
          v-model="queryParams.movementImportId"
          :placeholder="$t('payment.movementData.pleaseSelectReport')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in nuvieOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
        <el-button icon="el-icon-refresh" size="mini" @click="getNuvieOptions" style="margin-left: 10px"></el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.movementData.search') }}
        </el-button>
        <!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="warning"-->
      <!--          plain-->
      <!--          icon="el-icon-download"-->
      <!--          size="mini"-->
      <!--          @click="handleExport"-->
      <!--          v-hasPermi="['payment:movementData:export']"-->
      <!--        >导出</el-button>
            </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="movementDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--      <el-table-column label="主键ID" align="center" prop="id" v-if="true"/>-->
      <el-table-column :label="$t('payment.movementData.orderId')" align="center" prop="clientUniqueId" />
      <el-table-column :label="$t('payment.movementData.status')" align="center" prop="movementType" />
      <el-table-column
        :label="$t('payment.movementData.processingFee')"
        align="center"
        prop="merchantProcessingFee"
        width="150"
      />
      <el-table-column
        :label="$t('payment.movementData.gatewayFee')"
        align="center"
        prop="merchantGatewayFee"
        width="120"
      />
      <el-table-column
        :label="$t('payment.movementData.rollingReserve')"
        align="center"
        prop="merchantRollingReserve"
        width="150"
      />
      <el-table-column
        :label="$t('payment.movementData.processingFee')"
        align="center"
        prop="merchantProcessingFee"
        width="150"
      />
      <el-table-column
        :label="$t('payment.movementData.refundFee')"
        align="center"
        prop="merchantRefundFee"
        width="120"
      />
      <el-table-column :label="$t('payment.movementData.rdrFee')" align="center" prop="merchantRdrFee" />
      <el-table-column
        :label="$t('payment.movementData.chargebackFee')"
        align="center"
        prop="merchantChargebackFee"
        width="150"
      />
      <el-table-column
        :label="$t('payment.movementData.settlementAmount')"
        align="center"
        prop="merchantSettlementFee"
        width="180"
      />
      <el-table-column
        :label="$t('payment.movementData.settlementRemark')"
        align="center"
        prop="merchantSettlementRemark"
        width="150"
      >
        <template v-slot="{ row }">
          <div v-copy-hover class="ellipsis-text">{{ row.merchantSettlementRemark }}</div>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.movementData.acquirerBank')"
        align="center"
        prop="acquirerBank"
        width="120"
      />
      <el-table-column :label="$t('payment.movementData.reportDate')" align="center" prop="movementDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.movementDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.movementData.businessName')"
        align="center"
        prop="businessName"
        width="150"
      />
      <el-table-column :label="$t('payment.movementData.multiClient')" align="center" prop="multiClient" width="150" />
      <el-table-column :label="$t('payment.movementData.clientName')" align="center" prop="clientName" width="150" />
      <el-table-column
        :label="$t('payment.movementData.movementDetails')"
        align="center"
        prop="movementDetails"
        width="180"
      />
      <el-table-column
        :label="$t('payment.movementData.paymentMethod')"
        align="center"
        prop="paymentMethod"
        width="150"
      />
      <el-table-column :label="$t('payment.movementData.is3d')" align="center" prop="is3d" />
      <el-table-column :label="$t('payment.movementData.cardType')" align="center" prop="cardType" width="150" />
      <el-table-column :label="$t('payment.movementData.country')" align="center" prop="country" width="130" />
      <el-table-column
        :label="$t('payment.movementData.transactionResult')"
        align="center"
        prop="transactionResult"
        width="150"
      />
      <el-table-column
        :label="$t('payment.movementData.transactionId')"
        align="center"
        prop="transactionId"
        width="150"
      >
        <template v-slot="{ row }">
          <div v-copy-hover class="ellipsis-text">{{ row.transactionId }}</div>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.movementData.last4Digits')"
        align="center"
        prop="creditCardLast4Digits"
        width="150"
      />
      <el-table-column :label="$t('payment.movementData.currency')" align="center" prop="currency" />
      <el-table-column :label="$t('payment.movementData.originalAmount')" align="center" prop="amount" width="140" />
      <el-table-column :label="$t('payment.movementData.discountFee')" align="center" prop="discountFee" width="140" />
      <el-table-column
        :label="$t('payment.movementData.transactionFee')"
        align="center"
        prop="transactionFee"
        width="140"
      />
      <el-table-column :label="$t('payment.movementData.serviceFee')" align="center" prop="serviceFee" width="140" />
      <el-table-column :label="$t('payment.movementData.reserve')" align="center" prop="reserve" width="140" />
      <el-table-column :label="$t('payment.movementData.vatOnFees')" align="center" prop="vatOnFees" width="100" />
      <el-table-column :label="$t('payment.movementData.netAmount')" align="center" prop="netAmount" width="100" />
      <el-table-column
        :label="$t('payment.movementData.settlementCurrency')"
        align="center"
        prop="settlementCurrency"
        width="150"
      />
      <el-table-column
        :label="$t('payment.movementData.settlementAmountColumn')"
        align="center"
        prop="settlementAmount"
        width="180"
      />
      <el-table-column
        :label="$t('payment.movementData.settlementReserve')"
        align="center"
        prop="settlementReserve"
        width="150"
      />
      <el-table-column
        :label="$t('payment.movementData.jurisdiction')"
        align="center"
        prop="jurisdiction"
        width="140"
      />
      <el-table-column :label="$t('payment.movementData.prepaid')" align="center" prop="prepaid" />
      <el-table-column
        :label="$t('payment.movementData.plusCommission')"
        align="center"
        prop="plusCommission"
        width="130"
      />
      <el-table-column
        :label="$t('payment.movementData.externalTokenProvider')"
        align="center"
        prop="externalTokenProvider"
        width="180"
      />
      <el-table-column :label="$t('payment.movementData.onUs')" align="center" prop="onUs" />
      <el-table-column :label="$t('payment.movementData.orderIdColumn')" align="center" prop="orderId" width="100">
        <template v-slot="{ row }">
          <div v-copy-hover class="ellipsis-text">{{ row.orderId }}</div>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.movementData.isInterchangePlusPlus')"
        align="center"
        prop="isInterchangePlusPlus"
        width="180"
      />
      <el-table-column :label="$t('payment.movementData.resellerFee')" align="center" prop="resellerFee" width="160" />
      <el-table-column
        :label="$t('payment.movementData.isOnlinePayout')"
        align="center"
        prop="isOnlinePayout"
        width="160"
      />
      <el-table-column :label="$t('payment.movementData.movementId')" align="center" prop="movementId" width="140">
        <template v-slot="{ row }">
          <div v-copy-hover class="ellipsis-text">{{ row.movementId }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.movementData.schemeFee')" align="center" prop="schemeFee" width="140" />
      <el-table-column
        :label="$t('payment.movementData.resellerCommission')"
        align="center"
        prop="resellerCommission"
        width="170"
      />
      <el-table-column
        :label="$t('payment.movementData.interchangeFee')"
        align="center"
        prop="interchangeFee"
        width="150"
      />
      <el-table-column :label="$t('payment.movementData.siteId')" align="center" prop="siteId" />
      <el-table-column :label="$t('payment.movementData.siteName')" align="center" prop="siteName" width="180" />
      <el-table-column :label="$t('payment.movementData.cardProduct')" align="center" prop="cardProduct" width="150" />
      <el-table-column :label="$t('payment.movementData.customData')" align="center" prop="customData" width="150" />
      <el-table-column :label="$t('payment.movementData.arn')" align="center" prop="arn" width="150">
        <template v-slot="{ row }">
          <div v-copy-hover class="ellipsis-text">{{ row.arn }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.movementData.payoutId')" align="center" prop="payoutId" width="150" />
      <el-table-column
        :label="$t('payment.movementData.bankTransactionId')"
        align="center"
        prop="bankTransactionId"
        width="150"
      >
        <template v-slot="{ row }">
          <div v-copy-hover class="ellipsis-text">{{ row.bankTransactionId }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.movementData.isAft')" align="center" prop="isAft" width="150" />
      <el-table-column :label="$t('payment.movementData.batchId')" align="center" prop="batchId" width="150" />
      <el-table-column
        :label="$t('payment.movementData.processingChannel')"
        align="center"
        prop="processingChannel"
        width="160"
      />
      <el-table-column :label="$t('payment.movementData.bin')" align="center" prop="bin" width="150" />
      <el-table-column :label="$t('payment.movementData.recordId')" align="center" prop="recordId" width="150" />
      <el-table-column
        :label="$t('payment.movementData.isCurrencyConverted')"
        align="center"
        prop="isCurrencyConverted"
        width="180"
      />
      <el-table-column
        :label="$t('payment.movementData.paymentSubMethod')"
        align="center"
        prop="paymentSubMethod"
        width="180"
      />
      <el-table-column
        :label="$t('payment.movementData.endUserAmount')"
        align="center"
        prop="endUserAmount"
        width="180"
      />
      <el-table-column
        :label="$t('payment.movementData.endUserCurrency')"
        align="center"
        prop="endUserCurrency"
        width="170"
      />
      <!--      <el-table-column label="导入记录id" align="center" prop="movementImportId" width="120" />-->
      <el-table-column
        :label="$t('payment.movementData.operation')"
        align="center"
        class-name="small-padding fixed-width"
        width="100"
      >
        <template></template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import {
  addMovementData,
  delMovementData,
  exportMovementData,
  getMovementData,
  listMovementData,
  updateMovementData
} from '@/api/payment/movementData'
import { listMovementImport } from '@/api/payment/movementImport'

export default {
  name: 'MovementData',
  components: {},
  data() {
    return {
      // 遮罩层
      loading: false,
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
      // nuvei导入数据明细表格数据
      movementDataList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        movementImportId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      nuvieOptions: []
    }
  },
  watch: {
    // 监听路由变化，当 query 参数改变时重新加载数据
    '$route.query.movementImportId': {
      handler(newVal) {
        if (newVal) {
          this.queryParams.movementImportId = newVal
          this.getList()
        }
      },
      immediate: false
    }
  },
  created() {
    this.getNuvieOptions()

    // 获取查询参数
    this.queryParams.movementImportId = this.$route.query.movementImportId
    if (this.queryParams.movementImportId) {
      this.getList()
    }
  },
  methods: {
    /** 查询nuvei导入数据明细列表 */
    getList() {
      this.loading = true
      listMovementData(this.queryParams).then(response => {
        this.movementDataList = response.rows
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
        multiClient: undefined,
        clientName: undefined,
        acquirerBank: undefined,
        movementDate: undefined,
        movementType: undefined,
        movementDetails: undefined,
        paymentMethod: undefined,
        is3d: undefined,
        cardType: undefined,
        country: undefined,
        transactionResult: undefined,
        transactionId: undefined,
        creditCardLast4Digits: undefined,
        clientUniqueId: undefined,
        currency: undefined,
        amount: undefined,
        discountFee: undefined,
        transactionFee: undefined,
        serviceFee: undefined,
        reserve: undefined,
        vatOnFees: undefined,
        netAmount: undefined,
        settlementCurrency: undefined,
        settlementAmount: undefined,
        settlementReserve: undefined,
        jurisdiction: undefined,
        prepaid: undefined,
        plusCommission: undefined,
        externalTokenProvider: undefined,
        onUs: undefined,
        orderId: undefined,
        isInterchangePlusPlus: undefined,
        resellerFee: undefined,
        isOnlinePayout: undefined,
        movementId: undefined,
        schemeFee: undefined,
        resellerCommission: undefined,
        interchangeFee: undefined,
        siteId: undefined,
        siteName: undefined,
        cardProduct: undefined,
        customData: undefined,
        arn: undefined,
        payoutId: undefined,
        bankTransactionId: undefined,
        isAft: undefined,
        batchId: undefined,
        processingChannel: undefined,
        bin: undefined,
        recordId: undefined,
        isCurrencyConverted: undefined,
        paymentSubMethod: undefined,
        endUserAmount: undefined,
        endUserCurrency: undefined,
        movementImportId: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      if (!this.queryParams.movementImportId) {
        this.msgError(this.$t('payment.movementData.pleaseSelectReportFirst'))
        return
      }
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
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
      this.title = this.$t('payment.movementData.addMovementData')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMovementData(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.movementData.editMovementData')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMovementData(this.form).then(response => {
              this.msgSuccess(this.$t('payment.movementData.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addMovementData(this.form).then(response => {
              this.msgSuccess(this.$t('payment.movementData.addSuccess'))
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
        this.$t('payment.movementData.confirmDelete', { ids: ids }),
        this.$t('payment.movementData.warning'),
        {
          confirmButtonText: this.$t('payment.movementData.confirm'),
          cancelButtonText: this.$t('payment.movementData.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return delMovementData(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.movementData.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('payment.movementData.confirmExport'), this.$t('payment.movementData.warning'), {
        confirmButtonText: this.$t('payment.movementData.confirm'),
        cancelButtonText: this.$t('payment.movementData.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportMovementData(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    },
    getNuvieOptions() {
      listMovementImport().then(response => {
        this.nuvieOptions = response.rows.map(item => {
          return {
            dictLabel: item.originName,
            dictValue: item.id + ''
          }
        })
      })
    }
  }
}
</script>
