<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="118px">
      <el-form-item :label="$t('payment.customerWallet.walletAddress')" prop="walletAddress">
        <el-input
          v-model="queryParams.walletAddress"
          :placeholder="$t('payment.customerWallet.enterWalletAddress')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.customerWallet.type')" prop="active">
        <el-select
          v-model="queryParams.active"
          :placeholder="$t('payment.customerWallet.selectType')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in activeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.customerWallet.createdTime')" prop="createdAt">
        <el-date-picker
          v-model="queryParams.createdAt"
          clearable
          size="small"
          type="date"
          value-format="yyyy-MM-dd"
          :placeholder="$t('payment.customerWallet.selectCreatedTime')"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.customerWallet.walletName')" prop="walletName">
        <el-input
          v-model="queryParams.walletName"
          :placeholder="$t('payment.customerWallet.enterWalletName')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.customerWallet.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('payment.customerWallet.reset') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:customerWallet:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >
          {{ $t('payment.customerWallet.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:customerWallet:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >
          {{ $t('payment.customerWallet.edit') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:customerWallet:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >
          {{ $t('payment.customerWallet.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:customerWallet:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >
          {{ $t('payment.customerWallet.export') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="customerWalletList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column v-if="false" :label="$t('payment.customerWallet.customerWalletId')" align="center" prop="id" />
      <el-table-column :label="$t('payment.customerWallet.saasCustomerId')" align="center" prop="customerId" />
      <el-table-column :label="$t('payment.customerWallet.cryptoCurrencyId')" align="center" prop="cryptoId" />
      <el-table-column :label="$t('payment.customerWallet.walletAddress')" align="center" prop="walletAddress" />
      <el-table-column
        :label="$t('payment.customerWallet.type')"
        align="center"
        prop="active"
        :formatter="activeFormat"
      />
      <el-table-column :label="$t('payment.customerWallet.createdTime')" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.customerWallet.type')"
        align="center"
        prop="deleted"
        :formatter="deletedFormat"
      />
      <el-table-column :label="$t('payment.customerWallet.recordDescription')" align="center" prop="remark" />
      <el-table-column :label="$t('payment.customerWallet.walletName')" align="center" prop="walletName" />
      <el-table-column
        :label="$t('payment.customerWallet.operation')"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['payment:customerWallet:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >
            {{ $t('payment.customerWallet.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['payment:customerWallet:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >
            {{ $t('payment.customerWallet.delete') }}
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

    <!-- 添加或修改客户钱包对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.customerWallet.saasCustomerId')" prop="customerId">
              <el-input v-model="form.customerId" :placeholder="$t('payment.customerWallet.enterSaasCustomerId')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerWallet.cryptoCurrencyId')" prop="cryptoId">
              <el-input v-model="form.cryptoId" :placeholder="$t('payment.customerWallet.enterCryptoCurrencyId')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerWallet.walletAddress')" prop="walletAddress">
              <el-input v-model="form.walletAddress" :placeholder="$t('payment.customerWallet.enterWalletAddress')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerWallet.type')" prop="active">
              <el-select v-model="form.active" :placeholder="$t('payment.customerWallet.selectType')" filterable>
                <el-option
                  v-for="dict in activeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.customerWallet.createdTime')" prop="createdAt">
              <el-date-picker
                v-model="form.createdAt"
                clearable
                size="small"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.customerWallet.selectCreatedTime')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerWallet.createdBy')" prop="createdBy">
              <el-input v-model="form.createdBy" :placeholder="$t('payment.customerWallet.enterCreatedBy')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerWallet.deletedTime')" prop="deletedAt">
              <el-date-picker
                v-model="form.deletedAt"
                clearable
                size="small"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.customerWallet.selectDeletedTime')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerWallet.deletedBy')" prop="deletedBy">
              <el-input v-model="form.deletedBy" :placeholder="$t('payment.customerWallet.enterDeletedBy')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerWallet.type')" prop="deleted">
              <el-select v-model="form.deleted" :placeholder="$t('payment.customerWallet.selectType')" filterable>
                <el-option
                  v-for="dict in deletedOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('payment.customerWallet.recordDescription')" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                :placeholder="$t('payment.customerWallet.enterRecordDescription')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.customerWallet.walletName')" prop="walletName">
              <el-input v-model="form.walletName" :placeholder="$t('payment.customerWallet.enterWalletName')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.customerWallet.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.customerWallet.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addCustomerWallet,
  delCustomerWallet,
  exportCustomerWallet,
  getCustomerWallet,
  listCustomerWallet,
  updateCustomerWallet
} from '@/api/payment/customerWallet'

export default {
  name: 'CustomerWallet',
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
      // 客户钱包表格数据
      customerWalletList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 类型字典
      activeOptions: [],
      // 类型字典
      deletedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        walletAddress: undefined,
        active: undefined,
        createdAt: undefined,
        walletName: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        customerId: [
          { required: true, message: this.$t('payment.customerWallet.customerIdRequired'), trigger: 'blur' }
        ],
        cryptoId: [{ required: true, message: this.$t('payment.customerWallet.cryptoIdRequired'), trigger: 'blur' }],
        active: [{ required: true, message: this.$t('payment.customerWallet.activeRequired'), trigger: 'change' }],
        createdAt: [
          { required: true, message: this.$t('payment.customerWallet.createdTimeRequired'), trigger: 'blur' }
        ],
        deleted: [{ required: true, message: this.$t('payment.customerWallet.typeRequired'), trigger: 'change' }],
        walletName: [{ required: true, message: this.$t('payment.customerWallet.walletNameRequired'), trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('CUSTOMER_WALLET_ACTIVE').then(response => {
      this.activeOptions = response.data
    })
    this.getDicts('DELETE_STATUS').then(response => {
      this.deletedOptions = response.data
    })
  },
  methods: {
    /** 查询客户钱包列表 */
    getList() {
      this.loading = true
      listCustomerWallet(this.queryParams).then(response => {
        this.customerWalletList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 类型字典翻译
    activeFormat(row, column) {
      return this.selectDictLabel(this.activeOptions, row.active)
    },
    // 类型字典翻译
    deletedFormat(row, column) {
      return this.selectDictLabel(this.deletedOptions, row.deleted)
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
        customerId: undefined,
        cryptoId: undefined,
        walletAddress: undefined,
        active: undefined,
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
      this.title = this.$t('payment.customerWallet.addCustomerWallet')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getCustomerWallet(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.customerWallet.editCustomerWallet')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCustomerWallet(this.form).then(response => {
              this.msgSuccess(this.$t('payment.customerWallet.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addCustomerWallet(this.form).then(response => {
              this.msgSuccess(this.$t('payment.customerWallet.addSuccess'))
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
        this.$t('payment.customerWallet.deleteConfirm', { ids: ids }),
        this.$t('payment.customerWallet.warning'),
        {
          confirmButtonText: this.$t('payment.customerWallet.confirm'),
          cancelButtonText: this.$t('payment.customerWallet.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return delCustomerWallet(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.customerWallet.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('payment.customerWallet.exportConfirm'), this.$t('payment.customerWallet.warning'), {
        confirmButtonText: this.$t('payment.customerWallet.confirm'),
        cancelButtonText: this.$t('payment.customerWallet.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportCustomerWallet(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
