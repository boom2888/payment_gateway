<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="130px">
      <el-form-item :label="$t('payment.order.orderId')" prop="id">
        <el-input
          v-model="queryParams.id"
          :placeholder="$t('payment.order.orderId')"
          clearable
          size="small"
          @input="handleIntegerInput('id')"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.order.ssoOrderId')" prop="ssoOrderId">
        <el-input
          v-model="queryParams.ssoOrderId"
          :placeholder="$t('payment.order.ssoOrderId')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.order.merchantId')" prop="merchantIdSearch">
        <el-input
          v-model="queryParams.merchantIdSearch"
          :placeholder="$t('payment.order.merchantId')"
          clearable
          size="small"
          @input="handleIntegerInput('merchantIdSearch')"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item
        v-if="checkPermi(['payment:order:merchantName'])"
        :label="$t('payment.order.merchantName')"
        prop="merchantId"
      >
        <el-select
          v-model="queryParams.merchantId"
          :placeholder="$t('payment.order.merchantName')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in merchantIdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.order.type')" prop="type">
        <el-select v-model="queryParams.type" :placeholder="$t('payment.order.type')" clearable size="small" filterable>
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.order.currencyId')" prop="currencyId">
        <el-select
          v-model="queryParams.currencyId"
          :placeholder="$t('payment.order.currencyId')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in currencyIdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.order.cryptoId')" prop="cryptoId">
        <el-select
          v-model="queryParams.cryptoId"
          :placeholder="$t('payment.order.cryptoId')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in cryptoIdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.order.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('payment.order.status')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.order.callbackStatus')" prop="callbackStatus">
        <el-select
          v-model="queryParams.callbackStatus"
          :placeholder="$t('payment.order.callbackStatus')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in callbackStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('payment.order.createdAt')" prop="createdAt">
        <el-date-picker
          v-model="dateRange"
          clearable
          size="small"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          :start-placeholder="$t('common.startDate')"
          :end-placeholder="$t('common.endDate')"
          :placeholder="$t('common.createdAt')"
        />
      </el-form-item>

      <el-form-item style="margin-left: 60px">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('common.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['payment:order:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exporting"
          :disabled="exporting"
          @click="handleExport"
        >
          {{ exporting ? ($t('payment.order.exporting') || '导出中...') : $t('common.export') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column v-if="true" :label="$t('payment.order.orderId')" align="center" prop="id" width="80" />
      <el-table-column
        v-if="true"
        :label="$t('payment.order.merchantId')"
        align="center"
        prop="merchantId"
        width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.merchantId }}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="true"
        :label="$t('payment.order.merchantName')"
        align="center"
        prop="merchantId"
        :formatter="merchantIdFormat"
        width="100">
      </el-table-column>
      <el-table-column :label="$t('payment.order.ssoOrderId')" align="center" prop="ssoOrderId" width="150">
        <template #default="{ row }">
          <div v-copy-hover class="ellipsis-text">{{ row.ssoOrderId }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.order.fiatAmount')" align="center" prop="fiatAmount" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.fiatAmount }} {{ currencyIdFormat(scope.row) }}</span>
        </template>
      </el-table-column>

      <el-table-column
        :label="$t('payment.order.status')"
        align="center"
        prop="status"
        width="100">
        <template slot-scope="scope">
          <div
            v-if="scope.row.status !== 8 && scope.row.failReasons"
            v-copy-hover="scope.row.failReasons"
            class="status-with-fail-reason">
            {{ statusFormat(scope.row) }}
          </div>
          <span v-else>{{ statusFormat(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.order.createdAt')" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>

      <el-table-column :label="$t('payment.order.transactionId')" align="center" prop="transactionId" width="150">
        <template slot-scope="scope">
          <div v-copy-hover class="ellipsis-text">{{ scope.row.transactionId }}</div>
        </template>
      </el-table-column>

      <el-table-column
        :label="$t('payment.order.callbackStatus')"
        align="center"
        prop="callbackStatus"
        :formatter="callbackStatusFormat"
        width="100"
      />
      <el-table-column
        :label="$t('common.operation')"
        align="center"
        class-name="small-padding fixed-width"
        width="140"
      >
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.callbackStatus !== 'OK'"
            size="mini"
            type="text"
            icon="el-icon-refresh"
            @click="handleCallBack(scope.row)"
          >
            {{ $t('common.callback') }}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-info" @click="handleInfo(scope.row)">
            {{ $t('common.detail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('payment.order.merchantId')" prop="merchantId">
              <el-select v-model="form.merchantId" :placeholder="$t('payment.order.merchantId')" filterable>
                <el-option
                  v-for="dict in merchantIdOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.order.customerId')" prop="customerId">
              <el-select v-model="form.customerId" :placeholder="$t('payment.order.customerId')" filterable>
                <el-option
                  v-for="dict in customerIdOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.order.customerWalletId')" prop="customerWalletId">
              <el-select v-model="form.customerWalletId" :placeholder="$t('payment.order.customerWalletId')" filterable>
                <el-option
                  v-for="dict in customerWalletIdOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.order.type')" prop="type">
              <el-select v-model="form.type" :placeholder="$t('payment.order.type')" filterable>
                <el-option
                  v-for="dict in typeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.order.currencyId')" prop="currencyId">
              <el-select v-model="form.currencyId" :placeholder="$t('payment.order.currencyId')" filterable>
                <el-option
                  v-for="dict in currencyIdOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.fiatAmount')" prop="fiatAmount">
              <el-input v-model="form.fiatAmount" :placeholder="$t('payment.order.fiatAmount')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.cryptoId')" prop="cryptoId">
              <el-select v-model="form.cryptoId" :placeholder="$t('payment.order.cryptoId')" filterable>
                <el-option
                  v-for="dict in cryptoIdOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.cryptoAmount')" prop="cryptoAmount">
              <el-input v-model="form.cryptoAmount" :placeholder="$t('payment.order.cryptoAmount')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.processingFee')" prop="processingFee">
              <el-input v-model="form.processingFee" :placeholder="$t('payment.order.processingFee')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.liquidityQuote')" prop="liquidityQuote">
              <el-input v-model="form.liquidityQuote" :placeholder="$t('payment.order.liquidityQuote')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.liquidityProvider')" prop="liquidityProvider">
              <el-input v-model="form.liquidityProvider" :placeholder="$t('payment.order.liquidityProvider')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.reference')" prop="reference">
              <el-input v-model="form.reference" :placeholder="$t('payment.order.reference')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.travelRuleStatus')" prop="travelRuleStatus">
              <el-select v-model="form.travelRuleStatus" :placeholder="$t('payment.order.travelRuleStatus')" filterable>
                <el-option
                  v-for="dict in travelRuleStatusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.order.status')" prop="status">
              <el-select v-model="form.status" :placeholder="$t('payment.order.status')" filterable>
                <el-option
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.amlId')" prop="amlId">
              <el-input v-model="form.amlId" :placeholder="$t('payment.order.amlId')" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item :label="$t('payment.order.amlPayload')" prop="amlPayload">
              <el-input v-model="form.amlPayload" type="textarea" :placeholder="$t('payment.order.amlPayload')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.amlScore')" prop="amlScore">
              <el-input v-model="form.amlScore" :placeholder="$t('payment.order.amlScore')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.amlStatus')" prop="amlStatus">
              <el-select v-model="form.amlStatus" :placeholder="$t('payment.order.amlStatus')" filterable>
                <el-option
                  v-for="dict in amlStatusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('payment.order.remark')" prop="remark">
              <el-input v-model="form.remark" type="textarea" :placeholder="$t('payment.order.remark')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.travelRuleId')" prop="travelRuleId">
              <el-input v-model="form.travelRuleId" :placeholder="$t('payment.order.travelRuleId')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.paymentIp')" prop="paymentIp">
              <el-input v-model="form.paymentIp" :placeholder="$t('payment.order.paymentIp')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.mid')" prop="mid">
              <el-input v-model="form.mid" :placeholder="$t('payment.order.mid')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.isOtc')" prop="isOtc">
              <el-select v-model="form.isOtc" :placeholder="$t('payment.order.isOtc')" filterable>
                <el-option
                  v-for="dict in isOtcOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.ssoOrderId')" prop="ssoOrderId">
              <el-input v-model="form.ssoOrderId" :placeholder="$t('payment.order.ssoOrderId')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.includeFee')" prop="includeFee">
              <el-select v-model="form.includeFee" :placeholder="$t('payment.order.includeFee')" filterable>
                <el-option
                  v-for="dict in includeFeeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.sum90Checked')" prop="sum90Checked">
              <el-input v-model="form.sum90Checked" :placeholder="$t('payment.order.sum90Checked')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.fraudScore')" prop="fraudScore">
              <el-input v-model="form.fraudScore" :placeholder="$t('payment.order.fraudScore')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.gatewayFee')" prop="gatewayFee">
              <el-input v-model="form.gatewayFee" :placeholder="$t('payment.order.gatewayFee')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.referralCode')" prop="referralCode">
              <el-input v-model="form.referralCode" :placeholder="$t('payment.order.referralCode')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.bankAccountId')" prop="bankAccountId">
              <el-select v-model="form.bankAccountId" :placeholder="$t('payment.order.bankAccountId')" filterable>
                <el-option
                  v-for="dict in bankAccountIdOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.aniCode')" prop="aniCode">
              <el-input v-model="form.aniCode" :placeholder="$t('payment.order.aniCode')" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.order.isRollback')" prop="isRollback">
              <el-select v-model="form.isRollback" :placeholder="$t('payment.order.isRollback')" filterable>
                <el-option
                  v-for="dict in isRollbackOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('payment.order.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('payment.order.cancel') }}</el-button>
      </div>
    </el-dialog>
    <!-- 导出字段选择对话框 -->
    <el-dialog :title="$t('payment.order.exportFieldSelection')" :visible.sync="exportFieldDialogVisible" width="800px" append-to-body>
      <div class="export-field-dialog">
        <div class="field-operations">
          <el-button size="mini" @click="selectAllFields">{{ $t('common.selectAll') }}</el-button>
          <el-button size="mini" @click="unselectAllFields">{{ $t('common.unselectAll') }}</el-button>
          <el-button size="mini" @click="invertSelection">{{ $t('common.invertSelection') }}</el-button>
          <el-button size="mini" type="warning" @click="restoreDefaultFields">{{ $t('common.restoreDefault') }}</el-button>
        </div>
        <el-checkbox-group v-model="selectedExportFields" class="field-checkbox-group">
          <el-checkbox
            v-for="field in exportFieldOptions"
            :key="field.value"
            :label="field.value"
            class="field-checkbox-item"
          >
            {{ field.label }}
          </el-checkbox>
        </el-checkbox-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="exportFieldDialogVisible = false" :disabled="exporting">{{ $t('payment.order.cancel') }}</el-button>
        <el-button type="primary" @click="confirmExport" :loading="exporting" :disabled="exporting">
          {{ exporting ? ($t('payment.order.exporting') || '导出中...') : $t('payment.order.confirmExport') }}
        </el-button>
      </div>
    </el-dialog>

    <!-- 订单详情对话框 -->
    <el-dialog :title="$t('payment.order.orderDetail')" :visible.sync="infoOpen" width="1200px" append-to-body>
      <!-- 订单流程时间线 -->
      <div class="order-timeline">
        <div class="timeline-item">
          <div class="timeline-icon" />
          <div class="timeline-content">
            <div class="timeline-title">{{ $t('payment.order.userSubmitOrder') }}</div>
            <div class="timeline-time">{{ form.createdAt }}</div>
          </div>
        </div>
        <div class="timeline-arrow" />
        <div class="timeline-item">
          <div class="timeline-icon" />
          <div class="timeline-content">
            <div v-if="form.status >= 8" class="timeline-title">{{ $t('payment.order.userPaid') }}</div>
            <div v-if="form.status === '6'" class="timeline-title">{{ $t('payment.order.failedCancelled') }}</div>
            <div class="timeline-time">{{ form.payTime }}</div>
          </div>
        </div>
        <div class="timeline-arrow" />
        <div class="timeline-item">
          <div class="timeline-icon" />
          <div class="timeline-content">
            <div class="timeline-title">{{ $t('payment.order.acquirerCallback') }}</div>
            <div class="timeline-time" />
          </div>
        </div>
        <div class="timeline-arrow" />
        <div class="timeline-item">
          <div class="timeline-icon" />
          <div class="timeline-content">
            <div class="timeline-title">{{ $t('payment.order.platformCallback') }}</div>
            <div class="timeline-time">{{ form.callbackTime }}</div>
          </div>
        </div>
      </div>

      <!-- 订单状态指示器 -->
      <el-card class="mb20" shadow="never">
        <div slot="header">
          <span>{{ $t('payment.order.orderInfo') }}</span>
          <span style="margin-left: 20px">
            {{ $t('payment.order.currentStatus') }}: {{ getStatusName(form.status) }}
          </span>
          <span style="margin-left: 20px">{{ form.failReasons }}</span>
        </div>

        <div class="detail-list">
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.orderId') }}：</span>
            <span class="detail-value">{{ form.id }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.merchantName') }}：</span>
            <span class="detail-value">{{ getMerchantName(form.merchantId) }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.merchantNo') }}：</span>
            <span class="detail-value">{{ form.merchantId }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.paymentCurrency') }}：</span>
            <span class="detail-value">{{ getCurrencyName(form.currencyId) }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.otcType') }}：</span>
            <span class="detail-value">{{ getIsOtcName(form.isOtc) }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.cryptoCurrency') }}：</span>
            <span class="detail-value">{{ getCryptoName(form.cryptoId) }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.currencyExchange') }}：</span>
            <span class="detail-value">{{ form.liquidityQuote }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.type') }}：</span>
            <span class="detail-value">{{ getTypeName(form.type) }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.actualPayment') }}：</span>
            <span class="detail-value amount-red">{{ form.fiatAmount }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.refundAmount') }}：</span>
            <span class="detail-value amount-orange">
              {{ form.refundAmount || 0 }} {{ getCurrencyName(form.currencyId) }}
            </span>
          </div>
        </div>
      </el-card>

      <el-card class="mb20" shadow="never">
        <div slot="header">
          <span>{{ $t('payment.order.paymentInfo') }}</span>
        </div>
        <div class="detail-list">
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.platformOrderNo') }}：</span>
            <span class="detail-value">{{ form.id }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.transactionId') }}：</span>
            <span class="detail-value">{{ form.transactionId }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.ssoOrderId') }}：</span>
            <span class="detail-value">{{ form.ssoOrderId }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.paymentInstitution') }}：</span>
            <span class="detail-value">{{ form.fromSource }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.paymentMethod') }}：</span>
            <span class="detail-value">{{ form.payInfoVo && form.payInfoVo.paymentType }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.country') }}：</span>
            <span class="detail-value">
              {{ form.payInfoVo && form.payInfoVo.addressInfo && form.payInfoVo.addressInfo.country }}
            </span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.cardNo') }}：</span>
            <span class="detail-value">
              {{ form.payInfoVo && form.payInfoVo.cardInfo && form.payInfoVo.cardInfo.cardNumber }}
            </span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.expiryDate') }}：</span>
            <span class="detail-value">
              {{ form.payInfoVo && form.payInfoVo.cardInfo && form.payInfoVo.cardInfo.expiryYear }} -
              {{ form.payInfoVo && form.payInfoVo.cardInfo && form.payInfoVo.cardInfo.expiryMonth }}
            </span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.cardholder') }}：</span>
            <span class="detail-value">
              {{ form.payInfoVo && form.payInfoVo.cardInfo && form.payInfoVo.cardInfo.firstName }} -
              {{ form.payInfoVo && form.payInfoVo.cardInfo && form.payInfoVo.cardInfo.lastName }}
            </span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.city') }}：</span>
            <span class="detail-value">
              {{ form.payInfoVo && form.payInfoVo.addressInfo && form.payInfoVo.addressInfo.city }}
            </span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.zip') }}：</span>
            <span class="detail-value">
              {{ form.payInfoVo && form.payInfoVo.addressInfo && form.payInfoVo.addressInfo.zip }}
            </span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.addressLine1') }}：</span>
            <span class="detail-value">
              {{ form.payInfoVo && form.payInfoVo.addressInfo && form.payInfoVo.addressInfo.addressLine1 }}
            </span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.email') }}：</span>
            <span class="detail-value">
              {{ form.email }}
            </span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.paymentIp') }}：</span>
            <span class="detail-value">
              {{ form.paymentIp }}
            </span>
          </div>
        </div>
      </el-card>
      <el-card class="mb20" shadow="never">
        <div slot="header">
          <span>{{ $t('payment.order.feeInfo') }}</span>
        </div>
        <div class="detail-list">
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.circulationDeposit') }}：</span>
            <span class="detail-value">{{ form.rollingFee }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.gatewayFee') }}：</span>
            <span class="detail-value">{{ form.gatewayFee }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.processingFee') }}：</span>
            <span class="detail-value">{{ form.processingFee }}</span>
          </div>
        </div>
      </el-card>
      <!-- 商品状态指示器 -->
      <el-card shadow="never">
        <div slot="header">
          <span>{{ $t('payment.order.productInfo') }}</span>
          <span style="margin-left: 20px">
            {{ $t('payment.order.currentStatus') }}: {{ getStatusName(form.status) }}
          </span>
          <span />
        </div>
        <div class="detail-list">
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.goodsName') }}：</span>
            <span class="detail-value">{{ form.productName }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.goodsAmount') }}：</span>
            <span class="detail-value">{{ getCurrencyName(form.currencyId) }}{{ form.fiatAmount }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.walletAddress') }}：</span>
            <span class="detail-value">{{ form.walletAddress }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.cryptoCurrency') }}：</span>
            <span class="detail-value">{{ getCryptoName(form.cryptoId) }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">{{ $t('payment.order.purchaseQuantity') }}：</span>
            <span class="detail-value">{{ form.cryptoAmount }}</span>
          </div>
        </div>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
import { addOrder, delOrder, exportOrder, getOrder, listOrder, tradeCallBack, updateOrder } from '@/api/payment/order'
import { getUserExportConfig, saveUserExportConfig } from '@/api/system/userExportConfig'
import { checkPermi } from '@/utils/permission'
import { parseTime } from '../../../utils/ruoyi'

export default {
  name: 'Order',
  components: {},
  data() {
    return {
      // 模块名称，用于保存导出配置
      MODULE_NAME: 'order',
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
      // 订单表格数据
      orderList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      infoOpen: false,
      // 商户字典
      merchantIdOptions: [],
      // 客户字典
      customerIdOptions: [],
      // 客户钱包字典
      customerWalletIdOptions: [],
      // 出入类型字典
      typeOptions: [],
      // 法币字典
      currencyIdOptions: [],
      // 加密货币字典
      cryptoIdOptions: [],
      // 旅行规则状态字典
      travelRuleStatusOptions: [],
      // 订单状态字典
      statusOptions: [],
      callbackStatusOptions: [],
      // AML字典
      amlStatusOptions: [],
      // OTC字典
      isOtcOptions: [],
      // 包含费用字典
      includeFeeOptions: [],
      // 银行账户字典
      bankAccountIdOptions: [],
      // 回滚状态字典
      isRollbackOptions: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: undefined,
        ssoOrderId: undefined,
        merchantId: undefined,
        merchantIdSearch: undefined,
        customerId: undefined,
        type: undefined,
        currencyId: undefined,
        cryptoId: undefined,
        status: undefined,
        amlStatus: undefined,
        createdAt: undefined,
        isOtc: undefined,
        includeFee: undefined,
        isRollback: undefined,
        callbackStatus: undefined,
        callbackNum: undefined
      },
      // 表单参数
      form: {},
      // 导出字段选择对话框
      exportFieldDialogVisible: false,
      // 导出中状态（防止重复点击）
      exporting: false,
      // 待导出的查询参数（临时存储）
      pendingExportParams: {},
      // 选中的导出字段
      selectedExportFields: [],
      // 导出字段选项
      exportFieldOptions: [],
      // 默认导出字段（用于恢复默认）
      defaultExportFields: [
        'id',
        'ssoOrderId',
        'merchantId',
        'merchantName',
        'type',
        'faitCurrency',
        'fiatAmount',
        'cryptoCurrency',
        'cryptoAmount',
        'cardNumber',
        'transactionId',
        'status',
        'callbackStatus',
        'createdAt'
      ]
    }
  },
  computed: {
    // 表单校验规则
    rules() {
      return {
        merchantId: [{ required: true, message: this.$t('payment.order.merchantRequired'), trigger: 'change' }],
        customerId: [{ required: true, message: this.$t('payment.order.customerRequired'), trigger: 'change' }],
        customerWalletId: [
          { required: true, message: this.$t('payment.order.customerWalletRequired'), trigger: 'change' }
        ],
        type: [{ required: true, message: this.$t('payment.order.typeRequired'), trigger: 'change' }],
        currencyId: [{ required: true, message: this.$t('payment.order.currencyRequired'), trigger: 'change' }],
        fiatAmount: [{ required: true, message: this.$t('payment.order.fiatAmountRequired'), trigger: 'blur' }],
        cryptoId: [{ required: true, message: this.$t('payment.order.cryptoRequired'), trigger: 'change' }],
        cryptoAmount: [{ required: true, message: this.$t('payment.order.cryptoAmountRequired'), trigger: 'blur' }],
        processingFee: [{ required: true, message: this.$t('payment.order.processingFeeRequired'), trigger: 'blur' }],
        liquidityQuote: [{ required: true, message: this.$t('payment.order.liquidityQuoteRequired'), trigger: 'blur' }],
        travelRuleStatus: [
          { required: true, message: this.$t('payment.order.travelRuleStatusRequired'), trigger: 'change' }
        ],
        status: [{ required: true, message: this.$t('payment.order.orderStatusRequired'), trigger: 'change' }],
        amlStatus: [{ required: true, message: this.$t('payment.order.amlStatusRequired'), trigger: 'change' }],
        deleted: [{ required: true, message: this.$t('payment.order.deletedRequired'), trigger: 'blur' }],
        isOtc: [{ required: true, message: this.$t('payment.order.otcRequired'), trigger: 'change' }],
        includeFee: [{ required: true, message: this.$t('payment.order.includeFeeRequired'), trigger: 'change' }]
      }
    }
  },
  async created() {
    this.initExportFieldOptions()
    await this.loadExportFieldsConfig()
    this.getList()

    this.getDicts('MERCHANT_ID').then(response => {
      this.merchantIdOptions = response.data
      console.log(this.merchantIdOptions)
    })
    this.getDicts('CUSTOMER_ID').then(response => {
      this.customerIdOptions = response.data
    })
    this.getDicts('CUSTOMER_WALLET_ID').then(response => {
      this.customerWalletIdOptions = response.data
    })
    this.getDicts('ORDER_TYPE').then(response => {
      this.typeOptions = response.data
    })
    this.getDicts('CURRENCY_ID').then(response => {
      this.currencyIdOptions = response.data
    })
    this.getDicts('CRYPTO_ID').then(response => {
      this.cryptoIdOptions = response.data
    })
    this.getDicts('ORDER_TRAVEL_RULE_STATUS').then(response => {
      this.travelRuleStatusOptions = response.data
    })
    this.getDicts('ORDER_STATUS').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('ORDER_CALLBACK_STATUS').then(response => {
      this.callbackStatusOptions = response.data
    })
    this.getDicts('ORDER_AML_STATUS').then(response => {
      this.amlStatusOptions = response.data
    })
    this.getDicts('ORDER_IS_OTC').then(response => {
      this.isOtcOptions = response.data
    })
    this.getDicts('ORDER_INCLUDE_FEE').then(response => {
      this.includeFeeOptions = response.data
    })
    this.getDicts('BANK_ACCOUNT_ID').then(response => {
      this.bankAccountIdOptions = response.data
    })
    this.getDicts('sys_yes_no').then(response => {
      this.isRollbackOptions = response.data
    })
  },
  methods: {
    parseTime,
    checkPermi,
    /** 限制输入框只能输入整数 */
    handleIntegerInput(field) {
      const value = this.queryParams[field]
      if (value) {
        // 移除所有非数字字符
        this.queryParams[field] = value.replace(/[^\d]/g, '')
      }
    },
    /** 查询订单列表 */
    getList() {
      this.loading = true
      // 处理查询参数：如果输入了商户ID搜索，则使用它；否则使用商户名称下拉选择
      const params = { ...this.queryParams }
      if (params.merchantIdSearch) {
        params.merchantId = params.merchantIdSearch
      }
      delete params.merchantIdSearch

      listOrder(this.addDateRange(params, this.dateRange)).then(response => {
        this.orderList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 商户字典翻译
    merchantIdFormat(row, column) {
      return this.selectDictLabel(this.merchantIdOptions, row.merchantId)
    },
    // 客户字典翻译
    customerIdFormat(row, column) {
      return this.selectDictLabel(this.customerIdOptions, row.customerId)
    },
    // 客户钱包字典翻译
    customerWalletIdFormat(row, column) {
      return this.selectDictLabel(this.customerWalletIdOptions, row.customerWalletId)
    },
    // 出入类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type)
    },
    // 法币字典翻译
    currencyIdFormat(row, column) {
      return this.selectDictLabel(this.currencyIdOptions, row.currencyId)
    },
    // 加密货币字典翻译
    cryptoIdFormat(row, column) {
      return this.selectDictLabel(this.cryptoIdOptions, row.cryptoId)
    },
    // 旅行规则状态字典翻译
    travelRuleStatusFormat(row, column) {
      return this.selectDictLabel(this.travelRuleStatusOptions, row.travelRuleStatus)
    },
    // 订单状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 订单状态字典翻译
    callbackStatusFormat(row, column) {
      return this.selectDictLabel(this.callbackStatusOptions, row.callbackStatus)
    },
    // AML字典翻译
    amlStatusFormat(row, column) {
      return this.selectDictLabel(this.amlStatusOptions, row.amlStatus)
    },
    // OTC字典翻译
    isOtcFormat(row, column) {
      return this.selectDictLabel(this.isOtcOptions, row.isOtc)
    },
    // 包含费用字典翻译
    includeFeeFormat(row, column) {
      return this.selectDictLabel(this.includeFeeOptions, row.includeFee)
    },
    // 银行账户字典翻译
    bankAccountIdFormat(row, column) {
      return this.selectDictLabel(this.bankAccountIdOptions, row.bankAccountId)
    },
    // 回滚状态字典翻译
    isRollbackFormat(row, column) {
      return this.selectDictLabel(this.isRollbackOptions, row.isRollback)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        ssoOrderId: undefined,
        merchantId: undefined,
        customerId: undefined,
        customerWalletId: undefined,
        type: undefined,
        currencyId: undefined,
        fiatAmount: undefined,
        cryptoId: undefined,
        cryptoAmount: undefined,
        processingFee: undefined,
        liquidityQuote: undefined,
        liquidityProvider: undefined,
        reference: undefined,
        travelRuleStatus: undefined,
        status: undefined,
        amlId: undefined,
        amlPayload: undefined,
        amlScore: undefined,
        amlStatus: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        deleted: undefined,
        remark: undefined,
        travelRuleId: undefined,
        paymentIp: undefined,
        mid: undefined,
        isOtc: undefined,
        includeFee: undefined,
        sum90Checked: undefined,
        refundAmount: undefined,
        fraudScore: undefined,
        gatewayFee: undefined,
        referralCode: undefined,
        bankAccountId: undefined,
        aniCode: undefined,
        isRollback: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = this.$t('payment.order.addOrder')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.order.editOrder')
      })
    },
    /** 回调按钮操作 */
    handleCallBack(row) {
      const id = row.id
      this.$confirm(this.$t('payment.order.confirmCallback'), this.$t('payment.order.warning'), {
        confirmButtonText: this.$t('payment.order.confirm'),
        cancelButtonText: this.$t('payment.order.cancel'),
        type: 'warning'
      })
        .then(function () {
          return tradeCallBack({
            id: id
          })
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.order.callbackSuccess'))
        })
        .catch(() => {})
    },
    /** 详情按钮操作 */
    handleInfo(row) {
      const id = row.id
      getOrder(id).then(response => {
        this.form = response.data
        this.infoOpen = true
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOrder(this.form).then(response => {
              this.msgSuccess(this.$t('payment.order.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addOrder(this.form).then(response => {
              this.msgSuccess(this.$t('payment.order.addSuccess'))
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$confirm(this.$t('payment.order.confirmDelete', { ids: ids }), this.$t('payment.order.warning'), {
        confirmButtonText: this.$t('payment.order.confirm'),
        cancelButtonText: this.$t('payment.order.cancel'),
        type: 'warning'
      })
        .then(function () {
          return delOrder(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.order.deleteSuccess'))
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      console.log('打印导出数据===============:', this.ids)
      let queryParamsForExport = {}

      if (this.ids && this.ids.length > 0) {
        // 当勾选了ids时，只导出ids，忽略其他搜索条件
        queryParamsForExport = { ids: this.ids }
      } else {
        // 没有勾选ids时，收集表单的所有搜索条件
        queryParamsForExport = this.collectExportConditions()
      }

      // 保存导出参数并显示字段选择弹框
      this.pendingExportParams = queryParamsForExport
      this.exportFieldDialogVisible = true
    },

    /** 初始化导出字段选项 */
    initExportFieldOptions() {
      // 定义所有可导出的字段（仅包含后端 @Excel 注解标记的字段）
      this.exportFieldOptions = [
        { label: this.$t('payment.order.orderId'), value: 'id' },
        { label: this.$t('payment.order.merchantId'), value: 'merchantId' },
        { label: this.$t('payment.order.merchantName'), value: 'merchantName' },
        { label: this.$t('payment.order.ssoOrderId'), value: 'ssoOrderId' },
        { label: this.$t('payment.order.fiatAmount'), value: 'fiatAmount' },
        { label: this.$t('payment.order.faitCurrency'), value: 'faitCurrency' },
        { label: this.$t('payment.order.status'), value: 'status' },
        { label: this.$t('payment.order.createdAt'), value: 'createdAt' },
        { label: this.$t('payment.order.cryptoAmount'), value: 'cryptoAmount' },
        { label: this.$t('payment.order.cryptoCurrency'), value: 'cryptoCurrency' },
        { label: this.$t('payment.order.transactionId'), value: 'transactionId' },
        { label: this.$t('payment.order.callbackStatus'), value: 'callbackStatus' },
        { label: this.$t('payment.order.failReasons'), value: 'failReasons' },
        { label: this.$t('payment.order.paymentIp'), value: 'paymentIp' },
        { label: this.$t('payment.order.customerId'), value: 'customerId' },
        { label: this.$t('payment.order.customerWalletId'), value: 'customerWalletId' },
        { label: this.$t('payment.order.type'), value: 'type' },
        { label: this.$t('payment.order.processingFee'), value: 'processingFee' },
        { label: this.$t('payment.order.liquidityQuote'), value: 'liquidityQuote' },
        { label: this.$t('payment.order.includeFee'), value: 'includeFee' },
        { label: this.$t('payment.order.gatewayFee'), value: 'gatewayFee' },
        { label: this.$t('payment.order.rollingFee'), value: 'rollingFee' },
        { label: this.$t('payment.order.bankAccountId'), value: 'bankAccountId' },
        { label: this.$t('payment.order.updatedAt'), value: 'updateAt' },
        { label: this.$t('payment.order.cardNumber'), value: 'cardNumber' },
        { label: this.$t('payment.order.refundAmount'), value: 'refundAmount' },
        { label: this.$t('payment.order.email'), value: 'email' },
        { label: this.$t('payment.order.productName'), value: 'productName' },
        { label: this.$t('payment.order.walletAddress'), value: 'walletAddress' },
        { label: this.$t('payment.order.fromSource'), value: 'fromSource' }
      ]

      // 默认选中常用字段（如果用户没有保存配置，会使用这个默认值）
      // 注意：如果 loadExportFieldsConfig 加载到配置，会覆盖这个默认值
      if (this.selectedExportFields.length === 0) {
        this.selectedExportFields = [...this.defaultExportFields]
      }
    },

    /** 全选字段 */
    selectAllFields() {
      this.selectedExportFields = this.exportFieldOptions.map(field => field.value)
    },

    /** 取消全选 */
    unselectAllFields() {
      this.selectedExportFields = []
    },

    /** 反选 */
    invertSelection() {
      const allFields = this.exportFieldOptions.map(field => field.value)
      this.selectedExportFields = allFields.filter(field => !this.selectedExportFields.includes(field))
    },

    /** 恢复默认字段 */
    restoreDefaultFields() {
      this.selectedExportFields = [...this.defaultExportFields]
      this.$message.success(this.$t('common.restoreSuccess'))
    },

    /** 确认导出 */
    confirmExport() {
      if (this.selectedExportFields.length === 0) {
        this.$message.warning(this.$t('payment.order.pleaseSelectFields'))
        return
      }

      // 防止重复点击
      if (this.exporting) {
        this.$message.warning(this.$t('payment.order.exportingPleaseWait') || '正在导出中，请稍候...')
        return
      }

      // 保存用户的字段选择配置（异步，不阻塞导出）
      this.saveExportFieldsConfig()

      // 关闭弹框
      this.exportFieldDialogVisible = false

      // 准备确认文本
      let confirmText = ''
      if (this.ids && this.ids.length > 0) {
        confirmText = this.$t('payment.order.confirmExportSelected', { count: this.ids.length })
      } else {
        confirmText = this.$t('payment.order.confirmExportAll', { total: this.total })
      }

      this.$confirm(confirmText, this.$t('payment.order.warning'), {
        confirmButtonText: this.$t('payment.order.confirm'),
        cancelButtonText: this.$t('payment.order.cancel'),
        type: 'warning'
      })
        .then(() => {
          // 设置导出中状态
          this.exporting = true

          // 将选中的字段添加到导出参数中
          const exportParams = {
            ...this.pendingExportParams,
            exportFields: this.selectedExportFields.join(',')
          }

          // 如果是英文，则lang=en
          let lang = ''
          if (this.$i18n.locale === 'en') {
            lang = 'en'
          }

          // 执行导出
          return exportOrder(exportParams, lang)
        })
        .then(response => {
          this.download(response.msg)
          this.$message.success(this.$t('payment.order.exportSuccess') || '导出成功')
        })
        .catch(error => {
          // 用户取消导出，重新打开字段选择弹框
          if (error === 'cancel') {
            this.exportFieldDialogVisible = true
          } else {
            this.$message.error(this.$t('payment.order.exportFailed') || '导出失败')
          }
        })
        .finally(() => {
          // 重置导出状态
          this.exporting = false
        })
    },

    /** 加载用户的导出字段配置（混合方案：优先localStorage，然后从后端加载） */
    async loadExportFieldsConfig() {
      try {
        // 1. 优先从 localStorage 读取（快速）
        const localStorageKey = `exportFields_${this.MODULE_NAME}`
        const localConfig = localStorage.getItem(localStorageKey)

        if (localConfig) {
          try {
            const fields = JSON.parse(localConfig)
            if (Array.isArray(fields) && fields.length > 0) {
              this.selectedExportFields = fields
              console.log('从 localStorage 加载导出配置:', fields)
              return
            }
          } catch (e) {
            console.warn('解析 localStorage 配置失败:', e)
          }
        }

        // 2. localStorage 为空，从后端加载
        const response = await getUserExportConfig(this.MODULE_NAME)
        if (response.code === 200 && response.data) {
          const fields = response.data.split(',').filter(f => f.trim())
          if (fields.length > 0) {
            this.selectedExportFields = fields
            // 同步到 localStorage
            localStorage.setItem(localStorageKey, JSON.stringify(fields))
            console.log('从后端加载导出配置:', fields)
          }
        }
      } catch (error) {
        console.error('加载导出配置失败:', error)
        // 出错不影响功能，使用默认配置
      }
    },

    /** 保存用户的导出字段配置（混合方案：同时保存到localStorage和后端） */
    saveExportFieldsConfig() {
      try {
        const fields = this.selectedExportFields
        if (!fields || fields.length === 0) {
          return
        }

        const localStorageKey = `exportFields_${this.MODULE_NAME}`
        const fieldsStr = fields.join(',')

        // 1. 立即保存到 localStorage（快速响应）
        localStorage.setItem(localStorageKey, JSON.stringify(fields))
        console.log('已保存到 localStorage:', fields)

        // 2. 异步保存到后端（不阻塞用户操作）
        saveUserExportConfig(this.MODULE_NAME, fieldsStr)
          .then(response => {
            if (response.code === 200) {
              console.log('已保存到后端:', fieldsStr)
            }
          })
          .catch(error => {
            console.error('保存到后端失败:', error)
            // 失败不影响功能，localStorage已保存
          })
      } catch (error) {
        console.error('保存导出配置失败:', error)
      }
    },

    /** 收集导出条件 */
    collectExportConditions() {
      // 基础查询条件
      const conditions = {
        id: this.queryParams.id,
        ssoOrderId: this.queryParams.ssoOrderId,
        merchantId: this.queryParams.merchantId,
        customerId: this.queryParams.customerId,
        type: this.queryParams.type,
        currencyId: this.queryParams.currencyId,
        cryptoId: this.queryParams.cryptoId,
        status: this.queryParams.status,
        callbackStatus: this.queryParams.callbackStatus,
        amlStatus: this.queryParams.amlStatus,
        isOtc: this.queryParams.isOtc,
        includeFee: this.queryParams.includeFee,
        isRollback: this.queryParams.isRollback,
        callbackNum: this.queryParams.callbackNum,
        pageNum: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize
      }

      // 添加日期范围条件
      const conditionsWithDate = this.addDateRange(conditions, this.dateRange)

      // 过滤掉空值，只保留有效的查询条件
      const finalConditions = {}
      Object.keys(conditionsWithDate).forEach(key => {
        const value = conditionsWithDate[key]
        if (value !== undefined && value !== null && value !== '') {
          finalConditions[key] = value
        }
      })

      console.log('收集的导出条件:', finalConditions)
      return finalConditions
    },
    // 详情对话框格式化方法
    getMerchantName(merchantId) {
      return this.selectDictLabel(this.merchantIdOptions, merchantId)
    },
    getCurrencyName(currencyId) {
      return this.selectDictLabel(this.currencyIdOptions, currencyId)
    },
    getTypeName(type) {
      return this.selectDictLabel(this.typeOptions, type)
    },
    getStatusName(status) {
      return this.selectDictLabel(this.statusOptions, status)
    },
    getCryptoName(cryptoId) {
      return this.selectDictLabel(this.cryptoIdOptions, cryptoId)
    },
    getAmlStatusName(amlStatus) {
      return this.selectDictLabel(this.amlStatusOptions, amlStatus)
    },
    getTravelRuleStatusName(travelRuleStatus) {
      return this.selectDictLabel(this.travelRuleStatusOptions, travelRuleStatus)
    },
    getIsOtcName(isOtc) {
      return this.selectDictLabel(this.isOtcOptions, isOtc)
    },
    getIncludeFeeName(includeFee) {
      return this.selectDictLabel(this.includeFeeOptions, includeFee)
    },
    getBankAccountName(bankAccountId) {
      return this.selectDictLabel(this.bankAccountIdOptions, bankAccountId)
    },
    getIsRollbackName(isRollback) {
      return this.selectDictLabel(this.isRollbackOptions, isRollback)
    }
  }
}
</script>
<style scoped>
.mb20 {
  margin-bottom: 20px;
}

/* 订单详情对话框样式 */
.el-dialog__body {
  padding: 20px;
}

.el-card {
  margin-bottom: 20px;
}

.el-card:last-child {
  margin-bottom: 0;
}

.el-card__header {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
  padding: 15px 20px;
}

.el-card__header span {
  font-weight: 600;
  color: #303133;
}

.el-descriptions {
  margin: 0;
}

.el-descriptions__body {
  background-color: #fff;
}

.el-descriptions__label {
  background-color: #fafafa;
  color: #606266;
  font-weight: 500;
}

.el-descriptions__content {
  color: #303133;
}

/* AML载荷内容样式 */
pre {
  background-color: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 4px;
  padding: 12px;
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  line-height: 1.4;
  color: #495057;
}

/* 金额显示样式 */
.amount-red {
  color: #f56c6c !important;
  font-weight: 600;
}

.amount-orange {
  color: #e6a23c !important;
  font-weight: 600;
}

/* 自定义描述列表样式 */
.detail-list {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 10px;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.detail-item {
  display: flex;
  flex-direction: row;
  align-items: center;
  min-height: 40px;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
  padding: 8px 0;
  background-color: #fafafa;
}

.detail-label {
  font-weight: 500;
  color: #606266;
  text-align: right;
  margin-right: 0;
  font-size: 13px;
  flex: 0 0 130px;
}

.detail-value {
  color: #303133;
  text-align: left;
  font-weight: 500;
  word-break: break-all;
  flex: 1;
  display: flex;
  align-items: center;
}

/* 订单流程时间线样式 */
.order-timeline {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.timeline-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}

.timeline-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #c0c4cc;
  margin-bottom: 8px;
  position: relative;
}

.timeline-content {
  text-align: center;
}

.timeline-title {
  font-size: 12px;
  color: #606266;
  margin-bottom: 4px;
  font-weight: 500;
}

.timeline-time {
  font-size: 11px;
  color: #909399;
  line-height: 1.2;
}

.timeline-arrow {
  width: 60px;
  height: 2px;
  background: repeating-linear-gradient(to right, #c0c4cc 0, #c0c4cc 4px, transparent 4px, transparent 8px);
  margin: 0 10px;
  position: relative;
}

.timeline-arrow::after {
  content: '';
  position: absolute;
  right: -6px;
  top: -3px;
  width: 0;
  height: 0;
  border-left: 8px solid #c0c4cc;
  border-top: 4px solid transparent;
  border-bottom: 4px solid transparent;
}

/* 订单状态指示器样式 */
.order-status-indicator {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 20px;
}

.status-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-right: 20px;
}

.status-box {
  background-color: #f56c6c;
  color: #fff;
  padding: 12px 16px;
  border-radius: 4px;
  flex: 1;
}

.status-line {
  font-size: 14px;
  line-height: 1.4;
  font-weight: 500;
}

.status-line:first-child {
  margin-bottom: 4px;
}

/* 导出字段选择对话框样式 */
.export-field-dialog {
  max-height: 500px;
  overflow-y: auto;
}

.field-operations {
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e4e7ed;
}

.field-checkbox-group {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.field-checkbox-item {
  margin: 0 !important;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #f5f7fa;
  transition: all 0.3s;
}

.field-checkbox-item:hover {
  background-color: #ecf5ff;
  border-color: #409eff;
}

.field-checkbox-item.is-checked {
  background-color: #ecf5ff;
  border-color: #409eff;
}

/* 订单状态带失败原因的样式 */
.status-with-fail-reason {
  display: inline-block;
  cursor: pointer;
  border-bottom: 1px dashed #f56c6c;
  color: #f56c6c;
  font-weight: 500;
}
</style>
