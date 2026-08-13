<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名字" prop="firstName">
        <el-input
          v-model="queryParams.firstName"
          placeholder="请输入名字"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="中间名" prop="middleName">
        <el-input
          v-model="queryParams.middleName"
          placeholder="请输入中间名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓氏" prop="lastName">
        <el-input
          v-model="queryParams.lastName"
          placeholder="请输入姓氏"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="国籍" prop="nationality">
        <el-input
          v-model="queryParams.nationality"
          placeholder="请输入国籍"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="证件类型" prop="documentType">
        <el-select v-model="queryParams.documentType" placeholder="请选择证件类型" clearable size="small" filterable>
          <el-option
            v-for="dict in documentTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="身份证号" prop="documentNo">
        <el-input
          v-model="queryParams.documentNo"
          placeholder="请输入身份证号"
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
          v-hasPermi="['payment:kycInfo:add']"
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
          v-hasPermi="['payment:kycInfo:edit']"
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
          v-hasPermi="['payment:kycInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:kycInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="kycInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="KYC个人信息ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="名字" align="center" prop="firstName" />
      <el-table-column label="中间名" align="center" prop="middleName" />
      <el-table-column label="姓氏" align="center" prop="lastName" />
      <el-table-column label="生日" align="center" prop="birthday" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.birthday, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="国籍" align="center" prop="nationality" />
      <el-table-column label="证件类型" align="center" prop="documentType" :formatter="documentTypeFormat" />
      <el-table-column label="KYC载荷" align="center" prop="kycPayload" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="deleted" :formatter="deletedFormat" />
      <el-table-column label="记录描述" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:kycInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:kycInfo:remove']"
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

    <!-- 添加或修改KYC信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="名字" prop="firstName">
          <el-input v-model="form.firstName" placeholder="请输入名字" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="中间名" prop="middleName">
          <el-input v-model="form.middleName" placeholder="请输入中间名" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="姓氏" prop="lastName">
          <el-input v-model="form.lastName" placeholder="请输入姓氏" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="生日" prop="birthday">
          <el-date-picker clearable size="small"
            v-model="form.birthday"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择生日">
          </el-date-picker>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="国籍" prop="nationality">
          <el-input v-model="form.nationality" placeholder="请输入国籍" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="证件类型" prop="documentType">
          <el-select v-model="form.documentType" placeholder="请选择证件类型" filterable>
            <el-option
              v-for="dict in documentTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="身份证号" prop="documentNo">
          <el-input v-model="form.documentNo" placeholder="请输入身份证号" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="身份证正面" prop="documentFrontUrl">
          <el-input v-model="form.documentFrontUrl" placeholder="请输入身份证正面" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="身份证背面" prop="documentBackUrl">
          <el-input v-model="form.documentBackUrl" placeholder="请输入身份证背面" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="手持身份证" prop="documentHandyUrl">
          <el-input v-model="form.documentHandyUrl" placeholder="请输入手持身份证" />
        </el-form-item>
    </el-col>
        <el-col :span="24">
        <el-form-item label="KYC载荷" prop="kycPayload">
          <el-input v-model="form.kycPayload" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        </el-col>
        <el-col :span="24">
        <el-form-item label="监控名单筛查载荷" prop="kycAmlPayload">
          <el-input v-model="form.kycAmlPayload" type="textarea" placeholder="请输入内容" />
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
        <el-form-item label="状态" prop="deleted">
          <el-select v-model="form.deleted" placeholder="请选择状态" filterable>
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
    <el-col :span="12">
        <el-form-item label="SSO KYC默认为0-假，然后设置SSO KYC为1" prop="ssoKyc">
          <el-select v-model="form.ssoKyc" placeholder="请选择SSO KYC默认为0-假，然后设置SSO KYC为1" filterable>
            <el-option
              v-for="dict in ssoKycOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
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
import { listKycInfo, getKycInfo, delKycInfo, addKycInfo, updateKycInfo, exportKycInfo } from "@/api/payment/kycInfo";

export default {
  name: "KycInfo",
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
      // KYC信息表格数据
      kycInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 证件类型字典
      documentTypeOptions: [],
      // 状态字典
      deletedOptions: [],
      // SSO KYC默认为0-假，然后设置SSO KYC为1字典
      ssoKycOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        firstName: undefined,
        middleName: undefined,
        lastName: undefined,
        nationality: undefined,
        documentType: undefined,
        documentNo: undefined,
        createdAt: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        firstName: [
          { required: true, message: "名字不能为空", trigger: "blur" }
        ],
        lastName: [
          { required: true, message: "姓氏不能为空", trigger: "blur" }
        ],
        birthday: [
          { required: true, message: "生日不能为空", trigger: "blur" }
        ],
        documentType: [
          { required: true, message: "证件类型不能为空", trigger: "change" }
        ],
        documentNo: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        ssoKyc: [
          { required: true, message: "SSO KYC默认为0-假，然后设置SSO KYC为1不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("KYC_INFO_DOCUMENT_TYPE").then(response => {
      this.documentTypeOptions = response.data;
    });
    this.getDicts("DELETE_STATUS").then(response => {
      this.deletedOptions = response.data;
    });
    this.getDicts("KYC_INFO_SSO_KYC").then(response => {
      this.ssoKycOptions = response.data;
    });
  },
  methods: {
    /** 查询KYC信息列表 */
    getList() {
      this.loading = true;
      listKycInfo(this.queryParams).then(response => {
        this.kycInfoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 证件类型字典翻译
    documentTypeFormat(row, column) {
      return this.selectDictLabel(this.documentTypeOptions, row.documentType);
    },
    // 状态字典翻译
    deletedFormat(row, column) {
      return this.selectDictLabel(this.deletedOptions, row.deleted);
    },
    // SSO KYC默认为0-假，然后设置SSO KYC为1字典翻译
    ssoKycFormat(row, column) {
      return this.selectDictLabel(this.ssoKycOptions, row.ssoKyc);
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
        firstName: undefined,
        middleName: undefined,
        lastName: undefined,
        birthday: undefined,
        nationality: undefined,
        documentType: undefined,
        documentNo: undefined,
        documentFrontUrl: undefined,
        documentBackUrl: undefined,
        documentHandyUrl: undefined,
        kycPayload: undefined,
        kycAmlPayload: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
        remark: undefined,
        ssoKyc: undefined
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
      this.title = "添加KYC信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getKycInfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改KYC信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateKycInfo(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addKycInfo(this.form).then(response => {
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
      this.$confirm('是否确认删除KYC信息编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delKycInfo(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有KYC信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportKycInfo(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
