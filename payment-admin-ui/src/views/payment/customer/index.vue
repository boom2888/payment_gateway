<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('payment.customer.customerType')" prop="type">
        <el-select v-model="queryParams.type" :placeholder="$t('payment.customer.selectCustomerType')" clearable size="small" filterable>
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.customer.createdTime')" prop="createdAt">
        <el-date-picker clearable size="small"
          v-model="queryParams.createdAt"
          type="date"
          value-format="yyyy-MM-dd"
          :placeholder="$t('payment.customer.selectCreatedTime')">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('payment.customer.referralCode')" prop="referralCode">
        <el-input
          v-model="queryParams.referralCode"
          :placeholder="$t('payment.customer.enterReferralCode')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('payment.customer.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('payment.customer.reset') }}</el-button>
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
          v-hasPermi="['payment:customer:add']"
        >{{ $t('payment.customer.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['payment:customer:edit']"
        >{{ $t('payment.customer.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['payment:customer:remove']"
        >{{ $t('payment.customer.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:customer:export']"
        >{{ $t('payment.customer.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="customerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('payment.customer.saasCustomerId')" align="center" prop="id" v-if="false"/>
      <el-table-column :label="$t('payment.customer.userId')" align="center" prop="userId" />
      <el-table-column :label="$t('payment.customer.customerType')" align="center" prop="type" :formatter="typeFormat" />
      <el-table-column :label="$t('payment.customer.customerManager')" align="center" prop="manager" />
      <el-table-column :label="$t('payment.customer.createdTime')" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.customer.type')" align="center" prop="deleted" :formatter="deletedFormat" />
      <el-table-column :label="$t('payment.customer.recordDescription')" align="center" prop="remark" />
      <el-table-column :label="$t('payment.customer.orders90Days')" align="center" prop="sum90Day" />
      <el-table-column :label="$t('payment.customer.referralCode')" align="center" prop="referralCode" />
      <el-table-column :label="$t('payment.customer.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:customer:edit']"
          >{{ $t('payment.customer.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:customer:remove']"
          >{{ $t('payment.customer.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改客户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.userId')" prop="userId">
          <el-input v-model="form.userId" :placeholder="$t('payment.customer.enterUserId')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.saasUserCorporationId')" prop="saasUserCorporationId">
          <el-input v-model="form.saasUserCorporationId" :placeholder="$t('payment.customer.enterSaasUserCorporationId')" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.customerType')" prop="type">
          <el-select v-model="form.type" :placeholder="$t('payment.customer.selectCustomerType')" filterable>
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
        <el-form-item :label="$t('payment.customer.customerManager')" prop="manager">
          <el-input v-model="form.manager" :placeholder="$t('payment.customer.enterCustomerManager')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.subscriptionLevelId')" prop="subscriptionModelId">
          <el-input v-model="form.subscriptionModelId" :placeholder="$t('payment.customer.enterSubscriptionLevelId')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.riskLevelId')" prop="riskLevel">
          <el-input v-model="form.riskLevel" :placeholder="$t('payment.customer.enterRiskLevelId')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.humanReadableId')" prop="idRef">
          <el-input v-model="form.idRef" :placeholder="$t('payment.customer.enterHumanReadableId')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.vipId')" prop="vipId">
          <el-input v-model="form.vipId" :placeholder="$t('payment.customer.enterVipId')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.merchantId')" prop="merchantId">
          <el-input v-model="form.merchantId" :placeholder="$t('payment.customer.enterMerchantId')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.ssoCustomerId')" prop="ssoCustomerId">
          <el-input v-model="form.ssoCustomerId" :placeholder="$t('payment.customer.enterSsoCustomerId')" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.createdTime')" prop="createdAt">
          <el-date-picker clearable size="small"
            v-model="form.createdAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            :placeholder="$t('payment.customer.selectCreatedTime')">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.createdBy')" prop="createdBy">
          <el-input v-model="form.createdBy" :placeholder="$t('payment.customer.enterCreatedBy')" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.deletedTime')" prop="deletedAt">
          <el-date-picker clearable size="small"
            v-model="form.deletedAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            :placeholder="$t('payment.customer.selectDeletedTime')">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.deletedBy')" prop="deletedBy">
          <el-input v-model="form.deletedBy" :placeholder="$t('payment.customer.enterDeletedBy')" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.type')" prop="deleted">
          <el-select v-model="form.deleted" :placeholder="$t('payment.customer.selectType')" filterable>
            <el-option
              v-for="dict in deletedOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>
        <el-col :span="24">
        <el-form-item :label="$t('payment.customer.recordDescription')" prop="remark">
          <el-input v-model="form.remark" type="textarea" :placeholder="$t('payment.customer.enterRecordDescription')" />
        </el-form-item>
        </el-col>
    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.fireblocksVaultAccountId')" prop="fireblocksVaultAccountId">
          <el-input v-model="form.fireblocksVaultAccountId" :placeholder="$t('payment.customer.enterFireblocksVaultAccountId')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.orders90Days')" prop="sum90Day">
          <el-input v-model="form.sum90Day" :placeholder="$t('payment.customer.enter90DaysOrders')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.customer.referralCode')" prop="referralCode">
          <el-input v-model="form.referralCode" :placeholder="$t('payment.customer.enterReferralCode')" />
        </el-form-item>
    </el-col>

      </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.customer.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.customer.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCustomer, getCustomer, delCustomer, addCustomer, updateCustomer, exportCustomer } from "@/api/payment/customer";

export default {
  name: "Customer",
  components: {
  },
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
      // 客户表格数据
      customerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 客户类型字典
      typeOptions: [],
      // 类型字典
      deletedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: undefined,
        createdAt: undefined,
        referralCode: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: this.$t('payment.customer.userIdRequired'), trigger: "blur" }
        ],
        saasUserCorporationId: [
          { required: true, message: this.$t('payment.customer.saasUserCorporationIdRequired'), trigger: "blur" }
        ],
        type: [
          { required: true, message: this.$t('payment.customer.customerTypeRequired'), trigger: "change" }
        ],
        subscriptionModelId: [
          { required: true, message: this.$t('payment.customer.subscriptionLevelIdRequired'), trigger: "blur" }
        ],
        riskLevel: [
          { required: true, message: this.$t('payment.customer.riskLevelIdRequired'), trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: this.$t('payment.customer.createdTimeRequired'), trigger: "blur" }
        ],
        deleted: [
          { required: true, message: this.$t('payment.customer.typeRequired'), trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("CUSTOMER_TYPE").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("DELETE_STATUS").then(response => {
      this.deletedOptions = response.data;
    });
  },
  methods: {
    /** 查询客户列表 */
    getList() {
      this.loading = true;
      listCustomer(this.queryParams).then(response => {
        this.customerList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 客户类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 类型字典翻译
    deletedFormat(row, column) {
      return this.selectDictLabel(this.deletedOptions, row.deleted);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        userId: undefined,
        saasUserCorporationId: undefined,
        type: undefined,
        manager: undefined,
        subscriptionModelId: undefined,
        riskLevel: undefined,
        idRef: undefined,
        vipId: undefined,
        merchantId: undefined,
        ssoCustomerId: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
        remark: undefined,
        fireblocksVaultAccountId: undefined,
        sum90Day: undefined,
        referralCode: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t('payment.customer.addCustomer');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCustomer(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('payment.customer.editCustomer');
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCustomer(this.form).then(response => {
              this.msgSuccess(this.$t('payment.customer.editSuccess'));
              this.open = false;
              this.getList();
            });
          } else {
            addCustomer(this.form).then(response => {
              this.msgSuccess(this.$t('payment.customer.addSuccess'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm(this.$t('payment.customer.deleteConfirm', { ids: ids }), this.$t('payment.customer.warning'), {
          confirmButtonText: this.$t('payment.customer.confirm'),
          cancelButtonText: this.$t('payment.customer.cancel'),
          type: "warning"
        }).then(function() {
          return delCustomer(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess(this.$t('payment.customer.deleteSuccess'));
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm(this.$t('payment.customer.exportConfirm'), this.$t('payment.customer.warning'), {
          confirmButtonText: this.$t('payment.customer.confirm'),
          cancelButtonText: this.$t('payment.customer.cancel'),
          type: "warning"
        }).then(function() {
          return exportCustomer(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
