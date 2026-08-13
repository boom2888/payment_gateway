<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="128px">
      <el-form-item :label="$t('payment.apiLog.orderId')" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          :placeholder="$t('payment.apiLog.orderIdPlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.apiLog.ssoOrderId')" prop="ssoOrderId">
        <el-input
          v-model="queryParams.ssoOrderId"
          :placeholder="$t('payment.apiLog.ssoOrderIdPlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.apiLog.mobile')" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          :placeholder="$t('payment.apiLog.mobilePlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.apiLog.status')" prop="status">
        <el-input
          v-model="queryParams.status"
          :placeholder="$t('payment.apiLog.statusPlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.apiLog.location')" prop="location">
        <el-input
          v-model="queryParams.location"
          :placeholder="$t('payment.apiLog.locationPlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.apiLog.apiType')" prop="apiType">
        <el-select
          v-model="queryParams.apiType"
          :placeholder="$t('payment.apiLog.apiTypePlaceholder')"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in apiLogTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.apiLog.shopId')" prop="shopId">
        <el-select
          v-model="queryParams.shopId"
          :placeholder="$t('payment.apiLog.shopIdPlaceholder')"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in shopIdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.apiLog.param')" prop="param">
        <el-input
          v-model="queryParams.param"
          :placeholder="$t('payment.apiLog.paramPlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.apiLog.response')" prop="response">
        <el-input
          v-model="queryParams.response"
          :placeholder="$t('payment.apiLog.responsePlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.apiLog.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('payment.apiLog.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          plain-->
      <!--          icon="el-icon-plus"-->
      <!--          size="mini"-->
      <!--          @click="handleAdd"-->
      <!--          v-hasPermi="['payment:apiLog:add']"-->
      <!--        >新增</el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="success"-->
      <!--          plain-->
      <!--          icon="el-icon-edit"-->
      <!--          size="mini"-->
      <!--          :disabled="single"-->
      <!--          @click="handleUpdate"-->
      <!--          v-hasPermi="['payment:apiLog:edit']"-->
      <!--        >修改</el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="danger"-->
      <!--          plain-->
      <!--          icon="el-icon-delete"-->
      <!--          size="mini"-->
      <!--          :disabled="multiple"-->
      <!--          @click="handleDelete"-->
      <!--          v-hasPermi="['payment:apiLog:remove']"-->
      <!--        >删除</el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="warning"-->
      <!--          plain-->
      <!--          icon="el-icon-download"-->
      <!--          size="mini"-->
      <!--          @click="handleExport"-->
      <!--          v-hasPermi="['payment:apiLog:export']"-->
      <!--        >导出</el-button>-->
      <!--      </el-col>-->
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="apiLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column v-if="true" :label="$t('payment.apiLog.id')" align="center" prop="id" />
      <el-table-column :label="$t('payment.apiLog.tableOrderId')" align="center" prop="orderId" />
      <el-table-column :label="$t('payment.apiLog.tableSsoOrderId')" align="center" prop="ssoOrderId" min-width="150" />
      <el-table-column :label="$t('payment.apiLog.tableMobile')" align="center" prop="mobile" width="150" />
      <el-table-column :label="$t('payment.apiLog.tableParam')" align="center" prop="param" width="200">
        <template #default="{ row }">
          <div v-copy-hover class="ellipsis-text">{{ row.param }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.apiLog.tableResponse')" align="center" prop="response" width="200">
        <template #default="{ row }">
          <div v-copy-hover class="ellipsis-text">{{ row.response }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.apiLog.tableStatus')" align="center" prop="status" width="120" />
      <el-table-column :label="$t('payment.apiLog.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.apiLog.remark')" align="center" prop="remark" />
      <el-table-column :label="$t('payment.apiLog.tableLocation')" align="center" prop="location" width="120" />
      <el-table-column :label="$t('payment.apiLog.tableApiType')" align="center" prop="apiType" width="120" />
      <el-table-column
        :label="$t('payment.apiLog.tableShopId')"
        align="center"
        prop="shopId"
        :formatter="shopIdFormat"
        width="150"
      />
      <el-table-column :label="$t('payment.apiLog.operations')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!-- 操作按钮可按需国际化 -->
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

    <!-- 添加或修改接口日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.apiLog.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.apiLog.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addApiLog, delApiLog, exportApiLog, getApiLog, listApiLog, updateApiLog } from '@/api/payment/apiLog'

export default {
  name: 'ApiLog',
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
      // 接口日志表格数据
      apiLogList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 商户字典
      shopIdOptions: [],
      apiLogTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: undefined,
        ssoOrderId: undefined,
        mobile: undefined,
        status: undefined,
        location: undefined,
        apiType: undefined,
        shopId: undefined,
        param: undefined,
        response: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    }
  },
  created() {
    this.getList()
    this.getDicts('MERCHANT_ID').then(response => {
      this.shopIdOptions = response.data
    })
    this.getDicts('API_LOG_TYPE').then(response => {
      this.apiLogTypeOptions = response.data
    })
  },
  methods: {
    /** 查询接口日志列表 */
    getList() {
      this.loading = true
      listApiLog(this.queryParams).then(response => {
        this.apiLogList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 商户字典翻译
    shopIdFormat(row, column) {
      return this.selectDictLabel(this.shopIdOptions, row.shopId)
    },
    // 接口类型字典翻译
    apiLogTypeFormat(row, column) {
      return this.selectDictLabel(this.apiLogTypeOptions, row.apiType)
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
        orderId: undefined,
        mobile: undefined,
        param: undefined,
        response: undefined,
        status: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined,
        location: undefined,
        apiType: undefined,
        shopId: undefined
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
      this.title = this.$t('payment.apiLog.addDialogTitle')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getApiLog(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.apiLog.editDialogTitle')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateApiLog(this.form).then(response => {
              this.msgSuccess(this.$t('payment.apiLog.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addApiLog(this.form).then(response => {
              this.msgSuccess(this.$t('payment.apiLog.addSuccess'))
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
      this.$confirm(this.$t('payment.apiLog.deleteConfirm', { ids }), this.$t('payment.apiLog.warning'), {
        confirmButtonText: this.$t('payment.apiLog.confirm'),
        cancelButtonText: this.$t('payment.apiLog.cancel'),
        type: 'warning'
      })
        .then(function () {
          return delApiLog(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.apiLog.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('payment.apiLog.exportConfirm'), this.$t('payment.apiLog.warning'), {
        confirmButtonText: this.$t('payment.apiLog.confirm'),
        cancelButtonText: this.$t('payment.apiLog.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportApiLog(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
