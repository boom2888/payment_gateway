<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="90px">
      <el-form-item :label="$t('monitor.online.ipaddr')" prop="ipaddr">
        <el-input
          v-model="queryParams.ipaddr"
          :placeholder="$t('monitor.online.placeholderIp')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('monitor.online.userName')" prop="userName">
        <el-input
          v-model="queryParams.userName"
          :placeholder="$t('monitor.online.placeholderUser')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('monitor.online.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('monitor.online.reset') }}</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="list.slice((pageNum - 1) * pageSize, pageNum * pageSize)" style="width: 100%">
      <el-table-column :label="$t('monitor.online.index')" type="index" align="center" width="160">
        <template slot-scope="scope">
          <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('monitor.online.tokenId')"
        align="center"
        prop="tokenId"
        :show-overflow-tooltip="true"
        width="220"
      />
      <el-table-column
        :label="$t('monitor.online.userName')"
        align="center"
        prop="userName"
        :show-overflow-tooltip="true"
        width="180px"
      />
      <el-table-column :label="$t('monitor.online.deptName')" align="center" prop="deptName" width="180px" />
      <el-table-column
        :label="$t('monitor.online.ipaddr')"
        align="center"
        prop="ipaddr"
        :show-overflow-tooltip="true"
        width="150px"
      />
      <el-table-column
        :label="$t('monitor.online.loginLocation')"
        align="center"
        prop="loginLocation"
        :show-overflow-tooltip="true"
        width="200px"
      />
      <el-table-column :label="$t('monitor.online.browser')" align="center" prop="browser" width="180px" />
      <el-table-column :label="$t('monitor.online.os')" align="center" prop="os" width="180px" />
      <el-table-column :label="$t('monitor.online.loginTime')" align="center" prop="loginTime" width="200px">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('monitor.online.action')"
        align="center"
        class-name="small-padding fixed-width"
        width="200px"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['monitor:online:forceLogout']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleForceLogout(scope.row)"
          >
            {{ $t('monitor.online.forceLogout') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" />
  </div>
</template>

<script>
import { forceLogout, list } from '@/api/monitor/online'

export default {
  name: 'Online',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      pageNum: 1,
      pageSize: 10,
      // 查询参数
      queryParams: {
        ipaddr: undefined,
        userName: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true
      list(this.queryParams).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 强退按钮操作 */
    handleForceLogout(row) {
      this.$confirm(
        this.$t('monitor.online.confirmContent', { userName: row.userName }),
        this.$t('monitor.online.confirmTitle'),
        {
          confirmButtonText: this.$t('monitor.online.confirmButton'),
          cancelButtonText: this.$t('monitor.online.cancelButton'),
          type: 'warning'
        }
      )
        .then(function () {
          return forceLogout(row.tokenId)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('monitor.online.successMsg'))
        })
    }
  }
}
</script>
