<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="付款姓名" prop="payerName">
        <el-input
          v-model="queryParams.payerName"
          placeholder="请输入付款人姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="国家ISO" prop="countryIsoCode">
        <el-input
          v-model="queryParams.countryIsoCode"
          placeholder="请输入国家ISO代码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['payment:thunesPayer:add']"
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
          v-hasPermi="['payment:thunesPayer:edit']"
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
          v-hasPermi="['payment:thunesPayer:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:thunesPayer:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="thunesPayerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="付款人ID" align="center" prop="payerId" />
      <el-table-column label="服务ID" align="center" prop="serviceId" />
      <el-table-column label="付款姓名" align="center" prop="payerName" />
      <el-table-column label="国家ISO" align="center" prop="countryIsoCode" />
      <el-table-column label="货币" align="center" prop="currency" />
      <el-table-column label="内容" align="center" prop="content" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:thunesPayer:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:thunesPayer:remove']"
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

    <!-- 添加或修改Thunes付款人对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="付款人ID" prop="payerId">
          <el-input v-model="form.payerId" placeholder="请输入付款人ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="服务ID" prop="serviceId">
          <el-input v-model="form.serviceId" placeholder="请输入服务ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="付款姓名" prop="payerName">
          <el-input v-model="form.payerName" placeholder="请输入付款人姓名" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="国家ISO" prop="countryIsoCode">
          <el-input v-model="form.countryIsoCode" placeholder="请输入国家ISO代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="货币" prop="currency">
          <el-input v-model="form.currency" placeholder="请输入货币" />
        </el-form-item>
    </el-col>

    <el-form-item label="内容">
        <editor v-model="form.content" :min-height="192"/>
    </el-form-item>
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
import { listThunesPayer, getThunesPayer, delThunesPayer, addThunesPayer, updateThunesPayer, exportThunesPayer } from "@/api/payment/thunesPayer";
import Editor from '@/components/Editor';

export default {
  name: "ThunesPayer",
  components: {
    Editor,
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
      // Thunes付款人表格数据
      thunesPayerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        payerName: undefined,
        countryIsoCode: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        payerId: [
          { required: true, message: "付款人ID不能为空", trigger: "blur" }
        ],
        serviceId: [
          { required: true, message: "服务ID不能为空", trigger: "blur" }
        ],
        payerName: [
          { required: true, message: "付款人姓名不能为空", trigger: "blur" }
        ],
        countryIsoCode: [
          { required: true, message: "国家ISO代码不能为空", trigger: "blur" }
        ],
        currency: [
          { required: true, message: "货币不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "内容不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询Thunes付款人列表 */
    getList() {
      this.loading = true;
      listThunesPayer(this.queryParams).then(response => {
        this.thunesPayerList = response.rows;
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
        payerId: undefined,
        serviceId: undefined,
        payerName: undefined,
        countryIsoCode: undefined,
        currency: undefined,
        content: undefined
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
      this.title = "添加Thunes付款人";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getThunesPayer(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改Thunes付款人";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateThunesPayer(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addThunesPayer(this.form).then(response => {
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
      this.$confirm('是否确认删除Thunes付款人编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delThunesPayer(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有Thunes付款人数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportThunesPayer(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
