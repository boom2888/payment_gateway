<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('system.dict.dictName')" prop="dictType">
        <el-select v-model="queryParams.dictType" size="small">
          <el-option v-for="item in typeOptions" :key="item.dictId" :label="item.dictName" :value="item.dictType" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('system.dict.dictLabel')" prop="dictLabel">
        <el-input
          v-model="queryParams.dictLabel"
          :placeholder="$t('system.dict.dictLabelPlaceholder')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('system.dict.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('system.dict.dataStatusPlaceholder')" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('system.dict.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('system.dict.reset') }}</el-button>
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
          v-hasPermi="['system:dict:add']"
        >
          {{ $t('system.dict.add') }}
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
          v-hasPermi="['system:dict:edit']"
        >
          {{ $t('system.dict.edit') }}
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
          v-hasPermi="['system:dict:remove']"
        >
          {{ $t('system.dict.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:dict:export']"
        >
          {{ $t('system.dict.export') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('system.dict.dictCode')" align="center" prop="dictCode" />
      <el-table-column :label="$t('system.dict.dictLabel')" align="center" prop="dictLabel" />
      <el-table-column :label="$t('system.dict.dictValue')" align="center" prop="dictValue" />
      <el-table-column :label="$t('system.dict.dictSort')" align="center" prop="dictSort" />
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
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dict:edit']"
          >
            {{ $t('system.dict.edit') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dict:remove']"
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
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('system.dict.dictType')">
          <el-input v-model="form.dictType" :disabled="true" />
        </el-form-item>
        <el-form-item :label="$t('system.dict.dictLabel')" prop="dictLabel">
          <el-input v-model="form.dictLabel" :placeholder="$t('system.dict.dictLabelPlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('system.dict.dictLabelZh')" prop="dictLabelZh">
          <el-input v-model="form.dictLabelZh" :placeholder="$t('system.dict.dictLabelZhPlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('system.dict.dictValue')" prop="dictValue">
          <el-input v-model="form.dictValue" :placeholder="$t('system.dict.dictValuePlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('system.dict.dictSort')" prop="dictSort">
          <el-input-number v-model="form.dictSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item :label="$t('system.dict.status')" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictValue">
              {{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('system.dict.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" :placeholder="$t('system.dict.remarkPlaceholder')"></el-input>
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
import { addData, delData, exportData, getData, listData, updateData } from '@/api/system/dict/data'
import { getType, listType } from '@/api/system/dict/type'

export default {
  name: 'Data',
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
      dataList: [],
      // 默认字典类型
      defaultDictType: '',
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 类型数据字典
      typeOptions: [],
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
        dictLabel: [{ required: true, message: this.$t('system.dict.dictLabelRequired'), trigger: 'blur' }],
        dictValue: [{ required: true, message: this.$t('system.dict.dictValueRequired'), trigger: 'blur' }],
        dictSort: [{ required: true, message: this.$t('system.dict.dictSortRequired'), trigger: 'blur' }]
      }
    }
  },
  created() {
    const dictId = this.$route.params && this.$route.params.dictId
    this.getType(dictId)
    this.getTypeList()
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
  },
  methods: {
    /** 查询字典类型详细 */
    getType(dictId) {
      getType(dictId).then(response => {
        this.queryParams.dictType = response.data.dictType
        this.defaultDictType = response.data.dictType
        this.getList()
      })
    },
    /** 查询字典类型列表 */
    getTypeList() {
      listType().then(response => {
        this.typeOptions = response.rows
      })
    },
    /** 查询字典数据列表 */
    getList() {
      this.loading = true
      listData(this.queryParams).then(response => {
        this.dataList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 数据状态字典翻译
    statusFormat(row, column) {
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
        dictCode: undefined,
        dictLabel: undefined,
        dictLabelZh: undefined,
        dictValue: undefined,
        dictSort: 0,
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
      this.resetForm('queryForm')
      this.queryParams.dictType = this.defaultDictType
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = this.$t('system.dict.addDataDialogTitle')
      this.form.dictType = this.queryParams.dictType
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dictCode)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const dictCode = row.dictCode || this.ids
      getData(dictCode).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('system.dict.editDataDialogTitle')
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.dictCode != undefined) {
            updateData(this.form).then(response => {
              this.msgSuccess(this.$t('system.dict.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addData(this.form).then(response => {
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
      const dictCodes = row.dictCode || this.ids
      this.$confirm(this.$t('system.dict.deleteDataConfirm', { dictCodes }), this.$t('system.dict.warning'), {
        confirmButtonText: this.$t('system.dict.confirm'),
        cancelButtonText: this.$t('system.dict.cancel'),
        type: 'warning'
      })
        .then(function () {
          return delData(dictCodes)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('system.dict.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('system.dict.exportDataConfirm'), this.$t('system.dict.warning'), {
        confirmButtonText: this.$t('system.dict.confirm'),
        cancelButtonText: this.$t('system.dict.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportData(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
