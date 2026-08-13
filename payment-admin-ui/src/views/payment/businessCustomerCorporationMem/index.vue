<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="130px">
      <el-form-item :label="$t('payment.businessCustomerCorporationMem.searchForm.firstName')" prop="firstName">
        <el-input
          v-model="queryParams.firstName"
          :placeholder="$t('payment.businessCustomerCorporationMem.searchPlaceholder.firstName')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.businessCustomerCorporationMem.searchForm.middleName')" prop="middleName">
        <el-input
          v-model="queryParams.middleName"
          :placeholder="$t('payment.businessCustomerCorporationMem.searchPlaceholder.middleName')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.businessCustomerCorporationMem.searchForm.lastName')" prop="lastName">
        <el-input
          v-model="queryParams.lastName"
          :placeholder="$t('payment.businessCustomerCorporationMem.searchPlaceholder.lastName')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.businessCustomerCorporationMem.searchForm.documentType')" prop="documentType">
        <el-select
          v-model="queryParams.documentType"
          :placeholder="$t('payment.businessCustomerCorporationMem.searchPlaceholder.documentType')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in documentTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.businessCustomerCorporationMem.searchForm.memberType')" prop="memberType">
        <el-select
          v-model="queryParams.memberType"
          :placeholder="$t('payment.businessCustomerCorporationMem.searchPlaceholder.memberType')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in memberTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.businessCustomerCorporationMem.searchForm.createdAt')" prop="createdAt">
        <el-date-picker
          clearable
          size="small"
          v-model="queryParams.createdAt"
          type="date"
          value-format="yyyy-MM-dd"
          :placeholder="$t('payment.businessCustomerCorporationMem.searchPlaceholder.createdAt')"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('payment.businessCustomerCorporationMem.searchForm.searchBtn') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
          {{ $t('payment.businessCustomerCorporationMem.searchForm.resetBtn') }}
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
          v-hasPermi="['payment:businessCustomerCorporationMem:add']"
        >
          {{ $t('payment.businessCustomerCorporationMem.actions.add') }}
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
          v-hasPermi="['payment:businessCustomerCorporationMem:edit']"
        >
          {{ $t('payment.businessCustomerCorporationMem.actions.edit') }}
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
          v-hasPermi="['payment:businessCustomerCorporationMem:remove']"
        >
          {{ $t('payment.businessCustomerCorporationMem.actions.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:businessCustomerCorporationMem:export']"
        >
          {{ $t('payment.businessCustomerCorporationMem.actions.export') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="businessCustomerCorporationMemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.userId')"
        align="center"
        prop="id"
        v-if="false"
      />
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.firstName')"
        align="center"
        prop="firstName"
      />
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.middleName')"
        align="center"
        prop="middleName"
      />
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.lastName')"
        align="center"
        prop="lastName"
      />
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.documentType')"
        align="center"
        prop="documentType"
        :formatter="documentTypeFormat"
      />
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.memberType')"
        align="center"
        prop="memberType"
        :formatter="memberTypeFormat"
      />
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.createdAt')"
        align="center"
        prop="createdAt"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.deleted')"
        align="center"
        prop="deleted"
        :formatter="deletedFormat"
      />
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.businessName')"
        align="center"
        prop="businessName"
      />
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.businessRegistrationNo')"
        align="center"
        prop="businessRegistrationNo"
      />
      <el-table-column
        :label="$t('payment.businessCustomerCorporationMem.table.action')"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:businessCustomerCorporationMem:edit']"
          >
            {{ $t('payment.businessCustomerCorporationMem.actions.edit') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:businessCustomerCorporationMem:remove']"
          >
            {{ $t('payment.businessCustomerCorporationMem.actions.delete') }}
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

    <!-- 添加或修改企业客户公司成员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="250px">
        <el-row>
          <el-col :span="12">
            <el-form-item
              :label="$t('payment.businessCustomerCorporationMem.form.businessCustomerCorporationId')"
              prop="businessCustomerCorporationId"
            >
              <el-select
                v-model="form.businessCustomerCorporationId"
                :placeholder="
                  $t('payment.businessCustomerCorporationMem.formPlaceholder.businessCustomerCorporationId')
                "
                filterable
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="shop in shopOptions"
                  :key="shop.id"
                  :label="shop.businessName"
                  :value="shop.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.firstName')" prop="firstName">
              <el-input
                v-model="form.firstName"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.firstName')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.middleName')" prop="middleName">
              <el-input
                v-model="form.middleName"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.middleName')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.lastName')" prop="lastName">
              <el-input
                v-model="form.lastName"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.lastName')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.birthday')" prop="birthday">
              <el-date-picker
                clearable
                size="small"
                v-model="form.birthday"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.birthday')"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.nationality')" prop="nationality">
              <el-input
                v-model="form.nationality"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.nationality')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.documentType')" prop="documentType">
              <el-select
                v-model="form.documentType"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.documentType')"
                filterable
              >
                <el-option
                  v-for="dict in documentTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.documentNo')" prop="documentNo">
              <el-input
                v-model="form.documentNo"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.documentNo')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item
              :label="$t('payment.businessCustomerCorporationMem.form.documentFrontUrl')"
              prop="documentFrontUrl"
            >
              <el-input
                v-model="form.documentFrontUrl"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.documentFrontUrl')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="$t('payment.businessCustomerCorporationMem.form.documentBackUrl')"
              prop="documentBackUrl"
            >
              <el-input
                v-model="form.documentBackUrl"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.documentBackUrl')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item
              :label="$t('payment.businessCustomerCorporationMem.form.documentHandyUrl')"
              prop="documentHandyUrl"
            >
              <el-input
                v-model="form.documentHandyUrl"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.documentHandyUrl')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.memberType')" prop="memberType">
              <el-select
                v-model="form.memberType"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.memberType')"
                filterable
              >
                <el-option
                  v-for="dict in memberTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.email')" prop="email">
              <el-input
                v-model="form.email"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.email')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.countryCode')" prop="countryCode">
              <el-input
                v-model="form.countryCode"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.countryCode')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.phoneNumber')" prop="phoneNumber">
              <el-input
                v-model="form.phoneNumber"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.phoneNumber')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.address')" prop="address">
              <el-input
                v-model="form.address"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.address')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.city')" prop="city">
              <el-input
                v-model="form.city"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.city')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.postCode')" prop="postCode">
              <el-input
                v-model="form.postCode"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.postCode')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.country')" prop="country">
              <el-input
                v-model="form.country"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.country')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.kycVeriffUuid')" prop="kycVeriffUuid">
              <el-input
                v-model="form.kycVeriffUuid"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.kycVeriffUuid')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.kycStatus')" prop="kycStatus">
              <el-select
                v-model="form.kycStatus"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.kycStatus')"
                filterable
              >
                <el-option :label="$t('payment.businessCustomerCorporationMem.dict.selectOption')" value="" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.businessName')" prop="businessName">
              <el-input
                v-model="form.businessName"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.businessName')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="$t('payment.businessCustomerCorporationMem.form.businessRegistrationNo')"
              prop="businessRegistrationNo"
            >
              <el-input
                v-model="form.businessRegistrationNo"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.businessRegistrationNo')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('payment.businessCustomerCorporationMem.form.remark')" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                :placeholder="$t('payment.businessCustomerCorporationMem.formPlaceholder.remark')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">
          {{ $t('payment.businessCustomerCorporationMem.dialog.confirmBtn') }}
        </el-button>
        <el-button @click="cancel">{{ $t('payment.businessCustomerCorporationMem.dialog.cancelBtn') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addBusinessCustomerCorporationMem,
  delBusinessCustomerCorporationMem,
  exportBusinessCustomerCorporationMem,
  getBusinessCustomerCorporationMem,
  listBusinessCustomerCorporationMem,
  updateBusinessCustomerCorporationMem
} from '@/api/payment/businessCustomerCorporationMem'
import { listBusinessCustomerCorporation } from '@/api/payment/businessCustomerCorporation'

export default {
  name: 'BusinessCustomerCorporationMem',
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
      // 企业客户公司成员表格数据
      businessCustomerCorporationMemList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 证件类型字典
      documentTypeOptions: [],
      // 企业客户类型字典
      memberTypeOptions: [],
      // 类型字典
      deletedOptions: [],
      // 商户列表
      shopOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        firstName: undefined,
        middleName: undefined,
        lastName: undefined,
        documentType: undefined,
        memberType: undefined,
        createdAt: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        businessCustomerCorporationId: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.businessCustomerCorporationIdRequired'),
            trigger: 'change'
          }
        ],
        documentType: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.documentTypeRequired'),
            trigger: 'change'
          }
        ],
        firstName: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.firstNameRequired'),
            trigger: 'blur'
          }
        ],
        lastName: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.lastNameRequired'),
            trigger: 'blur'
          }
        ],
        memberType: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.memberTypeRequired'),
            trigger: 'change'
          }
        ],
        email: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.emailRequired'),
            trigger: 'blur'
          }
        ],
        countryCode: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.countryCodeRequired'),
            trigger: 'blur'
          }
        ],
        phoneNumber: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.phoneNumberRequired'),
            trigger: 'blur'
          }
        ],
        address: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.addressRequired'),
            trigger: 'blur'
          }
        ],
        city: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.cityRequired'),
            trigger: 'blur'
          }
        ],
        postCode: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.postCodeRequired'),
            trigger: 'blur'
          }
        ],
        country: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporationMem.validation.countryRequired'),
            trigger: 'blur'
          }
        ]
        // 注意：createdAt 和 deleted 是系统字段，不应该设置为必填
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_MEMBER_DOCUMENT_TYPE').then(response => {
      this.documentTypeOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_MEMBER_MEMBER_TYPE').then(response => {
      this.memberTypeOptions = response.data
    })
    this.getDicts('DELETE_STATUS').then(response => {
      this.deletedOptions = response.data
    })
    // 加载商户列表
    this.getShopList()
  },
  methods: {
    /** 查询企业客户公司成员列表 */
    getList() {
      this.loading = true
      listBusinessCustomerCorporationMem(this.queryParams).then(response => {
        this.businessCustomerCorporationMemList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 证件类型字典翻译
    documentTypeFormat(row, column) {
      return this.selectDictLabel(this.documentTypeOptions, row.documentType)
    },
    // 企业客户类型字典翻译
    memberTypeFormat(row, column) {
      return this.selectDictLabel(this.memberTypeOptions, row.memberType)
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
        businessCustomerCorporationId: undefined,
        firstName: undefined,
        middleName: undefined,
        lastName: undefined,
        birthday: undefined,
        nationality: undefined,
        documentType: undefined,
        documentNo: undefined,
        documentFrontUrl: undefined,
        documentBackUrl: undefined,
        documentHandyUrl: undefined,
        memberType: undefined,
        email: undefined,
        countryCode: undefined,
        phoneNumber: undefined,
        address: undefined,
        city: undefined,
        postCode: undefined,
        country: undefined,
        kycVeriffUuid: undefined,
        kycStatus: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        remark: undefined,
        deleted: undefined,
        businessName: undefined,
        businessRegistrationNo: undefined
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
      this.title = this.$t('payment.businessCustomerCorporationMem.dialog.addTitle')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getBusinessCustomerCorporationMem(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.businessCustomerCorporationMem.dialog.editTitle')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBusinessCustomerCorporationMem(this.form).then(response => {
              this.msgSuccess(this.$t('payment.businessCustomerCorporationMem.messages.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addBusinessCustomerCorporationMem(this.form).then(response => {
              this.msgSuccess(this.$t('payment.businessCustomerCorporationMem.messages.addSuccess'))
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
        this.$t('payment.businessCustomerCorporationMem.messages.deleteConfirm', { ids: ids }),
        this.$t('payment.businessCustomerCorporationMem.messages.warning'),
        {
          confirmButtonText: this.$t('payment.businessCustomerCorporationMem.messages.confirm'),
          cancelButtonText: this.$t('payment.businessCustomerCorporationMem.messages.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return delBusinessCustomerCorporationMem(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.businessCustomerCorporationMem.messages.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(
        this.$t('payment.businessCustomerCorporationMem.messages.exportConfirm'),
        this.$t('payment.businessCustomerCorporationMem.messages.warning'),
        {
          confirmButtonText: this.$t('payment.businessCustomerCorporationMem.messages.confirm'),
          cancelButtonText: this.$t('payment.businessCustomerCorporationMem.messages.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return exportBusinessCustomerCorporationMem(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
    },
    /** 获取商户列表 */
    getShopList() {
      listBusinessCustomerCorporation({ pageNum: 1, pageSize: 10000 }).then(response => {
        this.shopOptions = response.rows || []
      })
    }
  }
}
</script>
