<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="118px">
      <!--      <el-form-item label="收单机构MID" prop="midCode">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.midCode"-->
      <!--          placeholder="请输入收单机构MID"-->
      <!--          clearable-->
      <!--          size="small"-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <el-form-item :label="$t('payment.configMid.currencyType')" prop="settlementCurrencyType">
        <el-select
          v-model="queryParams.settlementCurrencyType"
          :placeholder="$t('payment.configMid.selectCurrencyType')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in settlementCurrencyTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.configMid.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('payment.configMid.selectStatus')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <!--      <el-form-item label="Nuvei站点名称" prop="siteName">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.siteName"-->
      <!--          placeholder="请输入Nuvei站点名称"-->
      <!--          clearable-->
      <!--          size="small"-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="Nuvei客户端名称" prop="clientName">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.clientName"-->
      <!--          placeholder="请输入Nuvei客户端名称"-->
      <!--          clearable-->
      <!--          size="small"-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <el-form-item :label="$t('payment.configMid.type')" prop="type">
        <el-select
          v-model="queryParams.type"
          :placeholder="$t('payment.configMid.selectType')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.configMid.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('payment.configMid.reset') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:configMid:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >
          {{ $t('payment.configMid.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:configMid:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >
          {{ $t('payment.configMid.edit') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:configMid:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >
          {{ $t('payment.configMid.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:configMid:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >
          {{ $t('payment.configMid.export') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="configMidList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--      <el-table-column label="收单机构ID外键" align="center" prop="acquirerId" />-->
      <el-table-column :label="$t('payment.configMid.acquirerMid')" align="center" prop="midCode" />
      <el-table-column :label="$t('payment.configMid.midDescription')" align="center" prop="description" />
      <!--      <el-table-column label="货币表或加密货币表主键" align="center" prop="settlementCurrencyId" />-->
      <el-table-column
        :label="$t('payment.configMid.currencyType')"
        align="center"
        prop="settlementCurrencyType"
        :formatter="settlementCurrencyTypeFormat"
      />
      <el-table-column :label="$t('payment.configMid.status')" align="center" prop="status" :formatter="statusFormat" />
      <!--      <el-table-column label="Nuvei站点ID" align="center" prop="siteId" />-->
      <el-table-column :label="$t('payment.configMid.nuveiSiteName')" align="center" prop="siteName" />
      <!--      <el-table-column label="Nuvei客户端ID" align="center" prop="clientId" />-->
      <el-table-column :label="$t('payment.configMid.nuveiClientName')" align="center" prop="clientName" />
      <!--      <el-table-column label="收单机构分配的商户ID" align="center" prop="acquirerMerchantId" />-->
      <el-table-column :label="$t('payment.configMid.type')" align="center" prop="type" :formatter="typeFormat" />
      <el-table-column :label="$t('payment.configMid.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['payment:configMid:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >
            {{ $t('payment.configMid.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['payment:configMid:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >
            {{ $t('payment.configMid.delete') }}
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

    <!-- 添加或修改MID配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.midId')" prop="id">
              <el-input v-model="form.id" :placeholder="$t('payment.configMid.pleaseEnterMidId')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.acquirerId')" prop="acquirerId">
              <el-input v-model="form.acquirerId" :placeholder="$t('payment.configMid.pleaseEnterAcquirerId')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.midCode')" prop="midCode">
              <el-input v-model="form.midCode" :placeholder="$t('payment.configMid.pleaseEnterMidCode')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('payment.configMid.description')" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                :placeholder="$t('payment.configMid.pleaseEnterContent')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.settlementCurrencyId')" prop="settlementCurrencyId">
              <el-input
                v-model="form.settlementCurrencyId"
                :placeholder="$t('payment.configMid.pleaseEnterSettlementCurrencyId')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.settlementCurrencyType')" prop="settlementCurrencyType">
              <el-select
                v-model="form.settlementCurrencyType"
                :placeholder="$t('payment.configMid.selectCurrencyTypeInForm')"
                filterable
              >
                <el-option
                  v-for="dict in settlementCurrencyTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.statusInForm')" prop="status">
              <el-select v-model="form.status" :placeholder="$t('payment.configMid.selectStatusInForm')" filterable>
                <el-option
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.nuveiSiteId')" prop="siteId">
              <el-input v-model="form.siteId" :placeholder="$t('payment.configMid.pleaseEnterNuveiSiteId')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.nuveiSiteName')" prop="siteName">
              <el-input v-model="form.siteName" :placeholder="$t('payment.configMid.pleaseEnterNuveiSiteName')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.nuveiClientId')" prop="clientId">
              <el-input v-model="form.clientId" :placeholder="$t('payment.configMid.pleaseEnterNuveiClientId')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.nuveiClientName')" prop="clientName">
              <el-input v-model="form.clientName" :placeholder="$t('payment.configMid.pleaseEnterNuveiClientName')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.acquirerMerchantId')" prop="acquirerMerchantId">
              <el-input
                v-model="form.acquirerMerchantId"
                :placeholder="$t('payment.configMid.pleaseEnterAcquirerMerchantId')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.configMid.typeInForm')" prop="type">
              <el-select v-model="form.type" :placeholder="$t('payment.configMid.selectTypeInForm')" filterable>
                <el-option
                  v-for="dict in typeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.configMid.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.configMid.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addConfigMid,
  delConfigMid,
  exportConfigMid,
  getConfigMid,
  listConfigMid,
  updateConfigMid
} from '@/api/payment/configMid'

export default {
  name: 'ConfigMid',
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
      // MID配置表格数据
      configMidList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 货币类型字典
      settlementCurrencyTypeOptions: [],
      // 类型字典
      statusOptions: [],
      // 类型字典
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        midCode: undefined,
        settlementCurrencyType: undefined,
        status: undefined,
        siteName: undefined,
        clientName: undefined,
        type: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [{ required: true, message: this.$t('payment.configMid.midIdRequired'), trigger: 'blur' }],
        acquirerId: [{ required: true, message: this.$t('payment.configMid.acquirerIdRequired'), trigger: 'blur' }],
        midCode: [{ required: true, message: this.$t('payment.configMid.midCodeRequired'), trigger: 'blur' }],
        settlementCurrencyType: [
          { required: true, message: this.$t('payment.configMid.currencyTypeRequired'), trigger: 'change' }
        ],
        status: [{ required: true, message: this.$t('payment.configMid.statusRequired'), trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('CONFIG_MID_SETTLEMENT_CURRENCY_TYPE').then(response => {
      this.settlementCurrencyTypeOptions = response.data
    })
    this.getDicts('CONFIG_MID_STATUS').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('CONFIG_MID_TYPE').then(response => {
      this.typeOptions = response.data
    })
  },
  methods: {
    /** 查询MID配置列表 */
    getList() {
      this.loading = true
      listConfigMid(this.queryParams).then(response => {
        this.configMidList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 货币类型字典翻译
    settlementCurrencyTypeFormat(row, column) {
      return this.selectDictLabel(this.settlementCurrencyTypeOptions, row.settlementCurrencyType)
    },
    // 类型字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type)
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
        acquirerId: undefined,
        midCode: undefined,
        description: undefined,
        settlementCurrencyId: undefined,
        settlementCurrencyType: undefined,
        status: undefined,
        siteId: undefined,
        siteName: undefined,
        clientId: undefined,
        clientName: undefined,
        acquirerMerchantId: undefined,
        type: undefined
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
      this.title = this.$t('payment.configMid.addConfigMid')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getConfigMid(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.configMid.editConfigMid')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateConfigMid(this.form).then(response => {
              this.msgSuccess(this.$t('payment.configMid.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addConfigMid(this.form).then(response => {
              this.msgSuccess(this.$t('payment.configMid.addSuccess'))
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
        this.$t('payment.configMid.deleteConfirm').replace('{ids}', ids),
        this.$t('payment.configMid.warning'),
        {
          confirmButtonText: this.$t('payment.configMid.confirm'),
          cancelButtonText: this.$t('payment.configMid.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return delConfigMid(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.configMid.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('payment.configMid.exportConfirm'), this.$t('payment.configMid.warning'), {
        confirmButtonText: this.$t('payment.configMid.confirm'),
        cancelButtonText: this.$t('payment.configMid.cancel'),
        type: 'warning'
      })
        .then(function () {
          return exportConfigMid(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    }
  }
}
</script>
