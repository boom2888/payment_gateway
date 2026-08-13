<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="98px">
      <el-form-item :label="$t('payment.limit.merchantId')" prop="shopId">
        <el-select filterable v-model="queryParams.shopId" :placeholder="$t('payment.limit.selectMerchantId')" clearable
          size="small">
          <el-option v-for="dict in shopIdOptions" :key="dict.dictValue" :label="dict.dictLabel"
            :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.limit.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('payment.limit.selectStatus')" clearable size="small">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel"
            :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.limit.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('payment.limit.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['payment:limit:add']">
          {{ $t('payment.limit.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['payment:limit:edit']">
          {{ $t('payment.limit.edit') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['payment:limit:remove']">
          {{ $t('payment.limit.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['payment:limit:export']">
          {{ $t('payment.limit.export') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="limitList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="" align="center" prop="id" v-if="false" />
      <el-table-column :label="$t('payment.limit.merchantId')" align="center" prop="shopId" :formatter="shopIdFormat" />
      <el-table-column :label="$t('payment.limit.mondayLimit')" align="center" prop="monday" />
      <el-table-column :label="$t('payment.limit.tuesdayLimit')" align="center" prop="tuesday" />
      <el-table-column :label="$t('payment.limit.wednesdayLimit')" align="center" prop="wednesday" />
      <el-table-column :label="$t('payment.limit.thursdayLimit')" align="center" prop="thursday" />
      <el-table-column :label="$t('payment.limit.fridayLimit')" align="center" prop="friday" />
      <el-table-column :label="$t('payment.limit.saturdayLimit')" align="center" prop="saturday" />
      <el-table-column :label="$t('payment.limit.sundayLimit')" align="center" prop="sunday" />
      <el-table-column :label="$t('payment.limit.currentValue')" align="center" prop="value" />
      <el-table-column :label="$t('payment.limit.status')" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column :label="$t('payment.limit.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.limit.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:limit:edit']">
            {{ $t('payment.limit.edit') }}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['payment:limit:remove']">
            {{ $t('payment.limit.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改商户限额对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.limit.merchantId')" prop="shopId">
              <el-select filterable v-model="form.shopId" :placeholder="$t('payment.limit.selectMerchantId')">
                <el-option v-for="dict in shopIdOptions" :key="dict.dictValue" :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.limit.mondayLimit')" prop="monday">
              <el-input v-model="form.monday" :placeholder="$t('payment.limit.inputMondayLimit')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.limit.tuesdayLimit')" prop="tuesday">
              <el-input v-model="form.tuesday" :placeholder="$t('payment.limit.inputTuesdayLimit')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.limit.wednesdayLimit')" prop="wednesday">
              <el-input v-model="form.wednesday" :placeholder="$t('payment.limit.inputWednesdayLimit')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.limit.thursdayLimit')" prop="thursday">
              <el-input v-model="form.thursday" :placeholder="$t('payment.limit.inputThursdayLimit')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.limit.fridayLimit')" prop="friday">
              <el-input v-model="form.friday" :placeholder="$t('payment.limit.inputFridayLimit')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.limit.saturdayLimit')" prop="saturday">
              <el-input v-model="form.saturday" :placeholder="$t('payment.limit.inputSaturdayLimit')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.limit.sundayLimit')" prop="sunday">
              <el-input v-model="form.sunday" :placeholder="$t('payment.limit.inputSundayLimit')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.limit.currentValue')" prop="value">
              <el-input v-model="form.value" :placeholder="$t('payment.limit.inputCurrentValue')" disabled="true" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.limit.status')" prop="status">
              <el-select v-model="form.status" :placeholder="$t('payment.limit.selectStatus')">
                <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel"
                  :value="dict.dictValue"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.limit.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.limit.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addLimit, delLimit, exportLimit, getLimit, listLimit, updateLimit } from '@/api/payment/limit'

export default {
  name: 'Limit',
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
      // 商户限额表格数据
      limitList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否为编辑状态
      isEdit: true,
      // 商户编号字典
      shopIdOptions: [],
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shopId: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        shopId: [{ required: true, message: this.$t('payment.limit.merchantIdRequired'), trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.initDicts()
  },
  // 处理 keep-alive 或首次渲染时机差异，确保进页面就拉取字典
  activated() {
    if (!this.shopIdOptions || this.shopIdOptions.length === 0 || !this.statusOptions || this.statusOptions.length === 0) {
      this.initDicts()
    }
  },
  methods: {
    // 初始化字典数据
    initDicts() {
      Promise.all([
        this.getDicts('MERCHANT_ID'),
        this.getDicts('sys_yes_no')
      ]).then(([merchantRes, statusRes]) => {
        this.shopIdOptions = merchantRes?.data || []
        this.statusOptions = statusRes?.data || []
      }).catch(() => {
        // 忽略字典异常，避免阻断页面
      })
    },
    /** 查询商户限额列表 */
    getList() {
      this.loading = true
      listLimit(this.queryParams).then(response => {
        this.limitList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 商户编号字典翻译
    shopIdFormat(row, column) {
      return this.selectDictLabel(this.shopIdOptions, row.shopId)
    },
    // 状态字典翻译
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
        id: undefined,
        shopId: undefined,
        monday: undefined,
        tuesday: undefined,
        wednesday: undefined,
        thursday: undefined,
        friday: undefined,
        saturday: undefined,
        sunday: undefined,
        value: undefined,
        status: undefined,
        createTime: undefined,
        updateTime: undefined
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
      this.title = this.$t('payment.limit.addMerchantLimit')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLimit(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.limit.editMerchantLimit')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLimit(this.form).then(response => {
              this.msgSuccess(this.$t('payment.limit.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addLimit(this.form).then(response => {
              this.msgSuccess(this.$t('payment.limit.addSuccess'))
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
      this.$confirm(this.$t('payment.limit.deleteConfirm').replace('{{id}}', ids), this.$t('payment.limit.warning'), {
        confirmButtonText: this.$t('payment.limit.confirm'),
        cancelButtonText: this.$t('payment.limit.cancel'),
        type: 'warning'
      })
        .then(function () {
          return delLimit(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.limit.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('payment.limit.exportConfirm'), this.$t('payment.limit.warning'), {
        confirmButtonText: this.$t('payment.limit.confirm'),
        cancelButtonText: this.$t('payment.limit.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportLimit(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
