<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="118px">
      <el-form-item :label="$t('payment.offRampingCustomerWallet.walletAddress')" prop="walletAddress">
        <el-input
          v-model="queryParams.walletAddress"
          :placeholder="$t('payment.offRampingCustomerWallet.pleaseEnterWalletAddress')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.offRampingCustomerWallet.createdAt')" prop="createdAt">
        <el-date-picker
          v-model="queryParams.createdAt"
          clearable
          size="small"
          type="date"
          value-format="yyyy-MM-dd"
          :placeholder="$t('payment.offRampingCustomerWallet.selectCreatedAt')"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.offRampingCustomerWallet.walletName')" prop="walletName">
        <el-input
          v-model="queryParams.walletName"
          :placeholder="$t('payment.offRampingCustomerWallet.pleaseEnterWalletName')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.offRampingCustomerWallet.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('payment.offRampingCustomerWallet.reset') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:offRampingCustomerWallet:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >
          {{ $t('payment.offRampingCustomerWallet.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:offRampingCustomerWallet:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >
          {{ $t('payment.offRampingCustomerWallet.edit') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:offRampingCustomerWallet:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >
          {{ $t('payment.offRampingCustomerWallet.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:offRampingCustomerWallet:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >
          {{ $t('payment.offRampingCustomerWallet.export') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="offRampingCustomerWalletList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column v-if="false" label="订单ID" align="center" prop="id" />
      <el-table-column :label="$t('payment.offRampingCustomerWallet.cryptoId')" align="center" prop="cryptoId" />
      <el-table-column
        :label="$t('payment.offRampingCustomerWallet.walletAddress')"
        align="center"
        prop="walletAddress"
      />
      <el-table-column :label="$t('payment.offRampingCustomerWallet.customerId')" align="center" prop="customerId" />
      <el-table-column
        :label="$t('payment.offRampingCustomerWallet.createdAt')"
        align="center"
        prop="createdAt"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.offRampingCustomerWallet.type')" align="center" prop="deleted" />
      <el-table-column :label="$t('payment.offRampingCustomerWallet.remark')" align="center" prop="remark" />
      <el-table-column :label="$t('payment.offRampingCustomerWallet.walletName')" align="center" prop="walletName" />
      <el-table-column
        :label="$t('payment.offRampingCustomerWallet.operation')"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['payment:offRampingCustomerWallet:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >
            {{ $t('payment.offRampingCustomerWallet.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['payment:offRampingCustomerWallet:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >
            {{ $t('payment.offRampingCustomerWallet.delete') }}
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

    <!-- 添加或修改出金客户钱包对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.offRampingCustomerWallet.cryptoId')" prop="cryptoId">
              <el-input
                v-model="form.cryptoId"
                :placeholder="$t('payment.offRampingCustomerWallet.pleaseEnterWalletAddress')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.offRampingCustomerWallet.walletAddress')" prop="walletAddress">
              <el-input
                v-model="form.walletAddress"
                :placeholder="$t('payment.offRampingCustomerWallet.pleaseEnterWalletAddress')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.offRampingCustomerWallet.customerId')" prop="customerId">
              <el-input
                v-model="form.customerId"
                :placeholder="$t('payment.offRampingCustomerWallet.pleaseEnterWalletAddress')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.offRampingCustomerWallet.createdAt')" prop="createdAt">
              <el-date-picker
                v-model="form.createdAt"
                clearable
                size="small"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.offRampingCustomerWallet.selectCreatedAt')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.offRampingCustomerWallet.createdBy')" prop="createdBy">
              <el-input
                v-model="form.createdBy"
                :placeholder="$t('payment.offRampingCustomerWallet.pleaseEnterCreatedBy')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.offRampingCustomerWallet.deletedAt')" prop="deletedAt">
              <el-date-picker
                v-model="form.deletedAt"
                clearable
                size="small"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.offRampingCustomerWallet.selectDeletedAt')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.offRampingCustomerWallet.deletedBy')" prop="deletedBy">
              <el-input
                v-model="form.deletedBy"
                :placeholder="$t('payment.offRampingCustomerWallet.pleaseEnterDeletedBy')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.offRampingCustomerWallet.type')" prop="deleted">
              <el-input v-model="form.deleted" :placeholder="$t('payment.offRampingCustomerWallet.pleaseEnterType')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('payment.offRampingCustomerWallet.remark')" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                :placeholder="$t('payment.offRampingCustomerWallet.pleaseEnterContent')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.offRampingCustomerWallet.walletName')" prop="walletName">
              <el-input
                v-model="form.walletName"
                :placeholder="$t('payment.offRampingCustomerWallet.pleaseEnterWalletName')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.offRampingCustomerWallet.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.offRampingCustomerWallet.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addOffRampingCustomerWallet,
  delOffRampingCustomerWallet,
  exportOffRampingCustomerWallet,
  getOffRampingCustomerWallet,
  listOffRampingCustomerWallet,
  updateOffRampingCustomerWallet
} from '@/api/payment/offRampingCustomerWallet'

export default {
  name: 'OffRampingCustomerWallet',
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
      // 出金客户钱包表格数据
      offRampingCustomerWalletList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        walletAddress: undefined,
        createdAt: undefined,
        walletName: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cryptoId: [
          { required: true, message: this.$t('payment.offRampingCustomerWallet.cryptoIdRequired'), trigger: 'blur' }
        ],
        customerId: [
          { required: true, message: this.$t('payment.offRampingCustomerWallet.customerIdRequired'), trigger: 'blur' }
        ],
        createdAt: [
          { required: true, message: this.$t('payment.offRampingCustomerWallet.createdAtRequired'), trigger: 'blur' }
        ],
        deleted: [
          { required: true, message: this.$t('payment.offRampingCustomerWallet.typeRequired'), trigger: 'blur' }
        ],
        walletName: [
          { required: true, message: this.$t('payment.offRampingCustomerWallet.walletNameRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询出金客户钱包列表 */
    getList() {
      this.loading = true
      listOffRampingCustomerWallet(this.queryParams).then(response => {
        this.offRampingCustomerWalletList = response.rows
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
        cryptoId: undefined,
        walletAddress: undefined,
        customerId: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
        remark: undefined,
        walletName: undefined
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
      this.title = this.$t('payment.offRampingCustomerWallet.addTitle')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOffRampingCustomerWallet(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.offRampingCustomerWallet.editTitle')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOffRampingCustomerWallet(this.form).then(response => {
              this.msgSuccess(this.$t('payment.offRampingCustomerWallet.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addOffRampingCustomerWallet(this.form).then(response => {
              this.msgSuccess(this.$t('payment.offRampingCustomerWallet.addSuccess'))
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
        this.$t('payment.offRampingCustomerWallet.confirmDelete').replace('{ids}', ids),
        this.$t('payment.offRampingCustomerWallet.warning'),
        {
          confirmButtonText: this.$t('payment.offRampingCustomerWallet.confirm'),
          cancelButtonText: this.$t('payment.offRampingCustomerWallet.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return delOffRampingCustomerWallet(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.offRampingCustomerWallet.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(
        this.$t('payment.offRampingCustomerWallet.confirmExport'),
        this.$t('payment.offRampingCustomerWallet.warning'),
        {
          confirmButtonText: this.$t('payment.offRampingCustomerWallet.confirm'),
          cancelButtonText: this.$t('payment.offRampingCustomerWallet.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return exportOffRampingCustomerWallet(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
