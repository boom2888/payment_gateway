<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item :label="$t('payment.shop.id')" prop="merchantId">
        <el-input
          v-model="queryParams.merchantId"
          :placeholder="$t('payment.shop.id')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.shop.externalOrderNo')" prop="externalId">
        <el-input
          v-model="queryParams.externalId"
          :placeholder="$t('payment.shop.externalOrderNo')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.shop.transactionId')" prop="transactionId">
        <el-input
          v-model="queryParams.transactionId"
          :placeholder="$t('payment.shop.transactionId')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.shop.transferStatus')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('payment.shop.transferStatus')" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column :label="$t('payment.shop.id')" align="center" prop="merchantId" />
      <el-table-column :label="$t('payment.shop.externalOrderNo')" align="center" prop="externalId" />
      <el-table-column :label="$t('payment.shop.transactionType')" align="center" prop="transactionType" />
      <el-table-column :label="$t('payment.shop.amount')" align="center" prop="amount" />
      <el-table-column :label="$t('payment.shop.currency')" align="center" prop="currency" />
      <el-table-column :label="$t('payment.shop.handlingFee')" align="center" prop="handlingFee" />
      <el-table-column :label="$t('payment.shop.channelFee')" align="center" prop="feeAmount" />
      <el-table-column :label="$t('payment.shop.transferStatus')" align="center" prop="status">
        <template slot-scope="scope">
           <el-tag :type="statusTypeFormat(scope.row.status)">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.createdAt')" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listTransferRecord } from "@/api/payment/transferRecord";

export default {
  name: "TransferRecord",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      dataList: [],
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        merchantId: null,
        externalId: null,
        transactionId: null,
        status: null,
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("thunes_transfer_status").then(response => {
      this.statusOptions = response.data;
    });
  },

  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listTransferRecord(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 状态类型格式化 */
    statusTypeFormat(status) {
       const statusMap = {
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'PROCESSING': 'info',
        'CONFIRMED': 'primary',
        'REVERSED': 'warning',
        'CANCELLED': 'warning'
      }
      return statusMap[status] || ''
    },
    /** 手动翻译状态 */
    formatStatus(status) {
      return this.selectDictLabel(this.statusOptions, status);
    }
  }
};
</script>
