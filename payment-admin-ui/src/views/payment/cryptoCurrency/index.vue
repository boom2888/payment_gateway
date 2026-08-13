<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('payment.cryptoCurrency.currencyShortName')" prop="shortName">
        <el-input
          v-model="queryParams.shortName"
          :placeholder="$t('payment.cryptoCurrency.enterCurrencyShortName')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.cryptoCurrency.currencyName')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="$t('payment.cryptoCurrency.enterCurrencyName')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.cryptoCurrency.currencyChain')" prop="chain">
        <el-input
          v-model="queryParams.chain"
          :placeholder="$t('payment.cryptoCurrency.enterCurrencyChain')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.cryptoCurrency.traditionalCode')" prop="b2c2Ticker">
        <el-input
          v-model="queryParams.b2c2Ticker"
          :placeholder="$t('payment.cryptoCurrency.enterTraditionalCode')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.cryptoCurrency.decimalPrecision')" prop="decimalPrecision">
        <el-input
          v-model="queryParams.decimalPrecision"
          :placeholder="$t('payment.cryptoCurrency.enterDecimalPrecision')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('payment.cryptoCurrency.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('payment.cryptoCurrency.reset') }}</el-button>
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
          v-hasPermi="['payment:cryptoCurrency:add']"
        >{{ $t('payment.cryptoCurrency.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['payment:cryptoCurrency:edit']"
        >{{ $t('payment.cryptoCurrency.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['payment:cryptoCurrency:remove']"
        >{{ $t('payment.cryptoCurrency.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:cryptoCurrency:export']"
        >{{ $t('payment.cryptoCurrency.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cryptoCurrencyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('payment.cryptoCurrency.currencyId')" align="center" prop="id" v-if="false"/>
      <el-table-column :label="$t('payment.cryptoCurrency.currencyShortName')" align="center" prop="shortName" />
      <el-table-column :label="$t('payment.cryptoCurrency.currencyName')" align="center" prop="name" />
      <el-table-column :label="$t('payment.cryptoCurrency.currencyChain')" align="center" prop="chain" />
      <el-table-column :label="$t('payment.cryptoCurrency.traditionalCode')" align="center" prop="b2c2Ticker" />
      <el-table-column :label="$t('payment.cryptoCurrency.decimalPrecision')" align="center" prop="decimalPrecision" />
      <el-table-column :label="$t('payment.cryptoCurrency.isUtxoBased')" align="center" prop="isUtxoBased" />
      <el-table-column :label="$t('payment.cryptoCurrency.fireblocksAssetId')" align="center" prop="fireblocksAssetId" />
      <el-table-column :label="$t('payment.cryptoCurrency.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:cryptoCurrency:edit']"
          >{{ $t('payment.cryptoCurrency.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:cryptoCurrency:remove']"
          >{{ $t('payment.cryptoCurrency.delete') }}</el-button>
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

    <!-- 添加或修改加密货币对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item :label="$t('payment.cryptoCurrency.currencyShortName')" prop="shortName">
          <el-input v-model="form.shortName" :placeholder="$t('payment.cryptoCurrency.enterCurrencyShortName')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.cryptoCurrency.currencyName')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('payment.cryptoCurrency.enterCurrencyName')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.cryptoCurrency.currencyChain')" prop="chain">
          <el-input v-model="form.chain" :placeholder="$t('payment.cryptoCurrency.enterCurrencyChain')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.cryptoCurrency.traditionalCode')" prop="b2c2Ticker">
          <el-input v-model="form.b2c2Ticker" :placeholder="$t('payment.cryptoCurrency.enterTraditionalCode')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.cryptoCurrency.decimalPrecision')" prop="decimalPrecision">
          <el-input v-model="form.decimalPrecision" :placeholder="$t('payment.cryptoCurrency.enterDecimalPrecision')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.cryptoCurrency.isUtxoBased')" prop="isUtxoBased">
          <el-input v-model="form.isUtxoBased" :placeholder="$t('payment.cryptoCurrency.isUtxoBased')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.cryptoCurrency.fireblocksAssetId')" prop="fireblocksAssetId">
          <el-input v-model="form.fireblocksAssetId" :placeholder="$t('payment.cryptoCurrency.fireblocksAssetId')" />
        </el-form-item>
    </el-col>

      </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.cryptoCurrency.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.cryptoCurrency.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCryptoCurrency, getCryptoCurrency, delCryptoCurrency, addCryptoCurrency, updateCryptoCurrency, exportCryptoCurrency } from "@/api/payment/cryptoCurrency";

export default {
  name: "CryptoCurrency",
  components: {
  },
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
      // 加密货币表格数据
      cryptoCurrencyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shortName: undefined,
        name: undefined,
        chain: undefined,
        b2c2Ticker: undefined,
        decimalPrecision: undefined,
        isUtxoBased: undefined,
        fireblocksAssetId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        shortName: [
          { required: true, message: this.$t('payment.cryptoCurrency.shortNameRequired'), trigger: "blur" }
        ],
        chain: [
          { required: true, message: this.$t('payment.cryptoCurrency.chainRequired'), trigger: "blur" }
        ],
        b2c2Ticker: [
          { required: true, message: this.$t('payment.cryptoCurrency.b2c2TickerRequired'), trigger: "blur" }
        ],
        isUtxoBased: [
          { required: true, message: this.$t('payment.cryptoCurrency.isUtxoBasedRequired'), trigger: "blur" }
        ],
        fireblocksAssetId: [
          { required: true, message: this.$t('payment.cryptoCurrency.fireblocksAssetIdRequired'), trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询加密货币列表 */
    getList() {
      this.loading = true;
      listCryptoCurrency(this.queryParams).then(response => {
        this.cryptoCurrencyList = response.rows;
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
        shortName: undefined,
        name: undefined,
        chain: undefined,
        b2c2Ticker: undefined,
        decimalPrecision: undefined,
        isUtxoBased: undefined,
        fireblocksAssetId: undefined
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t('payment.cryptoCurrency.addCryptoCurrency');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCryptoCurrency(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('payment.cryptoCurrency.editCryptoCurrency');
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCryptoCurrency(this.form).then(response => {
              this.msgSuccess(this.$t('payment.cryptoCurrency.editSuccess'));
              this.open = false;
              this.getList();
            });
          } else {
            addCryptoCurrency(this.form).then(response => {
              this.msgSuccess(this.$t('payment.cryptoCurrency.addSuccess'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm(this.$t('payment.cryptoCurrency.deleteConfirm', { ids: ids }), this.$t('payment.cryptoCurrency.warning'), {
          confirmButtonText: this.$t('payment.cryptoCurrency.confirm'),
          cancelButtonText: this.$t('payment.cryptoCurrency.cancel'),
          type: "warning"
        }).then(function() {
          return delCryptoCurrency(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess(this.$t('payment.cryptoCurrency.deleteSuccess'));
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm(this.$t('payment.cryptoCurrency.exportConfirm'), this.$t('payment.cryptoCurrency.warning'), {
          confirmButtonText: this.$t('payment.cryptoCurrency.confirm'),
          cancelButtonText: this.$t('payment.cryptoCurrency.cancel'),
          type: "warning"
        }).then(function() {
          return exportCryptoCurrency(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
