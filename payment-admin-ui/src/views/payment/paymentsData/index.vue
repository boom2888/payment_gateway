<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="欺诈方法" prop="methodName">
        <el-select v-model="queryParams.methodName" placeholder="请选择欺诈方法" clearable size="small" filterable>
          <el-option
            v-for="dict in methodNameOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="交易日期" prop="date">
        <el-date-picker clearable size="small"
          v-model="queryParams.date"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择交易日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择订单状态" clearable size="small" filterable>
          <el-option
            v-for="dict in orderStatusOptions"
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
          v-hasPermi="['payment:paymentsData:add']"
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
          v-hasPermi="['payment:paymentsData:edit']"
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
          v-hasPermi="['payment:paymentsData:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:paymentsData:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paymentsDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="订单表商户ID" align="center" prop="merchantId" />
      <el-table-column label="欺诈方法" align="center" prop="methodName" :formatter="methodNameFormat" />
      <el-table-column label="订单ID" align="center" prop="orderId" />
      <el-table-column label="交易日期" align="center" prop="date" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.date, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上传文件ID" align="center" prop="uploadId" />
      <el-table-column label="原始金额" align="center" prop="amount" />
      <el-table-column label="货币" align="center" prop="currency" />
      <el-table-column label="交易结果" align="center" prop="transactionResult" />
      <el-table-column label="欺诈卡BIN" align="center" prop="bin" />
      <el-table-column label="原因代码" align="center" prop="reasonCode" />
      <el-table-column label="转换为欧元汇率" align="center" prop="quote" />
      <el-table-column label="订单表SSO订单ID" align="center" prop="ssoOrderId" />
      <el-table-column label="订单表订单UUID" align="center" prop="orderUuid" />
      <el-table-column label="订单表货币ID" align="center" prop="currencyId" />
      <el-table-column label="欧元金额" align="center" prop="finalAmount" />
      <el-table-column label="订单表创建时间" align="center" prop="orderCreated" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.orderCreated, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="美元金额" align="center" prop="cryptoAmount" />
      <el-table-column label="订单状态" align="center" prop="orderStatus" :formatter="orderStatusFormat" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交易ID" align="center" prop="transactionId" />
      <el-table-column label="发卡国家" align="center" prop="issuerCountry" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:paymentsData:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:paymentsData:remove']"
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

    <!-- 添加或修改支付数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="订单表商户ID" prop="merchantId">
          <el-input v-model="form.merchantId" placeholder="请输入订单表商户ID" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="欺诈方法" prop="methodName">
          <el-select v-model="form.methodName" placeholder="请选择欺诈方法" filterable>
            <el-option
              v-for="dict in methodNameOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="订单ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入订单ID" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="交易日期" prop="date">
          <el-date-picker clearable size="small"
            v-model="form.date"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择交易日期">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="上传文件ID" prop="uploadId">
          <el-input v-model="form.uploadId" placeholder="请输入上传文件ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="原始金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入原始金额" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="货币" prop="currency">
          <el-input v-model="form.currency" placeholder="请输入货币" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="交易结果" prop="transactionResult">
          <el-input v-model="form.transactionResult" placeholder="请输入交易结果" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="欺诈卡BIN" prop="bin">
          <el-input v-model="form.bin" placeholder="请输入欺诈卡BIN" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="原因代码" prop="reasonCode">
          <el-input v-model="form.reasonCode" placeholder="请输入原因代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="转换为欧元汇率" prop="quote">
          <el-input v-model="form.quote" placeholder="请输入转换为欧元汇率" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="订单表SSO订单ID" prop="ssoOrderId">
          <el-input v-model="form.ssoOrderId" placeholder="请输入订单表SSO订单ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="订单表订单UUID" prop="orderUuid">
          <el-input v-model="form.orderUuid" placeholder="请输入订单表订单UUID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="订单表货币ID" prop="currencyId">
          <el-input v-model="form.currencyId" placeholder="请输入订单表货币ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="欧元金额" prop="finalAmount">
          <el-input v-model="form.finalAmount" placeholder="请输入欧元金额" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="订单表创建时间" prop="orderCreated">
          <el-date-picker clearable size="small"
            v-model="form.orderCreated"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择订单表创建时间">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="美元金额" prop="cryptoAmount">
          <el-input v-model="form.cryptoAmount" placeholder="请输入美元金额" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="订单状态" prop="orderStatus">
          <el-select v-model="form.orderStatus" placeholder="请选择订单状态" filterable>
            <el-option
              v-for="dict in orderStatusOptions"
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
        <el-form-item label="更新时间" prop="updatedAt">
          <el-date-picker clearable size="small"
            v-model="form.updatedAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择更新时间">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="交易ID" prop="transactionId">
          <el-input v-model="form.transactionId" placeholder="请输入交易ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="发卡国家" prop="issuerCountry">
          <el-input v-model="form.issuerCountry" placeholder="请输入发卡国家" />
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
import { listPaymentsData, getPaymentsData, delPaymentsData, addPaymentsData, updatePaymentsData, exportPaymentsData } from "@/api/payment/paymentsData";

export default {
  name: "PaymentsData",
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
      // 支付数据表格数据
      paymentsDataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 欺诈方法字典
      methodNameOptions: [],
      // 订单状态字典
      orderStatusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        methodName: undefined,
        date: undefined,
        orderStatus: undefined,
        createdAt: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        merchantId: [
          { required: true, message: "订单表商户ID不能为空", trigger: "blur" }
        ],
        methodName: [
          { required: true, message: "欺诈方法不能为空", trigger: "change" }
        ],
        orderId: [
          { required: true, message: "订单ID不能为空", trigger: "blur" }
        ],
        date: [
          { required: true, message: "交易日期不能为空", trigger: "blur" }
        ],
        uploadId: [
          { required: true, message: "上传文件ID不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "原始金额不能为空", trigger: "blur" }
        ],
        currency: [
          { required: true, message: "货币不能为空", trigger: "blur" }
        ],
        transactionResult: [
          { required: true, message: "交易结果不能为空", trigger: "blur" }
        ],
        reasonCode: [
          { required: true, message: "原因代码不能为空", trigger: "blur" }
        ],
        ssoOrderId: [
          { required: true, message: "订单表SSO订单ID不能为空", trigger: "blur" }
        ],
        orderUuid: [
          { required: true, message: "订单表订单UUID不能为空", trigger: "blur" }
        ],
        currencyId: [
          { required: true, message: "订单表货币ID不能为空", trigger: "blur" }
        ],
        orderCreated: [
          { required: true, message: "订单表创建时间不能为空", trigger: "blur" }
        ],
        cryptoAmount: [
          { required: true, message: "美元金额不能为空", trigger: "blur" }
        ],
        orderStatus: [
          { required: true, message: "订单状态不能为空", trigger: "change" }
        ],
        issuerCountry: [
          { required: true, message: "发卡国家不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("PAYMENTS_DATA_METHOD_NAME").then(response => {
      this.methodNameOptions = response.data;
    });
    this.getDicts("PAYMENTS_DATA_ORDER_STATUS").then(response => {
      this.orderStatusOptions = response.data;
    });
  },
  methods: {
    /** 查询支付数据列表 */
    getList() {
      this.loading = true;
      listPaymentsData(this.queryParams).then(response => {
        this.paymentsDataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 欺诈方法字典翻译
    methodNameFormat(row, column) {
      return this.selectDictLabel(this.methodNameOptions, row.methodName);
    },
    // 订单状态字典翻译
    orderStatusFormat(row, column) {
      return this.selectDictLabel(this.orderStatusOptions, row.orderStatus);
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
        merchantId: undefined,
        methodName: undefined,
        orderId: undefined,
        date: undefined,
        uploadId: undefined,
        amount: undefined,
        currency: undefined,
        transactionResult: undefined,
        bin: undefined,
        reasonCode: undefined,
        quote: undefined,
        ssoOrderId: undefined,
        orderUuid: undefined,
        currencyId: undefined,
        finalAmount: undefined,
        orderCreated: undefined,
        cryptoAmount: undefined,
        orderStatus: undefined,
        createdAt: undefined,
        updatedAt: undefined,
        transactionId: undefined,
        issuerCountry: undefined
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
      this.title = "添加支付数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPaymentsData(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改支付数据";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePaymentsData(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPaymentsData(this.form).then(response => {
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
      this.$confirm('是否确认删除支付数据编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delPaymentsData(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有支付数据数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportPaymentsData(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
