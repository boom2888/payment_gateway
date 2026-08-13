<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="电话号码" prop="phoneNumber">
        <el-input
          v-model="queryParams.phoneNumber"
          placeholder="请输入电话号码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择用户类型" clearable size="small" filterable>
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="激活状态" prop="activated">
        <el-select v-model="queryParams.activated" placeholder="请选择激活状态" clearable size="small" filterable>
          <el-option
            v-for="dict in activatedOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
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
          v-hasPermi="['payment:user:add']"
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
          v-hasPermi="['payment:user:edit']"
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
          v-hasPermi="['payment:user:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:user:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="登录邮箱" align="center" prop="email" />
      <el-table-column label="电话号码" align="center" prop="phoneNumber" />
      <el-table-column label="登录密码" align="center" prop="password" />
      <el-table-column label="显示名称" align="center" prop="displayName" />
      <el-table-column label="用户类型" align="center" prop="type" :formatter="typeFormat" />
      <el-table-column label="KYC状态" align="center" prop="kycStatus" :formatter="kycStatusFormat" />
      <el-table-column label="用户角色默认角色默认客户个人角色" align="center" prop="role" />
      <el-table-column label="激活状态" align="center" prop="activated" :formatter="activatedFormat" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="deleted" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:user:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:user:remove']"
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

    <!-- 添加或修改用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="登录邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入登录邮箱" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="电话国家代码" prop="countryCode">
          <el-input v-model="form.countryCode" placeholder="请输入电话国家代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="电话号码" prop="phoneNumber">
          <el-input v-model="form.phoneNumber" placeholder="请输入电话号码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="登录密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入登录密码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="显示名称" prop="displayName">
          <el-input v-model="form.displayName" placeholder="请输入显示名称" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="用户类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择用户类型" filterable>
            <el-option
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="用户头像文件URL" prop="avatar">
          <el-input v-model="form.avatar" placeholder="请输入用户头像文件URL" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="MFA设置" prop="mfaSetting">
          <el-select v-model="form.mfaSetting" placeholder="请选择MFA设置" filterable>
            <el-option
              v-for="dict in mfaSettingOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="通知设置：3位二进制" prop="notificationSetting">
          <el-input v-model="form.notificationSetting" placeholder="请输入通知设置：3位二进制" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="KYC信息ID" prop="kycId">
          <el-input v-model="form.kycId" placeholder="请输入KYC信息ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="传递给Veriff的UUID" prop="kycVeriffUuid">
          <el-input v-model="form.kycVeriffUuid" placeholder="请输入传递给Veriff的UUID" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="KYC状态" prop="kycStatus">
          <el-select v-model="form.kycStatus" placeholder="请选择KYC状态" filterable>
            <el-option
              v-for="dict in kycStatusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="用户角色默认角色默认客户个人角色" prop="role">
          <el-input v-model="form.role" placeholder="请输入用户角色默认角色默认客户个人角色" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="激活状态" prop="activated">
          <el-select v-model="form.activated" placeholder="请选择激活状态" filterable>
            <el-option
              v-for="dict in activatedOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="人类可读ID" prop="idRef">
          <el-input v-model="form.idRef" placeholder="请输入人类可读ID" />
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
import { listUser, getUser, delUser, addUser, updateUser, exportUser } from "@/api/payment/user";

export default {
  name: "User",
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
      // 用户表格数据
      userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 用户类型字典
      typeOptions: [],
      // MFA设置字典
      mfaSettingOptions: [],
      // KYC状态字典
      kycStatusOptions: [],
      // 激活状态字典
      activatedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        phoneNumber: undefined,
        type: undefined,
        activated: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        kycStatus: [
          { required: true, message: "KYC状态不能为空", trigger: "change" }
        ],
        role: [
          { required: true, message: "用户角色默认角色默认客户个人角色不能为空", trigger: "blur" }
        ],
        activated: [
          { required: true, message: "激活状态不能为空", trigger: "change" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "类型不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("USER_TYPE").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("USER_MFA_SETTING").then(response => {
      this.mfaSettingOptions = response.data;
    });
    this.getDicts("USER_KYC_STATUS").then(response => {
      this.kycStatusOptions = response.data;
    });
    this.getDicts("USER_ACTIVATED").then(response => {
      this.activatedOptions = response.data;
    });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.queryParams).then(response => {
        this.userList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 用户类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // MFA设置字典翻译
    mfaSettingFormat(row, column) {
      return this.selectDictLabel(this.mfaSettingOptions, row.mfaSetting);
    },
    // KYC状态字典翻译
    kycStatusFormat(row, column) {
      return this.selectDictLabel(this.kycStatusOptions, row.kycStatus);
    },
    // 激活状态字典翻译
    activatedFormat(row, column) {
      return this.selectDictLabel(this.activatedOptions, row.activated);
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
        email: undefined,
        countryCode: undefined,
        phoneNumber: undefined,
        password: undefined,
        displayName: undefined,
        type: undefined,
        avatar: undefined,
        mfaSetting: undefined,
        notificationSetting: undefined,
        kycId: undefined,
        kycVeriffUuid: undefined,
        kycStatus: undefined,
        role: undefined,
        activated: undefined,
        idRef: undefined,
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
      this.title = "添加用户";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUser(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUser(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
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
      this.$confirm('是否确认删除用户编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delUser(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有用户数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportUser(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
