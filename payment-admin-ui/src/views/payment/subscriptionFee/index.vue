<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用选项" prop="option">
        <el-select v-model="queryParams.option" placeholder="请选择应用选项" clearable size="small" filterable>
          <el-option
            v-for="dict in optionOptions"
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
          v-hasPermi="['payment:subscriptionFee:add']"
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
          v-hasPermi="['payment:subscriptionFee:edit']"
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
          v-hasPermi="['payment:subscriptionFee:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:subscriptionFee:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="subscriptionFeeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订阅模型" align="center" prop="id" v-if="false"/>
      <el-table-column label="订阅模型ID" align="center" prop="subscriptionModelId" />
      <el-table-column label="固定金额" align="center" prop="fixedAmount" />
      <el-table-column label="百分比" align="center" prop="percentage" />
      <el-table-column label="应用选项" align="center" prop="option" :formatter="optionFormat" />
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
            v-hasPermi="['payment:subscriptionFee:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:subscriptionFee:remove']"
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

    <!-- 添加或修改订阅费用对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="订阅模型ID" prop="subscriptionModelId">
          <el-input v-model="form.subscriptionModelId" placeholder="请输入订阅模型ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="订阅级别：默认，高级" prop="feeTypeId">
          <el-input v-model="form.feeTypeId" placeholder="请输入订阅级别：默认，高级" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="固定金额" prop="fixedAmount">
          <el-input v-model="form.fixedAmount" placeholder="请输入固定金额" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="百分比" prop="percentage">
          <el-input v-model="form.percentage" placeholder="请输入百分比" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="应用选项" prop="option">
          <el-select v-model="form.option" placeholder="请选择应用选项" filterable>
            <el-option
              v-for="dict in optionOptions"
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
import { listSubscriptionFee, getSubscriptionFee, delSubscriptionFee, addSubscriptionFee, updateSubscriptionFee, exportSubscriptionFee } from "@/api/payment/subscriptionFee";

export default {
  name: "SubscriptionFee",
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
      // 订阅费用表格数据
      subscriptionFeeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 应用选项字典
      optionOptions: [],
      // 类型字典
      deletedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        option: undefined,
        createdAt: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        subscriptionModelId: [
          { required: true, message: "订阅模型ID不能为空", trigger: "blur" }
        ],
        feeTypeId: [
          { required: true, message: "订阅级别：默认，高级不能为空", trigger: "blur" }
        ],
        fixedAmount: [
          { required: true, message: "固定金额不能为空", trigger: "blur" }
        ],
        percentage: [
          { required: true, message: "百分比不能为空", trigger: "blur" }
        ],
        option: [
          { required: true, message: "应用选项不能为空", trigger: "change" }
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
    this.getDicts("SUBSCRIPTION_FEE_OPTION").then(response => {
      this.optionOptions = response.data;
    });
    this.getDicts("DELETE_STATUS").then(response => {
      this.deletedOptions = response.data;
    });
  },
  methods: {
    /** 查询订阅费用列表 */
    getList() {
      this.loading = true;
      listSubscriptionFee(this.queryParams).then(response => {
        this.subscriptionFeeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 应用选项字典翻译
    optionFormat(row, column) {
      return this.selectDictLabel(this.optionOptions, row.option);
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
        subscriptionModelId: undefined,
        feeTypeId: undefined,
        fixedAmount: undefined,
        percentage: undefined,
        option: undefined,
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
      this.title = "添加订阅费用";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSubscriptionFee(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改订阅费用";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSubscriptionFee(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSubscriptionFee(this.form).then(response => {
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
      this.$confirm('是否确认删除订阅费用编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSubscriptionFee(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有订阅费用数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportSubscriptionFee(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
