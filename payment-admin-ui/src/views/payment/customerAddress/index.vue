<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="128px">
      <el-form-item :label="$t('payment.customerAddress.activatedStatus')" prop="activated">
        <el-select
          v-model="queryParams.activated"
          :placeholder="$t('payment.customerAddress.activatedStatusPlaceholder')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in activatedOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.customerAddress.createTime')" prop="createdAt">
        <el-date-picker
          v-model="queryParams.createdAt"
          clearable
          size="small"
          type="date"
          value-format="yyyy-MM-dd"
          :placeholder="$t('payment.customerAddress.selectCreateTime')"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.customerAddress.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('payment.customerAddress.reset') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:customerAddress:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >
          {{ $t('payment.customerAddress.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:customerAddress:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >
          {{ $t('payment.customerAddress.edit') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:customerAddress:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >
          {{ $t('payment.customerAddress.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:customerAddress:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >
          {{ $t('payment.customerAddress.export') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="customerAddressList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column v-if="false" :label="$t('payment.customerAddress.customerAddressId')" align="center" prop="id" />
      <el-table-column :label="$t('payment.customerAddress.customerId')" align="center" prop="customerId" />
      <el-table-column :label="$t('payment.customerAddress.addressId')" align="center" prop="addressId" />
      <el-table-column
        :label="$t('payment.customerAddress.activatedStatus')"
        align="center"
        prop="activated"
        :formatter="activatedFormat"
      />
      <el-table-column :label="$t('payment.customerAddress.createTime')" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.customerAddress.type')"
        align="center"
        prop="deleted"
        :formatter="deletedFormat"
      />
      <el-table-column :label="$t('payment.customerAddress.remark')" align="center" prop="remark" />
      <el-table-column
        :label="$t('payment.customerAddress.operation')"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['payment:customerAddress:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >
            {{ $t('payment.customerAddress.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['payment:customerAddress:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >
            {{ $t('payment.customerAddress.delete') }}
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

    <!-- 添加或修改客户地址对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.customerAddress.customerId')" prop="customerId">
              <el-input v-model="form.customerId" :placeholder="$t('payment.customerAddress.customerIdPlaceholder')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerAddress.addressId')" prop="addressId">
              <el-input v-model="form.addressId" :placeholder="$t('payment.customerAddress.addressIdPlaceholder')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerAddress.activatedStatus')" prop="activated">
              <el-select
                v-model="form.activated"
                :placeholder="$t('payment.customerAddress.activatedStatusPlaceholder')"
                filterable
              >
                <el-option
                  v-for="dict in activatedOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.customerAddress.createTime')" prop="createdAt">
              <el-date-picker
                v-model="form.createdAt"
                clearable
                size="small"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.customerAddress.selectCreateTime')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerAddress.createdBy')" prop="createdBy">
              <el-input v-model="form.createdBy" :placeholder="$t('payment.customerAddress.createdByPlaceholder')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerAddress.deletedAt')" prop="deletedAt">
              <el-date-picker
                v-model="form.deletedAt"
                clearable
                size="small"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.customerAddress.selectDeleteTime')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerAddress.deletedBy')" prop="deletedBy">
              <el-input v-model="form.deletedBy" :placeholder="$t('payment.customerAddress.deletedByPlaceholder')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.customerAddress.type')" prop="deleted">
              <el-select v-model="form.deleted" :placeholder="$t('payment.customerAddress.typePlaceholder')" filterable>
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
            <el-form-item :label="$t('payment.customerAddress.remark')" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                :placeholder="$t('payment.customerAddress.remarkPlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.customerAddress.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.customerAddress.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addCustomerAddress,
  delCustomerAddress,
  exportCustomerAddress,
  getCustomerAddress,
  listCustomerAddress,
  updateCustomerAddress
} from '@/api/payment/customerAddress'

export default {
  name: 'CustomerAddress',
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
      // 客户地址表格数据
      customerAddressList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 激活状态字典
      activatedOptions: [],
      // 类型字典
      deletedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        activated: undefined,
        createdAt: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        customerId: [
          { required: true, message: this.$t('payment.customerAddress.customerIdRequired'), trigger: 'blur' }
        ],
        addressId: [{ required: true, message: this.$t('payment.customerAddress.addressIdRequired'), trigger: 'blur' }],
        activated: [
          { required: true, message: this.$t('payment.customerAddress.activatedRequired'), trigger: 'change' }
        ],
        createdAt: [
          { required: true, message: this.$t('payment.customerAddress.createTimeRequired'), trigger: 'blur' }
        ],
        deleted: [{ required: true, message: this.$t('payment.customerAddress.typeRequired'), trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('CUSTOMER_ADDRESS_ACTIVATED').then(response => {
      this.activatedOptions = response.data
    })
    this.getDicts('DELETE_STATUS').then(response => {
      this.deletedOptions = response.data
    })
  },
  methods: {
    /** 查询客户地址列表 */
    getList() {
      this.loading = true
      listCustomerAddress(this.queryParams).then(response => {
        this.customerAddressList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 激活状态字典翻译
    activatedFormat(row, column) {
      return this.selectDictLabel(this.activatedOptions, row.activated)
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
        addressId: undefined,
        activated: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
        remark: undefined
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
      this.title = this.$t('payment.customerAddress.addCustomerAddress')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getCustomerAddress(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.customerAddress.editCustomerAddress')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCustomerAddress(this.form).then(response => {
              this.msgSuccess(this.$t('payment.customerAddress.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addCustomerAddress(this.form).then(response => {
              this.msgSuccess(this.$t('payment.customerAddress.addSuccess'))
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
        this.$t('payment.customerAddress.confirmDelete').replace('{ids}', ids),
        this.$t('payment.customerAddress.warning'),
        {
          confirmButtonText: this.$t('payment.customerAddress.confirm'),
          cancelButtonText: this.$t('payment.customerAddress.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return delCustomerAddress(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.customerAddress.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('payment.customerAddress.confirmExport'), this.$t('payment.customerAddress.warning'), {
        confirmButtonText: this.$t('payment.customerAddress.confirm'),
        cancelButtonText: this.$t('payment.customerAddress.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportCustomerAddress(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
