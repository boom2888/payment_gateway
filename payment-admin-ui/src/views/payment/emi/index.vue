<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item :label="$t('payment.emi.name')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="$t('payment.emi.namePlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.emi.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('payment.emi.statusPlaceholder')"
          clearable
          size="small"
          filterable
        >
          <el-option :label="$t('payment.emi.statusPendingSubmit')" :value="0" />
          <el-option :label="$t('payment.emi.statusPendingReview')" :value="1" />
          <el-option :label="$t('payment.emi.statusPendingBinnerReview')" :value="2" />
          <el-option :label="$t('payment.emi.statusApproved')" :value="3" />
          <el-option :label="$t('payment.emi.statusRejected')" :value="4" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.emi.enabledStatus')" prop="enabledStatus">
        <el-select
          v-model="queryParams.enabledStatus"
          :placeholder="$t('payment.emi.enabledStatusPlaceholder')"
          clearable
          size="small"
          filterable
        >
          <el-option :label="$t('payment.emi.enabled')" :value="0" />
          <el-option :label="$t('payment.emi.disabled')" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.emi.merchantType')" prop="merchantType">
        <el-select
          v-model="queryParams.merchantType"
          :placeholder="$t('payment.emi.merchantTypePlaceholder')"
          clearable
          size="small"
          filterable
        >
          <el-option :label="$t('payment.emi.personal')" :value="1" />
          <el-option :label="$t('payment.emi.company')" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.emi.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('payment.emi.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">
          删除
        </el-button>
      </el-col> -->
      <el-col :span="1.5">
        <!-- <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button> -->
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="emiList" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column :label="$t('payment.emi.id')" align="center" prop="id" />
      <el-table-column :label="$t('payment.emi.name')" align="center" prop="name" />
      <el-table-column :label="$t('payment.emi.account')" align="center" prop="account" />
      <el-table-column :label="$t('payment.emi.merchantType')" align="center" prop="merchantType">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.merchantType === 1" type="primary">{{ $t('payment.emi.personal') }}</el-tag>
          <el-tag v-else-if="scope.row.merchantType === 2" type="success">{{ $t('payment.emi.company') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.emi.status')" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="info">{{ $t('payment.emi.statusPendingSubmit') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="warning">{{ $t('payment.emi.statusPendingReview') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="success">
            {{ $t('payment.emi.statusPendingBinnerReview') }}
          </el-tag>
          <el-tag v-else-if="scope.row.status === 3" type="success">{{ $t('payment.emi.statusApproved') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 4" type="danger">{{ $t('payment.emi.statusRejected') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.emi.enabledStatus')" align="center" prop="enabledStatus">
        <template slot-scope="scope">
          <el-switch
            :value="scope.row.enabledStatus === 0"
            :active-text="$t('payment.emi.enabled')"
            :inactive-text="$t('payment.emi.disabled')"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row, $event)"
          />
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.emi.createdAt')" align="center" prop="createdAt">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.emi.operation')"
        align="center"
        class-name="small-padding fixed-width"
        width="180"
      >
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">
            {{ $t('payment.emi.detail') }}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">
            {{ $t('payment.emi.delete') }}
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
  </div>
</template>

<script>
import { emiCompanyDelete, emiCompanyEnable, emiUserDelete, emiUserEnable, emiUserList } from '@/api/payment/emi'

export default {
  name: 'Emi',
  components: {},
  data() {
    return {
      // 遮罩层
      loading: false,
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
      // EMI商户表格数据
      emiList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 详情表单参数
      detailForm: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        status: undefined,
        enabledStatus: undefined,
        merchantType: undefined,
        createdAt: undefined
      },
      // 表单参数
      form: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询EMI商户列表 */
    getList() {
      this.loading = true
      emiUserList(this.queryParams)
        .then(response => {
          this.emiList = response.rows
          this.total = response.total
          this.loading = false
        })
        .catch(() => {
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
        name: undefined,
        merchantType: undefined,
        status: undefined,
        enabledStatus: 1,
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
      this.title = this.$t('payment.emi.addTitle')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids[0]
      // 模拟获取详情
      const merchant = this.emiList.find(item => item.id === id)
      if (merchant) {
        this.form = { ...merchant }
        this.open = true
        this.title = this.$t('payment.emi.updateTitle')
      }
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      const id = row.id || this.ids[0]
      // 根据 merchantType 确定 type 参数
      const type = row.merchantType === 1 ? 'personal' : row.merchantType === 2 ? 'company' : ''
      // 跳转到详情页面
      this.$router.push({
        path: '/emi/emiInfo',
        query: {
          id: id,
          type: type
        }
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            // 模拟修改
            this.msgSuccess(this.$t('payment.emi.updateSuccess'))
            this.open = false
            this.getList()
          } else {
            // 模拟新增
            this.msgSuccess(this.$t('payment.emi.addSuccess'))
            this.open = false
            this.getList()
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$confirm(this.$t('payment.emi.deleteConfirm', { ids: ids }), this.$t('payment.emi.warning'), {
        confirmButtonText: this.$t('payment.emi.confirm'),
        cancelButtonText: this.$t('payment.emi.cancel'),
        type: 'warning'
      })
        .then(() => {
          // 根据商户类型调用不同的删除接口
          const deleteApi = row.merchantType === 2 ? emiCompanyDelete(row.id) : emiUserDelete(row.id)
          return deleteApi
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.emi.deleteSuccess'))
        })
        .catch(() => {
          this.msgError(this.$t('payment.emi.deleteError'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$confirm(this.$t('payment.emi.exportConfirm'), this.$t('payment.emi.warning'), {
        confirmButtonText: this.$t('payment.emi.confirm'),
        cancelButtonText: this.$t('payment.emi.cancel'),
        type: 'warning'
      })
        .then(function () {
          // 模拟导出操作
          return Promise.resolve()
        })
        .then(() => {
          this.msgSuccess(this.$t('payment.emi.exportSuccess'))
        })
    },
    /** 启用状态变更 */
    handleStatusChange(row, newValue) {
      const status = newValue ? this.$t('payment.emi.enabled') : this.$t('payment.emi.disabled')
      this.$confirm(
        this.$t('payment.emi.statusChangeConfirm', { status: status, name: row.name }),
        this.$t('payment.emi.warning'),
        {
          confirmButtonText: this.$t('payment.emi.confirm'),
          cancelButtonText: this.$t('payment.emi.cancel'),
          type: 'warning'
        }
      )
        .then(() => {
          const enabledStatus = newValue ? 0 : 1
          const params = {
            id: row.id,
            enabledStatus: enabledStatus
          }

          // 根据商户类型调用不同的接口
          const apiCall = row.merchantType === 2 ? emiCompanyEnable(params) : emiUserEnable(params)

          return apiCall
            .then(() => {
              row.enabledStatus = enabledStatus
              const successMsg = newValue ? this.$t('payment.emi.enableSuccess') : this.$t('payment.emi.disableSuccess')
              this.msgSuccess(successMsg)
            })
            .catch(() => {
              const errorMsg = newValue ? this.$t('payment.emi.enableError') : this.$t('payment.emi.disableError')
              this.msgError(errorMsg)
              this.$forceUpdate()
            })
        })
        .catch(() => {
          // 取消时不做任何操作，因为使用的是 :value 而不是 v-model
          this.$forceUpdate()
        })
    }
  }
}
</script>
