<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item :label="$t('system.config.configName')" prop="configName">
        <el-input
          v-model="queryParams.configName"
          :placeholder="$t('system.config.configNamePlaceholder')"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('system.config.configKey')" prop="configKey">
        <el-input
          v-model="queryParams.configKey"
          :placeholder="$t('system.config.configKeyPlaceholder')"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item :label="$t('system.config.configType')" prop="configType">-->
<!--        <el-select-->
<!--          v-model="queryParams.configType"-->
<!--          :placeholder="$t('system.config.configType')"-->
<!--          clearable-->
<!--          size="small"-->
<!--        >-->
<!--          <el-option-->
<!--            v-for="dict in typeOptions"-->
<!--            :key="dict.dictValue"-->
<!--            :label="dict.dictLabel"-->
<!--            :value="dict.dictValue"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item :label="$t('system.config.createTime')">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          :start-placeholder="$t('system.config.startDate')"
          :end-placeholder="$t('system.config.endDate')"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('system.config.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('system.config.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:config:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >
          {{ $t('system.config.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:config:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >
          {{ $t('system.config.edit') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:config:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >
          {{ $t('system.config.delete') }}
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
          {{ $t('system.config.export') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:config:remove']"
          type="danger"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleClearCache"
        >
          {{ $t('system.config.clearCache') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('system.config.configId')" align="center" prop="configId" />
      <el-table-column
        :label="$t('system.config.configName')"
        align="center"
        prop="configName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="$t('system.config.configKey')"
        align="center"
        prop="configKey"
        :show-overflow-tooltip="true"
      />
      <el-table-column :label="$t('system.config.configValue')" align="center" prop="configValue" />
      <el-table-column
        :label="$t('system.config.configType')"
        align="center"
        prop="configType"
        :formatter="typeFormat"
      />
      <el-table-column :label="$t('system.config.remark')" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('system.config.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.config.operations')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:config:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >
            {{ $t('system.config.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['system:config:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >
            {{ $t('system.config.delete') }}
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
        <el-form-item :label="$t('system.config.configName')" prop="configName">
          <el-input v-model="form.configName" :placeholder="$t('system.config.configNamePlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('system.config.configKey')" prop="configKey">
          <el-input v-model="form.configKey" :placeholder="$t('system.config.configKeyPlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('system.config.configValue')" prop="configValue">
          <div v-if="form.valueType === 'SELECT'">
            <el-select
              v-model="form.configValue"
              :placeholder="$t('system.config.selectPlaceholder')"
              filterable
              allow-create
              style="width: 100%"
            >
              <el-option
                v-for="option in selectOptions"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
            <div style="margin-top: 8px">
              <el-button type="text" size="mini" icon="el-icon-setting" @click="showOptionManager = true">
                {{ $t('system.config.manageOptions') }}
              </el-button>
            </div>
          </div>
          <el-input v-else v-model="form.configValue" :placeholder="$t('system.config.configValuePlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('system.config.valueType')" prop="valueType">
          <el-radio-group v-model="form.valueType" @change="handleValueTypeChange">
            <el-radio label="INPUT">{{ $t('system.config.inputType') }}</el-radio>
            <el-radio label="SELECT">{{ $t('system.config.selectType') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- configType 固定为 'N'（非系统内置），不再显示选择框 -->
        <!-- <el-form-item :label="$t('system.config.configType')" prop="configType">
          <el-radio-group v-model="form.configType">
            <el-radio v-for="dict in typeOptions" :key="dict.dictValue" :label="dict.dictValue">
              {{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item> -->
        <el-form-item :label="$t('system.config.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" :placeholder="$t('system.config.remarkPlaceholder')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('system.config.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('system.config.cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 选项管理对话框 -->
    <el-dialog
      :title="$t('system.config.optionManagerTitle')"
      :visible.sync="showOptionManager"
      width="600px"
      append-to-body
    >
      <div>
        <div style="margin-bottom: 16px">
          <el-button type="primary" size="small" @click="addOption">{{ $t('system.config.addOption') }}</el-button>
        </div>
        <el-table :data="selectOptions" border>
          <el-table-column :label="$t('system.config.displayLabel')" width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.label" size="mini" @blur="updateOption(scope.$index)" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('system.config.optionValue')" width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.value" size="mini" @blur="updateOption(scope.$index)" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('system.config.operations')" width="100">
            <template slot-scope="scope">
              <el-button type="text" size="mini" icon="el-icon-delete" @click="removeOption(scope.$index)">
                {{ $t('system.config.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveOptions">{{ $t('system.config.saveOptions') }}</el-button>
        <el-button @click="showOptionManager = false">{{ $t('system.config.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addConfig,
  clearCache,
  delConfig,
  exportConfig,
  getConfig,
  listConfig,
  updateConfig
} from '@/api/system/config'

export default {
  name: 'Config',
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
      // 参数表格数据
      configList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      typeOptions: [],
      // 是否显示选项管理对话框
      showOptionManager: false,
      // 下拉选项数据
      selectOptions: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        configName: undefined,
        configKey: undefined,
        configType: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        configName: [{ required: true, message: this.$t('system.config.configNameRequired'), trigger: 'blur' }],
        configKey: [{ required: true, message: this.$t('system.config.configKeyRequired'), trigger: 'blur' }],
        configValue: [{ required: true, message: this.$t('system.config.configValueRequired'), trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_yes_no').then(response => {
      this.typeOptions = response.data
    })
  },
  methods: {
    /** 查询参数列表 */
    getList() {
      this.loading = true
      listConfig(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.configList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 参数系统内置字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.configType)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        valueType: 'INPUT',
        selectOptions: undefined,
        remark: undefined
      }
      this.selectOptions = []
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
      this.title = this.$t('system.config.addDialogTitle')
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.configId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const configId = row.configId || this.ids
      getConfig(configId).then(response => {
        this.form = response.data

        // 确保 valueType 字段存在，如果不存在则默认为 INPUT
        if (!this.form.valueType) {
          this.form.valueType = 'INPUT'
        }

        // 初始化下拉选项
        if (this.form.valueType === 'SELECT') {
          if (this.form.selectOptions) {
            try {
              this.selectOptions = JSON.parse(this.form.selectOptions)
            } catch (e) {
              console.warn('解析下拉选项失败:', e)
              this.selectOptions = []
            }
          }
          // 如果没有选项，提供默认选项
          if (this.selectOptions.length === 0) {
            this.selectOptions = [
              { label: this.$t('system.config.defaultOptionEnable'), value: 'Y' },
              { label: this.$t('system.config.defaultOptionDisable'), value: 'N' }
            ]
          }
        } else {
          this.selectOptions = []
        }

        this.open = true
        this.title = this.$t('system.config.editDialogTitle')
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          // 确保 valueType 字段存在
          if (!this.form.valueType) {
            this.form.valueType = 'INPUT'
          }

          // 处理下拉选项数据
          if (this.form.valueType === 'SELECT') {
            // 验证选项数据
            if (this.selectOptions.length === 0) {
              this.$message.error(this.$t('system.config.optionRequired'))
              return
            }
            this.form.selectOptions = JSON.stringify(this.selectOptions)
          } else {
            this.form.selectOptions = null
          }

          console.log('提交的表单数据:', this.form)

          if (this.form.configId != undefined) {
            updateConfig(this.form)
              .then(response => {
                this.msgSuccess(this.$t('system.config.editSuccess'))
                this.open = false
                this.getList()
              })
              .catch(error => {
                console.error('更新配置失败:', error)
                this.$message.error(this.$t('system.config.updateFailed'))
              })
          } else {
            addConfig(this.form)
              .then(response => {
                this.msgSuccess(this.$t('system.config.addSuccess'))
                this.open = false
                this.getList()
              })
              .catch(error => {
                console.error('添加配置失败:', error)
                this.$message.error(this.$t('system.config.addFailed'))
              })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const configIds = row.configId || this.ids
      this.$confirm(this.$t('system.config.deleteConfirm', { ids: configIds }), this.$t('system.config.warning'), {
        confirmButtonText: this.$t('system.config.confirm'),
        cancelButtonText: this.$t('system.config.cancel'),
        type: 'warning'
      })
        .then(function () {
          return delConfig(configIds)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('system.config.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('system.config.exportConfirm'), this.$t('system.config.warning'), {
        confirmButtonText: this.$t('system.config.confirm'),
        cancelButtonText: this.$t('system.config.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportConfig(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    },
    /** 清理缓存按钮操作 */
    handleClearCache() {
      clearCache().then(response => {
        this.msgSuccess(this.$t('system.config.clearCacheSuccess'))
      })
    },
    /** 值类型变化处理 */
    handleValueTypeChange(value) {
      console.log('值类型变化:', value)
      if (value === 'SELECT') {
        // 切换到下拉选择时，初始化选项
        if (this.selectOptions.length === 0) {
          this.selectOptions = [
            { label: this.$t('system.config.defaultOptionEnable'), value: 'Y' },
            { label: this.$t('system.config.defaultOptionDisable'), value: 'N' }
          ]
        }
        console.log('初始化下拉选项:', this.selectOptions)
      } else {
        // 切换到输入框时，清空选项
        this.selectOptions = []
        this.form.configValue = ''
      }
    },
    /** 添加选项 */
    addOption() {
      this.selectOptions.push({
        label: `${this.$t('system.config.newOption')}${this.selectOptions.length + 1}`,
        value: `new_option_${this.selectOptions.length + 1}`
      })
    },
    /** 删除选项 */
    removeOption(index) {
      this.selectOptions.splice(index, 1)
    },
    /** 更新选项 */
    updateOption(index) {
      // 这里可以添加验证逻辑
      console.log('更新选项:', index, this.selectOptions[index])
    },
    /** 保存选项 */
    saveOptions() {
      // 验证选项数据
      for (let i = 0; i < this.selectOptions.length; i++) {
        const option = this.selectOptions[i]
        if (!option.label || !option.value) {
          this.$message.error(this.$t('system.config.optionLabelValueRequired', { index: i + 1 }))
          return
        }
      }

      // 检查重复值
      const values = this.selectOptions.map(option => option.value)
      const uniqueValues = [...new Set(values)]
      if (values.length !== uniqueValues.length) {
        this.$message.error(this.$t('system.config.optionValueDuplicate'))
        return
      }

      this.showOptionManager = false
      this.$message.success(this.$t('system.config.optionSaveSuccess'))
    }
  }
}
</script>
