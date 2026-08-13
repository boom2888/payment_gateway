<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="收款人姓名" prop="counterpartyName">
        <el-input
          v-model="queryParams.counterpartyName"
          placeholder="请输入收款人姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['payment:payToCard:add']"
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
          v-hasPermi="['payment:payToCard:edit']"
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
          v-hasPermi="['payment:payToCard:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:payToCard:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="payToCardList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="发送方" align="center" prop="userId" />
      <el-table-column label="货币ID" align="center" prop="currencyId" />
      <el-table-column label="收款人姓名" align="center" prop="counterpartyName" />
      <el-table-column label="交易对手ID" align="center" prop="counterpartyId" />
      <el-table-column label="链接状态" align="center" prop="state" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付到卡链接ID" align="center" prop="linkId" />
      <el-table-column label="账户ID" align="center" prop="accountId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:payToCard:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:payToCard:remove']"
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

    <!-- 添加或修改支付到卡对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="发送方" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入发送方" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="货币ID" prop="currencyId">
          <el-input v-model="form.currencyId" placeholder="请输入货币ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="收款人姓名" prop="counterpartyName">
          <el-input v-model="form.counterpartyName" placeholder="请输入收款人姓名" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易对手ID" prop="counterpartyId">
          <el-input v-model="form.counterpartyId" placeholder="请输入交易对手ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="链接状态可能的值：[已创建，失败，等待中，激活，过期，已取消，处理中，已处理]" prop="state">
          <el-input v-model="form.state" placeholder="请输入链接状态可能的值：[已创建，失败，等待中，激活，过期，已取消，处理中，已处理]" />
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
        <el-form-item label="支付到卡链接ID" prop="linkId">
          <el-input v-model="form.linkId" placeholder="请输入支付到卡链接ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="账户ID" prop="accountId">
          <el-input v-model="form.accountId" placeholder="请输入账户ID" />
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
import { listPayToCard, getPayToCard, delPayToCard, addPayToCard, updatePayToCard, exportPayToCard } from "@/api/payment/payToCard";

export default {
  name: "PayToCard",
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
      // 支付到卡表格数据
      payToCardList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        counterpartyName: undefined,
        createdAt: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "发送方不能为空", trigger: "blur" }
        ],
        currencyId: [
          { required: true, message: "货币ID不能为空", trigger: "blur" }
        ],
        counterpartyName: [
          { required: true, message: "收款人姓名不能为空", trigger: "blur" }
        ],
        counterpartyId: [
          { required: true, message: "交易对手ID不能为空", trigger: "blur" }
        ],
        state: [
          { required: true, message: "链接状态可能的值：[已创建，失败，等待中，激活，过期，已取消，处理中，已处理]不能为空", trigger: "blur" }
        ],
        linkId: [
          { required: true, message: "支付到卡链接ID不能为空", trigger: "blur" }
        ],
        accountId: [
          { required: true, message: "账户ID不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询支付到卡列表 */
    getList() {
      this.loading = true;
      listPayToCard(this.queryParams).then(response => {
        this.payToCardList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        currencyId: undefined,
        counterpartyName: undefined,
        counterpartyId: undefined,
        state: undefined,
        createdAt: undefined,
        linkId: undefined,
        accountId: undefined
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
      this.title = "添加支付到卡";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPayToCard(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改支付到卡";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePayToCard(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPayToCard(this.form).then(response => {
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
      this.$confirm('是否确认删除支付到卡编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delPayToCard(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有支付到卡数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportPayToCard(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
