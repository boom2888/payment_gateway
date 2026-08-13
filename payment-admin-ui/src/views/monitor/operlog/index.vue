<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item :label="$t('monitor.operlog.title')" prop="title">
        <el-input
          v-model="queryParams.title"
          :placeholder="$t('monitor.operlog.titlePlaceholder')"
          clearable
          style="width: 240px"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('monitor.operlog.operName')" prop="operName">
        <el-input
          v-model="queryParams.operName"
          :placeholder="$t('monitor.operlog.operNamePlaceholder')"
          clearable
          style="width: 240px"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('monitor.operlog.businessType')" prop="businessType">
        <el-select
          v-model="queryParams.businessType"
          :placeholder="$t('monitor.operlog.businessTypePlaceholder')"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('monitor.operlog.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('monitor.operlog.statusPlaceholder')"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('monitor.operlog.operTime')">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          :start-placeholder="$t('monitor.operlog.startDate')"
          :end-placeholder="$t('monitor.operlog.endDate')"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('monitor.operlog.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('monitor.operlog.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['monitor:operlog:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >
          {{ $t('monitor.operlog.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['monitor:operlog:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
        >
          {{ $t('monitor.operlog.clean') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:config:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >
          {{ $t('monitor.operlog.export') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('monitor.operlog.operId')" align="center" prop="operId" />
      <el-table-column :label="$t('monitor.operlog.tableTitle')" align="center" prop="title" />
      <el-table-column
        :label="$t('monitor.operlog.tableBusinessType')"
        align="center"
        prop="businessType"
        :formatter="typeFormat"
      />
      <el-table-column :label="$t('monitor.operlog.requestMethod')" align="center" prop="requestMethod" />
      <el-table-column :label="$t('monitor.operlog.tableOperName')" align="center" prop="operName" />
      <el-table-column
        :label="$t('monitor.operlog.operIp')"
        align="center"
        prop="operIp"
        width="130"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="$t('monitor.operlog.operLocation')"
        align="center"
        prop="operLocation"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="$t('monitor.operlog.tableStatus')"
        align="center"
        prop="status"
        :formatter="statusFormat"
      />
      <el-table-column :label="$t('monitor.operlog.tableOperTime')" align="center" prop="operTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('monitor.operlog.operations')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['monitor:operlog:query']"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row, scope.index)"
          >
            {{ $t('monitor.operlog.detail') }}
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

    <!-- 操作日志详细 -->
    <el-dialog :title="$t('monitor.operlog.detailDialogTitle')" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('monitor.operlog.detailModule')">
              {{ form.title }} / {{ typeFormat(form) }}
            </el-form-item>
            <el-form-item :label="$t('monitor.operlog.detailLogin')">
              {{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('monitor.operlog.detailOperUrl')">{{ form.operUrl }}</el-form-item>
            <el-form-item :label="$t('monitor.operlog.detailRequestMethod')">{{ form.requestMethod }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('monitor.operlog.detailMethod')">{{ form.method }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('monitor.operlog.detailOperParam')">{{ form.operParam }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('monitor.operlog.detailJsonResult')">{{ form.jsonResult }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('monitor.operlog.detailStatus')">
              <div v-if="form.status === 0">{{ $t('monitor.operlog.detailStatusNormal') }}</div>
              <div v-else-if="form.status === 1">{{ $t('monitor.operlog.detailStatusFail') }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('monitor.operlog.detailOperTime')">{{ parseTime(form.operTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.status === 1" :label="$t('monitor.operlog.detailErrorMsg')">
              {{ form.errorMsg }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">{{ $t('monitor.operlog.close') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { cleanOperlog, delOperlog, exportOperlog, list } from '@/api/monitor/operlog'

export default {
  name: 'Operlog',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      typeOptions: [],
      // 类型数据字典
      statusOptions: [],
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        operName: undefined,
        businessType: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_oper_type').then(response => {
      this.typeOptions = response.data
    })
    this.getDicts('sys_common_status').then(response => {
      this.statusOptions = response.data
    })
  },
  methods: {
    /** 查询登录日志 */
    getList() {
      this.loading = true
      list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 操作日志状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 操作日志类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.businessType)
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.operId)
      this.multiple = !selection.length
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true
      this.form = row
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const operIds = row.operId || this.ids
      this.$confirm(this.$t('monitor.operlog.deleteConfirm', { ids: operIds }), this.$t('monitor.operlog.warning'), {
        confirmButtonText: this.$t('monitor.operlog.confirm'),
        cancelButtonText: this.$t('monitor.operlog.cancel'),
        type: 'warning'
      })
        .then(function () {
          return delOperlog(operIds)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('monitor.operlog.deleteSuccess'))
        })
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$confirm(this.$t('monitor.operlog.cleanConfirm'), this.$t('monitor.operlog.warning'), {
        confirmButtonText: this.$t('monitor.operlog.confirm'),
        cancelButtonText: this.$t('monitor.operlog.cancel'),
        type: 'warning'
      })
        .then(function () {
          return cleanOperlog()
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('monitor.operlog.cleanSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('monitor.operlog.exportConfirm'), this.$t('monitor.operlog.warning'), {
        confirmButtonText: this.$t('monitor.operlog.confirm'),
        cancelButtonText: this.$t('monitor.operlog.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportOperlog(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
