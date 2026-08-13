<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="交易哈希" prop="transactionHash">
        <el-input
          v-model="queryParams.transactionHash"
          placeholder="请输入交易哈希"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="货币数量" prop="cryptoAmount">
        <el-input
          v-model="queryParams.cryptoAmount"
          placeholder="请输入货币数量"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来源钱包" prop="fromWallet">
        <el-input
          v-model="queryParams.fromWallet"
          placeholder="请输入来源钱包"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目标钱包" prop="toWallet">
        <el-input
          v-model="queryParams.toWallet"
          placeholder="请输入目标钱包"
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
          v-hasPermi="['payment:cryptoTransaction:add']"
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
          v-hasPermi="['payment:cryptoTransaction:edit']"
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
          v-hasPermi="['payment:cryptoTransaction:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:cryptoTransaction:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cryptoTransactionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="加密货币ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="订单ID" align="center" prop="orderId" />
      <el-table-column label="网络交易ID" align="center" prop="transactionId" />
      <el-table-column label="交易哈希" align="center" prop="transactionHash" />
      <el-table-column label="货币数量" align="center" prop="cryptoAmount" />
      <el-table-column label="网络费用" align="center" prop="networkFee" />
      <el-table-column label="来源钱包" align="center" prop="fromWallet" />
      <el-table-column label="目标钱包" align="center" prop="toWallet" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="deleted" :formatter="deletedFormat" />
      <el-table-column label="记录描述" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:cryptoTransaction:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:cryptoTransaction:remove']"
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

    <!-- 添加或修改加密货币交易对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="订单ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入订单ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="网络交易ID" prop="transactionId">
          <el-input v-model="form.transactionId" placeholder="请输入网络交易ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易哈希" prop="transactionHash">
          <el-input v-model="form.transactionHash" placeholder="请输入交易哈希" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="货币数量" prop="cryptoAmount">
          <el-input v-model="form.cryptoAmount" placeholder="请输入货币数量" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="网络费用" prop="networkFee">
          <el-input v-model="form.networkFee" placeholder="请输入网络费用" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="来源钱包" prop="fromWallet">
          <el-input v-model="form.fromWallet" placeholder="请输入来源钱包" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="目标钱包" prop="toWallet">
          <el-input v-model="form.toWallet" placeholder="请输入目标钱包" />
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
import { listCryptoTransaction, getCryptoTransaction, delCryptoTransaction, addCryptoTransaction, updateCryptoTransaction, exportCryptoTransaction } from "@/api/payment/cryptoTransaction";

export default {
  name: "CryptoTransaction",
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
      // 加密货币交易表格数据
      cryptoTransactionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 类型字典
      deletedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        transactionHash: undefined,
        cryptoAmount: undefined,
        fromWallet: undefined,
        toWallet: undefined,
        createdAt: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderId: [
          { required: true, message: "订单ID不能为空", trigger: "blur" }
        ],
        cryptoAmount: [
          { required: true, message: "货币数量不能为空", trigger: "blur" }
        ],
        networkFee: [
          { required: true, message: "网络费用不能为空", trigger: "blur" }
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
    this.getDicts("DELETE_STATUS").then(response => {
      this.deletedOptions = response.data;
    });
  },
  methods: {
    /** 查询加密货币交易列表 */
    getList() {
      this.loading = true;
      listCryptoTransaction(this.queryParams).then(response => {
        this.cryptoTransactionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        orderId: undefined,
        transactionId: undefined,
        transactionHash: undefined,
        cryptoAmount: undefined,
        networkFee: undefined,
        fromWallet: undefined,
        toWallet: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
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
      this.title = "添加加密货币交易";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCryptoTransaction(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改加密货币交易";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCryptoTransaction(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCryptoTransaction(this.form).then(response => {
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
      this.$confirm('是否确认删除加密货币交易编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCryptoTransaction(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有加密货币交易数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportCryptoTransaction(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
