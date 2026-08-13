<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item :label="$t('payment.acquirerOrder.orderType')" prop="type">
        <el-select
            v-model="queryParams.type"
            :placeholder="$t('payment.acquirerOrder.orderTypeSelect')"
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
      <el-form-item :label="$t('payment.acquirerOrder.status')" prop="status">
        <el-select
            v-model="queryParams.status"
            :placeholder="$t('payment.acquirerOrder.statusSelect')"
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.acquirerOrder.searchBtn') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('payment.acquirerOrder.resetBtn') }}
        </el-button>
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
            v-hasPermi="['payment:acquirerOrder:add']"
        >
          {{ $t('payment.acquirerOrder.add') }}
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
            v-hasPermi="['payment:acquirerOrder:edit']"
        >
          {{ $t('payment.acquirerOrder.edit') }}
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
            v-hasPermi="['payment:acquirerOrder:remove']"
        >
          {{ $t('payment.acquirerOrder.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['payment:acquirerOrder:export']"
        >
          {{ $t('payment.acquirerOrder.export') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="acquirerOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="订单ID" align="center" prop="id" v-if="false"/>
      <el-table-column :label="$t('payment.acquirerOrder.orderId')" align="center" prop="orderId"/>
      <el-table-column
          :label="$t('payment.acquirerOrder.orderType')"
          align="center"
          prop="type"
          :formatter="typeFormat"
      />
      <el-table-column :label="$t('payment.acquirerOrder.currency')" align="center" prop="currencyId"/>
      <el-table-column :label="$t('payment.acquirerOrder.amount')" align="center" prop="amount"/>
      <el-table-column
          :label="$t('payment.acquirerOrder.status')"
          align="center"
          prop="status"
          :formatter="statusFormat"
      />
      <el-table-column :label="$t('payment.acquirerOrder.createdAt')" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
          :label="$t('payment.acquirerOrder.deleted')"
          align="center"
          prop="deleted"
          :formatter="deletedFormat"
      />
      <el-table-column :label="$t('payment.acquirerOrder.remark')" align="center" prop="remark"/>
      <el-table-column
          :label="$t('payment.acquirerOrder.action')"
          align="center"
          class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['payment:acquirerOrder:edit']"
          >
            {{ $t('payment.acquirerOrder.edit') }}
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['payment:acquirerOrder:remove']"
          >
            {{ $t('payment.acquirerOrder.delete') }}
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

    <!-- Add or edit acquirer order dialog -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.acquirerOrder.acquirerId')" prop="acquirerId">
              <el-input v-model="form.acquirerId" :placeholder="$t('payment.acquirerOrder.acquirerIdPlaceholder')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.acquirerOrder.reconRequestId')" prop="reconRequestId">
              <el-input
                  v-model="form.reconRequestId"
                  :placeholder="$t('payment.acquirerOrder.reconRequestIdPlaceholder')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.acquirerOrder.orderId')" prop="orderId">
              <el-input v-model="form.orderId" :placeholder="$t('payment.acquirerOrder.orderIdPlaceholder')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.acquirerOrder.orderType')" prop="type">
              <el-select v-model="form.type" :placeholder="$t('payment.acquirerOrder.orderTypeSelect')" filterable>
                <el-option
                    v-for="dict in typeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="parseInt(dict.dictValue)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.acquirerOrder.currency')" prop="currencyId">
              <el-input v-model="form.currencyId" :placeholder="$t('payment.acquirerOrder.currencyPlaceholder')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.acquirerOrder.amount')" prop="amount">
              <el-input v-model="form.amount" :placeholder="$t('payment.acquirerOrder.amountPlaceholder')"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('payment.acquirerOrder.paymentDeclinedPayload')" prop="paymentDeclinedPayload">
              <el-input
                  v-model="form.paymentDeclinedPayload"
                  type="textarea"
                  :placeholder="$t('payment.acquirerOrder.paymentDeclinedPayloadPlaceholder')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.acquirerOrder.status')" prop="status">
              <el-select v-model="form.status" :placeholder="$t('payment.acquirerOrder.statusSelect')" filterable>
                <el-option
                    v-for="dict in statusOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="parseInt(dict.dictValue)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!--    <el-col :span="12">-->
          <!--        <el-form-item label="创建时间" prop="createdAt">-->
          <!--          <el-date-picker clearable size="small"-->
          <!--            v-model="form.createdAt"-->
          <!--            type="datetime"-->
          <!--            value-format="yyyy-MM-dd HH:mm:ss"-->
          <!--            placeholder="选择创建时间">-->
          <!--          </el-date-picker>-->
          <!--        </el-form-item>-->
          <!--    </el-col>-->

          <!--    <el-col :span="12">-->
          <!--        <el-form-item label="创建记录的用户ID" prop="createdBy">-->
          <!--          <el-input v-model="form.createdBy" placeholder="请输入创建记录的用户ID" />-->
          <!--        </el-form-item>-->
          <!--    </el-col>-->

          <!--    <el-col :span="12">-->
          <!--        <el-form-item label="删除时间" prop="deletedAt">-->
          <!--          <el-date-picker clearable size="small"-->
          <!--            v-model="form.deletedAt"-->
          <!--            type="datetime"-->
          <!--            value-format="yyyy-MM-dd HH:mm:ss"-->
          <!--            placeholder="选择删除时间">-->
          <!--          </el-date-picker>-->
          <!--        </el-form-item>-->
          <!--    </el-col>-->

          <!--    <el-col :span="12">-->
          <!--        <el-form-item label="最后删除记录的用户ID" prop="deletedBy">-->
          <!--          <el-input v-model="form.deletedBy" placeholder="请输入最后删除记录的用户ID" />-->
          <!--        </el-form-item>-->
          <!--    </el-col>-->

          <!--    <el-col :span="12">-->
          <!--        <el-form-item label="是否删除" prop="deleted">-->
          <!--          <el-select v-model="form.deleted" placeholder="请选择是否删除" filterable>-->
          <!--            <el-option-->
          <!--              v-for="dict in deletedOptions"-->
          <!--              :key="dict.dictValue"-->
          <!--              :label="dict.dictLabel"-->
          <!--              :value="parseInt(dict.dictValue)"-->
          <!--            ></el-option>-->
          <!--          </el-select>-->
          <!--        </el-form-item>-->
          <!--    </el-col>-->
          <el-col :span="24">
            <el-form-item :label="$t('payment.acquirerOrder.remark')" prop="remark">
              <el-input
                  v-model="form.remark"
                  type="textarea"
                  :placeholder="$t('payment.acquirerOrder.remarkPlaceholder')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('payment.acquirerOrder.paymentResponsePayload')" prop="paymentResponsePayload">
              <el-input
                  v-model="form.paymentResponsePayload"
                  type="textarea"
                  :placeholder="$t('payment.acquirerOrder.paymentResponsePayloadPlaceholder')"
              />
            </el-form-item>
          </el-col>
          <!--    <el-col :span="12">-->
          <!--        <el-form-item label="customer_card_token的ID" prop="tokenId">-->
          <!--          <el-input v-model="form.tokenId" placeholder="请输入customer_card_token的ID" />-->
          <!--        </el-form-item>-->
          <!--    </el-col>-->

          <!--    <el-col :span="12">-->
          <!--        <el-form-item label="商户响应代码" prop="responseCode">-->
          <!--          <el-input v-model="form.responseCode" placeholder="请输入商户响应代码" />-->
          <!--        </el-form-item>-->
          <!--    </el-col>-->

          <!--    <el-col :span="12">-->
          <!--        <el-form-item label="访问控制服务器交易ID" prop="acsTransactionId">-->
          <!--          <el-input v-model="form.acsTransactionId" placeholder="请输入访问控制服务器交易ID" />-->
          <!--        </el-form-item>-->
          <!--    </el-col>-->
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.acquirerOrder.confirmBtn') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.acquirerOrder.cancelBtn') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addAcquirerOrder,
  delAcquirerOrder,
  exportAcquirerOrder,
  getAcquirerOrder,
  listAcquirerOrder,
  updateAcquirerOrder
} from '@/api/payment/acquirerOrder'

export default {
  name: 'AcquirerOrder',
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
      // 收单机构订单表格数据
      acquirerOrderList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 订单类型字典
      typeOptions: [],
      // 状态字典
      statusOptions: [],
      // 是否删除字典
      deletedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        acquirerId: [{required: true, message: this.$t('payment.acquirerOrder.acquirerIdRequired'), trigger: 'blur'}],
        orderId: [{required: true, message: this.$t('payment.acquirerOrder.orderIdRequired'), trigger: 'blur'}],
        type: [{required: true, message: this.$t('payment.acquirerOrder.orderTypeRequired'), trigger: 'change'}],
        currencyId: [{required: true, message: this.$t('payment.acquirerOrder.currencyRequired'), trigger: 'blur'}],
        amount: [{required: true, message: this.$t('payment.acquirerOrder.amountRequired'), trigger: 'blur'}],
        status: [{required: true, message: this.$t('payment.acquirerOrder.statusRequired'), trigger: 'change'}],
        createdAt: [{required: true, message: this.$t('payment.acquirerOrder.createdAtRequired'), trigger: 'blur'}],
        deleted: [{required: true, message: this.$t('payment.acquirerOrder.deletedRequired'), trigger: 'change'}]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('ACQUIRER_ORDER_TYPE').then(response => {
      this.typeOptions = response.data
    })
    this.getDicts('ACQUIRER_ORDER_STATUS').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('DELETE_STATUS').then(response => {
      this.deletedOptions = response.data
    })
  },
  methods: {
    /** 查询收单机构订单列表 */
    getList() {
      this.loading = true
      listAcquirerOrder(this.queryParams).then(response => {
        this.acquirerOrderList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 订单类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type)
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 是否删除字典翻译
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
        acquirerId: undefined,
        reconRequestId: undefined,
        orderId: undefined,
        type: undefined,
        currencyId: undefined,
        amount: undefined,
        paymentDeclinedPayload: undefined,
        status: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
        remark: undefined,
        paymentResponsePayload: undefined,
        tokenId: undefined,
        responseCode: undefined,
        acsTransactionId: undefined
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
      this.title = this.$t('payment.acquirerOrder.addTitle')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getAcquirerOrder(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.acquirerOrder.editTitle')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAcquirerOrder(this.form).then(response => {
              this.msgSuccess(this.$t('payment.acquirerOrder.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addAcquirerOrder(this.form).then(response => {
              this.msgSuccess(this.$t('payment.acquirerOrder.addSuccess'))
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
          this.$t('payment.acquirerOrder.deleteConfirm', {id: ids}),
          this.$t('payment.acquirerOrder.warning'),
          {
            confirmButtonText: this.$t('payment.acquirerOrder.confirmBtn'),
            cancelButtonText: this.$t('payment.acquirerOrder.cancelBtn'),
            type: 'warning'
          }
      )
          .then(function () {
            return delAcquirerOrder(ids)
          })
          .then(() => {
            this.getList()
            this.msgSuccess(this.$t('payment.acquirerOrder.deleteSuccess'))
          })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(this.$t('payment.acquirerOrder.exportConfirm'), this.$t('payment.acquirerOrder.warning'), {
        confirmButtonText: this.$t('payment.acquirerOrder.confirmBtn'),
        cancelButtonText: this.$t('payment.acquirerOrder.cancelBtn'),
        type: 'warning'
      })
          .then(function () {
            return exportAcquirerOrder(queryParams)
          })
          .then(response => {
            this.download(response.msg)
          })
    }
  }
}
</script>
