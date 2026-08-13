<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="完整卡号" prop="cardNumber">
        <el-input
          v-model="queryParams.cardNumber"
          placeholder="请输入完整卡号"
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
      <el-form-item label="卡类型" prop="cardType">
        <el-select v-model="queryParams.cardType" placeholder="请选择卡类型" clearable size="small" filterable>
          <el-option label="请选择字典生成" value="" />
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
          v-hasPermi="['payment:customerCardToken:add']"
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
          v-hasPermi="['payment:customerCardToken:edit']"
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
          v-hasPermi="['payment:customerCardToken:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:customerCardToken:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="customerCardTokenList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="收单机构ID" align="center" prop="acquirerId" />
      <el-table-column label="令牌" align="center" prop="token" />
      <el-table-column label="完整卡号" align="center" prop="cardNumber" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="deleted" />
      <el-table-column label="记录描述" align="center" prop="remark" />
      <el-table-column label="卡类型" align="center" prop="cardType" />
      <el-table-column label="到期月份" align="center" prop="expiryMonth" />
      <el-table-column label="到期年份" align="center" prop="expiryYear" />
      <el-table-column label="卡BIN" align="center" prop="bin" />
      <el-table-column label="卡方案" align="center" prop="scheme" />
      <el-table-column label="卡货币" align="center" prop="currency" />
      <el-table-column label="发卡银行" align="center" prop="issuer" />
      <el-table-column label="发卡国家代码" align="center" prop="issuerCountry" />
      <el-table-column label="持卡人姓名" align="center" prop="cardholderName" />
      <el-table-column label="持卡人邮箱地址" align="center" prop="cardholderEmailAddress" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:customerCardToken:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:customerCardToken:remove']"
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

    <!-- 添加或修改客户卡令牌对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="收单机构ID" prop="acquirerId">
          <el-input v-model="form.acquirerId" placeholder="请输入收单机构ID" />
        </el-form-item>
    </el-col>
        <el-col :span="24">
        <el-form-item label="令牌" prop="token">
          <el-input v-model="form.token" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        </el-col>
    <el-col :span="12">
        <el-form-item label="完整卡号" prop="cardNumber">
          <el-input v-model="form.cardNumber" placeholder="请输入完整卡号" />
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
          <el-input v-model="form.deleted" placeholder="请输入类型" />
        </el-form-item>
    </el-col>
        <el-col :span="24">
        <el-form-item label="记录描述" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        </el-col>
    <el-col :span="12">
        <el-form-item label="卡类型" prop="cardType">
          <el-select v-model="form.cardType" placeholder="请选择卡类型" filterable>
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="到期月份" prop="expiryMonth">
          <el-input v-model="form.expiryMonth" placeholder="请输入到期月份" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="到期年份" prop="expiryYear">
          <el-input v-model="form.expiryYear" placeholder="请输入到期年份" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="卡BIN" prop="bin">
          <el-input v-model="form.bin" placeholder="请输入卡BIN" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="卡方案" prop="scheme">
          <el-input v-model="form.scheme" placeholder="请输入卡方案" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="卡货币" prop="currency">
          <el-input v-model="form.currency" placeholder="请输入卡货币" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="发卡银行" prop="issuer">
          <el-input v-model="form.issuer" placeholder="请输入发卡银行" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="发卡国家代码" prop="issuerCountry">
          <el-input v-model="form.issuerCountry" placeholder="请输入发卡国家代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="持卡人姓名" prop="cardholderName">
          <el-input v-model="form.cardholderName" placeholder="请输入持卡人姓名" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="持卡人邮箱地址" prop="cardholderEmailAddress">
          <el-input v-model="form.cardholderEmailAddress" placeholder="请输入持卡人邮箱地址" />
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
import { listCustomerCardToken, getCustomerCardToken, delCustomerCardToken, addCustomerCardToken, updateCustomerCardToken, exportCustomerCardToken } from "@/api/payment/customerCardToken";

export default {
  name: "CustomerCardToken",
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
      // 客户卡令牌表格数据
      customerCardTokenList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        token: undefined,
        cardNumber: undefined,
        createdAt: undefined,
        cardType: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        acquirerId: [
          { required: true, message: "收单机构ID不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "类型不能为空", trigger: "blur" }
        ],
        cardType: [
          { required: true, message: "卡类型不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询客户卡令牌列表 */
    getList() {
      this.loading = true;
      listCustomerCardToken(this.queryParams).then(response => {
        this.customerCardTokenList = response.rows;
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
        acquirerId: undefined,
        token: undefined,
        cardNumber: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
        remark: undefined,
        cardType: undefined,
        expiryMonth: undefined,
        expiryYear: undefined,
        bin: undefined,
        scheme: undefined,
        currency: undefined,
        issuer: undefined,
        issuerCountry: undefined,
        cardholderName: undefined,
        cardholderEmailAddress: undefined
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
      this.title = "添加客户卡令牌";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCustomerCardToken(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改客户卡令牌";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCustomerCardToken(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCustomerCardToken(this.form).then(response => {
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
      this.$confirm('是否确认删除客户卡令牌编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCustomerCardToken(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有客户卡令牌数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportCustomerCardToken(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
