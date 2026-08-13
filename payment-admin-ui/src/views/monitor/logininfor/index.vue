<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item :label="$t('monitor.logininfor.ipaddr')" prop="ipaddr">
        <el-input
          v-model="queryParams.ipaddr"
          :placeholder="$t('monitor.logininfor.ipaddrPlaceholder')"
          clearable
          style="width: 240px"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('monitor.logininfor.userName')" prop="userName">
        <el-input
          v-model="queryParams.userName"
          :placeholder="$t('monitor.logininfor.userNamePlaceholder')"
          clearable
          style="width: 240px"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('monitor.logininfor.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('monitor.logininfor.statusPlaceholder')"
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
      <el-form-item :label="$t('monitor.logininfor.loginTime')">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          :start-placeholder="$t('monitor.logininfor.startDate')"
          :end-placeholder="$t('monitor.logininfor.endDate')"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('monitor.logininfor.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('monitor.logininfor.reset') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['monitor:logininfor:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >
          {{ $t('monitor.logininfor.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['monitor:logininfor:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
        >
          {{ $t('monitor.logininfor.clean') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:logininfor:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >
          {{ $t('monitor.logininfor.export') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('monitor.logininfor.infoId')" align="center" prop="infoId" />
      <el-table-column :label="$t('monitor.logininfor.tableUserName')" align="center" prop="userName" />
      <el-table-column
        :label="$t('monitor.logininfor.tableIpaddr')"
        align="center"
        prop="ipaddr"
        width="130"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="$t('monitor.logininfor.loginLocation')"
        align="center"
        prop="loginLocation"
        :show-overflow-tooltip="true"
      />
      <el-table-column :label="$t('monitor.logininfor.browser')" align="center" prop="browser" />
      <el-table-column :label="$t('monitor.logininfor.os')" align="center" prop="os" />
      <el-table-column
        :label="$t('monitor.logininfor.tableStatus')"
        align="center"
        prop="status"
        :formatter="statusFormat"
      />
      <el-table-column :label="$t('monitor.logininfor.msg')" align="center" prop="msg" />
      <el-table-column :label="$t('monitor.logininfor.tableLoginTime')" align="center" prop="loginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
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
import { cleanLogininfor, delLogininfor, exportLogininfor, list } from '@/api/monitor/logininfor'

export default {
  name: 'Logininfor',
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
      // 状态数据字典
      statusOptions: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ipaddr: undefined,
        userName: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_common_status').then(response => {
      this.statusOptions = response.data
    })
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true
      list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 登录状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
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
      this.ids = selection.map(item => item.infoId)
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const infoIds = row.infoId || this.ids
      this.$confirm(
        this.$t('monitor.logininfor.deleteConfirm', { ids: infoIds }),
        this.$t('monitor.logininfor.warning'),
        {
          confirmButtonText: this.$t('monitor.logininfor.confirm'),
          cancelButtonText: this.$t('monitor.logininfor.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return delLogininfor(infoIds)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('monitor.logininfor.deleteSuccess'))
        })
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$confirm(this.$t('monitor.logininfor.cleanConfirm'), this.$t('monitor.logininfor.warning'), {
        confirmButtonText: this.$t('monitor.logininfor.confirm'),
        cancelButtonText: this.$t('monitor.logininfor.cancel'),
        type: 'warning'
      })
        .then(function () {
          return cleanLogininfor()
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('monitor.logininfor.cleanSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('monitor.logininfor.exportConfirm'), this.$t('monitor.logininfor.warning'), {
        confirmButtonText: this.$t('monitor.logininfor.confirm'),
        cancelButtonText: this.$t('monitor.logininfor.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportLogininfor(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
