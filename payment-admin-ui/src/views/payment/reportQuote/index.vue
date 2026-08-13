<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('payment.reportQuote.sourceCurrency')" prop="fromId">
        <el-select v-model="queryParams.fromId" :placeholder="$t('payment.reportQuote.selectSourceCurrency')" clearable size="small" filterable>
          <el-option
            v-for="dict in fromCodeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.reportQuote.targetCurrency')" prop="toId">
        <el-select v-model="queryParams.toId" :placeholder="$t('payment.reportQuote.selectTargetCurrency')" clearable size="small" filterable>
          <el-option
            v-for="dict in toCodeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('payment.reportQuote.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('payment.reportQuote.reset') }}</el-button>
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
          v-hasPermi="['payment:reportQuote:add']"
        >{{ $t('payment.reportQuote.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['payment:reportQuote:edit']"
        >{{ $t('payment.reportQuote.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['payment:reportQuote:remove']"
        >{{ $t('payment.reportQuote.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:reportQuote:export']"
        >{{ $t('payment.reportQuote.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reportQuoteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('payment.reportQuote.primaryId')" align="center" prop="id" v-if="false"/>
      <el-table-column :label="$t('payment.reportQuote.sourceCurrency')" align="center" prop="fromCode" :formatter="fromCodeFormat" />
      <el-table-column :label="$t('payment.reportQuote.targetCurrency')" align="center" prop="toCode" :formatter="toCodeFormat" />
      <el-table-column :label="$t('payment.reportQuote.createdTime')" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.reportQuote.updatedTime')" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.reportQuote.exchangeRate')" align="center" prop="quote" />
      <el-table-column :label="$t('payment.reportQuote.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:reportQuote:edit']"
>{{ $t('payment.reportQuote.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:reportQuote:remove']"
>{{ $t('payment.reportQuote.delete') }}</el-button>
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

    <!-- 添加或修改汇率报告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>
    <el-col :span="12">
        <el-form-item label="源货币" prop="fromCode">
          <el-select v-model="form.fromCode" placeholder="请选择源货币" @change="handleFromCodeChange" filterable>
            <el-option
              v-for="dict in fromCodeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.remark"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>
    <el-col :span="12">
        <el-form-item label="目标货币" prop="toCode">
          <el-select v-model="form.toCode" placeholder="请选择目标货币" @change="handleToCodeChange" filterable>
            <el-option
              v-for="dict in toCodeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.remark"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="汇率" prop="quote">
          <el-input v-model="form.quote" placeholder="请输入汇率" type="number" />
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
import { listReportQuote, getReportQuote, delReportQuote, addReportQuote, updateReportQuote, exportReportQuote } from "@/api/payment/reportQuote";

export default {
  name: "ReportQuote",
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
      // 汇率报告表格数据
      reportQuoteList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 源货币字典
      fromCodeOptions: [],
      // 目标货币字典
      toCodeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fromCode: undefined,
        toCode: undefined,
      },
      selectedFromCurrency:{},

      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fromCode: [
          { required: true, message: "源货币不能为空", trigger: "change" }
        ],
        fromId: [
          { required: true, message: "源货币代码对应的货币表ID不能为空", trigger: "blur" }
        ],
        toCode: [
          { required: true, message: "目标货币不能为空", trigger: "change" }
        ],
        toId: [
          { required: true, message: "目标货币代码对应的货币表ID不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updatedAt: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        quote: [
          { required: true, message: "汇率不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("CURRENCY_ID").then(response => {
      this.fromCodeOptions = response.data;
      this.toCodeOptions = response.data;
    });
  },
  methods: {
    /** 查询汇率报告列表 */
    getList() {
      this.loading = true;
      listReportQuote(this.queryParams).then(response => {
        this.reportQuoteList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 源货币字典翻译
    fromCodeFormat(row, column) {
      return this.selectDictLabel(this.fromCodeOptions, row.fromId);
    },
    // 目标货币字典翻译
    toCodeFormat(row, column) {
      return this.selectDictLabel(this.toCodeOptions, row.toId);
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
        fromCode: undefined,
        fromId: undefined,
        toCode: undefined,
        toId: undefined,
        createdAt: undefined,
        updatedAt: undefined,
        quote: undefined
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
      this.title = "添加汇率报告";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getReportQuote(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改汇率报告";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateReportQuote(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReportQuote(this.form).then(response => {
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
      this.$confirm('是否确认删除汇率报告编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delReportQuote(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有汇率报告数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportReportQuote(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    handleFromCodeChange(fromCode){
      this.selectedFromCurrency = this.fromCodeOptions.find(item => item.remark === fromCode);
      this.form.fromId = this.selectedFromCurrency.dictValue ? this.selectedFromCurrency.dictValue : undefined;
    },
    handleToCodeChange(toCode){
      this.selectedToCurrency = this.fromCodeOptions.find(item => item.remark === toCode);
      this.form.toId = this.selectedToCurrency ? this.selectedToCurrency.dictValue : undefined;
    },
    abc(a, b){
      console.log(a)
      console.log(b)
    }
  }
};
</script>
