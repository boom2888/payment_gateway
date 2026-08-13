<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="欺诈日期" prop="issueDate">
        <el-date-picker clearable size="small"
          v-model="queryParams.issueDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择欺诈日期">
        </el-date-picker>
      </el-form-item>
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
      <el-form-item label="订日期" prop="transactionDate">
        <el-date-picker clearable size="small"
          v-model="queryParams.transactionDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择订日期">
        </el-date-picker>
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
          v-hasPermi="['payment:fraudData:add']"
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
          v-hasPermi="['payment:fraudData:edit']"
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
          v-hasPermi="['payment:fraudData:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:fraudData:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fraudDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="欺诈卡BIN" align="center" prop="bin" />
      <el-table-column label="欺诈日期" align="center" prop="issueDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.issueDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="欺诈美元" align="center" prop="usdAmount" />
      <el-table-column label="欺诈方法" align="center" prop="methodName" :formatter="methodNameFormat" />
      <el-table-column label="订单ID" align="center" prop="trackId" />
      <el-table-column label="订日期" align="center" prop="transactionDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.transactionDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
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
      <el-table-column label="商户ID" align="center" prop="merchantId" />
      <el-table-column label="货币ID" align="center" prop="currencyId" />
      <el-table-column label="法币金额" align="center" prop="fiatAmount" />
      <el-table-column label="订单状态" align="center" prop="status" />
      <el-table-column label="订单创建时间" align="center" prop="orderCreated" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.orderCreated, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:fraudData:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:fraudData:remove']"
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

    <!-- 添加或修改欺诈数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="报告ID" prop="uploadId">
          <el-input v-model="form.uploadId" placeholder="请输入报告ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="report_fraudulent表ID" prop="reportFraudulentId">
          <el-input v-model="form.reportFraudulentId" placeholder="请输入report_fraudulent表ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="欺诈卡BIN" prop="bin">
          <el-input v-model="form.bin" placeholder="请输入欺诈卡BIN" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="欺诈日期" prop="issueDate">
          <el-date-picker clearable size="small"
            v-model="form.issueDate"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择欺诈日期">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="欺诈美元" prop="usdAmount">
          <el-input v-model="form.usdAmount" placeholder="请输入欺诈美元" />
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
        <el-form-item label="订单ID" prop="trackId">
          <el-input v-model="form.trackId" placeholder="请输入订单ID" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="订日期" prop="transactionDate">
          <el-date-picker clearable size="small"
            v-model="form.transactionDate"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择订日期">
          </el-date-picker>
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
        <el-form-item label="SSO订单ID" prop="ssoOrderId">
          <el-input v-model="form.ssoOrderId" placeholder="请输入SSO订单ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="商户ID" prop="merchantId">
          <el-input v-model="form.merchantId" placeholder="请输入商户ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="货币ID" prop="currencyId">
          <el-input v-model="form.currencyId" placeholder="请输入货币ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="法币金额" prop="fiatAmount">
          <el-input v-model="form.fiatAmount" placeholder="请输入法币金额" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="订单状态">
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
    </el-col>
    <el-col :span="12">
        <el-form-item label="订单创建时间" prop="orderCreated">
          <el-date-picker clearable size="small"
            v-model="form.orderCreated"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择订单创建时间">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="订单加密货币金额，订单美元金额" prop="cryptoAmount">
          <el-input v-model="form.cryptoAmount" placeholder="请输入订单加密货币金额，订单美元金额" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="欺诈报告发卡国家" prop="issuerCountry">
          <el-input v-model="form.issuerCountry" placeholder="请输入欺诈报告发卡国家" />
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
import { listFraudData, getFraudData, delFraudData, addFraudData, updateFraudData, exportFraudData } from "@/api/payment/fraudData";

export default {
  name: "FraudData",
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
      // 欺诈数据表格数据
      fraudDataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 欺诈方法字典
      methodNameOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        issueDate: undefined,
        methodName: undefined,
        transactionDate: undefined,
        createdAt: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        uploadId: [
          { required: true, message: "报告ID不能为空", trigger: "blur" }
        ],
        reportFraudulentId: [
          { required: true, message: "report_fraudulent表ID不能为空", trigger: "blur" }
        ],
        bin: [
          { required: true, message: "欺诈卡BIN不能为空", trigger: "blur" }
        ],
        issueDate: [
          { required: true, message: "欺诈日期不能为空", trigger: "blur" }
        ],
        usdAmount: [
          { required: true, message: "欺诈美元不能为空", trigger: "blur" }
        ],
        methodName: [
          { required: true, message: "欺诈方法不能为空", trigger: "change" }
        ],
        trackId: [
          { required: true, message: "订单ID不能为空", trigger: "blur" }
        ],
        transactionDate: [
          { required: true, message: "订日期不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updatedAt: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        ssoOrderId: [
          { required: true, message: "SSO订单ID不能为空", trigger: "blur" }
        ],
        merchantId: [
          { required: true, message: "商户ID不能为空", trigger: "blur" }
        ],
        currencyId: [
          { required: true, message: "货币ID不能为空", trigger: "blur" }
        ],
        fiatAmount: [
          { required: true, message: "法币金额不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "订单状态不能为空", trigger: "blur" }
        ],
        orderCreated: [
          { required: true, message: "订单创建时间不能为空", trigger: "blur" }
        ],
        cryptoAmount: [
          { required: true, message: "订单加密货币金额，订单美元金额不能为空", trigger: "blur" }
        ],
        issuerCountry: [
          { required: true, message: "欺诈报告发卡国家不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("FRAUD_DATA_METHOD_NAME").then(response => {
      this.methodNameOptions = response.data;
    });
  },
  methods: {
    /** 查询欺诈数据列表 */
    getList() {
      this.loading = true;
      listFraudData(this.queryParams).then(response => {
        this.fraudDataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 欺诈方法字典翻译
    methodNameFormat(row, column) {
      return this.selectDictLabel(this.methodNameOptions, row.methodName);
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
        uploadId: undefined,
        reportFraudulentId: undefined,
        bin: undefined,
        issueDate: undefined,
        usdAmount: undefined,
        methodName: undefined,
        trackId: undefined,
        transactionDate: undefined,
        createdAt: undefined,
        updatedAt: undefined,
        ssoOrderId: undefined,
        merchantId: undefined,
        currencyId: undefined,
        fiatAmount: undefined,
        status: 0,
        orderCreated: undefined,
        cryptoAmount: undefined,
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
      this.title = "添加欺诈数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFraudData(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改欺诈数据";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFraudData(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFraudData(this.form).then(response => {
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
      this.$confirm('是否确认删除欺诈数据编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delFraudData(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有欺诈数据数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportFraudData(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
