<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="150px">
      <el-form-item :label="$t('payment.merchantAccount.currency')" prop="currency">
        <el-input
          v-model="queryParams.currency"
          :placeholder="$t('payment.merchantAccount.enterCurrency')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.merchantAccount.createdAt')" prop="createdAt">
        <el-date-picker
          clearable
          size="small"
          v-model="queryParams.createdAt"
          type="date"
          value-format="yyyy-MM-dd"
          :placeholder="$t('payment.merchantAccount.selectCreatedAt')"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.merchantAccount.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('payment.merchantAccount.reset') }}
        </el-button>
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
          v-hasPermi="['payment:merchantAccount:add']"
        >
          {{ $t('payment.merchantAccount.add') }}
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
          v-hasPermi="['payment:merchantAccount:edit']"
        >
          {{ $t('payment.merchantAccount.edit') }}
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
          v-hasPermi="['payment:merchantAccount:remove']"
        >
          {{ $t('payment.merchantAccount.delete') }}
        </el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:merchantAccount:export']"
        >
          {{ $t('payment.merchantAccount.export') }}
        </el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="merchantAccountList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('payment.merchantAccount.id')" align="center" prop="id" v-if="false" />
      <el-table-column :label="$t('payment.merchantAccount.merchantId')" align="center" prop="merchantId" />
      <el-table-column :label="$t('payment.merchantAccount.accountId')" align="center" prop="accountId" />
      <el-table-column :label="$t('payment.merchantAccount.currency')" align="center" prop="currency" />
      <el-table-column :label="$t('payment.merchantAccount.minBalance')" align="center" prop="minBalance" />
      <el-table-column :label="$t('payment.merchantAccount.createdAt')" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.merchantAccount.updatedAt')" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.merchantAccount.operation')"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:merchantAccount:edit']"
          >
            {{ $t('payment.merchantAccount.edit') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:merchantAccount:remove']"
          >
            {{ $t('payment.merchantAccount.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改商户账户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantAccount.accountId')" prop="accountId">
              <el-input v-model="form.accountId" :placeholder="$t('payment.merchantAccount.enterAccountId')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantAccount.currency')" prop="currency">
              <el-input v-model="form.currency" :placeholder="$t('payment.merchantAccount.enterCurrency')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantAccount.minBalance')" prop="minBalance">
              <el-input v-model="form.minBalance" :placeholder="$t('payment.merchantAccount.enterMinBalance')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantAccount.createdAt')" prop="createdAt">
              <el-date-picker
                clearable
                size="small"
                v-model="form.createdAt"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.merchantAccount.selectCreatedAt')"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.merchantAccount.updatedAt')" prop="updatedAt">
              <el-date-picker
                clearable
                size="small"
                v-model="form.updatedAt"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.merchantAccount.selectUpdatedAt')"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.merchantAccount.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.merchantAccount.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addMerchantAccount,
  delMerchantAccount,
  exportMerchantAccount,
  getMerchantAccount,
  listMerchantAccount,
  updateMerchantAccount
} from '@/api/payment/merchantAccount'

export default {
  name: 'MerchantAccount',
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
      // 商户账户表格数据
      merchantAccountList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        currency: undefined,
        createdAt: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        merchantId: [
          { required: true, message: this.$t('payment.merchantAccount.merchantIdRequired'), trigger: 'blur' }
        ],
        accountId: [{ required: true, message: this.$t('payment.merchantAccount.accountIdRequired'), trigger: 'blur' }],
        currency: [{ required: true, message: this.$t('payment.merchantAccount.currencyRequired'), trigger: 'blur' }],
        minBalance: [
          { required: true, message: this.$t('payment.merchantAccount.minBalanceRequired'), trigger: 'blur' }
        ],
        createdAt: [{ required: true, message: this.$t('payment.merchantAccount.createdAtRequired'), trigger: 'blur' }],
        updatedAt: [{ required: true, message: this.$t('payment.merchantAccount.updatedAtRequired'), trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询商户账户列表 */
    getList() {
      this.loading = true
      listMerchantAccount(this.queryParams).then(response => {
        this.merchantAccountList = response.rows
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
        accountId: undefined,
        currency: undefined,
        minBalance: undefined,
        createdAt: undefined,
        updatedAt: undefined
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
      this.title = this.$t('payment.merchantAccount.addMerchantAccount')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMerchantAccount(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.merchantAccount.editMerchantAccount')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMerchantAccount(this.form).then(response => {
              this.msgSuccess(this.$t('payment.merchantAccount.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addMerchantAccount(this.form).then(response => {
              this.msgSuccess(this.$t('payment.merchantAccount.addSuccess'))
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
        this.$t('payment.merchantAccount.confirmDelete', { ids }),
        this.$t('payment.merchantAccount.warning'),
        {
          confirmButtonText: this.$t('payment.merchantAccount.confirm'),
          cancelButtonText: this.$t('payment.merchantAccount.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return delMerchantAccount(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.merchantAccount.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('payment.merchantAccount.confirmExport'), this.$t('payment.merchantAccount.warning'), {
        confirmButtonText: this.$t('payment.merchantAccount.confirm'),
        cancelButtonText: this.$t('payment.merchantAccount.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportMerchantAccount(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
