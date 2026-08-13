<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="交易类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择交易类型" clearable size="small" filterable>
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="出入金" prop="inOut">
        <el-select v-model="queryParams.inOut" placeholder="请选择入金或出金" clearable size="small" filterable>
          <el-option
            v-for="dict in inOutOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="交易状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择交易状态" clearable size="small" filterable>
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="内外交易" prop="internal">
        <el-select v-model="queryParams.internal" placeholder="请选择内部交易类型" clearable size="small" filterable>
          <el-option
            v-for="dict in internalOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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
          v-hasPermi="['payment:transaction:add']"
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
          v-hasPermi="['payment:transaction:edit']"
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
          v-hasPermi="['payment:transaction:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:transaction:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="transactionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="交易ID" align="center" prop="id" v-if="false"/>-->
<!--      <el-table-column label="订单ID" align="center" prop="orderId" />-->
<!--      <el-table-column label="账户信息的账户ID" align="center" prop="accountId" />-->
      <el-table-column label="货币代码" align="center" prop="currencyCode" />
<!--      <el-table-column label="对手受益人" align="center" prop="cpName" />-->
<!--      <el-table-column label="交易对手邮箱" align="center" prop="cpEmail" />-->
<!--      <el-table-column label="交易对手银行名称" align="center" prop="cpBankName" />-->
<!--      <el-table-column label="交易对手排序代码" align="center" prop="cpSortCode" />-->
<!--      <el-table-column label="对手账户名称" align="center" prop="cpAccountName" />-->
      <el-table-column label="对手账户" align="center" prop="cpAccountNumber" />
<!--      <el-table-column label="交易对手国际银行账户号码" align="center" prop="cpIban" />-->
<!--      <el-table-column label="交易对手BIC Swift" align="center" prop="cpBicSwift" />-->
      <el-table-column label="交易类型" align="center" prop="type" :formatter="typeFormat" />
      <el-table-column label="交易金额" align="center" prop="amount" />
      <el-table-column label="交易明细" align="center" prop="feeDetail" />
      <el-table-column label="交易金额" align="center" prop="feeAmount" />
      <el-table-column label="出入金" align="center" prop="inOut" :formatter="inOutFormat" />
      <el-table-column label="交易状态" align="center" prop="status" :formatter="statusFormat" />
<!--      <el-table-column label="交易结果注释" align="center" prop="comment" />-->
      <el-table-column label="内外交易" align="center" prop="internal" :formatter="internalFormat" />
<!--      <el-table-column label="客户输入的交易描述" align="center" prop="description" />-->
<!--      <el-table-column label="风险等级ID" align="center" prop="riskLevel" />-->
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:transaction:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:transaction:remove']"
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

    <!-- 添加或修改交易对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="订单ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入订单ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="账户信息的账户ID" prop="accountId">
          <el-input v-model="form.accountId" placeholder="请输入账户信息的账户ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易货币代码" prop="currencyCode">
          <el-input v-model="form.currencyCode" placeholder="请输入交易货币代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手受益人姓名" prop="cpName">
          <el-input v-model="form.cpName" placeholder="请输入交易对手受益人姓名" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手邮箱" prop="cpEmail">
          <el-input v-model="form.cpEmail" placeholder="请输入交易对手邮箱" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手银行名称" prop="cpBankName">
          <el-input v-model="form.cpBankName" placeholder="请输入交易对手银行名称" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手排序代码" prop="cpSortCode">
          <el-input v-model="form.cpSortCode" placeholder="请输入交易对手排序代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手账户名称" prop="cpAccountName">
          <el-input v-model="form.cpAccountName" placeholder="请输入交易对手账户名称" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手账户号码" prop="cpAccountNumber">
          <el-input v-model="form.cpAccountNumber" placeholder="请输入交易对手账户号码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手国际银行账户号码" prop="cpIban">
          <el-input v-model="form.cpIban" placeholder="请输入交易对手国际银行账户号码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手BIC Swift" prop="cpBicSwift">
          <el-input v-model="form.cpBicSwift" placeholder="请输入交易对手BIC Swift" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="交易类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择交易类型" filterable>
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
        <el-form-item label="交易金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入交易货币金额" />
        </el-form-item>
    </el-col>
        <el-col :span="24">
        <el-form-item label="交易明细" prop="feeDetail">
          <el-input v-model="form.feeDetail" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        </el-col>
    <el-col :span="12">
        <el-form-item label="交易金额" prop="feeAmount">
          <el-input v-model="form.feeAmount" placeholder="请输入交易费用金额" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="出入金" prop="inOut">
          <el-select v-model="form.inOut" placeholder="请选择入金或出金" filterable>
            <el-option
              v-for="dict in inOutOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>
    <el-col :span="12">
        <el-form-item label="交易状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择交易状态" filterable>
            <el-option
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>
        <el-col :span="24">
        <el-form-item label="交易结果注释" prop="comment">
          <el-input v-model="form.comment" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        </el-col>
    <el-col :span="12">
        <el-form-item label="内外交易" prop="internal">
          <el-select v-model="form.internal" placeholder="请选择内部交易类型" filterable>
            <el-option
              v-for="dict in internalOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>
        <el-col :span="24">
        <el-form-item label="客户输入的交易描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        </el-col>
    <el-col :span="12">
        <el-form-item label="风险等级ID" prop="riskLevel">
          <el-input v-model="form.riskLevel" placeholder="请输入风险等级ID" />
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
import { listTransaction, getTransaction, delTransaction, addTransaction, updateTransaction, exportTransaction } from "@/api/payment/transaction";

export default {
  name: "Transaction",
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
      // 交易表格数据
      transactionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 交易类型字典
      typeOptions: [],
      // 入金或出金字典
      inOutOptions: [],
      // 交易状态字典
      statusOptions: [],
      // 内部交易类型字典
      internalOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        currencyCode: undefined,
        cpName: undefined,
        cpBankName: undefined,
        cpAccountNumber: undefined,
        type: undefined,
        inOut: undefined,
        status: undefined,
        internal: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountId: [
          { required: true, message: "账户信息的账户ID不能为空", trigger: "blur" }
        ],
        currencyCode: [
          { required: true, message: "交易货币代码不能为空", trigger: "blur" }
        ],
        cpName: [
          { required: true, message: "交易对手受益人姓名不能为空", trigger: "blur" }
        ],
        cpEmail: [
          { required: true, message: "交易对手邮箱不能为空", trigger: "blur" }
        ],
        cpBankName: [
          { required: true, message: "交易对手银行名称不能为空", trigger: "blur" }
        ],
        cpSortCode: [
          { required: true, message: "交易对手排序代码不能为空", trigger: "blur" }
        ],
        cpAccountName: [
          { required: true, message: "交易对手账户名称不能为空", trigger: "blur" }
        ],
        cpAccountNumber: [
          { required: true, message: "交易对手账户号码不能为空", trigger: "blur" }
        ],
        cpIban: [
          { required: true, message: "交易对手国际银行账户号码不能为空", trigger: "blur" }
        ],
        cpBicSwift: [
          { required: true, message: "交易对手BIC Swift不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "交易类型不能为空", trigger: "change" }
        ],
        amount: [
          { required: true, message: "交易货币金额不能为空", trigger: "blur" }
        ],
        feeDetail: [
          { required: true, message: "交易费用明细不能为空", trigger: "blur" }
        ],
        feeAmount: [
          { required: true, message: "交易费用金额不能为空", trigger: "blur" }
        ],
        inOut: [
          { required: true, message: "入金或出金不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "交易状态不能为空", trigger: "change" }
        ],
        riskLevel: [
          { required: true, message: "风险等级ID不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("TRANSACTION_TYPE").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("TRANSACTION_IN_OUT").then(response => {
      this.inOutOptions = response.data;
    });
    this.getDicts("TRANSACTION_STATUS").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("TRANSACTION_INTERNAL").then(response => {
      this.internalOptions = response.data;
    });
  },
  methods: {
    /** 查询交易列表 */
    getList() {
      this.loading = true;
      listTransaction(this.queryParams).then(response => {
        this.transactionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 交易类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 入金或出金字典翻译
    inOutFormat(row, column) {
      return this.selectDictLabel(this.inOutOptions, row.inOut);
    },
    // 交易状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 内部交易类型字典翻译
    internalFormat(row, column) {
      return this.selectDictLabel(this.internalOptions, row.internal);
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
        orderId: undefined,
        accountId: undefined,
        currencyCode: undefined,
        cpName: undefined,
        cpEmail: undefined,
        cpBankName: undefined,
        cpSortCode: undefined,
        cpAccountName: undefined,
        cpAccountNumber: undefined,
        cpIban: undefined,
        cpBicSwift: undefined,
        type: undefined,
        amount: undefined,
        feeDetail: undefined,
        feeAmount: undefined,
        inOut: undefined,
        status: undefined,
        comment: undefined,
        internal: undefined,
        description: undefined,
        riskLevel: undefined,
        createdAt: undefined,
        createdBy: undefined
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
      this.title = "添加交易";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTransaction(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改交易";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTransaction(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTransaction(this.form).then(response => {
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
      this.$confirm('是否确认删除交易编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTransaction(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有交易数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTransaction(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
