<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账户名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入账户名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账户状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择账户状态" clearable size="small" filterable>
          <el-option
            v-for="dict in statusOptions"
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
          v-hasPermi="['payment:account:add']"
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
          v-hasPermi="['payment:account:edit']"
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
          v-hasPermi="['payment:account:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:account:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="accountList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账户ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="账户名称" align="center" prop="name" />
      <el-table-column label="总余额" align="center" prop="totalBalance" />
      <el-table-column label="可用余额" align="center" prop="availableBalance" />
      <el-table-column label="冻结余额" align="center" prop="freezedBalance" />
      <el-table-column label="分类颜色" align="center" prop="color" />
      <el-table-column label="账户状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除状态" align="center" prop="deleted" :formatter="deletedFormat" />
      <el-table-column label="记录描述" align="center" prop="remark" />
<!--      <el-table-column label="交易对手ID" align="center" prop="counterpartyId" />-->
<!--      <el-table-column label="交易对手状态" align="center" prop="counterpartyState" />-->
<!--      <el-table-column label="创建交易对手区分类型" align="center" prop="counterpartyType" :formatter="counterpartyTypeFormat" />-->
<!--      <el-table-column label="第三方账户ID" align="center" prop="internalBankAccountId" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:account:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:account:remove']"
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

    <!-- 添加或修改账户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="账户名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入账户名称" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="银行账户ID" prop="bankAccountId">
          <el-input v-model="form.bankAccountId" placeholder="请输入银行账户ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="总余额" prop="totalBalance">
          <el-input v-model="form.totalBalance" placeholder="请输入总余额" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="可用余额" prop="availableBalance">
          <el-input v-model="form.availableBalance" placeholder="请输入可用余额" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="冻结余额" prop="freezedBalance">
          <el-input v-model="form.freezedBalance" placeholder="请输入冻结余额" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="账户分类颜色" prop="color">
          <el-input v-model="form.color" placeholder="请输入账户分类颜色" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="账户状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择账户状态" filterable>
            <el-option
              v-for="dict in statusOptions"
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
        <el-form-item label="删除状态" prop="deleted">
          <el-select v-model="form.deleted" placeholder="请选择删除状态" filterable>
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
        <el-form-item label="交易对手ID" prop="counterpartyId">
          <el-input v-model="form.counterpartyId" placeholder="请输入交易对手ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手状态" prop="counterpartyState">
          <el-input v-model="form.counterpartyState" placeholder="请输入交易对手状态" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="创建交易对手区分类型" prop="counterpartyType">
          <el-select v-model="form.counterpartyType" placeholder="请选择创建交易对手区分类型" filterable>
            <el-option
              v-for="dict in counterpartyTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="第三方账户ID" prop="internalBankAccountId">
          <el-input v-model="form.internalBankAccountId" placeholder="请输入第三方账户ID" />
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
import { listAccount, getAccount, delAccount, addAccount, updateAccount, exportAccount } from "@/api/payment/account";

export default {
  name: "Account",
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
      // 账户表格数据
      accountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 账户状态字典
      statusOptions: [],
      // 删除状态字典
      deletedOptions: [],
      // 创建交易对手区分类型字典
      counterpartyTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        bankAccountId: undefined,
        status: undefined,
        createdAt: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "账户名称不能为空", trigger: "blur" }
        ],
        totalBalance: [
          { required: true, message: "总余额不能为空", trigger: "blur" }
        ],
        availableBalance: [
          { required: true, message: "可用余额不能为空", trigger: "blur" }
        ],
        freezedBalance: [
          { required: true, message: "冻结余额不能为空", trigger: "blur" }
        ],
        color: [
          { required: true, message: "账户分类颜色不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "账户状态不能为空", trigger: "change" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "删除状态不能为空", trigger: "change" }
        ],
        counterpartyId: [
          { required: true, message: "交易对手ID不能为空", trigger: "blur" }
        ],
        counterpartyState: [
          { required: true, message: "交易对手状态不能为空", trigger: "blur" }
        ],
        counterpartyType: [
          { required: true, message: "创建交易对手区分类型不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("ACCOUNT_STATUS").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("DELETE_STATUS").then(response => {
      this.deletedOptions = response.data;
    });
    this.getDicts("ACCOUNT_COUNTERPARTY_TYPE").then(response => {
      this.counterpartyTypeOptions = response.data;
    });
  },
  methods: {
    /** 查询账户列表 */
    getList() {
      this.loading = true;
      listAccount(this.queryParams).then(response => {
        this.accountList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 账户状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 删除状态字典翻译
    deletedFormat(row, column) {
      return this.selectDictLabel(this.deletedOptions, row.deleted);
    },
    // 创建交易对手区分类型字典翻译
    counterpartyTypeFormat(row, column) {
      return this.selectDictLabel(this.counterpartyTypeOptions, row.counterpartyType);
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
        name: undefined,
        bankAccountId: undefined,
        totalBalance: undefined,
        availableBalance: undefined,
        freezedBalance: undefined,
        color: undefined,
        status: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
        remark: undefined,
        counterpartyId: undefined,
        counterpartyState: undefined,
        counterpartyType: undefined,
        internalBankAccountId: undefined
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
      this.title = "添加账户";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAccount(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改账户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAccount(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAccount(this.form).then(response => {
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
      this.$confirm('是否确认删除账户编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delAccount(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有账户数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportAccount(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
