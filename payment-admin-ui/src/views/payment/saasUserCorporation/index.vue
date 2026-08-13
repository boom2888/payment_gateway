<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="公司代码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入公司代码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公司名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入公司名称"
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
          v-hasPermi="['payment:saasUserCorporation:add']"
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
          v-hasPermi="['payment:saasUserCorporation:edit']"
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
          v-hasPermi="['payment:saasUserCorporation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:saasUserCorporation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="saasUserCorporationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="SaaS用户公司ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="公司代码" align="center" prop="code" />
      <el-table-column label="公司名称" align="center" prop="name" />
      <el-table-column label="公司域名" align="center" prop="domain" />
      <el-table-column label="主题配置" align="center" prop="themeUrl" />
      <el-table-column label="合作银行" align="center" prop="partnerBank" />
      <el-table-column label="分成模式" align="center" prop="revenueShare" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:saasUserCorporation:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:saasUserCorporation:remove']"
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

    <!-- 添加或修改SaaS用户公司对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item label="收单机构配置表中的收单机构ID" prop="acquirerId">
          <el-input v-model="form.acquirerId" placeholder="请输入收单机构配置表中的收单机构ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="公司代码" prop="code">
          <el-input v-model="form.code" placeholder="请输入公司代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="公司名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入公司名称" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="公司域名" prop="domain">
          <el-input v-model="form.domain" placeholder="请输入公司域名" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="主题配置" prop="themeUrl">
          <el-input v-model="form.themeUrl" placeholder="请输入主题配置" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="合作银行" prop="partnerBank">
          <el-input v-model="form.partnerBank" placeholder="请输入合作银行" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="排序代码" prop="partnerBankSortCode">
          <el-input v-model="form.partnerBankSortCode" placeholder="请输入排序代码" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="验证码通知邮件的模板ID" prop="emailIdVerifyCode">
          <el-input v-model="form.emailIdVerifyCode" placeholder="请输入验证码通知邮件的模板ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Veriff通知邮件的模板ID" prop="emailIdVeriff">
          <el-input v-model="form.emailIdVeriff" placeholder="请输入Veriff通知邮件的模板ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Veriff地址通知邮件的模板ID" prop="emailIdVeriffAddress">
          <el-input v-model="form.emailIdVeriffAddress" placeholder="请输入Veriff地址通知邮件的模板ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="银行通知邮件的模板ID" prop="emailIdBank">
          <el-input v-model="form.emailIdBank" placeholder="请输入银行通知邮件的模板ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="重置密码通知邮件的模板ID" prop="emailIdForgotPwd">
          <el-input v-model="form.emailIdForgotPwd" placeholder="请输入重置密码通知邮件的模板ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Veriff KYC邀请邮件的模板ID" prop="emailIdVeriffKyc">
          <el-input v-model="form.emailIdVeriffKyc" placeholder="请输入Veriff KYC邀请邮件的模板ID" />
        </el-form-item>
    </el-col>
        <el-col :span="24">
        <el-form-item label="分成模式" prop="revenueShare">
          <el-input v-model="form.revenueShare" type="textarea" placeholder="请输入内容" />
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
    <el-col :span="12">
        <el-form-item label="地址ID" prop="addressId">
          <el-input v-model="form.addressId" placeholder="请输入地址ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Notabene VASP DID" prop="notabeneVaspDid">
          <el-input v-model="form.notabeneVaspDid" placeholder="请输入Notabene VASP DID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Notabene客户端ID" prop="notabeneClientId">
          <el-input v-model="form.notabeneClientId" placeholder="请输入Notabene客户端ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Notabene客户端密钥" prop="notabeneClientSecret">
          <el-input v-model="form.notabeneClientSecret" placeholder="请输入Notabene客户端密钥" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Notabene客户端凭据" prop="notabeneClientCredentials">
          <el-input v-model="form.notabeneClientCredentials" placeholder="请输入Notabene客户端凭据" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="邮件模板发送方" prop="emailTemplateSendfrom">
          <el-input v-model="form.emailTemplateSendfrom" placeholder="请输入邮件模板发送方" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="邮箱地址验证邮件的模板ID" prop="emailIdEmailAddressVerification">
          <el-input v-model="form.emailIdEmailAddressVerification" placeholder="请输入邮箱地址验证邮件的模板ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="法人姓名" prop="legalPersonName">
          <el-input v-model="form.legalPersonName" placeholder="请输入法人姓名" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="订单完成通知邮件的模板ID" prop="emailIdOrderCompletionNotification">
          <el-input v-model="form.emailIdOrderCompletionNotification" placeholder="请输入订单完成通知邮件的模板ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="平台钱包提供商" prop="houseWalletProvider">
          <el-input v-model="form.houseWalletProvider" placeholder="请输入平台钱包提供商" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="旅行规则检查限额" prop="travelRuleCheckLimit">
          <el-input v-model="form.travelRuleCheckLimit" placeholder="请输入旅行规则检查限额" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="旅行规则限额货币ID" prop="travelRuleLimitCurrencyId">
          <el-input v-model="form.travelRuleLimitCurrencyId" placeholder="请输入旅行规则限额货币ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Fireblocks提现账户ID" prop="fireblocksWithdrawalAccountId">
          <el-input v-model="form.fireblocksWithdrawalAccountId" placeholder="请输入Fireblocks提现账户ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Fireblocks运营账户ID" prop="fireblocksOperationalAccountId">
          <el-input v-model="form.fireblocksOperationalAccountId" placeholder="请输入Fireblocks运营账户ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Fireblocks在UI中隐藏" prop="fireblocksHiddenOnUi">
          <el-input v-model="form.fireblocksHiddenOnUi" placeholder="请输入Fireblocks在UI中隐藏" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="Fireblocks自动加油" prop="fireblocksAutoFuel">
          <el-input v-model="form.fireblocksAutoFuel" placeholder="请输入Fireblocks自动加油" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="出金订单交易完成通知邮件的模板ID" prop="emailIdOffOrderCompletionNotification">
          <el-input v-model="form.emailIdOffOrderCompletionNotification" placeholder="请输入出金订单交易完成通知邮件的模板ID" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="出金订单交易失败通知邮件的模板ID" prop="emailIdOffOrderFailedNotification">
          <el-input v-model="form.emailIdOffOrderFailedNotification" placeholder="请输入出金订单交易失败通知邮件的模板ID" />
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
import { listSaasUserCorporation, getSaasUserCorporation, delSaasUserCorporation, addSaasUserCorporation, updateSaasUserCorporation, exportSaasUserCorporation } from "@/api/payment/saasUserCorporation";

export default {
  name: "SaasUserCorporation",
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
      // SaaS用户公司表格数据
      saasUserCorporationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 类型字典
      deletedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: undefined,
        name: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        acquirerId: [
          { required: true, message: "收单机构配置表中的收单机构ID不能为空", trigger: "blur" }
        ],
        code: [
          { required: true, message: "公司代码不能为空", trigger: "blur" }
        ],
        domain: [
          { required: true, message: "公司域名不能为空", trigger: "blur" }
        ],
        partnerBank: [
          { required: true, message: "合作银行不能为空", trigger: "blur" }
        ],
        partnerBankSortCode: [
          { required: true, message: "排序代码不能为空", trigger: "blur" }
        ],
        emailIdVerifyCode: [
          { required: true, message: "验证码通知邮件的模板ID不能为空", trigger: "blur" }
        ],
        emailIdVeriff: [
          { required: true, message: "Veriff通知邮件的模板ID不能为空", trigger: "blur" }
        ],
        emailIdVeriffAddress: [
          { required: true, message: "Veriff地址通知邮件的模板ID不能为空", trigger: "blur" }
        ],
        emailIdBank: [
          { required: true, message: "银行通知邮件的模板ID不能为空", trigger: "blur" }
        ],
        emailIdForgotPwd: [
          { required: true, message: "重置密码通知邮件的模板ID不能为空", trigger: "blur" }
        ],
        emailIdVeriffKyc: [
          { required: true, message: "Veriff KYC邀请邮件的模板ID不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "类型不能为空", trigger: "change" }
        ],
        addressId: [
          { required: true, message: "地址ID不能为空", trigger: "blur" }
        ],
        notabeneVaspDid: [
          { required: true, message: "Notabene VASP DID不能为空", trigger: "blur" }
        ],
        notabeneClientId: [
          { required: true, message: "Notabene客户端ID不能为空", trigger: "blur" }
        ],
        notabeneClientSecret: [
          { required: true, message: "Notabene客户端密钥不能为空", trigger: "blur" }
        ],
        notabeneClientCredentials: [
          { required: true, message: "Notabene客户端凭据不能为空", trigger: "blur" }
        ],
        emailTemplateSendfrom: [
          { required: true, message: "邮件模板发送方不能为空", trigger: "blur" }
        ],
        emailIdEmailAddressVerification: [
          { required: true, message: "邮箱地址验证邮件的模板ID不能为空", trigger: "blur" }
        ],
        emailIdOrderCompletionNotification: [
          { required: true, message: "订单完成通知邮件的模板ID不能为空", trigger: "blur" }
        ],
        houseWalletProvider: [
          { required: true, message: "平台钱包提供商不能为空", trigger: "blur" }
        ],
        travelRuleCheckLimit: [
          { required: true, message: "旅行规则检查限额不能为空", trigger: "blur" }
        ],
        travelRuleLimitCurrencyId: [
          { required: true, message: "旅行规则限额货币ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("DELETE_STATUS").then(response => {
      this.deletedOptions = response.data;
    });
  },
  methods: {
    /** 查询SaaS用户公司列表 */
    getList() {
      this.loading = true;
      listSaasUserCorporation(this.queryParams).then(response => {
        this.saasUserCorporationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        acquirerId: undefined,
        code: undefined,
        name: undefined,
        domain: undefined,
        themeUrl: undefined,
        partnerBank: undefined,
        partnerBankSortCode: undefined,
        emailIdVerifyCode: undefined,
        emailIdVeriff: undefined,
        emailIdVeriffAddress: undefined,
        emailIdBank: undefined,
        emailIdForgotPwd: undefined,
        emailIdVeriffKyc: undefined,
        revenueShare: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
        remark: undefined,
        addressId: undefined,
        notabeneVaspDid: undefined,
        notabeneClientId: undefined,
        notabeneClientSecret: undefined,
        notabeneClientCredentials: undefined,
        emailTemplateSendfrom: undefined,
        emailIdEmailAddressVerification: undefined,
        legalPersonName: undefined,
        emailIdOrderCompletionNotification: undefined,
        houseWalletProvider: undefined,
        travelRuleCheckLimit: undefined,
        travelRuleLimitCurrencyId: undefined,
        fireblocksWithdrawalAccountId: undefined,
        fireblocksOperationalAccountId: undefined,
        fireblocksHiddenOnUi: undefined,
        fireblocksAutoFuel: undefined,
        emailIdOffOrderCompletionNotification: undefined,
        emailIdOffOrderFailedNotification: undefined
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
      this.title = "添加SaaS用户公司";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSaasUserCorporation(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改SaaS用户公司";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSaasUserCorporation(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSaasUserCorporation(this.form).then(response => {
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
      this.$confirm('是否确认删除SaaS用户公司编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSaasUserCorporation(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有SaaS用户公司数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportSaasUserCorporation(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
