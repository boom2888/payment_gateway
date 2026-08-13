<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
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
          v-hasPermi="['payment:movenmentData:add']"
        >{{ $t('common.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['payment:movenmentData:edit']"
        >{{ $t('common.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['payment:movenmentData:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payment:movenmentData:export']"
        >{{ $t('common.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="movenmentDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" v-if="false"/>
      <el-table-column label="multiClient" align="center" prop="multiClient" />
      <el-table-column label="clientName" align="center" prop="clientName" />
      <el-table-column label="acquirerBank" align="center" prop="acquirerBank" />
      <el-table-column label="movementDate" align="center" prop="movementDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.movementDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="movementType" align="center" prop="movementType" />
      <el-table-column label="movementDetails" align="center" prop="movementDetails" />
      <el-table-column label="paymentMethod" align="center" prop="paymentMethod" />
      <el-table-column label="is3d" align="center" prop="is3d" />
      <el-table-column label="cardType" align="center" prop="cardType" />
      <el-table-column label="country" align="center" prop="country" />
      <el-table-column label="transactionResult" align="center" prop="transactionResult" />
      <el-table-column label="transactionId" align="center" prop="transactionId" />
      <el-table-column label="creditCardLast4Digits" align="center" prop="creditCardLast4Digits" />
      <el-table-column label="clientUniqueId" align="center" prop="clientUniqueId" />
      <el-table-column label="currency" align="center" prop="currency" />
      <el-table-column label="amount" align="center" prop="amount" />
      <el-table-column label="discountFee" align="center" prop="discountFee" />
      <el-table-column label="transactionFee" align="center" prop="transactionFee" />
      <el-table-column label="serviceFee" align="center" prop="serviceFee" />
      <el-table-column label="reserve" align="center" prop="reserve" />
      <el-table-column label="vatOnFees" align="center" prop="vatOnFees" />
      <el-table-column label="netAmount" align="center" prop="netAmount" />
      <el-table-column label="settlementCurrency" align="center" prop="settlementCurrency" />
      <el-table-column label="settlementAmount" align="center" prop="settlementAmount" />
      <el-table-column label="settlementReserve" align="center" prop="settlementReserve" />
      <el-table-column label="jurisdiction" align="center" prop="jurisdiction" />
      <el-table-column label="prepaid" align="center" prop="prepaid" />
      <el-table-column label="plusCommission" align="center" prop="plusCommission" />
      <el-table-column label="externalTokenProvider" align="center" prop="externalTokenProvider" />
      <el-table-column label="onUs" align="center" prop="onUs" />
      <el-table-column label="orderId" align="center" prop="orderId" />
      <el-table-column label="isInterchangePlusPlus" align="center" prop="isInterchangePlusPlus" />
      <el-table-column label="resellerFee" align="center" prop="resellerFee" />
      <el-table-column label="isOnlinePayout" align="center" prop="isOnlinePayout" />
      <el-table-column label="movementId" align="center" prop="movementId" />
      <el-table-column label="schemeFee" align="center" prop="schemeFee" />
      <el-table-column label="resellerCommission" align="center" prop="resellerCommission" />
      <el-table-column label="interchangeFee" align="center" prop="interchangeFee" />
      <el-table-column label="siteId" align="center" prop="siteId" />
      <el-table-column label="siteName" align="center" prop="siteName" />
      <el-table-column label="cardProduct" align="center" prop="cardProduct" />
      <el-table-column label="customData" align="center" prop="customData" />
      <el-table-column label="arn" align="center" prop="arn" />
      <el-table-column label="payoutId" align="center" prop="payoutId" />
      <el-table-column label="bankTransactionId" align="center" prop="bankTransactionId" />
      <el-table-column label="isAft" align="center" prop="isAft" />
      <el-table-column label="batchId" align="center" prop="batchId" />
      <el-table-column label="processingChannel" align="center" prop="processingChannel" />
      <el-table-column label="bin" align="center" prop="bin" />
      <el-table-column label="recordId" align="center" prop="recordId" />
      <el-table-column label="isCurrencyConverted" align="center" prop="isCurrencyConverted" />
      <el-table-column label="paymentSubMethod" align="center" prop="paymentSubMethod" />
      <el-table-column label="endUserAmount" align="center" prop="endUserAmount" />
      <el-table-column label="endUserCurrency" align="center" prop="endUserCurrency" />
      <el-table-column :label="$t('payment.movenmentData.movementImportId')" align="center" prop="movementImportId" />
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payment:movenmentData:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payment:movenmentData:remove']"
          >{{ $t('common.delete') }}</el-button>
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

    <!-- 添加或修改nuvei导入数据明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>

    <el-col :span="12">
        <el-form-item :label="$t('payment.movenmentData.multiClient')" prop="multiClient">
          <el-input v-model="form.multiClient" :placeholder="$t('payment.movenmentData.pleaseEnterMultiClient')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.movenmentData.clientName')" prop="clientName">
          <el-input v-model="form.clientName" :placeholder="$t('payment.movenmentData.pleaseEnterClientName')" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item :label="$t('payment.movenmentData.acquirerBank')" prop="acquirerBank">
          <el-input v-model="form.acquirerBank" :placeholder="$t('payment.movenmentData.pleaseEnterAcquirerBank')" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item :label="$t('payment.movenmentData.movementDate')" prop="movementDate">
          <el-date-picker clearable size="small"
            v-model="form.movementDate"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            :placeholder="$t('payment.movenmentData.pleaseSelectMovementDate')">
          </el-date-picker>
        </el-form-item>
    </el-col>
    <el-col :span="12">
        <el-form-item :label="$t('payment.movenmentData.movementType')" prop="movementType">
          <el-select v-model="form.movementType" :placeholder="$t('payment.movenmentData.pleaseSelectMovementType')" filterable>
            <el-option :label="$t('payment.movenmentData.pleaseSelectFromDict')" value="" />
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="movementDetails" prop="movementDetails">
          <el-input v-model="form.movementDetails" placeholder="请输入movementDetails" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="paymentMethod" prop="paymentMethod">
          <el-input v-model="form.paymentMethod" placeholder="请输入paymentMethod" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="is3d" prop="is3d">
          <el-input v-model="form.is3d" placeholder="请输入is3d" />
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="cardType" prop="cardType">
          <el-select v-model="form.cardType" placeholder="请选择cardType" filterable>
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
    </el-col>

    <el-col :span="12">
        <el-form-item label="country" prop="country">
          <el-input v-model="form.country" placeholder="请输入country" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="transactionResult" prop="transactionResult">
          <el-input v-model="form.transactionResult" placeholder="请输入transactionResult" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="transactionId" prop="transactionId">
          <el-input v-model="form.transactionId" placeholder="请输入transactionId" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="creditCardLast4Digits" prop="creditCardLast4Digits">
          <el-input v-model="form.creditCardLast4Digits" placeholder="请输入creditCardLast4Digits" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="clientUniqueId" prop="clientUniqueId">
          <el-input v-model="form.clientUniqueId" placeholder="请输入clientUniqueId" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="currency" prop="currency">
          <el-input v-model="form.currency" placeholder="请输入currency" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="amount" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入amount" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="discountFee" prop="discountFee">
          <el-input v-model="form.discountFee" placeholder="请输入discountFee" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="transactionFee" prop="transactionFee">
          <el-input v-model="form.transactionFee" placeholder="请输入transactionFee" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="serviceFee" prop="serviceFee">
          <el-input v-model="form.serviceFee" placeholder="请输入serviceFee" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="reserve" prop="reserve">
          <el-input v-model="form.reserve" placeholder="请输入reserve" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="vatOnFees" prop="vatOnFees">
          <el-input v-model="form.vatOnFees" placeholder="请输入vatOnFees" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="netAmount" prop="netAmount">
          <el-input v-model="form.netAmount" placeholder="请输入netAmount" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="settlementCurrency" prop="settlementCurrency">
          <el-input v-model="form.settlementCurrency" placeholder="请输入settlementCurrency" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="settlementAmount" prop="settlementAmount">
          <el-input v-model="form.settlementAmount" placeholder="请输入settlementAmount" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="settlementReserve" prop="settlementReserve">
          <el-input v-model="form.settlementReserve" placeholder="请输入settlementReserve" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="jurisdiction" prop="jurisdiction">
          <el-input v-model="form.jurisdiction" placeholder="请输入jurisdiction" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="prepaid" prop="prepaid">
          <el-input v-model="form.prepaid" placeholder="请输入prepaid" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="plusCommission" prop="plusCommission">
          <el-input v-model="form.plusCommission" placeholder="请输入plusCommission" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="externalTokenProvider" prop="externalTokenProvider">
          <el-input v-model="form.externalTokenProvider" placeholder="请输入externalTokenProvider" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="onUs" prop="onUs">
          <el-input v-model="form.onUs" placeholder="请输入onUs" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="orderId" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入orderId" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="isInterchangePlusPlus" prop="isInterchangePlusPlus">
          <el-input v-model="form.isInterchangePlusPlus" placeholder="请输入isInterchangePlusPlus" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="resellerFee" prop="resellerFee">
          <el-input v-model="form.resellerFee" placeholder="请输入resellerFee" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="isOnlinePayout" prop="isOnlinePayout">
          <el-input v-model="form.isOnlinePayout" placeholder="请输入isOnlinePayout" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="movementId" prop="movementId">
          <el-input v-model="form.movementId" placeholder="请输入movementId" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="schemeFee" prop="schemeFee">
          <el-input v-model="form.schemeFee" placeholder="请输入schemeFee" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="resellerCommission" prop="resellerCommission">
          <el-input v-model="form.resellerCommission" placeholder="请输入resellerCommission" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="interchangeFee" prop="interchangeFee">
          <el-input v-model="form.interchangeFee" placeholder="请输入interchangeFee" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="siteId" prop="siteId">
          <el-input v-model="form.siteId" placeholder="请输入siteId" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="siteName" prop="siteName">
          <el-input v-model="form.siteName" placeholder="请输入siteName" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="cardProduct" prop="cardProduct">
          <el-input v-model="form.cardProduct" placeholder="请输入cardProduct" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="customData" prop="customData">
          <el-input v-model="form.customData" placeholder="请输入customData" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="arn" prop="arn">
          <el-input v-model="form.arn" placeholder="请输入arn" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="payoutId" prop="payoutId">
          <el-input v-model="form.payoutId" placeholder="请输入payoutId" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="bankTransactionId" prop="bankTransactionId">
          <el-input v-model="form.bankTransactionId" placeholder="请输入bankTransactionId" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="isAft" prop="isAft">
          <el-input v-model="form.isAft" placeholder="请输入isAft" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="batchId" prop="batchId">
          <el-input v-model="form.batchId" placeholder="请输入batchId" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="processingChannel" prop="processingChannel">
          <el-input v-model="form.processingChannel" placeholder="请输入processingChannel" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="bin" prop="bin">
          <el-input v-model="form.bin" placeholder="请输入bin" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="recordId" prop="recordId">
          <el-input v-model="form.recordId" placeholder="请输入recordId" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="isCurrencyConverted" prop="isCurrencyConverted">
          <el-input v-model="form.isCurrencyConverted" placeholder="请输入isCurrencyConverted" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="paymentSubMethod" prop="paymentSubMethod">
          <el-input v-model="form.paymentSubMethod" placeholder="请输入paymentSubMethod" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="endUserAmount" prop="endUserAmount">
          <el-input v-model="form.endUserAmount" placeholder="请输入endUserAmount" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="endUserCurrency" prop="endUserCurrency">
          <el-input v-model="form.endUserCurrency" placeholder="请输入endUserCurrency" />
        </el-form-item>
    </el-col>


    <el-col :span="12">
        <el-form-item label="nuvie导入id" prop="movementImportId">
          <el-input v-model="form.movementImportId" placeholder="请输入nuvie导入id" />
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
import { listMovenmentData, getMovenmentData, delMovenmentData, addMovenmentData, updateMovenmentData, exportMovenmentData } from "@/api/payment/movenmentData";

export default {
  name: "MovenmentData",
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
      // nuvei导入数据明细表格数据
      movenmentDataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询nuvei导入数据明细列表 */
    getList() {
      this.loading = true;
      listMovenmentData(this.queryParams).then(response => {
        this.movenmentDataList = response.rows;
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
        multiClient: undefined,
        clientName: undefined,
        acquirerBank: undefined,
        movementDate: undefined,
        movementType: undefined,
        movementDetails: undefined,
        paymentMethod: undefined,
        is3d: undefined,
        cardType: undefined,
        country: undefined,
        transactionResult: undefined,
        transactionId: undefined,
        creditCardLast4Digits: undefined,
        clientUniqueId: undefined,
        currency: undefined,
        amount: undefined,
        discountFee: undefined,
        transactionFee: undefined,
        serviceFee: undefined,
        reserve: undefined,
        vatOnFees: undefined,
        netAmount: undefined,
        settlementCurrency: undefined,
        settlementAmount: undefined,
        settlementReserve: undefined,
        jurisdiction: undefined,
        prepaid: undefined,
        plusCommission: undefined,
        externalTokenProvider: undefined,
        onUs: undefined,
        orderId: undefined,
        isInterchangePlusPlus: undefined,
        resellerFee: undefined,
        isOnlinePayout: undefined,
        movementId: undefined,
        schemeFee: undefined,
        resellerCommission: undefined,
        interchangeFee: undefined,
        siteId: undefined,
        siteName: undefined,
        cardProduct: undefined,
        customData: undefined,
        arn: undefined,
        payoutId: undefined,
        bankTransactionId: undefined,
        isAft: undefined,
        batchId: undefined,
        processingChannel: undefined,
        bin: undefined,
        recordId: undefined,
        isCurrencyConverted: undefined,
        paymentSubMethod: undefined,
        endUserAmount: undefined,
        endUserCurrency: undefined,
        movementImportId: undefined
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
      this.title = this.$t('payment.movenmentData.addTitle');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMovenmentData(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('payment.movenmentData.editTitle');
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMovenmentData(this.form).then(response => {
              this.msgSuccess(this.$t('payment.movenmentData.editSuccess'));
              this.open = false;
              this.getList();
            });
          } else {
            addMovenmentData(this.form).then(response => {
              this.msgSuccess(this.$t('payment.movenmentData.addSuccess'));
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
      this.$confirm(this.$t('payment.movenmentData.confirmDelete', { ids }), this.$t('common.warning'), {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: "warning"
        }).then(function() {
          return delMovenmentData(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess(this.$t('payment.movenmentData.deleteSuccess'));
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm(this.$t('payment.movenmentData.confirmExport'), this.$t('common.warning'), {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: "warning"
        }).then(function() {
          return exportMovenmentData(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
