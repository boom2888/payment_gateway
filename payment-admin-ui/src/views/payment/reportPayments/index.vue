<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="参考号" prop="reference">
        <el-input
          v-model="queryParams.reference"
          placeholder="请输入参考号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="方法名" prop="methodName">
        <el-select v-model="queryParams.methodName" placeholder="请选择方法名" clearable size="small" filterable>
          <el-option
            v-for="dict in methodNameOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small" filterable>
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="添加时间" prop="createdAt">
        <el-date-picker clearable size="small"
          v-model="queryParams.createdAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择添加时间">
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
          v-hasPermi="['payment:reportPayments:add']"
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
          v-hasPermi="['payment:reportPayments:edit']"
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
          v-hasPermi="['payment:reportPayments:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:reportPayments:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reportPaymentsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="上传文件ID" align="center" prop="uploadId" />
      <el-table-column label="卡BIN" align="center" prop="bin" />
      <el-table-column label="参考号" align="center" prop="reference" />
      <el-table-column label="方法名" align="center" prop="methodName" :formatter="methodNameFormat" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="添加时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新记录时间" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发卡国家" align="center" prop="issuerCountry" />
      <el-table-column label="失败原因" align="center" prop="failReason" />
      <el-table-column label="操作类型" align="center" prop="actionType" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:reportPayments:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:reportPayments:remove']"
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

    <!-- 添加或修改支付报告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="上传文件ID" prop="uploadId">
          <el-input v-model="form.uploadId" placeholder="请输入上传文件ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="卡BIN" prop="bin">
          <el-input v-model="form.bin" placeholder="请输入卡BIN" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="参考号" prop="reference">
          <el-input v-model="form.reference" placeholder="请输入参考号" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="方法名" prop="methodName">
          <el-select v-model="form.methodName" placeholder="请选择方法名" filterable>
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
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" filterable>
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
    </el-col>
    <el-col :span="12">
        <el-form-item label="添加时间" prop="createdAt">
          <el-date-picker clearable size="small"
            v-model="form.createdAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择添加时间">
          </el-date-picker>
        </el-form-item>
    </el-col>
    <el-col :span="12">
        <el-form-item label="更新记录时间" prop="updatedAt">
          <el-date-picker clearable size="small"
            v-model="form.updatedAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择更新记录时间">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="发卡国家" prop="issuerCountry">
          <el-input v-model="form.issuerCountry" placeholder="请输入发卡国家" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="失败原因" prop="failReason">
          <el-input v-model="form.failReason" placeholder="请输入失败原因" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="操作类型" prop="actionType">
          <el-select v-model="form.actionType" placeholder="请选择操作类型" filterable>
            <el-option label="请选择字典生成" value="" />
          </el-select>
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
import { listReportPayments, getReportPayments, delReportPayments, addReportPayments, updateReportPayments, exportReportPayments } from "@/api/payment/reportPayments";

export default {
  name: "ReportPayments",
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
      // 支付报告表格数据
      reportPaymentsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 方法名字典
      methodNameOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        reference: undefined,
        methodName: undefined,
        status: undefined,
        createdAt: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        uploadId: [
          { required: true, message: "上传文件ID不能为空", trigger: "blur" }
        ],
        bin: [
          { required: true, message: "卡BIN不能为空", trigger: "blur" }
        ],
        reference: [
          { required: true, message: "参考号不能为空", trigger: "blur" }
        ],
        methodName: [
          { required: true, message: "方法名不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        createdAt: [
          { required: true, message: "添加时间不能为空", trigger: "blur" }
        ],
        updatedAt: [
          { required: true, message: "更新记录时间不能为空", trigger: "blur" }
        ],
        issuerCountry: [
          { required: true, message: "发卡国家不能为空", trigger: "blur" }
        ],
        failReason: [
          { required: true, message: "失败原因不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("REPORT_PAYMENTS_METHOD_NAME").then(response => {
      this.methodNameOptions = response.data;
    });
  },
  methods: {
    /** 查询支付报告列表 */
    getList() {
      this.loading = true;
      listReportPayments(this.queryParams).then(response => {
        this.reportPaymentsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 方法名字典翻译
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
        bin: undefined,
        reference: undefined,
        methodName: undefined,
        status: undefined,
        createdAt: undefined,
        updatedAt: undefined,
        issuerCountry: undefined,
        failReason: undefined,
        actionType: undefined
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
      this.title = "添加支付报告";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getReportPayments(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改支付报告";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateReportPayments(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReportPayments(this.form).then(response => {
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
      this.$confirm('是否确认删除支付报告编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delReportPayments(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有支付报告数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportReportPayments(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
