<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
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
          v-hasPermi="['payment:settlementLedger:add']"
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
          v-hasPermi="['payment:settlementLedger:edit']"
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
          v-hasPermi="['payment:settlementLedger:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:settlementLedger:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="settlementLedgerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="对账ID" align="center" prop="reconId" />
      <el-table-column label="商户ID" align="center" prop="merchantId" />
      <el-table-column label="结算金额" align="center" prop="settlementAmount" />
      <el-table-column label="拒付退款金额" align="center" prop="chargebacksRefundsAmount" />
      <el-table-column label="滚动准备金扣除" align="center" prop="rollingReserveDeducted" />
      <el-table-column label="滚动准备金支付" align="center" prop="rollingReservePayout" />
      <el-table-column label="支付金额" align="center" prop="payout" />
      <el-table-column label="备注" align="center" prop="notes" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="记录描述" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:settlementLedger:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:settlementLedger:remove']"
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

    <!-- 添加或修改结算分类账对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="对账ID" prop="reconId">
          <el-input v-model="form.reconId" placeholder="请输入对账ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="商户ID" prop="merchantId">
          <el-input v-model="form.merchantId" placeholder="请输入商户ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="结算金额" prop="settlementAmount">
          <el-input v-model="form.settlementAmount" placeholder="请输入结算金额" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="拒付退款金额" prop="chargebacksRefundsAmount">
          <el-input v-model="form.chargebacksRefundsAmount" placeholder="请输入拒付退款金额" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="滚动准备金扣除" prop="rollingReserveDeducted">
          <el-input v-model="form.rollingReserveDeducted" placeholder="请输入滚动准备金扣除" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="滚动准备金支付" prop="rollingReservePayout">
          <el-input v-model="form.rollingReservePayout" placeholder="请输入滚动准备金支付" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="支付金额" prop="payout">
          <el-input v-model="form.payout" placeholder="请输入支付金额" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="备注" prop="notes">
          <el-input v-model="form.notes" placeholder="请输入备注" />
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
        <el-form-item label="创建者ID" prop="createdBy">
          <el-input v-model="form.createdBy" placeholder="请输入创建者ID" />
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
        <el-form-item label="删除者ID" prop="deletedBy">
          <el-input v-model="form.deletedBy" placeholder="请输入删除者ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="记录描述" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入记录描述" />
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
import { listSettlementLedger, getSettlementLedger, delSettlementLedger, addSettlementLedger, updateSettlementLedger, exportSettlementLedger } from "@/api/payment/settlementLedger";

export default {
  name: "SettlementLedger",
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
      // 结算分类账表格数据
      settlementLedgerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        createdAt: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询结算分类账列表 */
    getList() {
      this.loading = true;
      listSettlementLedger(this.queryParams).then(response => {
        this.settlementLedgerList = response.rows;
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
        reconId: undefined,
        merchantId: undefined,
        settlementAmount: undefined,
        chargebacksRefundsAmount: undefined,
        rollingReserveDeducted: undefined,
        rollingReservePayout: undefined,
        payout: undefined,
        notes: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        remark: undefined
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
      this.title = "添加结算分类账";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSettlementLedger(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改结算分类账";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSettlementLedger(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSettlementLedger(this.form).then(response => {
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
      this.$confirm('是否确认删除结算分类账编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSettlementLedger(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有结算分类账数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportSettlementLedger(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
