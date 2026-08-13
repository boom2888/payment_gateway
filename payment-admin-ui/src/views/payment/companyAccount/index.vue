<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('payment.companyAccount.searchForm.currency')" prop="currency">
        <el-input
          v-model="queryParams.currency"
          :placeholder="$t('payment.companyAccount.form.currencyPlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.companyAccount.searchForm.createdAt')" prop="createdAt">
        <el-date-picker
          clearable
          size="small"
          v-model="queryParams.createdAt"
          type="date"
          value-format="yyyy-MM-dd"
          :placeholder="$t('payment.companyAccount.form.createdAtPlaceholder')"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.companyAccount.searchForm.searchBtn') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('payment.companyAccount.searchForm.resetBtn') }}
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
          v-hasPermi="['payment:companyAccount:add']"
        >
          {{ $t('payment.companyAccount.actions.add') }}
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
          v-hasPermi="['payment:companyAccount:edit']"
        >
          {{ $t('payment.companyAccount.actions.edit') }}
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
          v-hasPermi="['payment:companyAccount:remove']"
        >
          {{ $t('payment.companyAccount.actions.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:companyAccount:export']"
        >
          {{ $t('payment.companyAccount.actions.export') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="companyAccountList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('payment.companyAccount.table.id')" align="center" prop="id" v-if="false" />
      <el-table-column :label="$t('payment.companyAccount.table.accountId')" align="center" prop="accountId" />
      <el-table-column :label="$t('payment.companyAccount.table.currency')" align="center" prop="currency" />
      <el-table-column
        :label="$t('payment.companyAccount.table.createdAt')"
        align="center"
        prop="createdAt"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.companyAccount.table.updatedAt')"
        align="center"
        prop="updatedAt"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.companyAccount.table.action')"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:companyAccount:edit']"
          >
            {{ $t('payment.companyAccount.actions.edit') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:companyAccount:remove']"
          >
            {{ $t('payment.companyAccount.actions.delete') }}
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

    <!-- 添加或修改公司账户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="200px" size="small">
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item :label="$t('payment.companyAccount.form.accountId')" prop="accountId">
              <el-input
                v-model="form.accountId"
                :placeholder="$t('payment.companyAccount.form.accountIdPlaceholder')"
                size="small"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.companyAccount.form.currency')" prop="currency">
              <el-input
                v-model="form.currency"
                :placeholder="$t('payment.companyAccount.form.currencyPlaceholder')"
                size="small"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.companyAccount.form.createdAt')" prop="createdAt">
              <el-date-picker
                clearable
                size="small"
                v-model="form.createdAt"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.companyAccount.form.createdAtPlaceholder')"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.companyAccount.form.updatedAt')" prop="updatedAt">
              <el-date-picker
                clearable
                size="small"
                v-model="form.updatedAt"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.companyAccount.form.updatedAtPlaceholder')"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.companyAccount.dialog.confirmBtn') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.companyAccount.dialog.cancelBtn') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addCompanyAccount,
  delCompanyAccount,
  exportCompanyAccount,
  getCompanyAccount,
  listCompanyAccount,
  updateCompanyAccount
} from '@/api/payment/companyAccount'

export default {
  name: 'CompanyAccount',
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
      // 公司账户表格数据
      companyAccountList: [],
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
        accountId: [
          { required: true, message: this.$t('payment.companyAccount.validation.accountIdRequired'), trigger: 'blur' }
        ],
        currency: [
          { required: true, message: this.$t('payment.companyAccount.validation.currencyRequired'), trigger: 'blur' }
        ],
        createdAt: [
          { required: true, message: this.$t('payment.companyAccount.validation.createdAtRequired'), trigger: 'blur' }
        ],
        updatedAt: [
          { required: true, message: this.$t('payment.companyAccount.validation.updatedAtRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询公司账户列表 */
    getList() {
      this.loading = true
      listCompanyAccount(this.queryParams).then(response => {
        this.companyAccountList = response.rows
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
        accountId: undefined,
        currency: undefined,
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
      this.title = this.$t('payment.companyAccount.dialog.addTitle')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getCompanyAccount(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.companyAccount.dialog.editTitle')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCompanyAccount(this.form).then(response => {
              this.msgSuccess(this.$t('payment.companyAccount.messages.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addCompanyAccount(this.form).then(response => {
              this.msgSuccess(this.$t('payment.companyAccount.messages.addSuccess'))
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
        this.$t('payment.companyAccount.messages.deleteConfirm', { id: ids }),
        this.$t('payment.companyAccount.messages.warning'),
        {
          confirmButtonText: this.$t('payment.companyAccount.dialog.confirmBtn'),
          cancelButtonText: this.$t('payment.companyAccount.dialog.cancelBtn'),
          type: 'warning'
        }
      )
        .then(function () {
          return delCompanyAccount(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.companyAccount.messages.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(
        this.$t('payment.companyAccount.messages.exportConfirm'),
        this.$t('payment.companyAccount.messages.warning'),
        {
          confirmButtonText: this.$t('payment.companyAccount.dialog.confirmBtn'),
          cancelButtonText: this.$t('payment.companyAccount.dialog.cancelBtn'),
          type: 'warning'
        }
      )
        .then(function () {
          return exportCompanyAccount(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
