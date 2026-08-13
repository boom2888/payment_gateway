<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="英文名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入英文名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="完整英文" prop="fullName">
        <el-input
          v-model="queryParams.fullName"
          placeholder="请输入完整英文"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="三字母码" prop="iso3">
        <el-input
          v-model="queryParams.iso3"
          placeholder="请输入三字母码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="三数字号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入三数字号"
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
          v-hasPermi="['payment:configCountry:add']"
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
          v-hasPermi="['payment:configCountry:edit']"
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
          v-hasPermi="['payment:configCountry:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:configCountry:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configCountryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="两字母国家代码" align="center" prop="code" v-if="false"/>
      <el-table-column label="英文名称" align="center" prop="name" />
      <el-table-column label="完整英文" align="center" prop="fullName" />
      <el-table-column label="三字母码" align="center" prop="iso3" />
      <el-table-column label="三数字号" align="center" prop="number" />
      <el-table-column label="大洲代码" align="center" prop="continentCode" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:configCountry:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:configCountry:remove']"
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

    <!-- 添加或修改国家配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="英文名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入英文名称" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="完整英文" prop="fullName">
          <el-input v-model="form.fullName" placeholder="请输入完整英文" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="三字母码" prop="iso3">
          <el-input v-model="form.iso3" placeholder="请输入三字母码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="三数字号" prop="number">
          <el-input v-model="form.number" placeholder="请输入三数字号" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="大洲代码" prop="continentCode">
          <el-input v-model="form.continentCode" placeholder="请输入大洲代码" />
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
import { listConfigCountry, getConfigCountry, delConfigCountry, addConfigCountry, updateConfigCountry, exportConfigCountry } from "@/api/payment/configCountry";

export default {
  name: "ConfigCountry",
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
      // 国家配置表格数据
      configCountryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        fullName: undefined,
        iso3: undefined,
        number: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "英文名称不能为空", trigger: "blur" }
        ],
        fullName: [
          { required: true, message: "完整英文不能为空", trigger: "blur" }
        ],
        iso3: [
          { required: true, message: "三字母码不能为空", trigger: "blur" }
        ],
        number: [
          { required: true, message: "三数字号不能为空", trigger: "blur" }
        ],
        continentCode: [
          { required: true, message: "大洲代码不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询国家配置列表 */
    getList() {
      this.loading = true;
      listConfigCountry(this.queryParams).then(response => {
        this.configCountryList = response.rows;
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
        code: undefined,
        name: undefined,
        fullName: undefined,
        iso3: undefined,
        number: undefined,
        continentCode: undefined
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
      this.ids = selection.map(item => item.code)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加国家配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const code = row.code || this.ids
      getConfigCountry(code).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改国家配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.code != null) {
            updateConfigCountry(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addConfigCountry(this.form).then(response => {
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
      const codes = row.code || this.ids;
      this.$confirm('是否确认删除国家配置编号为"' + codes + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delConfigCountry(codes);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有国家配置数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportConfigCountry(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
