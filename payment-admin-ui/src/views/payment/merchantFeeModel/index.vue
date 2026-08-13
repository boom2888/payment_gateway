<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item :label="$t('payment.merchantFeeModel.modelName')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="$t('payment.merchantFeeModel.modelName')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['payment:merchantFeeModel:add']"
        >{{ $t('common.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['payment:merchantFeeModel:edit']"
        >{{ $t('common.edit') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['payment:merchantFeeModel:remove']"
        >{{ $t('common.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:merchantFeeModel:export']"
        >{{ $t('common.export') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="merchantFeeModelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column :label="$t('payment.merchantFeeModel.id')" align="center" prop="id"/>
      <el-table-column :label="$t('payment.merchantFeeModel.modelName')" align="center" prop="name" width="100" />
      <el-table-column :label="$t('payment.merchantFeeModel.usedNum')" align="center" prop="shopNum" width="90" />
      <el-table-column :label="$t('payment.merchantFeeModel.processFeeEuUsd')" align="center" prop="processFeeEuUsd" min-width="220">
        <template v-slot="scope">
          <span>{{ scope.row.processFeeEuUsd }}%</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.merchantFeeModel.processFeeEuNonUsd')" align="center" prop="processFeeEuNonUsd" min-width="220">
        <template v-slot="scope">
          <span>{{ scope.row.processFeeEuNonUsd }}%</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.merchantFeeModel.processFeeNonEuUsd')" align="center" prop="processFeeNonEuUsd" min-width="220">
        <template v-slot="scope">
          <span>{{ scope.row.processFeeNonEuUsd }}%</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.merchantFeeModel.processFeeNonEuNonUsd')" align="center" prop="processFeeNonEuNonUsd" min-width="250">
        <template v-slot="scope">
          <span>{{ scope.row.processFeeNonEuNonUsd }}%</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.merchantFeeModel.gatewayFeeThreshold')" align="center" prop="gatewayFeeThreshold" width="170px" />
      <el-table-column :label="$t('payment.merchantFeeModel.gatewayFeeBelowThreshold')" align="center" prop="gatewayFeeBelowThreshold" width="210px" />
      <el-table-column :label="$t('payment.merchantFeeModel.gatewayFeeAboveThreshold')" align="center" prop="gatewayFeeAboveThreshold" width="210px" />
      <el-table-column :label="$t('payment.merchantFeeModel.rollingReserveRate')" align="center" prop="rollingReserveRate" width="160px">
        <template v-slot="scope">
          <span>{{ scope.row.rollingReserveRate }}%</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.merchantFeeModel.rollingReserveReleaseDays')" align="center" prop="rollingReserveReleaseDays" width="210px" />
      <el-table-column :label="$t('payment.merchantFeeModel.exchangeRateBps')" align="center" prop="exchangeRateBps" width="180px">
        <template v-slot="scope">
          <span>{{ scope.row.exchangeRateBps }}‱</span>
        </template>
      </el-table-column>

      <el-table-column :label="$t('payment.merchantFeeModel.refundFee')" align="center" prop="refundFee" width="100px" />
      <el-table-column :label="$t('payment.merchantFeeModel.rdrFee')" align="center" prop="rdrFee" width="100px" />
      <el-table-column :label="$t('payment.merchantFeeModel.cbFee')" align="center" prop="cbFee" />
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:merchantFeeModel:edit']"
          >{{ $t('common.edit') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:merchantFeeModel:remove']"
          >{{ $t('common.delete') }}
          </el-button>
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

    <MerchantFeeModelSaveForm
      :visible.sync="open"
      :title="title"
      :form-data="form"
      @close="handleCloseForm"
      @submit="submitForm"
    />

  </div>
</template>

<script>
import {
  addMerchantFeeModel,
  delMerchantFeeModel,
  exportMerchantFeeModel,
  getMerchantFeeModel,
  listMerchantFeeModel,
  updateMerchantFeeModel
} from "@/api/payment/merchantFeeModel";
import MerchantFeeModelSaveForm from "@/views/payment/merchantFeeModel/MerchantFeeModelSaveForm.vue";

export default {
  name: "MerchantFeeModel",
  components: {MerchantFeeModelSaveForm},
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
      // 商户收费模型表格数据
      merchantFeeModelList: [],
      feeData: [
        { region: this.$t('payment.merchantFeeModel.europe'), currency: this.$t('payment.merchantFeeModel.eur'), feePercentage: "0.6" },
        { region: this.$t('payment.merchantFeeModel.europe'), currency: this.$t('payment.merchantFeeModel.nonEur'), feePercentage: "0.6" },
        { region: this.$t('payment.merchantFeeModel.nonEurope'), currency: this.$t('payment.merchantFeeModel.eur'), feePercentage: "0.6" },
        { region: this.$t('payment.merchantFeeModel.nonEurope'), currency: this.$t('payment.merchantFeeModel.nonEur'), feePercentage: "0.6" },
      ],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询商户收费模型列表 */
    getList() {
      this.loading = true;
      listMerchantFeeModel(this.queryParams).then(response => {
        this.merchantFeeModelList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        processFeeEuUsd: undefined,
        processFeeEuNonUsd: undefined,
        processFeeNonEuUsd: undefined,
        processFeeNonEuNonUsd: undefined,
        gatewayFeeThreshold: undefined,
        gatewayFeeBelowThreshold: undefined,
        gatewayFeeAboveThreshold: undefined,
        rollingReserveRate: undefined,
        rollingReserveReleaseDays: undefined,
        exchangeRateBps: undefined,
        refundFee: undefined,
        rdrFee: undefined,
        cbFee: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        deleteTime: undefined,
        deleted: undefined,
        remark: undefined
      };
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t('payment.merchantFeeModel.addMerchantFeeModel');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMerchantFeeModel(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('payment.merchantFeeModel.editMerchantFeeModel');
      });
    },
    /** 提交按钮 */
    submitForm(formData) {
      if (formData.id != null) {
        updateMerchantFeeModel(formData).then(response => {
          this.msgSuccess(this.$t('common.editSuccess'));
          this.open = false;
          this.getList();
        });
      } else {
        addMerchantFeeModel(formData).then(response => {
          this.msgSuccess(this.$t('common.addSuccess'));
          this.open = false;
          this.getList();
        });
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      if (row.shopNum > 0) {
        this.msgError(this.$t('payment.merchantFeeModel.deleteNotAllowed'));
        return
      }
      this.$confirm(this.$t('payment.merchantFeeModel.confirmDeleteMerchantFeeModel', { ids }), this.$t('common.warning'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: "warning"
      }).then(function () {
        return delMerchantFeeModel(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess(this.$t('payment.merchantFeeModel.deleteSuccess'));
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm(this.$t('payment.merchantFeeModel.confirmExportAll'), this.$t('common.warning'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: "warning"
      }).then(function () {
        return exportMerchantFeeModel(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    },
    /** 关闭弹窗 */
    handleCloseForm() {
      // this.open = false;
      this.reset();
    },
  }
};
</script>

<style scoped>
/* 表头文字不换行 */
::v-deep .el-table .el-table__header-wrapper th .cell{
  white-space: nowrap !important; /* 强制表头文字在一行内显示 */
  overflow: visible !important; /* 允许文字超出显示 */
}

/* 费用显示样式 */
.fee-display {
  font-size: 14px;
  font-weight: 600;
  color: #67c23a;
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
  padding: 4px 8px;
  border-radius: 4px;
  border: 1px solid #b3d8ff;
  display: inline-block;
  min-width: 50px;
  text-align: center;
}

</style>
