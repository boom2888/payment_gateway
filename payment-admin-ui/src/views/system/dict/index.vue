<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="100px">
      <el-form-item :label="$t('system.dict.dictName')" prop="dictName">
        <el-input
          v-model="queryParams.dictName"
          :placeholder="$t('system.dict.dictNamePlaceholder')"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('system.dict.dictType')" prop="dictType">
        <el-input
          v-model="queryParams.dictType"
          :placeholder="$t('system.dict.dictTypePlaceholder')"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('system.dict.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('system.dict.dictStatus')"
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
      <el-form-item :label="$t('system.dict.createTime')">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          :start-placeholder="$t('system.dict.startDate')"
          :end-placeholder="$t('system.dict.endDate')"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('system.dict.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('system.dict.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:dict:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >
          {{ $t('system.dict.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:dict:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >
          {{ $t('system.dict.edit') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:dict:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >
          {{ $t('system.dict.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:dict:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >
          {{ $t('system.dict.export') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:dict:remove']"
          type="danger"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleClearCache"
        >
          {{ $t('system.dict.clearCache') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="typeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('system.dict.dictId')" align="center" prop="dictId" />
      <el-table-column
        :label="$t('system.dict.dictName')"
        align="center"
        prop="dictName"
        :show-overflow-tooltip="true"
      />
      <el-table-column :label="$t('system.dict.dictType')" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <router-link :to="'/dict/type/data/' + scope.row.dictId" class="link-type">
            <span>{{ scope.row.dictType }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.dict.status')" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column :label="$t('system.dict.remark')" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('system.dict.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.dict.operations')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:dict:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >
            {{ $t('system.dict.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['system:dict:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >
            {{ $t('system.dict.delete') }}
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

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$t('system.dict.dictName')" prop="dictName">
          <el-input v-model="form.dictName" :placeholder="$t('system.dict.dictNamePlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('system.dict.dictType')" prop="dictType">
          <el-input v-model="form.dictType" :placeholder="$t('system.dict.dictTypePlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('system.dict.status')" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictValue">
              {{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('system.dict.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" :placeholder="$t('system.dict.remarkPlaceholder')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('system.dict.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('system.dict.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addType, clearCache, delType, exportType, getType, listType, updateType } from '@/api/system/dict/type'

export default {
  name: 'Dict',
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
      // 字典表格数据
      typeList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dictName: undefined,
        dictType: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        dictName: [{ required: true, message: this.$t('system.dict.dictNameRequired'), trigger: 'blur' }],
        dictType: [{ required: true, message: this.$t('system.dict.dictTypeRequired'), trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data()
    })
  },
  methods: {
    /** 查询字典类型列表 */
    getList() {
      this.loading = true
      listType(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.typeList = [...response.rows]
        console.log('typeList', JSON.stringify(this.typeList))
        this.total = response.total
        this.loading = false
      })
    },
    // 字典状态字典翻译
    statusFormat(row, column) {
      console.log('this.statusOptions---->', this.statusOptions)
      console.log('row.status', row.status)
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        dictId: undefined,
        dictName: undefined,
        dictType: undefined,
        status: '0',
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
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = this.$t('system.dict.addDialogTitle')
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dictId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const dictId = row.dictId || this.ids
      getType(dictId).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('system.dict.editDialogTitle')
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.dictId != undefined) {
            updateType(this.form).then(response => {
              this.msgSuccess(this.$t('system.dict.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addType(this.form).then(response => {
              this.msgSuccess(this.$t('system.dict.addSuccess'))
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const dictIds = row.dictId || this.ids
      this.$confirm(this.$t('system.dict.deleteConfirm', { ids: dictIds }), this.$t('system.dict.warning'), {
        confirmButtonText: this.$t('system.dict.confirm'),
        cancelButtonText: this.$t('system.dict.cancel'),
        type: 'warning'
      })
        .then(function () {
          return delType(dictIds)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('system.dict.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('system.dict.exportConfirm'), this.$t('system.dict.warning'), {
        confirmButtonText: this.$t('system.dict.confirm'),
        cancelButtonText: this.$t('system.dict.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportType(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    },
    /** 清理缓存按钮操作 */
    handleClearCache() {
      clearCache().then(response => {
        this.msgSuccess(this.$t('system.dict.clearCacheSuccess'))
      })
    }
  }
}
</script>
