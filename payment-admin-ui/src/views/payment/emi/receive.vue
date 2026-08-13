<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <!-- 付款方列表标签页 -->
      <el-tab-pane :label="$t('payment.emi.receive.payerListTab')" name="payerList">
        <div class="filter-container">
          <el-select
            v-model="payerQuery.type"
            :placeholder="$t('payment.emi.receive.all')"
            clearable
            style="width: 150px; margin-right: 10px"
          >
            <el-option :label="$t('payment.emi.receive.all')" value=""></el-option>
            <el-option :label="$t('payment.emi.personal')" value="personal"></el-option>
            <el-option :label="$t('payment.emi.company')" value="company"></el-option>
          </el-select>
          <el-button type="primary" icon="el-icon-search" @click="handlePayerQuery">
            {{ $t('payment.emi.search') }}
          </el-button>
          <!-- <el-button type="primary" icon="el-icon-plus" style="float: right" @click="handleAddPayer">
            {{ $t('payment.emi.receive.addPayer') }}
          </el-button> -->
        </div>

        <el-table v-loading="payerLoading" :data="payerList" border style="margin-top: 20px">
          <el-table-column :label="$t('payment.emi.receive.payerName')" prop="payerName" align="center" />
          <el-table-column
            :label="$t('payment.emi.receive.paymentAccountName')"
            prop="paymentAccountName"
            align="center"
          />
          <el-table-column :label="$t('payment.emi.receive.remitArea')" prop="remitArea" align="center" />
          <el-table-column :label="$t('payment.emi.receive.paymentAccount')" prop="paymentAccount" align="center" />
          <el-table-column :label="$t('payment.emi.receive.paymentBank')" prop="paymentBank" align="center" />
          <el-table-column :label="$t('payment.emi.receive.use')" prop="use" align="center" />
        </el-table>

        <pagination
          v-show="payerTotal > 0"
          :total="payerTotal"
          :page.sync="payerQuery.pageNum"
          :limit.sync="payerQuery.pageSize"
          @pagination="getPayerList"
        />
      </el-tab-pane>

      <!-- 订单管理标签页 -->
      <el-tab-pane :label="$t('payment.emi.receive.orderManageTab')" name="orderManage">
        <div class="filter-container">
          <el-date-picker
            v-model="orderQuery.dateRange"
            type="daterange"
            range-separator="~"
            :start-placeholder="$t('payment.emi.receive.startDate')"
            :end-placeholder="$t('payment.emi.receive.endDate')"
            value-format="yyyy-MM-dd"
            style="width: 300px; margin-right: 10px"
          />
          <el-select
            v-model="orderQuery.status"
            :placeholder="$t('payment.emi.status')"
            clearable
            style="width: 150px; margin-right: 10px"
          >
            <el-option :label="$t('payment.emi.receive.all')" value=""></el-option>
            <el-option :label="$t('payment.emi.receive.completed')" value="已完成"></el-option>
            <el-option :label="$t('payment.emi.receive.partialPayment')" value="部分付款"></el-option>
            <el-option :label="$t('payment.emi.receive.pendingPayment')" value="待付款"></el-option>
          </el-select>
          <el-button type="primary" icon="el-icon-search" @click="handleOrderQuery">
            {{ $t('payment.emi.search') }}
          </el-button>
        </div>

        <el-table v-loading="orderLoading" :data="orderList" border style="margin-top: 20px">
          <el-table-column :label="$t('payment.emi.receive.payerName')" prop="payerName" align="center" />
          <el-table-column
            :label="$t('payment.emi.receive.paymentAccountName')"
            prop="paymentAccountName"
            align="center"
          />
          <el-table-column :label="$t('payment.emi.receive.paymentAccount')" prop="paymentAccount" align="center" />
          <el-table-column :label="$t('payment.emi.receive.paymentBank')" prop="paymentBank" align="center" />
          <el-table-column :label="$t('payment.emi.receive.orderTime')" prop="orderTime" width="180" align="center" />
          <el-table-column
            :label="$t('payment.emi.receive.orderNumber')"
            prop="orderNumber"
            width="200"
            align="center"
          />
          <el-table-column :label="$t('payment.emi.receive.orderAmount')" prop="orderAmount" align="center">
            <template slot-scope="scope">{{ scope.row.currency }} {{ scope.row.orderAmount }}</template>
          </el-table-column>
          <el-table-column :label="$t('payment.emi.receive.remainingBalance')" prop="remainingBalance" align="center">
            <template slot-scope="scope">{{ scope.row.currency }} {{ scope.row.remainingBalance }}</template>
          </el-table-column>
          <el-table-column :label="$t('payment.emi.receive.nation')" prop="nation" align="center" />
          <el-table-column :label="$t('payment.emi.receive.tradeMethod')" prop="tradeMethod" align="center" />
          <el-table-column :label="$t('payment.emi.receive.buyerName')" prop="buyerName" align="center" />
          <el-table-column :label="$t('payment.emi.status')" prop="status" align="center" />
        </el-table>

        <pagination
          v-show="orderTotal > 0"
          :total="orderTotal"
          :page.sync="orderQuery.pageNum"
          :limit.sync="orderQuery.pageSize"
          @pagination="getOrderList"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { reListOrder, reListPayer } from '@/api/payment/emi'

export default {
  name: 'ReceivePayment',
  components: {},
  data() {
    return {
      // 当前激活的标签页
      activeTab: 'payerList',

      // 收款记录相关数据
      receiveLoading: false,
      receiveList: [],
      receiveTotal: 0,
      receiveQuery: {
        pageNum: 1,
        pageSize: 10,
        dateRange: [],
        status: ''
      },

      // 付款方列表相关数据
      payerLoading: false,
      payerList: [],
      payerTotal: 0,
      payerQuery: {
        pageNum: 1,
        pageSize: 10,
        type: ''
      },

      // 订单管理相关数据
      orderLoading: false,
      orderList: [],
      orderTotal: 0,
      orderQuery: {
        pageNum: 1,
        pageSize: 10,
        dateRange: [],
        status: ''
      }
    }
  },
  created() {
    this.getPayerList()
  },
  methods: {
    // 标签页切换
    handleTabClick(tab) {
      if (tab.name === 'payerList') {
        this.getPayerList()
      } else if (tab.name === 'orderManage') {
        this.getOrderList()
      }
    },

    handleReceiveQuery() {
      this.receiveQuery.pageNum = 1
      this.getReceiveList()
    },
    // handleAddReceive() {
    //   this.$message.info('打开新增收款对话框')
    // },
    handleReceiveDetail(row) {
      this.$message.info('查看收款详情：' + row.orderNumber)
    },
    handleReceiveApprove(row) {
      this.$message.info('审核收款：' + row.orderNumber)
    },

    // ========== 付款方列表相关方法 ==========
    getPayerList() {
      this.payerLoading = true
      reListPayer(this.payerQuery)
        .then(response => {
          this.payerList = response.rows || []
          this.payerTotal = response.total || 0
          this.payerLoading = false
        })
        .catch(() => {
          this.payerLoading = false
        })
    },
    handlePayerQuery() {
      this.payerQuery.pageNum = 1
      this.getPayerList()
    },

    // ========== 订单管理相关方法 ==========
    getOrderList() {
      this.orderLoading = true
      // 处理日期范围参数
      const params = { ...this.orderQuery }
      if (params.dateRange && params.dateRange.length === 2) {
        params.beginTime = params.dateRange[0]
        params.endTime = params.dateRange[1]
      }
      delete params.dateRange

      reListOrder(params)
        .then(response => {
          this.orderList = response.rows || []
          this.orderTotal = response.total || 0
          this.orderLoading = false
        })
        .catch(() => {
          this.orderLoading = false
        })
    },
    handleOrderQuery() {
      this.orderQuery.pageNum = 1
      this.getOrderList()
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
