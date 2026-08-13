<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="100px">
      <el-form-item :label="$t('payment.blacklistedCreditCards.binNumber')" prop="binNumber" label-width="100px">
        <el-input
          v-model="queryParams.binNumber"
          :placeholder="$t('payment.blacklistedCreditCards.placeholderBinNumber')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item
        :label="$t('payment.blacklistedCreditCards.lastFourNumber')"
        prop="lastFourNumber"
        label-width="100px"
      >
        <el-input
          v-model="queryParams.lastFourNumber"
          :placeholder="$t('payment.blacklistedCreditCards.placeholderLastFourNumber')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.blacklistedCreditCards.createdAt')" prop="createdAt" label-width="100px">
        <el-date-picker
          v-model="queryParams.createdAt"
          clearable
          size="small"
          type="date"
          value-format="yyyy-MM-dd"
          :placeholder="$t('payment.blacklistedCreditCards.placeholderCreatedAt')"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.blacklistedCreditCards.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('payment.blacklistedCreditCards.reset') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:blacklistedCreditCards:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >
          {{ $t('payment.blacklistedCreditCards.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:blacklistedCreditCards:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >
          {{ $t('payment.blacklistedCreditCards.edit') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:blacklistedCreditCards:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >
          {{ $t('payment.blacklistedCreditCards.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:blacklistedCreditCards:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >
          {{ $t('payment.blacklistedCreditCards.export') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

	    <el-table v-loading="loading" :data="blacklistedCreditCardsList" @selection-change="handleSelectionChange">
	      <el-table-column type="selection" width="55" align="center" />
	      <el-table-column :label="$t('payment.blacklistedCreditCards.serialNumber')" align="center" prop="id" />
	      <el-table-column :label="$t('payment.blacklistedCreditCards.binNumber')" align="center" prop="binNumber" />
	      <el-table-column
	        :label="$t('payment.blacklistedCreditCards.lastFourNumber')"
	        align="center"
	        prop="lastFourNumber"
	      />
	      <el-table-column
	        :label="$t('payment.blacklistedCreditCards.cardFinger')"
	        align="center"
	        prop="cardFinger"
	        width="320"
	      />
	      <el-table-column
	        :label="$t('payment.blacklistedCreditCards.createdAt')"
	        align="center"
	        prop="createdAt"
	        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.blacklistedCreditCards.updatedAt')"
        align="center"
        prop="updatedAt"
        width="120"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.blacklistedCreditCards.remark')" align="center" prop="remark" />
      <el-table-column
        :label="$t('payment.blacklistedCreditCards.operation')"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['payment:blacklistedCreditCards:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >
            {{ $t('payment.blacklistedCreditCards.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['payment:blacklistedCreditCards:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >
            {{ $t('payment.blacklistedCreditCards.delete') }}
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

    <!-- 添加或修改黑名单信用卡对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col v-if="!form.id" :span="12">
            <el-form-item
              :label="$t('payment.blacklistedCreditCards.fullNumber')"
              prop="fullNumber"
              label-width="130px"
            >
              <el-input
                v-model="form.fullNumber"
                :placeholder="$t('payment.blacklistedCreditCards.placeholderFullNumber')"
                @input="handleFullNumberChange"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.blacklistedCreditCards.binNumber')" prop="binNumber" label-width="100px">
              <el-input
                v-model="form.binNumber"
                :placeholder="$t('payment.blacklistedCreditCards.placeholderBinNumber')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item
              :label="$t('payment.blacklistedCreditCards.lastFourNumber')"
              prop="lastFourNumber"
              label-width="130px"
            >
              <el-input
                v-model="form.lastFourNumber"
                :placeholder="$t('payment.blacklistedCreditCards.placeholderLastFourNumber')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.blacklistedCreditCards.remark')" prop="remark" label-width="100px">
              <el-input v-model="form.remark" :placeholder="$t('payment.blacklistedCreditCards.placeholderRemark')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.blacklistedCreditCards.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.blacklistedCreditCards.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addBlacklistedCreditCards,
  delBlacklistedCreditCards,
  exportBlacklistedCreditCards,
  getBlacklistedCreditCards,
  listBlacklistedCreditCards,
  updateBlacklistedCreditCards
} from '@/api/payment/blacklistedCreditCards'

export default {
  name: 'BlacklistedCreditCards',
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
      // 黑名单信用卡表格数据
      blacklistedCreditCardsList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 类型字典
      deletedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        binNumber: undefined,
        lastFourNumber: undefined,
        createdAt: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    }
  },
  computed: {
    validationRules() {
      return {
        binNumber: [
          { required: true, message: this.$t('payment.blacklistedCreditCards.binRequired'), trigger: 'blur' },
          { pattern: /^\d{6}$/, message: this.$t('payment.blacklistedCreditCards.binPattern'), trigger: 'blur' }
        ],
        lastFourNumber: [
          { required: true, message: this.$t('payment.blacklistedCreditCards.lastFourRequired'), trigger: 'blur' },
          { pattern: /^\d{4}$/, message: this.$t('payment.blacklistedCreditCards.lastFourPattern'), trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    validationRules: {
      handler(newRules) {
        this.rules = newRules
      },
      immediate: true
    }
  },
  created() {
    this.getList()
    this.getDicts('DELETE_STATUS').then(response => {
      this.deletedOptions = response.data
    })
  },
  methods: {
    /** 查询黑名单信用卡列表 */
    getList() {
      this.loading = true
      listBlacklistedCreditCards(this.queryParams).then(response => {
        this.blacklistedCreditCardsList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 类型字典翻译
    deletedFormat(row, column) {
      return this.selectDictLabel(this.deletedOptions, row.deleted)
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
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
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
      this.title = this.$t('payment.blacklistedCreditCards.dialogTitleAdd')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getBlacklistedCreditCards(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.blacklistedCreditCards.dialogTitleEdit')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          const payload = { ...this.form }
          if (payload.id != null) {
            delete payload.fullNumber
            updateBlacklistedCreditCards(payload).then(response => {
              this.msgSuccess(this.$t('payment.blacklistedCreditCards.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addBlacklistedCreditCards(payload).then(response => {
              this.msgSuccess(this.$t('payment.blacklistedCreditCards.addSuccess'))
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
      this.$confirm(
        this.$t('payment.blacklistedCreditCards.confirmDelete', { ids }),
        this.$t('payment.blacklistedCreditCards.warning'),
        {
          confirmButtonText: this.$t('payment.blacklistedCreditCards.confirmButtonText'),
          cancelButtonText: this.$t('payment.blacklistedCreditCards.cancelButtonText'),
          type: 'warning'
        }
      )
        .then(function () {
          return delBlacklistedCreditCards(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.blacklistedCreditCards.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(
        this.$t('payment.blacklistedCreditCards.confirmExport'),
        this.$t('payment.blacklistedCreditCards.warning'),
        {
          confirmButtonText: this.$t('payment.blacklistedCreditCards.confirmButtonText'),
          cancelButtonText: this.$t('payment.blacklistedCreditCards.cancelButtonText'),
          type: 'warning'
        }
      )
        .then(function () {
          return exportBlacklistedCreditCards(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    },
    // 当完整卡号发生变化时，自动填充卡后四位
    handleFullNumberChange() {
      if (this.form.fullNumber && this.form.fullNumber.length >= 4) {
        this.form.lastFourNumber = this.form.fullNumber.slice(-4)
      }
    }
  }
}
</script>
