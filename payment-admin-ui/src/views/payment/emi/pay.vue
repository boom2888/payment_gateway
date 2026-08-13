<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <!-- 付款记录标签页 -->
      <el-tab-pane :label="$t('payment.emi.pay.paymentRecordTab')" name="paymentRecord">
        <div class="filter-container">
          <el-date-picker
              v-model="recordQuery.dateRange"
              type="daterange"
              range-separator="~"
              :start-placeholder="$t('payment.emi.pay.startDate')"
              :end-placeholder="$t('payment.emi.pay.endDate')"
              value-format="yyyy-MM-dd"
              style="width: 300px; margin-right: 40px"
          />
          <el-button type="primary" @click="handleRecordQuery">{{ $t('payment.emi.pay.search') }}</el-button>
        </div>

        <el-table v-loading="recordLoading" :data="recordList" border style="margin-top: 20px">
          <el-table-column :label="$t('payment.emi.pay.payTime')" prop="payTime" width="180" align="center"/>
          <el-table-column :label="$t('payment.emi.pay.withdrawalNumber')" prop="withdrawalNumber" width="200"
                           align="center"/>
          <el-table-column :label="$t('payment.emi.pay.beneficiaryBank')" prop="beneficiaryBank" align="center"/>
          <el-table-column :label="$t('payment.emi.pay.payee')" prop="payee" align="center"/>
          <el-table-column :label="$t('payment.emi.pay.paymentAccountNumber')" prop="paymentAccountNumber"
                           align="center"/>
          <el-table-column :label="$t('payment.emi.pay.paymentAmount')" prop="paymentAmount" align="center"/>
          <el-table-column :label="$t('payment.emi.pay.amountReceived')" prop="amountReceived" align="center"/>
        </el-table>

        <pagination
            v-show="recordTotal > 0"
            :total="recordTotal"
            :page.sync="recordQuery.pageNum"
            :limit.sync="recordQuery.pageSize"
            @pagination="getRecordList"
        />
      </el-tab-pane>

      <!-- 收款方列表标签页 -->
      <el-tab-pane :label="$t('payment.emi.pay.payeeListTab')" name="payeeList">
        <div class="filter-container">
          <span style="margin-right: 5px">{{ $t('payment.emi.pay.accountLabel') }}:</span>
          <el-input
              v-model="payeeQuery.account"
              :placeholder="$t('payment.emi.pay.accountPlaceholder')"
              clearable
              style="width: 200px; margin-right: 40px"
          />
          <span style="margin-right: 5px">{{ $t('payment.emi.pay.statusLabel') }}:</span>
          <el-select
              v-model="payeeQuery.status"
              :placeholder="$t('payment.emi.pay.statusPlaceholder')"
              clearable
              style="width: 150px; margin-right: 40px"
          >
            <el-option :label="$t('payment.emi.pay.all')" value=""></el-option>
            <el-option :label="$t('payment.emi.pay.enabled')" value="启用"></el-option>
            <el-option :label="$t('payment.emi.pay.disabled')" value="停用"></el-option>
          </el-select>
          <span style="margin-right: 5px">{{ $t('payment.emi.pay.accountTypeLabel') }}:</span>
          <el-select
              v-model="payeeQuery.type"
              :placeholder="$t('payment.emi.pay.accountTypePlaceholder')"
              clearable
              style="width: 150px; margin-right: 10px"
          >
            <el-option :label="$t('payment.emi.pay.all')" value=""></el-option>
            <el-option :label="$t('payment.emi.pay.personal')" value="个人"></el-option>
            <el-option :label="$t('payment.emi.pay.company')" value="企业"></el-option>
          </el-select>
          <el-button type="primary" @click="handlePayeeQuery">{{ $t('payment.emi.pay.search') }}</el-button>
        </div>

        <el-table v-loading="payeeLoading" :data="payeeList" border style="margin-top: 20px">
          <el-table-column :label="$t('payment.emi.pay.account')" prop="account" align="center"/>
          <el-table-column :label="$t('payment.emi.pay.bankOfDeposit')" prop="bankOfDeposit" align="center"/>
          <el-table-column :label="$t('payment.emi.pay.accountName')" prop="accountName" align="center"/>
          <el-table-column :label="$t('payment.emi.pay.accountType')" prop="accountType" align="center"/>
          <el-table-column :label="$t('payment.emi.pay.status')" prop="status" align="center"/>
          <el-table-column :label="$t('payment.emi.pay.use')" prop="use" align="center"/>
        </el-table>

        <pagination
            v-show="payeeTotal > 0"
            :total="payeeTotal"
            :page.sync="payeeQuery.pageNum"
            :limit.sync="payeeQuery.pageSize"
            @pagination="getPayeeList"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {paListPayee, paListRecord} from '@/api/payment/emi'

export default {
  name: 'PaymentManage',
  data() {
    return {
      // 当前激活的标签页
      activeTab: 'paymentRecord',

      // 付款记录相关数据
      recordLoading: false,
      recordList: [],
      recordTotal: 0,
      recordQuery: {
        pageNum: 1,
        pageSize: 10,
        dateRange: []
      },

      // 收款方列表相关数据
      payeeLoading: false,
      payeeList: [],
      payeeTotal: 0,
      payeeQuery: {
        pageNum: 1,
        pageSize: 10,
        account: '',
        status: '',
        type: ''
      }
    }
  },
  created() {
    this.getRecordList()
  },
  methods: {
    // 标签页切换
    handleTabClick(tab) {
      if (tab.name === 'paymentRecord') {
        this.getRecordList()
      } else if (tab.name === 'payeeList') {
        this.getPayeeList()
      }
    },

    // ========== 付款记录相关方法 ==========
    getRecordList() {
      this.recordLoading = true

      // 构建查询参数
      const params = {
        pageNum: this.recordQuery.pageNum,
        pageSize: this.recordQuery.pageSize
      }

      // 处理日期范围参数
      if (this.recordQuery.dateRange && this.recordQuery.dateRange.length === 2) {
        params.beginTime = this.recordQuery.dateRange[0]
        params.endTime = this.recordQuery.dateRange[1]
      }

      // 调用真实接口
      paListRecord(params)
          .then(response => {
            this.recordList = response.rows || []
            this.recordTotal = response.total || 0
            this.recordLoading = false
          })
          .catch(error => {
            this.$message.error(this.$t('payment.emi.pay.fetchRecordError'))
            this.recordLoading = false
          })
    },
    handleRecordQuery() {
      this.recordQuery.pageNum = 1
      this.getRecordList()
    },
    handleExport() {
      this.$message.info(this.$t('payment.emi.pay.exportPaymentRecord'))
    },
    handleCreatePayment() {
      this.$message.info(this.$t('payment.emi.pay.createPayment'))
    },
    handleRecordDetail(row) {
      this.$message.info(this.$t('payment.emi.pay.viewPaymentDetail') + row.withdrawalNumber)
    },
    handleCancelPayment(row) {
      this.$confirm(
          this.$t('payment.emi.pay.cancelPaymentConfirm', {number: row.withdrawalNumber}),
          this.$t('payment.emi.pay.warning'),
          {
            confirmButtonText: this.$t('payment.emi.pay.confirm'),
            cancelButtonText: this.$t('payment.emi.pay.cancel'),
            type: 'warning'
          }
      )
          .then(() => {
            this.$message.success(this.$t('payment.emi.pay.paymentCancelled'))
            this.getRecordList()
          })
          .catch(() => {
          })
    },

    // ========== 收款方列表相关方法 ==========
    getPayeeList() {
      this.payeeLoading = true

      // 构建查询参数
      const params = {
        pageNum: this.payeeQuery.pageNum,
        pageSize: this.payeeQuery.pageSize
      }

      // 添加可选的查询条件
      if (this.payeeQuery.account) {
        params.account = this.payeeQuery.account
      }
      if (this.payeeQuery.status) {
        params.status = this.payeeQuery.status
      }
      if (this.payeeQuery.type) {
        params.type = this.payeeQuery.type
      }

      // 调用真实接口
      paListPayee(params)
          .then(response => {
            this.payeeList = response.rows || []
            this.payeeTotal = response.total || 0
            this.payeeLoading = false
          })
          .catch(error => {
            console.error('获取收款方列表失败:', error)
            this.$message.error(this.$t('payment.emi.pay.fetchPayeeError'))
            this.payeeLoading = false
          })
    },
    handlePayeeQuery() {
      this.payeeQuery.pageNum = 1
      this.getPayeeList()
    },
    handleAddPayee() {
      this.$message.info(this.$t('payment.emi.pay.addPayee'))
    },
    handleEditPayee(row) {
      this.$message.info(this.$t('payment.emi.pay.editPayee') + row.accountName)
    },
    handleViewPayee(row) {
      this.$message.info(this.$t('payment.emi.pay.viewPayeeDetail') + row.accountName)
    },
    handleDeletePayee(row) {
      this.$confirm(this.$t('payment.emi.pay.deletePayeeConfirm', {name: row.accountName}), this.$t('payment.emi.pay.warning'), {
        confirmButtonText: this.$t('payment.emi.pay.confirm'),
        cancelButtonText: this.$t('payment.emi.pay.cancel'),
        type: 'warning'
      })
          .then(() => {
            this.$message.success(this.$t('payment.emi.pay.deleteSuccess'))
            this.getPayeeList()
          })
          .catch(() => {
          })
    }
  }
}
</script>

<style scoped>
.filter-container {
  padding: 10px 0;
  background: #fff;
}
</style>
