<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="银行名称" prop="bankName">
        <el-input
          v-model="queryParams.bankName"
          placeholder="请输入银行名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账户类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择账户类型" clearable size="small" filterable>
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createdAt">
        <el-date-picker clearable size="small"
          v-model="queryParams.createdAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
          v-hasPermi="['payment:bankAccount:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['payment:bankAccount:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['payment:bankAccount:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:bankAccount:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bankAccountList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账户ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="银行名称" align="center" prop="bankName" />
      <el-table-column label="银行地址" align="center" prop="bankAddressId" />
      <el-table-column label="银行排序代码" align="center" prop="sortCode" />
      <el-table-column label="账户号码" align="center" prop="accountNumber" />
      <el-table-column label="国际银行账户号码" align="center" prop="iban" />
      <el-table-column label="Swift代码" align="center" prop="bicSwift" />
      <el-table-column label="货币ID" align="center" prop="currencyId" />
      <el-table-column label="账户类型" align="center" prop="type" :formatter="typeFormat" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="deleted" :formatter="deletedFormat" />
      <el-table-column label="记录描述" align="center" prop="remark" />
      <el-table-column label="路由号码" align="center" prop="routingNumber" />
<!--      <el-table-column label="交易对手ID" align="center" prop="bankCounterpartyId" />-->
      <el-table-column label="发卡国家" align="center" prop="bankCountry" />
      <!--      <el-table-column label="AUD的BSB代码" align="center" prop="bsbCode" />
            <el-table-column label="INR的IFSC" align="center" prop="ifsc" />
            <el-table-column label="MXN的CLABE" align="center" prop="clabe" />
            <el-table-column label="交易对手地址邮政编码" align="center" prop="cptyAddressPostcode" />-->
      <el-table-column label="交易对手地址" align="center" prop="cptyAddress" />
      <el-table-column label="交易对手国家" align="center" prop="cptyCountry" />
      <el-table-column label="交易对手城市" align="center" prop="cptyCity" />
<!--      <el-table-column label="交易对手街道第一行" align="center" prop="cptyStreetLine1" />-->
      <el-table-column label="收款人名字" align="center" prop="firstName" />
      <el-table-column label="收款人姓氏" align="center" prop="lastName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:bankAccount:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:bankAccount:remove']"
          >删除</el-button>
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

    <!-- 添加或修改银行账户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="银行名称" prop="bankName">
          <el-input v-model="form.bankName" placeholder="请输入银行名称" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="银行地址" prop="bankAddressId">
          <el-input v-model="form.bankAddressId" placeholder="请输入银行地址" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="银行排序代码" prop="sortCode">
          <el-input v-model="form.sortCode" placeholder="请输入银行排序代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="账户号码" prop="accountNumber">
          <el-input v-model="form.accountNumber" placeholder="请输入账户号码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="国际银行账户号码" prop="iban">
          <el-input v-model="form.iban" placeholder="请输入国际银行账户号码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Swift代码" prop="bicSwift">
          <el-input v-model="form.bicSwift" placeholder="请输入Swift代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="货币ID" prop="currencyId">
          <el-input v-model="form.currencyId" placeholder="请输入货币ID" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="账户类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择账户类型" filterable>
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
        <el-form-item label="创建时间" prop="createdAt">
          <el-date-picker clearable size="small"
            v-model="form.createdAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择创建时间">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="创建记录的用户ID" prop="createdBy">
          <el-input v-model="form.createdBy" placeholder="请输入创建记录的用户ID" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="删除时间" prop="deletedAt">
          <el-date-picker clearable size="small"
            v-model="form.deletedAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择删除时间">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="最后删除记录的用户ID" prop="deletedBy">
          <el-input v-model="form.deletedBy" placeholder="请输入最后删除记录的用户ID" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="类型" prop="deleted">
          <el-select v-model="form.deleted" placeholder="请选择类型" filterable>
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
        <el-form-item label="记录描述" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        </el-col>
    <el-col :span="12">
        <el-form-item label="路由号码" prop="routingNumber">
          <el-input v-model="form.routingNumber" placeholder="请输入路由号码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手ID" prop="bankCounterpartyId">
          <el-input v-model="form.bankCounterpartyId" placeholder="请输入交易对手ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="发卡国家" prop="bankCountry">
          <el-input v-model="form.bankCountry" placeholder="请输入发卡国家" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="AUD的BSB代码" prop="bsbCode">
          <el-input v-model="form.bsbCode" placeholder="请输入AUD的BSB代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="INR的IFSC" prop="ifsc">
          <el-input v-model="form.ifsc" placeholder="请输入INR的IFSC" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="MXN的CLABE" prop="clabe">
          <el-input v-model="form.clabe" placeholder="请输入MXN的CLABE" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手地址邮政编码" prop="cptyAddressPostcode">
          <el-input v-model="form.cptyAddressPostcode" placeholder="请输入交易对手地址邮政编码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手地址" prop="cptyAddress">
          <el-input v-model="form.cptyAddress" placeholder="请输入交易对手地址" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手国家" prop="cptyCountry">
          <el-input v-model="form.cptyCountry" placeholder="请输入交易对手国家" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手城市" prop="cptyCity">
          <el-input v-model="form.cptyCity" placeholder="请输入交易对手城市" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手街道第一行" prop="cptyStreetLine1">
          <el-input v-model="form.cptyStreetLine1" placeholder="请输入交易对手街道第一行" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="收款人名字" prop="firstName">
          <el-input v-model="form.firstName" placeholder="请输入收款人名字" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="收款人姓氏" prop="lastName">
          <el-input v-model="form.lastName" placeholder="请输入收款人姓氏" />
        </el-form-item>
    </el-col>

      </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBankAccount, getBankAccount, delBankAccount, addBankAccount, updateBankAccount, exportBankAccount } from "@/api/payment/bankAccount";

export default {
  name: "BankAccount",
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
      // 银行账户表格数据
      bankAccountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 账户类型字典
      typeOptions: [],
      // 类型字典
      deletedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bankName: undefined,
        type: undefined,
        createdAt: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        bankName: [
          { required: true, message: "银行名称不能为空", trigger: "blur" }
        ],
        currencyId: [
          { required: true, message: "货币ID不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "账户类型不能为空", trigger: "change" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "类型不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("BANK_ACCOUNT_TYPE").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("DELETE_STATUS").then(response => {
      this.deletedOptions = response.data;
    });
  },
  methods: {
    /** 查询银行账户列表 */
    getList() {
      this.loading = true;
      listBankAccount(this.queryParams).then(response => {
        this.bankAccountList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 账户类型字典翻译
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
        bankName: undefined,
        bankAddressId: undefined,
        sortCode: undefined,
        accountNumber: undefined,
        iban: undefined,
        bicSwift: undefined,
        currencyId: undefined,
        type: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
        remark: undefined,
        routingNumber: undefined,
        bankCounterpartyId: undefined,
        bankCountry: undefined,
        bsbCode: undefined,
        ifsc: undefined,
        clabe: undefined,
        cptyAddressPostcode: undefined,
        cptyAddress: undefined,
        cptyCountry: undefined,
        cptyCity: undefined,
        cptyStreetLine1: undefined,
        firstName: undefined,
        lastName: undefined
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
      this.title = "添加银行账户";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBankAccount(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改银行账户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBankAccount(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBankAccount(this.form).then(response => {
              this.msgSuccess("新增成功");
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
      this.$confirm('是否确认删除银行账户编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delBankAccount(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有银行账户数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportBankAccount(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
