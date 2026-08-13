<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item :label="t.fullNumber" prop="fullNumber" label-width="100px">
        <el-input
          v-model="queryParams.fullNumber"
          :placeholder="t.placeholderFullNumber"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t.binNumber" prop="binNumber" label-width="100px">
        <el-input v-model="queryParams.binNumber" :placeholder="t.placeholderBinNumber" clearable size="small" />
      </el-form-item>
      <el-form-item :label="t.lastFourNumber" prop="lastFourNumber" label-width="100px">
        <el-input
          v-model="queryParams.lastFourNumber"
          :placeholder="t.placeholderLastFourNumber"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item :label="t.riskLevel" prop="riskLevel" label-width="100px">
        <el-input v-model.number="queryParams.riskLevel" :placeholder="t.placeholderRiskLevel" clearable size="small" />
      </el-form-item>
      <el-form-item :label="t.shopId" prop="shopId" label-width="120px">
        <el-select v-model="queryParams.shopId" :placeholder="t.placeholderShopId" clearable size="small" filterable>
          <el-option
            v-for="dict in merchantIdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t.originName" prop="originName" label-width="100px">
        <el-input
          v-model="queryParams.originName"
          :placeholder="t.placeholderOriginName"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t.createdAt" prop="dateRange" label-width="100px">
        <el-date-picker
          v-model="queryParams.dateRange"
          clearable
          size="small"
          type="daterange"
          :range-separator="t.rangeSeparator"
          :start-placeholder="t.startPlaceholder"
          :end-placeholder="t.endPlaceholder"
          value-format="yyyy-MM-dd"
          @change="handleDateRangeChange"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ t.search }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ t.reset }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:whiteUploadRecord:import']"
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >
          {{ t.import }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <!--        <el-button-->
        <!--          type="primary"-->
        <!--          plain-->
        <!--          icon="el-icon-plus"-->
        <!--          size="mini"-->
        <!--          @click="handleAdd"-->
        <!--          v-hasPermi="['payment:whitelistedCreditCards:add']"-->
        <!--        >新增-->
        <!--        </el-button>-->
      </el-col>
      <el-col v-if="showWhitelistCrudActions" :span="1.5">
        <el-button
          v-hasPermi="['payment:whitelistedCreditCards:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >
          {{ t.edit }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:whitelistedCreditCards:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >
          {{ t.delete }}
        </el-button>
      </el-col>
      <el-col v-if="showWhitelistCrudActions" :span="1.5">
        <el-button
          v-hasPermi="['payment:whitelistedCreditCards:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >
          {{ t.export }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="whitelistedCreditCardsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="t.serialNumber" align="center" prop="id" />
      <el-table-column :label="t.originName" align="center" prop="originName" width="180" />
      <el-table-column :label="t.merchantId" align="center" prop="shopId" />
      <el-table-column :label="t.businessName" align="center" prop="businessName" width="180" />
      <el-table-column :label="t.fullCardNumber" align="center" prop="fullNumber" width="200" />
      <el-table-column :label="t.cardFinger" align="center" prop="cardFinger" width="320" />
      <el-table-column :label="t.binNumber" align="center" prop="binNumber" />
      <el-table-column :label="t.lastFourNumber" align="center" prop="lastFourNumber" />
      <el-table-column :label="t.riskLevel" align="center" prop="riskLevel" />
      <!--      <el-table-column label="卡BIN" align="center" prop="binNumber" />-->
      <!--      <el-table-column label="白名单邮箱" align="center" prop="email" width="150" />-->
      <el-table-column :label="t.createdAt" align="center" prop="createdAt" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t.updatedAt" align="center" prop="updatedAt" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t.operation" align="center" class-name="small-padding fixed-width">
      <template slot-scope="scope">
          <el-button
            v-hasPermi="['payment:whitelistedCreditCards:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >
            {{ t.edit }}
          </el-button>
          <el-button
            v-hasPermi="['payment:whitelistedCreditCards:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >
            {{ t.delete }}
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

    <!-- 添加或修改白名单信用卡对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="t.merchantId" prop="shopId">
              <el-input v-model.number="form.shopId" :placeholder="t.placeholderMerchantId" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="t.riskLevel" prop="riskLevel">
              <el-input v-model.number="form.riskLevel" :placeholder="t.placeholderRiskLevelInput" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="t.fullCardNumber" prop="fullNumber">
              <el-input
                v-model="form.fullNumber"
                :placeholder="t.placeholderFullNumber"
                @input="(val) => (form.fullNumber = val.replace(/\D/g, ''))"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="t.cardBin" prop="binNumber">
              <el-input v-model="form.binNumber" :placeholder="t.placeholderCardBin" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="t.lastFourNumber" prop="lastFourNumber">
              <el-input v-model="form.lastFourNumber" :placeholder="t.placeholderLastFour" />
            </el-form-item>
          </el-col>

          <!-- <el-col :span="12">
            <el-form-item label="白名单邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入白名单邮箱" />
            </el-form-item>
          </el-col> -->

          <el-col :span="24">
            <el-form-item :label="t.remark" prop="remark">
              <el-input v-model="form.remark" type="textarea" :placeholder="t.placeholderRemark" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ t.confirm }}</el-button>
        <el-button @click="cancel">{{ t.cancel }}</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="600px" append-to-body>
      <!-- 步骤1: 下载模板 -->
      <div class="import-step">
        <div class="step-header">
          <span class="step-number">1</span>
          <span class="step-title">{{ t.stepDownloadTemplate }}</span>
        </div>
        <div class="step-content">
          <div class="download-row">
            <el-button type="primary" icon="el-icon-download" size="small" @click="importTemplate">
              {{ t.btnDownloadTemplate }}
            </el-button>
            <span class="inline-tip">{{ t.downloadTemplateTip }}</span>
          </div>
        </div>
      </div>

      <!-- 步骤2: 选择导入类型 -->
      <div class="import-step">
        <div class="step-header">
          <span class="step-number">2</span>
          <span class="step-title">{{ t.stepSelectImportType }}</span>
        </div>
        <div class="step-content">
          <el-radio-group v-model="uploadForm.importType" class="import-type-radio-group">
            <div class="radio-option">
              <el-radio label="1">
                <span class="radio-text">{{ t.importTypeCover }}</span>
              </el-radio>
              <el-tooltip :content="t.importTypeCoverTip" placement="top">
                <i class="el-icon-question radio-help-icon" />
              </el-tooltip>
            </div>
            <div class="radio-option">
              <el-radio label="3">
                <span class="radio-text">{{ t.importTypeRemove }}</span>
              </el-radio>
              <el-tooltip :content="t.importTypeRemoveTip" placement="top">
                <i class="el-icon-question radio-help-icon" />
              </el-tooltip>
            </div>
          </el-radio-group>
        </div>
      </div>

      <!-- 步骤3: 上传文件 -->
      <div class="import-step">
        <div class="step-header">
          <span class="step-number">3</span>
          <span class="step-title">{{ t.stepUploadFile }}</span>
          <div class="upload-tips">
            <i class="el-icon-warning"></i>
            {{ t.uploadTip.replace('{maxSize}', upload.maxSizeMB) }}
          </div>
        </div>
        <div class="step-content">
          <el-upload
            ref="upload"
            accept=".xlsx, .xls"
            :headers="upload.headers"
            :action="upload.url + '?importType=' + uploadForm.importType"
            :disabled="upload.isUploading"
            :on-progress="handleFileUploadProgress"
            :on-success="handleFileSuccess"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            :auto-upload="false"
            drag
            class="upload-area"
          >
            <i class="el-icon-upload" />
            <div class="el-upload__text">
              {{ t.uploadText }}
              <em>{{ t.uploadClick }}</em>
            </div>
            <div v-if="selectedFileName" class="selected-file-info">
              <i class="el-icon-document"></i>
              {{ selectedFileName }}
            </div>
          </el-upload>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <div v-if="isParsing" class="parsing-status">
          <i class="el-icon-loading"></i>
          <span>{{ parseProgress }}</span>
        </div>
        <el-button
          type="primary"
          :loading="isSubmitting"
          :disabled="isSubmitting || isParsing || !selectedFileName"
          @click="submitFileForm"
        >
          {{ t.confirm }}
        </el-button>
        <el-button @click="upload.open = false">{{ t.cancel }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addWhitelistedCreditCards,
  delWhitelistedCreditCards,
  exportWhitelistedCreditCards,
  getWhitelistedCreditCards,
  importTemplate,
  listWhitelistedCreditCards,
  updateWhitelistedCreditCards
} from '@/api/payment/whitelistedCreditCards'
import { getToken } from '@/utils/auth'
import { getLangCode } from '@/utils/langUtils'
import WhitelistWorker from 'worker-loader!@/workers/whitelistWorker.js'

export default {
  name: 'WhitelistedCreditCards',
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
      // 白名单信用卡表格数据
      whitelistedCreditCardsList: [],
      // 商户字典
      merchantIdOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fullNumber: undefined,
        lastFourNumber: undefined,
        riskLevel: undefined,
        shopId: undefined,
        originName: undefined,
        startDate: undefined,
        endDate: undefined,
        dateRange: undefined
      },
      // 表单参数
      form: {},
      // 导入表单
      uploadForm: {
        importType: '1' // 默认导入类型为覆盖
      },
      // 导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: '',
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        // eslint-disable-next-line no-undef
        headers: { Authorization: 'Bearer ' + getToken(), lang: getLangCode() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + '/payment/whiteUploadRecord/import',
        maxSizeMB: 10
      },
      // Web Worker 控制
      workerInstance: null,
      workerChunkSize: 2000,
      currentWorkerTaskId: null,
      workerTaskSerial: 0,
      validationPassed: false,
      validatedRowCount: 0,
      // Excel文件数据
      excelData: [],
      // 验证后的有效数据（缓存）
      validatedData: [],
      // 预生成的上传文件
      filteredBlob: null,
      // 原始上传文件
      originalUploadFile: null,
      // 当前选择的文件名
      selectedFileName: '',
      // 提交状态标志，防止重复提交
      isSubmitting: false,
      // 文件解析状态
      isParsing: false,
      // 解析进度提示
      parseProgress: '',
      // 显示若依默认的修改/删除/导出动作（需求要求屏蔽）
      showWhitelistCrudActions: false
    }
  },
  computed: {
    t() {
      return this.$t('payment.whitelistedCreditCards')
    },
    rules() {
      return {
        binNumber: [
          { required: true, message: this.t.binRequired, trigger: 'blur' },
          { pattern: /^\d{6}$/, message: this.t.binPattern, trigger: 'blur' }
        ],
        lastFourNumber: [
          { required: true, message: this.t.lastFourRequired, trigger: 'blur' },
          { pattern: /^\d{4}$/, message: this.t.lastFourPattern, trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getDicts('MERCHANT_ID').then(response => {
      this.merchantIdOptions = response.data
    })
    this.getList()
    this.initWorker()
  },
  beforeDestroy() {
    this.terminateWorker()
  },
  methods: {
    initWorker() {
      this.terminateWorker()
      try {
        this.workerInstance = new WhitelistWorker()
        this.workerInstance.onmessage = this.handleWorkerMessage
        this.workerInstance.onerror = this.handleWorkerError
      } catch (error) {
        console.error('初始化 Web Worker 失败', error)
        this.workerInstance = null
        this.$message.error(this.t.workerInitFailed || '浏览器不支持后台校验，请联系管理员')
      }
    },
    terminateWorker() {
      if (this.workerInstance) {
        this.workerInstance.terminate()
        this.workerInstance = null
      }
      this.currentWorkerTaskId = null
    },
    handleWorkerError(error) {
      console.error('白名单校验 Worker 报错', error)
      this.isParsing = false
      this.validationPassed = false
      this.parseProgress = ''
      this.$message.error(this.t.workerCrashed || '后台校验失败，请重试')
    },
    handleWorkerMessage(event) {
      const { type, payload } = event.data || {}
      if (!payload) {
        return
      }
      if (this.currentWorkerTaskId !== payload.taskId) {
        return
      }
      if (type === 'progress') {
        const total = payload.totalRows || 0
        const processed = payload.processedRows || 0
        this.parseProgress = `正在校验 ${processed}/${total} 行`
        return
      }
      if (type === 'error') {
        this.isParsing = false
        this.validationPassed = false
        this.parseProgress = ''
        const errorMessage = this.translateWorkerError(payload)
        this.$message.error(errorMessage)
        this.clearFileSelection()
        return
      }
      if (type === 'success') {
        this.isParsing = false
        this.validationPassed = true
        this.parseProgress = ''
        const successCount = payload.totalRows ?? payload.rawTotalRows ?? 0
        this.validatedRowCount = successCount
        this.filteredBlob = this.originalUploadFile
        const msg = this.t.excelReadSuccess.replace('{count}', successCount)
        this.$message.success(msg)
      }
    },
    translateWorkerError(payload = {}) {
      const { code, message, meta = {} } = payload
      if (code === 'FULL_NUMBER_LENGTH_OUT_OF_RANGE') {
        const min = meta.expectedMin || 16
        const max = meta.expectedMax || 19
        const currentLength = meta.actualLength ?? '-'
        const row = meta.rowNumber ?? '-'
        const fieldLabel = this.t.fullCardNumber || 'Full Card Number'
        const rawValue = meta.rawValue ?? ''

        const detailTemplate =
          this.t.validationFullNumberLengthRange ||
          '长度必须为{min}-{max}位，当前为{current}位'
        const detail = detailTemplate
          .replace('{min}', min)
          .replace('{max}', max)
          .replace('{current}', currentLength)

        const rowTemplate =
          this.t.excelRowErrorDetail || '第{row}行【{field}】{reason}，当前值：{value}'
        return rowTemplate
          .replace('{row}', row)
          .replace('{field}', fieldLabel)
          .replace('{reason}', detail)
          .replace('{value}', rawValue)
      }
      if (message) {
        return message
      }
      return this.t.excelReadError.replace('{error}', 'Unknown')
    },
    startWorkerTask(arrayBuffer) {
      if (!this.workerInstance) {
        this.initWorker()
      }
      if (!this.workerInstance) {
        this.isParsing = false
        return
      }
      this.workerTaskSerial += 1
      const taskId = this.workerTaskSerial
      this.currentWorkerTaskId = taskId
      const headersMap = this.buildHeaderMap()
      this.workerInstance.postMessage(
        {
          type: 'PROCESS_FILE',
          payload: {
            buffer: arrayBuffer,
            headersMap,
            locale: this.$i18n?.locale || 'zh',
            chunkSize: this.workerChunkSize,
            taskId
          }
        },
        [arrayBuffer]
      )
    },
    buildHeaderMap() {
      const messages = (this.$i18n && this.$i18n.messages) || {}
      const result = {}
      Object.keys(messages).forEach(localeKey => {
        const headers = this.getExcelHeadersByLocale(localeKey)
        if (headers && headers.length) {
          result[localeKey] = headers
        }
      })
      return result
    },
    resetValidationState() {
      this.validationPassed = false
      this.validatedRowCount = 0
      this.filteredBlob = null
      this.isParsing = false
      this.parseProgress = ''
      this.currentWorkerTaskId = null
    },
    /** 查询白名单信用卡列表 */
    getList() {
      this.loading = true
      listWhitelistedCreditCards(this.queryParams).then(response => {
        this.whitelistedCreditCardsList = response.rows
        this.total = response.total
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
        fullNumber: undefined,
        binNumber: undefined,
        lastFourNumber: undefined,
        email: undefined,
        createdAt: undefined,
        createdBy: undefined,
        remark: undefined,
        shopId: undefined,
        riskLevel: undefined
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
    /** 处理日期范围变化 */
    handleDateRangeChange(dateRange) {
      if (dateRange && dateRange.length === 2) {
        this.queryParams.startDate = dateRange[0]
        this.queryParams.endDate = dateRange[1]
      } else {
        this.queryParams.startDate = undefined
        this.queryParams.endDate = undefined
      }
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
      this.title = this.t.dialogTitleAdd
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getWhitelistedCreditCards(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.t.dialogTitleEdit
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWhitelistedCreditCards(this.form).then(() => {
              this.msgSuccess(this.t.editSuccess)
              this.open = false
              this.getList()
            })
          } else {
            addWhitelistedCreditCards(this.form).then(() => {
              this.msgSuccess(this.t.addSuccess)
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
      this.$confirm(this.t.confirmDelete.replace('{ids}', ids), this.t.warning, {
        confirmButtonText: this.t.confirmButtonText,
        cancelButtonText: this.t.cancelButtonText,
        type: 'warning'
      })
        .then(function () {
          return delWhitelistedCreditCards(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.t.deleteSuccess)
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      let queryParamsForExport = {}
      let confirmText = ''
      if (this.ids && this.ids.length > 0) {
        // 当勾选了ids时，只导出ids，忽略其他搜索条件
        queryParamsForExport = { ids: this.ids }
        confirmText = this.t.confirmExportSelected.replace('{count}', this.ids.length)
      } else {
        // 没有勾选ids时，导出当前搜索条件下的数据
        // 复制查询参数，但排除分页参数
        queryParamsForExport = { ...this.queryParams }
        delete queryParamsForExport.pageNum
        delete queryParamsForExport.pageSize
        delete queryParamsForExport.dateRange // 不需要dateRange，因为已经转换为startDate和endDate
        confirmText = this.t.confirmExportAll.replace('{total}', this.total)
      }

      this.$confirm(confirmText, this.t.warning, {
        confirmButtonText: this.t.confirmButtonText,
        cancelButtonText: this.t.cancelButtonText,
        type: 'warning'
      })
        .then(() => {
          return exportWhitelistedCreditCards(queryParamsForExport)
        })
        .then(response => {
          this.download(response.msg)
        })
    },

    /** 下载模板操作 */
    importTemplate() {
      // 获取当前语言
      const lang = getLangCode()
      importTemplate(lang).then(response => {
        this.download(response.msg)
      })
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = this.t.dialogTitleImport
      this.uploadForm.importType = '1'
      // 清除之前的数据
      this.excelData = []
      this.validatedData = []
      this.selectedFileName = ''
      this.filteredBlob = null

      // 重置上传状态和提交状态
      this.upload.isUploading = false
      this.isSubmitting = false

      // 清除上传组件的文件列表
      if (this.$refs.upload) {
        this.$refs.upload.clearFiles()
      }

      // 确保在DOM更新后再打开对话框
      this.$nextTick(() => {
        this.upload.open = true
      })
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.selectedFileName = ''
      this.$alert(response.msg, this.t.importResult, { dangerouslyUseHTMLString: true })
      this.getList()
    },

    // 文件移除处理
    handleFileRemove(file, fileList) {
      console.log('文件被移除:', file.name, '剩余文件数量:', fileList.length)

      this.resetValidationState()
      // 如果没有文件了，清空相关数据
      if (fileList.length === 0) {
        this.selectedFileName = ''
        this.excelData = []
        this.validatedData = []
        this.originalUploadFile = null
      } else {
        // 如果还有其他文件，更新当前选择的文件名
        const remainingFile = fileList[fileList.length - 1]
        if (remainingFile && remainingFile.name) {
          this.selectedFileName = remainingFile.name
          this.originalUploadFile = remainingFile.raw || remainingFile
        }
      }
    },

    // 处理文件选择变化
    handleFileChange(file, fileList) {
      console.log('文件选择变化:', file.name, '当前文件列表长度:', fileList.length)

      // 如果检测到多个文件，通过异步方式清理旧文件，避免同步移除导致的问题
      if (fileList.length > 1) {
        console.log('检测到多个文件，保留最新的文件:', file.name)
        // 使用 nextTick 确保DOM更新后再处理文件移除
        this.$nextTick(() => {
          // 获取最新的文件列表
          const currentFileList = this.$refs.upload.uploadFiles
          if (currentFileList && currentFileList.length > 1) {
            // 只保留最后一个文件（最新上传的）
            const filesToRemove = currentFileList.slice(0, -1)
            filesToRemove.forEach(fileItem => {
              try {
                this.$refs.upload.handleRemove(fileItem)
              } catch (error) {
                console.warn('移除文件失败:', error)
              }
            })
          }
        })
      }

      // 处理当前文件
      this.processNewFile(file)
    },

    // 处理新文件的通用方法,如果之前有数据，提示用户将覆盖
    processNewFile(file) {
      console.log('处理新文件:', file.name)

      // 检查文件是否有效
      if (!file || !file.raw) {
        console.warn('文件对象无效:', file)
        this.$message.warning(this.t.fileInvalid)
        return
      }

      const maxSizeBytes = this.upload.maxSizeMB * 1024 * 1024
      const fileSize = file.size || file.raw.size
      if (fileSize > maxSizeBytes) {
        this.$message.error(this.t.fileSizeLimit.replace('{maxSize}', this.upload.maxSizeMB))
        this.clearFileSelection()
        return
      }

      // 设置当前选择的文件名
      this.selectedFileName = file.name

      this.resetValidationState()
      // 清除之前的Excel数据
      this.excelData = []
      this.validatedData = []
      this.originalUploadFile = file.raw || file

      // 读取Excel文件
      this.readExcelFile(file.raw)
    },

    // 通过 Web Worker 读取并校验 Excel
    readExcelFile(file) {
      this.isParsing = true
      this.parseProgress = '正在读取文件...'
      const reader = new FileReader()
      reader.onload = e => {
        try {
          const buffer = e.target.result
          this.parseProgress = '文件读取完成，正在后台校验...'
          this.startWorkerTask(buffer)
        } catch (error) {
          console.error('读取文件失败', error)
          this.isParsing = false
          this.parseProgress = ''
          this.$message.error(this.t.excelReadError.replace('{error}', error.message))
          this.clearFileSelection()
        }
      }
      reader.onerror = () => {
        this.isParsing = false
        this.parseProgress = ''
        const message = reader.error && reader.error.message ? reader.error.message : '无法读取文件'
        this.$message.error(this.t.excelReadError.replace('{error}', message))
        this.clearFileSelection()
      }
      reader.readAsArrayBuffer(file)
    },

    getExcelHeadersByLocale(locale) {
      const localeMessages = this.$i18n?.messages?.[locale]?.payment?.whitelistedCreditCards
      if (!localeMessages) {
        return null
      }
      return [
        localeMessages.excelHeaderFullNumber,
        localeMessages.excelHeaderBinNumber,
        localeMessages.excelHeaderLastFour,
        localeMessages.excelHeaderRiskLevel,
        localeMessages.excelHeaderMerchantIds
      ]
    },

    // 新增：清理文件选择状态的方法
    clearFileSelection() {
      // 清除选择的文件名
      this.selectedFileName = ''

      // 清除Excel数据
      this.excelData = []

      // 清除缓存的验证数据
      this.validatedData = []
      this.resetValidationState()
      this.originalUploadFile = null

      // 安全地清除上传组件的文件列表
      this.$nextTick(() => {
        if (this.$refs.upload) {
          try {
            this.$refs.upload.clearFiles()
          } catch (error) {
            console.warn('清除文件列表失败:', error)
          }
        }
      })
    },

    // 发送过滤后的数据到后端
    submitFileForm() {
      // 防止重复提交
      if (this.isSubmitting) {
        return
      }

      // 设置提交状态
      this.isSubmitting = true

      // 检查是否有上传的文件
      const uploadFiles = this.$refs.upload?.uploadFiles || []
      console.log('打印上传文件数据:', uploadFiles)

      // 安全检查：确保有文件被选择
      if (uploadFiles.length === 0) {
        this.$message.warning(this.t.pleaseSelectFile)
        this.isSubmitting = false
        return
      }

      // 安全检查：确保文件名存在
      const currentFile = uploadFiles[0]
      if (!currentFile || !currentFile.name) {
        this.$message.warning(this.t.fileInfoError)
        this.isSubmitting = false
        return
      }

      // 确保已通过后台校验
      if (!this.validationPassed) {
        this.$message.warning(this.t.pleaseUploadValidFile)
        this.isSubmitting = false
        return
      }

      if (!this.filteredBlob) {
        this.$message.warning(this.t.fileProcessing || '文件仍在处理中，请稍后再试')
        this.isSubmitting = false
        return
      }

      // 创建FormData对象
      const formData = new FormData()
      // 使用当前文件名，如果没有则使用默认名称
      const originalFileName = currentFile.name || this.selectedFileName || 'whitelist.xlsx'
      formData.append('file', this.filteredBlob, originalFileName)
      formData.append('importType', this.uploadForm.importType)

      // 调试日志
      console.log('上传参数 - importType:', this.uploadForm.importType)
      console.log('上传参数 - fileName:', originalFileName)

      // 发送文件到后端
      this.uploadFileToBackend(formData)
    },

    // 使用原生fetch发送multipart/form-data格式请求
    uploadFileToBackend(formData) {
      // 设置上传状态
      this.upload.isUploading = true

      // 使用原生fetch发送multipart/form-data请求
      fetch(this.upload.url, {
        method: 'POST',
        headers: {
          Authorization: this.upload.headers.Authorization,
          lang: this.upload.headers.lang
        },
        body: formData
      })
        .then(response => {
          // 检查响应状态
          if (!response.ok) {
            return response.text().then(text => {
              throw new Error(`HTTP ${response.status}: ${text.substring(0, 200)}`)
            })
          }

          // 检查内容类型
          const contentType = response.headers.get('content-type')
          if (!contentType || !contentType.includes('application/json')) {
            return response.text().then(text => {
              console.error('非JSON响应:', text.substring(0, 500))
              throw new Error('服务器返回了非JSON格式的响应，可能是权限不足或服务器错误')
            })
          }

          return response.json()
        })
        .then(data => {
          // 处理响应
          this.upload.isUploading = false
          this.isSubmitting = false
          this.upload.open = false
          this.$refs.upload.clearFiles()
          this.selectedFileName = ''
          this.filteredBlob = null
          this.originalUploadFile = null

          this.$alert(data.msg || this.t.importComplete, this.t.importResult, { dangerouslyUseHTMLString: true })
          this.getList()
        })
        .catch(error => {
          this.upload.isUploading = false
          this.isSubmitting = false
          console.error('上传失败:', error)
          this.$message.error(this.t.uploadError.replace('{error}', error.message))
        })
    }
  }
}
</script>

<style scoped>
/* 导入步骤样式 */
.import-step {
  margin-bottom: 12px;
  padding: 12px 16px;
  background: #f5f7fa;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
}

.import-step:last-of-type {
  margin-bottom: 0;
}

.step-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 10px;
  gap: 12px;
}

.step-number {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background: #409eff;
  color: white;
  border-radius: 50%;
  font-weight: bold;
  font-size: 13px;
  flex-shrink: 0;
}

.step-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  flex: 1;
  white-space: nowrap;
  line-height: 24px;
}

.step-content {
  padding-left: 36px;
}

.download-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.inline-tip {
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
}

/* 导入类型选择样式 */
.import-type-radio-group {
  display: flex;
  gap: 20px;
}

.radio-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.radio-text {
  font-size: 14px;
  color: #606266;
}

.radio-help-icon {
  color: #909399;
  cursor: pointer;
  font-size: 16px;
  transition: color 0.3s;
}

.radio-help-icon:hover {
  color: #409eff;
}

/* 上传区域样式 */
.upload-area {
  width: 100%;
}

.upload-area .el-icon-upload {
  font-size: 42px;
  color: #c0c4cc;
  margin-bottom: 10px;
}

.upload-area .el-upload__text {
  font-size: 13px;
}

.selected-file-info {
  margin-top: 8px;
  padding: 6px 12px;
  background: #e6f7ff;
  border: 1px solid #91d5ff;
  border-radius: 4px;
  color: #1890ff;
  font-size: 12px;
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.selected-file-info i {
  font-size: 14px;
}

.upload-tips {
  display: flex;
  align-items: flex-start;
  gap: 5px;
  font-size: 11px;
  color: #f56c6c;
  line-height: 1.5;
  padding: 6px 10px;
  background: #fef0f0;
  border-radius: 4px;
  border: 1px solid #fde2e2;
  max-width: 300px;
  word-break: break-all;
}

.upload-tips i {
  flex-shrink: 0;
  font-size: 13px;
  margin-top: 1px;
}

/* 解析状态提示 */
.parsing-status {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-right: 12px;
  color: #409eff;
  font-size: 14px;
}

.parsing-status i {
  font-size: 16px;
}
</style>
