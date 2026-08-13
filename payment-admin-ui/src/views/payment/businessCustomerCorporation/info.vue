<template>
  <!-- 在iframe中且有OAuth回调时，只显示loading并发送消息 -->
  <div v-if="isInIframeWithOAuth" class="oauth-callback-loading">
    <i class="el-icon-loading" style="font-size: 40px; color: #409eff"></i>
    <p style="margin-top: 20px; color: #606266">授权成功，正在处理...</p>
  </div>

  <div v-else v-loading="auditLoading" :element-loading-text="loadingText" class="app-container">
    <!-- 顶部状态栏 -->
    <el-card class="status-card" shadow="never">
      <div style="display: flex; justify-content: space-between; align-items: center">
        <div>
          <el-tag :type="getStatusType(form.businessProfileStatus)" size="medium">
            {{ $t('payment.shop.currentStatus') }}：{{ getStatusText(form.businessProfileStatus) }}
          </el-tag>
        </div>
        <div>
          <el-button v-if="form.businessProfileStatus == 0" type="primary" size="small" @click="showAuditDialog">
            {{ $t('common.audit') }}
          </el-button>
          <el-button v-if="form.businessProfileStatus == 1" type="warning" size="small" @click="showEditDialog">
            {{ $t('common.edit') }}
          </el-button>
          <el-button v-if="form.businessProfileStatus == 3" type="info" size="small" disabled>
            {{ $t('payment.shop.binderrAuditing') || 'Binderr审核中' }}
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 基础信息 -->
    <el-card class="info-card" shadow="never">
      <div slot="header" class="clearfix">
        <i class="el-icon-user" />
        <span style="margin-left: 8px; font-weight: bold">{{ $t('payment.shop.basicInfo') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.id') }}：</label>
            <span>{{ form.id }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.businessName') }}：</label>
            <span>{{ form.businessName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.businessNo') }}：</label>
            <span>{{ form.businessNo }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.industry') }}：</label>
            <span>{{ form.industry }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.sideIndustry') }}：</label>
            <span>{{ form.sideIndustry }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.country') }}：</label>
            <span>{{ form.country }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.registrationDate') }}：</label>
            <span>{{ form.registrationDate }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.registrationAddress') }}：</label>
            <span>{{ form.registrationAddress }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.businessDesc') }}：</label>
            <span>{{ form.businessDesc }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.turnoverYear') }}：</label>
            <span>{{ form.turnoverYear }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.turnoverWeek') }}：</label>
            <span>{{ form.turnoverWeek }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('common.userName') }}：</label>
            <span>{{ form.userName }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.website') }}：</label>
            <span>{{ form.website }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.supplierName') }}：</label>
            <span>{{ form.supplierName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.vatNumber') }}：</label>
            <span>{{ form.vatNumber }}</span>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" />
    </el-card>

    <!-- 计费信息 -->
    <el-card class="info-card" shadow="never">
      <div slot="header" class="clearfix">
        <i class="el-icon-money" />
        <span style="margin-left: 8px; font-weight: bold">{{ $t('payment.shop.billingInfo') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.feeModelName') }}：</label>
            <span>{{ form.feeModelName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.settlementCurrency') }}：</label>
            <span>{{ getSettlementCurrencyText(form.settlementCurrency) }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.isHouseMerchant') }}：</label>
            <span>{{ form.isHouseMerchant ? $t('common.yes') : $t('common.no') }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.cryptoDeliveryMethod') }}：</label>
            <span>{{ getCryptoDeliveryMethodText(form.cryptoDeliveryMethod) }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.reservePercentage') }}：</label>
            <span>{{ form.reservePercentage }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.thunesTransferFee') }}：</label>
            <span>{{ form.thunesTransferFee || '-' }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 配置信息 -->
    <el-card class="info-card" shadow="never">
      <div slot="header" class="clearfix">
        <i class="el-icon-setting" />
        <span style="margin-left: 8px; font-weight: bold">{{ $t('payment.shop.configurationInfo') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.status') }}：</label>
            <el-tag :type="form.status === 0 ? 'success' : 'danger'" size="mini">
              {{ form.status === 0 ? $t('common.enabled') : $t('common.disabled') }}
            </el-tag>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.notifyOrderStatusEndpoint') }}：</label>
            <span>{{ form.notifyOrderStatusEndpoint }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.is3dsOn') }}：</label>
            <span>{{ form.is3dsOn ? $t('common.yes') : $t('common.no') }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.merchantHashSecretKey') }}：</label>
            <span>{{ form.merchantHashSecretKey }}</span>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.cryptoDeliveryDelayTime') }}：</label>
            <span>{{ form.cryptoDeliveryDelayTime }}</span>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.descriptor') }}：</label>
            <span>{{ form.descriptor }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.defaultRedirectSuccessUrl') }}：</label>
            <span>{{ form.defaultRedirectSuccessUrl }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.defaultRedirectFailureUrl') }}：</label>
            <span>{{ form.defaultRedirectFailureUrl }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.threeDSuccessUrl') }}：</label>
            <span>{{ form.threeDSuccessUrl }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.threeDFailureUrl') }}：</label>
            <span>{{ form.threeDFailureUrl }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.onRampMin') }}：</label>
            <span>{{ form.onRampMin || '-' }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.onRampMax') }}：</label>
            <span>{{ form.onRampMax || '-' }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.offRampMin') }}：</label>
            <span>{{ form.offRampMin || '-' }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.offRampMax') }}：</label>
            <span>{{ form.offRampMax || '-' }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('payment.shop.station') }}：</label>
            <span>{{ form.station }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 附件信息 -->
    <el-card class="info-card" shadow="never">
      <div slot="header" class="clearfix">
        <i class="el-icon-folder" />
        <span style="margin-left: 8px; font-weight: bold">{{ $t('payment.shop.attachmentInfo') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="attachment-item">
            <div class="attachment-title">{{ $t('payment.shop.companyAuthTypeFile') }}</div>
            <div
              class="attachment-box"
              :class="{ 'has-file': form.companyAuthTypeUrl }"
              @click="handleAttachmentClick(form.companyAuthTypeUrl, $t('payment.shop.companyAuthTypeFile'))"
            >
              <div class="attachment-content">
                <i :class="getFileIcon(form.companyAuthTypeUrl)" class="file-icon" />
                <div class="file-info">
                  <div v-if="form.companyAuthTypeUrl" class="file-name">
                    {{ getFileName(form.companyAuthTypeUrl) }}
                  </div>
                  <div v-else class="no-file">{{ $t('payment.shop.noAttachment') }}</div>
                  <div v-if="form.companyAuthTypeUrl" class="file-type">
                    {{ getFileExtension(form.companyAuthTypeUrl).toUpperCase() }} {{ $t('payment.shop.file') }}
                  </div>
                </div>
              </div>
              <div v-if="form.companyAuthTypeUrl" class="download-overlay">
                <i class="el-icon-download" />
                <span>{{ $t('payment.shop.clickToDownload') }}</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="attachment-item">
            <div class="attachment-title">{{ $t('payment.shop.companyCharter') }}</div>
            <div
              class="attachment-box"
              :class="{ 'has-file': form.companyDocUrl }"
              @click="handleAttachmentClick(form.companyDocUrl, $t('payment.shop.companyCharter'))"
            >
              <div class="attachment-content">
                <i :class="getFileIcon(form.companyDocUrl)" class="file-icon" />
                <div class="file-info">
                  <div v-if="form.companyDocUrl" class="file-name">
                    {{ getFileName(form.companyDocUrl) }}
                  </div>
                  <div v-else class="no-file">{{ $t('payment.shop.noAttachment') }}</div>
                  <div v-if="form.companyDocUrl" class="file-type">
                    {{ getFileExtension(form.companyDocUrl).toUpperCase() }} {{ $t('payment.shop.file') }}
                  </div>
                </div>
              </div>
              <div v-if="form.companyDocUrl" class="download-overlay">
                <i class="el-icon-download" />
                <span>{{ $t('payment.shop.clickToDownload') }}</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="attachment-item">
            <div class="attachment-title">{{ $t('payment.shop.companyOrgChart') }}</div>
            <div
              class="attachment-box"
              :class="{ 'has-file': form.shareholdingStructure }"
              @click="handleAttachmentClick(form.shareholdingStructure, $t('payment.shop.companyOrgChart'))"
            >
              <div class="attachment-content">
                <i :class="getFileIcon(form.shareholdingStructure)" class="file-icon" />
                <div class="file-info">
                  <div v-if="form.shareholdingStructure" class="file-name">
                    {{ getFileName(form.shareholdingStructure) }}
                  </div>
                  <div v-else class="no-file">{{ $t('payment.shop.noAttachment') }}</div>
                  <div v-if="form.shareholdingStructure" class="file-type">
                    {{ getFileExtension(form.shareholdingStructure).toUpperCase() }} {{ $t('payment.shop.file') }}
                  </div>
                </div>
              </div>
              <div v-if="form.shareholdingStructure" class="download-overlay">
                <i class="el-icon-download" />
                <span>{{ $t('payment.shop.clickToDownload') }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 受益人信息 ========================================================================================================-->
    <el-card class="info-card" shadow="never">
      <div slot="header" class="clearfix">
        <i class="el-icon-user-solid" />
        <span style="margin-left: 8px; font-weight: bold">{{ $t('payment.shop.beneficiaryInfo') }}</span>
      </div>
      <el-table
        :data="form.ubos || []"
        style="width: 100%"
        border
        stripe
        :header-cell-style="{ backgroundColor: '#f8f9fa', color: '#495057' }"
      >
        <el-table-column prop="uboEmail" :label="$t('common.email')" width="180" />
        <el-table-column prop="authType" :label="$t('payment.shop.authType')" width="150">
          <template slot-scope="scope">
            <span>{{ getAuthTypeText(scope.row.authType) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.attachment')" width="200">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.idDocFrontUrl"
              type="text"
              size="small"
              @click="handleAttachmentClick(scope.row.idDocFrontUrl, $t('common.attachment'))"
            >
              {{ $t('payment.shop.viewAttachment') }}，{{ $t('payment.shop.clickToView') }}...
            </el-button>
            <span v-else>{{ $t('payment.shop.noAttachment') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="addressType" :label="$t('payment.shop.addressType')" width="140">
          <template slot-scope="scope">
            <span>{{ getAddressTypeText(scope.row.addressType) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('payment.shop.addressAttachment')" min-width="200">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.addressDocUrl"
              type="text"
              size="small"
              @click="handleAttachmentClick(scope.row.addressDocUrl, $t('payment.shop.addressAttachment'))"
            >
              {{ $t('payment.shop.viewAttachment') }}，{{ $t('payment.shop.clickToDownload') }}...
            </el-button>
            <span v-else>{{ $t('payment.shop.noAttachment') }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 图片预览弹框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="imageDialogVisible"
      width="60%"
      center
      :before-close="handleCloseDialog"
    >
      <div class="image-preview-container">
        <el-image :src="currentImageUrl" fit="contain" class="preview-image" :preview-src-list="[currentImageUrl]">
          <div slot="error" class="image-error">
            <i class="el-icon-picture-outline" />
            <div>{{ $t('payment.shop.imageLoadFailed') }}</div>
          </div>
        </el-image>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="imageDialogVisible = false">{{ $t('common.close') }}</el-button>
      </span>
    </el-dialog>

    <!-- 商户审核弹框 -->
    <el-dialog
      :title="$t('payment.shop.merchantAudit')"
      :visible.sync="auditDialogVisible"
      width="800px"
      center
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <div class="audit-form">
        <div class="audit-type">
          <span class="audit-label">{{ $t('payment.shop.auditType') }}：</span>
          <el-radio-group v-model="auditForm.businessProfileStatus">
            <el-radio :label="1">{{ $t('payment.shop.approve') }}</el-radio>
            <el-radio :label="2">{{ $t('payment.shop.reject') }}</el-radio>
          </el-radio-group>
        </div>

        <!-- 审核通过时的配置项 -->
        <div v-if="auditForm.businessProfileStatus === 1" class="approval-config">
          <el-row :gutter="20" style="margin-bottom: 16px">
            <el-col :span="12">
              <div class="config-item">
                <span class="config-label">{{ $t('payment.shop.onRampMin') }}：</span>
                <el-input v-model="auditForm.onRampMin" :placeholder="$t('common.pleaseEnter')" size="small">
                  <template slot="append">USD</template>
                </el-input>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="config-item">
                <span class="config-label">{{ $t('payment.shop.onRampMax') }}：</span>
                <el-input v-model="auditForm.onRampMax" :placeholder="$t('common.pleaseEnter')" size="small">
                  <template slot="append">USD</template>
                </el-input>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-bottom: 16px">
            <el-col :span="12">
              <div class="config-item">
                <span class="config-label">{{ $t('payment.shop.offRampMin') }}：</span>
                <el-input v-model="auditForm.offRampMin" :placeholder="$t('common.pleaseEnter')" size="small">
                  <template slot="append">USD</template>
                </el-input>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="config-item">
                <span class="config-label">{{ $t('payment.shop.offRampMax') }}：</span>
                <el-input v-model="auditForm.offRampMax" :placeholder="$t('common.pleaseEnter')" size="small">
                  <template slot="append">USD</template>
                </el-input>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-bottom: 16px">
            <el-col :span="12">
              <div class="config-item">
                <span class="config-label">{{ $t('payment.shop.feeModel') }}：</span>
                <el-select v-model="auditForm.merchantFeeModelId" :placeholder="$t('common.pleaseSelect')" size="small">
                  <el-option v-for="model in feeModels" :key="model.value" :label="model.label" :value="model.value" />
                </el-select>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="config-item">
                <span class="config-label">{{ $t('payment.shop.settlementCurrency') }}：</span>
                <el-select v-model="auditForm.settlementCurrency" :placeholder="$t('common.pleaseSelect')" size="small">
                  <el-option
                    v-for="currency in settlementCurrencyOptions"
                    :key="currency.value"
                    :label="currency.label"
                    :value="currency.value"
                  />
                </el-select>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-bottom: 16px">
            <el-col :span="12">
              <div class="config-item">
                <span class="config-label">{{ $t('payment.shop.station') }}：</span>
                <el-input v-model="auditForm.station" :placeholder="$t('common.pleaseEnter')" size="small" />
              </div>
            </el-col>
            <el-col :span="12">
              <div class="config-item">
                <span class="config-label">{{ $t('payment.shop.descriptor') }}：</span>
                <el-input v-model="auditForm.descriptor" :placeholder="$t('common.pleaseEnter')" size="small" />
              </div>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <div class="config-item">
                <span class="config-label">{{ $t('payment.shop.is3dsOn') }}：</span>
                <el-radio-group v-model="auditForm.is3dsOn">
                  <el-radio :label="true">{{ $t('common.enabled') }}</el-radio>
                  <el-radio :label="false">{{ $t('common.disabled') }}</el-radio>
                </el-radio-group>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-bottom: 16px">
            <el-col :span="12">
              <div class="config-item">
                <span class="config-label">{{ $t('payment.shop.fromSource') }}：</span>
                <el-select v-model="auditForm.fromSource" :placeholder="$t('common.pleaseSelect')" size="small">
                  <el-option
                    v-for="channel in paymentChannelOptions"
                    :key="channel.value"
                    :label="channel.label"
                    :value="channel.value"
                  />
                </el-select>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 审核拒绝时的拒绝原因 -->
        <div v-if="auditForm.businessProfileStatus === 2" class="rejection-reason">
          <div class="reason-item">
            <span class="reason-label">* {{ $t('payment.shop.rejectionReason') }}：</span>
            <el-input
              v-model="auditForm.remark"
              type="textarea"
              :rows="4"
              :placeholder="$t('payment.shop.enterRemark')"
              maxlength="200"
              show-word-limit
            />
          </div>
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="auditDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="submitAudit">{{ $t('common.confirm') }}</el-button>
      </span>
    </el-dialog>

    <!-- OAuth授权弹框 -->
    <el-dialog
      title="Binderr Authorization"
      :visible.sync="oauthDialogVisible"
      width="800px"
      center
      :close-on-click-modal="false"
      :before-close="closeOAuthDialog"
    >
      <div class="oauth-container">
        <iframe v-if="oauthDialogVisible && oauthUrl" :src="oauthUrl" frameborder="0" class="oauth-iframe" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeOAuthDialog">取消授权</el-button>
      </span>
    </el-dialog>

    <!-- 商户修改弹框 -->
    <el-dialog
      :title="$t('payment.shop.merchantEdit')"
      :visible.sync="editDialogVisible"
      width="1200px"
      center
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <div class="edit-form">
        <!-- 基础信息卡片 -->
        <el-card class="edit-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-user" />
            <span>{{ $t('payment.shop.basicInfo') }}</span>
          </div>
          <el-form ref="basicForm" :model="editForm" :rules="editRules" label-width="140px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.businessName')" prop="businessName">
                  <el-input v-model="editForm.businessName" :placeholder="$t('common.pleaseEnter')" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.businessNo')" prop="businessNo">
                  <el-input v-model="editForm.businessNo" :placeholder="$t('common.pleaseEnter')" disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.industry')" prop="industry">
                  <el-input v-model="editForm.industry" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.sideIndustry')" prop="sideIndustry">
                  <el-input v-model="editForm.sideIndustry" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.country')" prop="country">
                  <el-input v-model="editForm.country" :placeholder="$t('common.pleaseEnter')" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.registrationDate')" prop="registrationDate">
                  <el-date-picker
                    v-model="editForm.registrationDate"
                    type="date"
                    :placeholder="$t('common.pleaseSelect')"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.registrationAddress')" prop="registrationAddress">
                  <el-input
                    v-model="editForm.registrationAddress"
                    :placeholder="$t('common.pleaseEnter')"
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.website')" prop="website">
                  <el-input v-model="editForm.website" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.turnoverYear')" prop="turnoverYear">
                  <el-input v-model="editForm.turnoverYear" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.turnoverWeek')" prop="turnoverWeek">
                  <el-input v-model="editForm.turnoverWeek" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.supplierName')" prop="supplierName">
                  <el-input v-model="editForm.supplierName" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.businessDesc')" prop="businessDesc">
                  <el-input
                    v-model="editForm.businessDesc"
                    type="textarea"
                    :rows="2"
                    :placeholder="$t('common.pleaseEnter')"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.vatNumber')" prop="vatNumber">
                  <el-input v-model="editForm.vatNumber" :placeholder="$t('common.pleaseEnter')" disabled />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>

        <!-- 计费信息卡片 -->
        <el-card class="edit-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-money" />
            <span>{{ $t('payment.shop.billingInfo') }}</span>
          </div>
          <el-form ref="billingForm" :model="editForm" :rules="editRules" label-width="140px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.feeModel')" prop="merchantFeeModelId">
                  <el-select
                    v-model="editForm.merchantFeeModelId"
                    :placeholder="$t('common.pleaseSelect')"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="model in feeModels"
                      :key="model.value"
                      :label="model.label"
                      :value="model.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.settlementCurrency')" prop="settlementCurrency">
                  <el-select
                    v-model="editForm.settlementCurrency"
                    :placeholder="$t('common.pleaseSelect')"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="currency in settlementCurrencyOptions"
                      :key="currency.value"
                      :label="currency.label"
                      :value="currency.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.reservePercentage')" prop="reservePercentage">
                  <el-input v-model="editForm.reservePercentage" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.cryptoDeliveryMethod')" prop="cryptoDeliveryMethod">
                  <el-select
                    v-model="editForm.cryptoDeliveryMethod"
                    :placeholder="$t('common.pleaseSelect')"
                    style="width: 100%"
                  >
                    <el-option :value="1" :label="$t('payment.shop.platformInteraction')" />
                    <el-option :value="0" :label="$t('payment.shop.merchantInteraction')" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  :label="$t('payment.shop.isHouseMerchant')"
                  prop="isHouseMerchant"
                  class="radio-form-item"
                >
                  <el-radio-group v-model="editForm.isHouseMerchant">
                    <el-radio :label="true">{{ $t('common.yes') }}</el-radio>
                    <el-radio :label="false">{{ $t('common.no') }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>

        <!-- 配置信息卡片 -->
        <el-card class="edit-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-setting" />
            <span>{{ $t('payment.shop.configurationInfo') }}</span>
          </div>
          <el-form ref="configForm" :model="editForm" :rules="editRules" label-width="180px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.is3dsOn')" prop="is3dsOn" class="radio-form-item">
                  <el-radio-group v-model="editForm.is3dsOn">
                    <el-radio :label="true">{{ $t('common.enabled') }}</el-radio>
                    <el-radio :label="false">{{ $t('common.disabled') }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="12">
                <el-form-item :label="$t('payment.shop.status')" prop="status" class="radio-form-item">
                  <el-radio-group v-model="editForm.status">
                    <el-radio :label="0">{{ $t('common.enabled') }}</el-radio>
                    <el-radio :label="1">{{ $t('common.disabled') }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col> -->
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.merchantHashSecretKey')" prop="merchantHashSecretKey">
                  <el-input v-model="editForm.merchantHashSecretKey" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.notifyOrderStatusEndpoint')" prop="notifyOrderStatusEndpoint">
                  <el-input v-model="editForm.notifyOrderStatusEndpoint" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.station')" prop="station">
                  <el-input v-model="editForm.station" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.descriptor')" prop="descriptor">
                  <el-input v-model="editForm.descriptor" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.defaultRedirectSuccessUrl')" prop="defaultRedirectSuccessUrl">
                  <el-input v-model="editForm.defaultRedirectSuccessUrl" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.defaultRedirectFailureUrl')" prop="defaultRedirectFailureUrl">
                  <el-input v-model="editForm.defaultRedirectFailureUrl" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.threeDSuccessUrl')" prop="threeDSuccessUrl">
                  <el-input v-model="editForm.threeDSuccessUrl" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.threeDFailureUrl')" prop="threeDFailureUrl">
                  <el-input v-model="editForm.threeDFailureUrl" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.onRampMin')" prop="onRampMin">
                  <el-input v-model="editForm.onRampMin" :placeholder="$t('common.pleaseEnter')">
                    <template slot="append">USD</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.onRampMax')" prop="onRampMax">
                  <el-input v-model="editForm.onRampMax" :placeholder="$t('common.pleaseEnter')">
                    <template slot="append">USD</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.offRampMin')" prop="offRampMin">
                  <el-input v-model="editForm.offRampMin" :placeholder="$t('common.pleaseEnter')">
                    <template slot="append">USD</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.offRampMax')" prop="offRampMax">
                  <el-input v-model="editForm.offRampMax" :placeholder="$t('common.pleaseEnter')">
                    <template slot="append">USD</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.cryptoDeliveryDelayTime')" prop="cryptoDeliveryDelayTime">
                  <el-input v-model="editForm.cryptoDeliveryDelayTime" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.notifyOrderStatusEndpoint2')" prop="notifyOrderStatusEndpoint2">
                  <el-input
                    v-model="editForm.notifyOrderStatusEndpoint2"
                    :placeholder="$t('payment.shop.notifyOrderStatusEndpoint2')"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.endpoint2Percent')" prop="endpoint2Percent">
                  <el-input
                    v-model.number="editForm.endpoint2Percent"
                    type="number"
                    :placeholder="$t('payment.shop.endpoint2Percent')"
                    :min="0"
                    :max="100"
                    clearable
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.notifyOrderStatusEndpointNft')" prop="notifyOrderStatusEndpointNft">
                  <el-input
                    v-model="editForm.notifyOrderStatusEndpointNft"
                    :placeholder="$t('payment.shop.notifyOrderStatusEndpointNft')"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.fromSource')" prop="fromSource">
                  <el-select v-model="editForm.fromSource" :placeholder="$t('common.pleaseSelect')" style="width: 100%">
                    <el-option
                      v-for="channel in paymentChannelOptions"
                      :key="channel.value"
                      :label="channel.label"
                      :value="channel.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('payment.shop.thunesTransferFee')" prop="thunesTransferFee">
                  <el-input v-model="editForm.thunesTransferFee" :placeholder="$t('common.pleaseEnter')" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button :loading="editLoading" type="primary" @click="submitEdit">{{ $t('common.confirm') }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  auditBusinessCustomerCorporation,
  getBusinessCustomerCorporation,
  getFeeModels,
  updateBusinessCustomerCorporation
} from '@/api/payment/businessCustomerCorporation'
import { getFileExtension, getFileIcon, getFileName } from '@/utils/fileRelated'

export default {
  name: 'BusinessCustomerCorporation',
  components: {},
  data() {
    return {
      form: {},
      // 检测是否在iframe中且有OAuth回调
      isInIframeWithOAuth: false,
      // 图片预览弹框相关数据
      imageDialogVisible: false,
      currentImageUrl: '',
      dialogTitle: '',
      businessProfileStatusOptions: [], // 商户状态
      settlementCurrencyOptions: [], // 结算货币
      paymentChannelOptions: [], // 支付渠道
      feeModels: [],
      // 审核弹框相关数据
      auditDialogVisible: false,
      auditLoading: false,
      loadingText: '正在处理中...',
      // OAuth授权弹框相关数据
      oauthDialogVisible: false,
      oauthUrl: '',
      auditForm: {
        businessProfileStatus: 1, // 1: 通过, 2: 拒绝
        onRampMin: '', // 最小入金
        onRampMax: '', // 最大入金
        offRampMin: '', // 最小出金
        offRampMax: '', // 最大出金
        merchantFeeModelId: '', // 计费模型
        settlementCurrency: '', // 结算货币
        station: '', // 商户域名
        descriptor: '', // 商户标识
        is3dsOn: true,
        fromSource: 'NUVEI', // 支付渠道
        remark: '' // 失败原因
      },
      // 修改弹框相关数据
      editDialogVisible: false,
      editLoading: false,
      editForm: {
        // 基础信息
        businessName: '',
        businessNo: '',
        industry: '',
        sideIndustry: '',
        country: '',
        registrationDate: '',
        registrationAddress: '',
        website: '',
        supplierName: '',
        businessDesc: '',
        turnoverYear: '',
        turnoverWeek: '',
        vatNumber: '',
        // 计费信息
        merchantFeeModelId: '',
        settlementCurrency: '',
        isHouseMerchant: false,
        cryptoDeliveryMethod: 0,
        reservePercentage: '',
        // 配置信息
        merchantHashSecretKey: '',
        notifyOrderStatusEndpoint: '',
        is3dsOn: false,
        cryptoDeliveryDelayTime: '',
        station: '',
        descriptor: '',
        defaultRedirectSuccessUrl: '',
        defaultRedirectFailureUrl: '',
        threeDSuccessUrl: '',
        threeDFailureUrl: '',
        onRampMin: '',
        onRampMax: '',
        offRampMin: '',
        offRampMax: '',
        // 新增字段
        notifyOrderStatusEndpoint2: '',
        endpoint2Percent: '',
        notifyOrderStatusEndpointNft: '',
        fromSource: '' // 支付渠道
      },
      editRules: {
        businessNo: [
          { required: true, message: this.$t('payment.shop.validation.pleaseEnterBusinessNo'), trigger: 'blur' }
        ],
        businessName: [
          { required: true, message: this.$t('payment.shop.validation.pleaseEnterBusinessName'), trigger: 'blur' }
        ],
        merchantFeeModelId: [
          { required: true, message: this.$t('payment.shop.validation.feeModelRequired'), trigger: 'change' }
        ],
        saasUserCorporationId: [
          { required: true, message: this.$t('payment.shop.validation.saasUserCorporationIdRequired'), trigger: 'blur' }
        ],
        isSiftOn: [
          { required: true, message: this.$t('payment.shop.validation.siftTypeRequired'), trigger: 'change' }
        ],
        is3dsOn: [
          { required: true, message: this.$t('payment.shop.validation.threeDsTypeRequired'), trigger: 'change' }
        ],
        isWhitelistOn: [
          { required: true, message: this.$t('payment.shop.validation.typeRequired'), trigger: 'change' }
        ],
        notifyOrderStatusEndpoint3: [
          {
            required: true,
            message: this.$t('payment.shop.validation.notifyOrderStatusEndpoint3Required'),
            trigger: 'blur'
          }
        ],
        aniCheck: [
          { required: true, message: this.$t('payment.shop.validation.aniCheckRequired'), trigger: 'blur' }
        ],
        acquirerId: [
          { required: true, message: this.$t('payment.shop.validation.acquirerIdRequired'), trigger: 'blur' }
        ],
        endpoint2Percent: [
          {
            validator: (rule, value, callback) => {
              if (value === '' || value === null || value === undefined) {
                // 允许为空
                callback()
              } else {
                const num = Number(value)
                if (isNaN(num)) {
                  callback(new Error(this.$t('payment.shop.validation.pleaseEnterNumber')))
                } else if (!Number.isInteger(num)) {
                  callback(new Error(this.$t('payment.shop.validation.pleaseEnterInteger')))
                } else if (num < 0 || num > 100) {
                  callback(new Error(this.$t('payment.shop.validation.pleaseEnter0To100')))
                } else {
                  callback()
                }
              }
            },
            trigger: 'blur'
          }
        ]
      }
    }
  },
  watch: {
    // 监听路由变化，当路由参数改变时重新初始化数据
    $route(to, from) {
      if (to.path === '/payment/businessInfo') {
        const newId = to.params.id || to.query.id
        const oldId = from.params.id || from.query.id

        // 如果是不同的ID，或者是从其他页面跳转过来的
        if (newId && (newId !== oldId || from.path !== to.path)) {
          console.log('检测到ID变化或新进入页面，重新加载数据')
          this.initData()
        }
      }
    }
  },
  async created() {
    // 优先检查是否在iframe中且有OAuth回调
    const isInIframe = window.self !== window.top
    const urlParams = new URLSearchParams(window.location.search)
    const hasOAuthCode = urlParams.has('code')

    if (isInIframe && hasOAuthCode) {
      // 设置标志，让页面只显示loading
      this.isInIframeWithOAuth = true

      // 立即向父窗口发送消息
      console.log('在 iframe 中检测到 OAuth 回调，向父窗口发送消息')
      window.parent.postMessage(
        {
          code: urlParams.get('code'),
          from_oauth: true
        },
        '*'
      )

      // 不继续执行其他初始化逻辑
      return
    }

    // 正常初始化流程
    await this.initData()
    await this.getBusinessProfileStatusOptions()
    await this.getSettlementCurrencyOptions()
    await this.getPaymentChannelOptions()
    await this.getFeeModels()
    // 检查是否是从OAuth授权回来的
    this.checkOAuthCallback()
  },
  beforeDestroy() {
    // 组件销毁前移除OAuth消息监听器
    if (this.oauthMessageHandler) {
      window.removeEventListener('message', this.oauthMessageHandler)
      this.oauthMessageHandler = null
    }
  },
  methods: {
    // 工具函数,
    getFileName,
    getFileExtension,
    getFileIcon,
    async initData() {
      console.log('initData 方法被调用')
      // 获取路由参数中的ID
      const id = this.$route.params.id || this.$route.query.id
      console.log('当前路由ID:', id)
      if (id) {
        await this.getInfo(id)
      }
    },
    async getInfo(id) {
      try {
        const res = await getBusinessCustomerCorporation(id)
        this.form = res.data || {}
      } catch (error) {
        console.error('获取商户信息失败:', error)
        this.$message.error('获取商户信息失败')
      }
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        0: 'warning', // KYB待审核
        1: 'success', // 审核通过
        2: 'danger', // 审核拒绝
        3: 'info' // Binderr审核中
      }
      return statusMap[status] || 'warning'
    },

    // 获取状态文本
    getStatusText(status) {
      const statusOption = this.businessProfileStatusOptions.find(item => item.value == status)
      return statusOption ? statusOption.label : '未知状态'
    },

    // 获取加密货币交付方式文本
    getCryptoDeliveryMethodText(method) {
      // 处理各种可能的值类型
      if (method === 1 || method === '1') {
        return this.$t('payment.shop.platformInteraction')
      } else if (method === 0 || method === '0') {
        return this.$t('payment.shop.merchantInteraction')
      } else if (method === undefined || method === null || method === '') {
        return '-'
      } else {
        return method
      }
    },

    // 获取证件类型文本
    getAuthTypeText(authType) {
      const authTypeMap = {
        PASSPORT: this.$t('common.passport'),
        ID_CARD: this.$t('common.idCard'),
        DRIVING_LICENSE: this.$t('common.drivingLicense'),
        OTHER: this.$t('common.other')
      }
      return authTypeMap[authType] || authType
    },

    // 获取地址类型文本
    getAddressTypeText(addressType) {
      const addressTypeMap = {
        LEASE_CONTRACT: '租赁合同',
        UTILITY_BILL: '水电费账单',
        BANK_STATEMENT: '银行流水',
        OTHER: '其他'
      }
      return addressTypeMap[addressType] || addressType
    },

    // 获取结算货币文本
    getSettlementCurrencyText(currencyId) {
      if (!currencyId || !this.settlementCurrencyOptions || this.settlementCurrencyOptions.length === 0) {
        return currencyId || '-'
      }
      const currency = this.settlementCurrencyOptions.find(item => item.value == currencyId)
      return currency ? currency.label : currencyId
    },

    // 查询收费模型
    async getFeeModels() {
      try {
        const response = await getFeeModels()
        console.log(response)
        this.feeModels =
          response.rows.map(item => ({
            value: parseInt(item.id),
            label: item.name
          })) || []
        console.log('feeModels:', this.feeModels)
      } catch (error) {
        console.error('获取计费模型失败:', error)
        this.$message.error('获取计费模型失败')
        this.feeModels = []
      }
    },

    // 获取商户状态字典选项
    async getBusinessProfileStatusOptions() {
      try {
        const response = await this.getDicts('BUSINESS_CUSTOMER_CORPORATION_BUSINESS_PROFILE_STATUS')
        this.businessProfileStatusOptions = response.data.map(item => ({
          value: parseInt(item.dictValue),
          label: item.dictLabel
        }))
        console.log('businessProfileStatusOptions:', this.businessProfileStatusOptions)
      } catch (error) {
        console.error('获取商户状态字典失败:', error)
        this.$message.error('获取商户状态字典失败')
        this.businessProfileStatusOptions = []
      }
    },

    // 获取结算货币
    async getSettlementCurrencyOptions() {
      try {
        const response = await this.getDicts('CURRENCY_ID')
        this.settlementCurrencyOptions = response.data.map(item => ({
          value: parseInt(item.dictValue),
          label: item.dictLabel
        }))
        console.log('settlementCurrencyOptions:', this.settlementCurrencyOptions)
      } catch (error) {
        console.error('获取结算货币失败:', error)
        this.$message.error('获取结算货币失败')
        this.settlementCurrencyOptions = []
      }
    },

    // 获取支付渠道字典选项
    async getPaymentChannelOptions() {
      try {
        const response = await this.getDicts('PAYMENT_CHANNEL')
        this.paymentChannelOptions = response.data.map(item => ({
          value: item.dictValue,
          label: item.dictLabel
        }))
        console.log('paymentChannelOptions:', this.paymentChannelOptions)
      } catch (error) {
        console.error('获取支付渠道字典失败:', error)
        this.$message.error('获取支付渠道字典失败')
        this.paymentChannelOptions = []
      }
    },

    // 显示图片预览弹框
    showImageDialog(imageUrl, title) {
      this.currentImageUrl = imageUrl
      this.dialogTitle = title
      this.imageDialogVisible = true
    },

    // 关闭弹框
    handleCloseDialog() {
      this.imageDialogVisible = false
      this.currentImageUrl = ''
      this.dialogTitle = ''
    },

    // 处理附件点击事件
    handleAttachmentClick(fileUrl, fileName) {
      if (!fileUrl) {
        this.$message.warning('暂无附件')
        return
      }
      // 如果是图片类型，显示预览弹框
      if (this.isImageFile(fileUrl)) {
        this.showImageDialog(fileUrl, fileName)
      } else {
        // 其他文件类型直接下载
        this.downloadFile(fileUrl, fileName)
      }
    },

    // 下载文件
    downloadFile(fileUrl, fileName) {
      const link = document.createElement('a')
      link.href = fileUrl
      link.download = `${fileName}_${Date.now()}.${getFileExtension(fileUrl)}`
      link.target = '_blank'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      this.$message.success(`${fileName}下载已开始`)
    },

    // 判断是否为图片文件
    isImageFile(fileUrl) {
      const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg']
      const extension = getFileExtension(fileUrl).toLowerCase()
      return imageExtensions.includes(extension)
    },

    // 显示审核弹框
    showAuditDialog() {
      // 安全检查：只有待审核状态(0)才能审核
      if (this.form.businessProfileStatus !== 0) {
        this.$message.warning('当前状态不允许审核操作')
        return
      }

      this.auditDialogVisible = true
      // 重置表单数据，确保数据类型匹配
      this.auditForm = {
        businessProfileStatus: this.form.businessProfileStatus || 1,
        onRampMin: this.form.onRampMin || '',
        onRampMax: this.form.onRampMax || '',
        offRampMin: this.form.offRampMin || '',
        offRampMax: this.form.offRampMax || '',
        merchantFeeModelId: this.form.merchantFeeModelId ? Number(this.form.merchantFeeModelId) : '', // 如果没有数据则为空
        settlementCurrency: this.form.settlementCurrency ? Number(this.form.settlementCurrency) : '', // 如果没有数据则为空
        is3dsOn: this.form.is3dsOn != 0,
        station: this.form.station || '', // 商户域名
        descriptor: this.form.descriptor || '', // 商户标识
        fromSource: this.form.fromSource || 'NUVEI', // 支付渠道
        remark: ''
      }
      console.log('初始化审核表单数据:', this.auditForm)
      console.log('当前计费模型选项:', this.feeModels)
      console.log('当前结算货币选项:', this.settlementCurrencyOptions)
    },

    // 提交审核（先进行OAuth授权）
    async submitAudit() {
      // 验证表单
      if (this.auditForm.businessProfileStatus === 2 && !this.auditForm.remark) {
        this.$message.error('请输入拒绝原因')
        return
      }

      // 验证审核通过时的必填项
      if (this.auditForm.businessProfileStatus === 1) {
        if (
          !this.auditForm.onRampMin ||
          !this.auditForm.onRampMax ||
          !this.auditForm.offRampMin ||
          !this.auditForm.offRampMax ||
          !this.auditForm.merchantFeeModelId ||
          !this.auditForm.settlementCurrency
        ) {
          this.$message.error('请填写完整的审核配置信息')
          return
        }

        // 保存审核数据到本地存储，授权回来后使用
        this.saveAuditDataToStorage()

        // 关闭弹框
        this.auditDialogVisible = false

        // 跳转到OAuth授权页面
        this.redirectToOAuthAuthorization()
      } else {
        // 直接拒绝,不用通过OAuth授权，直接提交数据
        await this.submitRejectAudit()
      }
    },

    // 保存审核数据到本地存储
    saveAuditDataToStorage() {
      const auditData = {
        id: this.form.id,
        businessProfileStatus: this.auditForm.businessProfileStatus,
        timestamp: Date.now()
      }

      // 如果是审核通过，添加配置参数
      if (this.auditForm.businessProfileStatus === 1) {
        auditData.onRampMin = this.auditForm.onRampMin
        auditData.onRampMax = this.auditForm.onRampMax
        auditData.offRampMin = this.auditForm.offRampMin
        auditData.offRampMax = this.auditForm.offRampMax
        auditData.merchantFeeModelId = this.auditForm.merchantFeeModelId
        auditData.settlementCurrency = this.auditForm.settlementCurrency
        auditData.station = this.auditForm.station
        auditData.descriptor = this.auditForm.descriptor
        auditData.is3dsOn = this.auditForm.is3dsOn ? 1 : 0
        auditData.fromSource = this.auditForm.fromSource
      } else if (this.auditForm.businessProfileStatus === 2) {
        // 如果是审核拒绝，只添加拒绝原因
        auditData.remark = this.auditForm.remark
      }

      // 存储到localStorage
      localStorage.setItem('pendingAuditData', JSON.stringify(auditData))
      console.log('审核数据已保存到本地存储:', auditData)
    },

    // 跳转到OAuth授权页面
    redirectToOAuthAuthorization() {
      try {
        // 获取当前页面路由中的id参数
        const currentId = this.$route.params.id || this.$route.query.id
        console.log('当前页面ID:', currentId)

        if (!currentId) {
          this.$message.error('无法获取页面ID，打开授权失败')
          return
        }

        // 构建OAuth授权URL
        const oauthBaseUrl = process.env.VUE_APP_BINDERR_OAUTH_URL || ''
        const clientId = process.env.VUE_APP_BINDERR_CLIENT_ID || ''
        const host = process.env.VUE_APP_HOST || ''
        if (!oauthBaseUrl || !clientId || !host) {
          console.error('OAuth配置错误:', { oauthBaseUrl, clientId, host })
          this.$message.error('OAuth配置错误，请联系管理员')
          return
        }
        // 使用当前页面URL作为回调地址，移除可能存在的code和state参数
        const currentUrl = new URL(window.location.href)
        currentUrl.searchParams.delete('code')
        currentUrl.searchParams.delete('state')
        const redirectUri = currentUrl.toString()
        const oauthUrl = `${oauthBaseUrl}?client_id=${clientId}&redirect_uri=${encodeURIComponent(redirectUri)}`

        console.log('准备在弹框中打开OAuth授权页面:', oauthUrl)

        // 设置OAuth URL并显示弹框
        this.oauthUrl = oauthUrl
        this.oauthDialogVisible = true

        // 监听OAuth授权回调消息
        this.setupOAuthMessageListener()
      } catch (error) {
        console.error('打开OAuth授权页面失败:', error)
        this.$message.error('打开授权页面失败，请重试')
      }
    },

    // 设置OAuth消息监听器
    setupOAuthMessageListener() {
      // 移除之前的监听器（如果存在）
      if (this.oauthMessageHandler) {
        window.removeEventListener('message', this.oauthMessageHandler)
      }

      // 创建新的消息处理器
      this.oauthMessageHandler = async event => {
        // 检查消息是否包含OAuth授权码
        if (event.data && event.data.code && event.data.from_oauth) {
          console.log('收到OAuth授权回调消息:', event.data)

          // 立即关闭OAuth弹框（不使用await，确保立即执行）
          this.oauthDialogVisible = false
          // 清空OAuth URL
          this.oauthUrl = ''

          // 移除监听器
          window.removeEventListener('message', this.oauthMessageHandler)
          this.oauthMessageHandler = null

          // 处理授权回调（异步处理，不阻塞弹框关闭）
          await this.handleOAuthSuccess(event.data.code)
        }
      }

      // 添加消息监听器
      window.addEventListener('message', this.oauthMessageHandler)
    },

    // 处理OAuth授权成功
    async handleOAuthSuccess(oauthCode) {
      try {
        this.auditLoading = true
        this.loadingText = '正在提交审核...'

        // 从本地存储获取待处理的审核数据
        const auditDataStr = localStorage.getItem('pendingAuditData')
        if (!auditDataStr) {
          this.$message.error('未找到待处理的审核数据，请重新审核')
          this.auditLoading = false
          return
        }

        const auditData = JSON.parse(auditDataStr)
        console.log('从本地存储获取的审核数据:', auditData)

        // 构建提交参数
        const params = {
          id: auditData.id,
          businessProfileStatus: auditData.businessProfileStatus,
          code: oauthCode
        }

        // 如果是审核通过，添加配置参数
        if (auditData.businessProfileStatus === 1) {
          params.onRampMin = auditData.onRampMin
          params.onRampMax = auditData.onRampMax
          params.offRampMin = auditData.offRampMin
          params.offRampMax = auditData.offRampMax
          params.merchantFeeModelId = auditData.merchantFeeModelId
          params.settlementCurrency = auditData.settlementCurrency
          params.station = auditData.station
          params.descriptor = auditData.descriptor
          params.is3dsOn = auditData.is3dsOn
        }

        console.log('提交审核参数:', params)

        // 调用审核接口
        const res = await auditBusinessCustomerCorporation(params)

        if (res.code === 200) {
          this.$message.success('审核提交成功')
          // 清除本地存储的审核数据
          localStorage.removeItem('pendingAuditData')
          // 重新加载页面数据
          await this.getInfo(this.form.id)
        } else {
          this.$message.error(res.message || '审核提交失败')
        }
      } catch (error) {
        console.error('提交审核失败:', error)
        this.$message.error('提交审核失败，请重试')
      } finally {
        this.auditLoading = false
      }
    },

    // 关闭OAuth弹框
    closeOAuthDialog() {
      this.oauthDialogVisible = false
      // 清空OAuth URL，确保下次打开时重新加载
      this.oauthUrl = ''
      // 移除消息监听器
      if (this.oauthMessageHandler) {
        window.removeEventListener('message', this.oauthMessageHandler)
        this.oauthMessageHandler = null
      }
    },

    // 处理OAuth授权回调后的审核提交
    async processOAuthCallback() {
      try {
        const oauthCode = this.$route.query.code
        if (!oauthCode) {
          this.$message.error('从路由参数中获取到code:未获取到OAuth授权码，请重新授权')
          return oauthCode
        }

        // 从本地存储获取待处理的审核数据
        const auditDataStr = localStorage.getItem('pendingAuditData')
        if (!auditDataStr) {
          return
        }

        const auditData = JSON.parse(auditDataStr)

        // 检查数据是否过期（5分钟内有效）
        if (Date.now() - auditData.timestamp > 5 * 60 * 1000) {
          localStorage.removeItem('pendingAuditData')
          this.$message.error('授权超时，请重新提交审核')
          return
        }

        // 显示页面级加载状态
        this.auditLoading = true
        this.loadingText = '正在处理OAuth授权回调...'

        // 将OAuth授权码添加到审核数据中
        const submitData = {
          ...auditData,
          code: oauthCode // 添加OAuth授权码
        }

        // 移除时间戳，后台不需要
        delete submitData.timestamp

        console.log('准备提交的完整数据（包含OAuth code）:', submitData)

        // 提交审核数据（包含OAuth授权码）
        await auditBusinessCustomerCorporation(submitData)

        const auditTypeText = auditData.businessProfileStatus === 1 ? '通过' : '拒绝'
        this.$message.success(`OAuth授权成功，商户审核${auditTypeText}提交成功，进入Binderr审核中`)

        // 清除本地存储的数据
        localStorage.removeItem('pendingAuditData')

        // 重新获取数据
        await this.getInfo(this.form.id)

        // 移除URL中的OAuth相关参数
        this.cleanupOAuthParams()
      } catch (error) {
        console.error('OAuth回调后审核提交失败:')
        console.error(error)

        this.$message.error('审核提交失败，请重试')
        // 清除本地存储的数据
        localStorage.removeItem('pendingAuditData')
      } finally {
        this.auditLoading = false
      }
    },

    // 直接提交拒绝审核（无需OAuth授权）
    async submitRejectAudit() {
      try {
        // 显示页面级加载状态
        this.auditLoading = true
        this.loadingText = '正在提交审核状态...'

        // 构建拒绝审核的数据
        const submitData = {
          id: this.form.id,
          businessProfileStatus: 2, // 拒绝状态
          remark: this.auditForm.remark // 拒绝原因
        }

        console.log('提交拒绝审核数据:', submitData)

        // 直接提交审核数据
        await auditBusinessCustomerCorporation(submitData)

        this.$message.success('商户审核拒绝成功')

        // 关闭弹框
        this.auditDialogVisible = false

        // 重新获取数据
        await this.getInfo(this.form.id)
      } catch (error) {
        console.error('拒绝审核提交失败:', error)
        this.$message.error('审核提交失败，请重试')
      } finally {
        this.auditLoading = false
      }
    },

    // 检查OAuth回调
    checkOAuthCallback() {
      // iframe中的OAuth回调已经在created钩子中处理，这里只处理父窗口的情况

      // 只有在待审核状态(0)时才处理OAuth回调
      if (this.$route.query.code && this.form.businessProfileStatus === 0) {
        this.$nextTick(async () => {
          await this.processOAuthCallback()
        })
      } else if (this.$route.query.code && this.form.businessProfileStatus !== 0) {
        // 如果不是待审核状态，清理OAuth参数
        console.log('当前状态不是待审核，清理OAuth参数')
        this.cleanupOAuthParams()
      } else {
        // 如果商户状态不是待审核，直接清理URL参数，避免重复处理
        this.cleanupOAuthParams()
      }
    },

    // 清理URL中的OAuth参数
    cleanupOAuthParams() {
      const currentRoute = this.$route
      if (currentRoute.query.from_oauth) {
        const newQuery = { ...currentRoute.query }
        delete newQuery.from_oauth
        delete newQuery.code
        delete newQuery.state
        this.$router.replace({
          path: currentRoute.path,
          query: Object.keys(newQuery).length > 0 ? newQuery : undefined
        })
      }
    },

    // 显示修改弹框
    async showEditDialog() {
      // 先刷新数据，确保获取最新的商户信息
      await this.getInfo(this.form.id)

      this.editDialogVisible = true
      // 填充表单数据
      this.editForm = {
        // 基础信息
        businessName: this.form.businessName || '',
        businessNo: this.form.businessNo || '',
        industry: this.form.industry || '',
        sideIndustry: this.form.sideIndustry || '',
        country: this.form.country || '',
        registrationDate: this.form.registrationDate || '',
        registrationAddress: this.form.registrationAddress || '',
        website: this.form.website || '',
        supplierName: this.form.supplierName || '',
        businessDesc: this.form.businessDesc || '',
        turnoverYear: this.form.turnoverYear || '',
        turnoverWeek: this.form.turnoverWeek || '',
        vatNumber: this.form.vatNumber || '',
        // 计费信息
        merchantFeeModelId: this.form.merchantFeeModelId ? Number(this.form.merchantFeeModelId) : '',
        settlementCurrency: this.form.settlementCurrency ? Number(this.form.settlementCurrency) : '',
        isHouseMerchant: this.form.isHouseMerchant === true || this.form.isHouseMerchant === 1,
        cryptoDeliveryMethod:
          this.form.cryptoDeliveryMethod !== undefined && this.form.cryptoDeliveryMethod !== null
            ? Number(this.form.cryptoDeliveryMethod)
            : 0,
        reservePercentage: this.form.reservePercentage || '',
        // 配置信息
        merchantHashSecretKey: this.form.merchantHashSecretKey || '',
        notifyOrderStatusEndpoint: this.form.notifyOrderStatusEndpoint || '',
        is3dsOn: this.form.is3dsOn === true || this.form.is3dsOn === 1,
        cryptoDeliveryDelayTime: this.form.cryptoDeliveryDelayTime || '',
        station: this.form.station || '',
        descriptor: this.form.descriptor || '',
        defaultRedirectSuccessUrl: this.form.defaultRedirectSuccessUrl || '',
        defaultRedirectFailureUrl: this.form.defaultRedirectFailureUrl || '',
        threeDSuccessUrl: this.form.threeDSuccessUrl || '',
        threeDFailureUrl: this.form.threeDFailureUrl || '',
        onRampMin: this.form.onRampMin || '',
        onRampMax: this.form.onRampMax || '',
        offRampMin: this.form.offRampMin || '',
        offRampMax: this.form.offRampMax || '',
        // 新增字段
        notifyOrderStatusEndpoint2: this.form.notifyOrderStatusEndpoint2 || '',
        endpoint2Percent: this.form.endpoint2Percent || '',
        notifyOrderStatusEndpointNft: this.form.notifyOrderStatusEndpointNft || '',
        fromSource: this.form.fromSource || '' // 支付渠道
      }
    },

    // 提交修改
    async submitEdit() {
      // 验证所有表单
      const formRefs = ['basicForm', 'billingForm', 'configForm']
      let allValid = true

      for (const formRef of formRefs) {
        if (this.$refs[formRef]) {
          try {
            await this.$refs[formRef].validate()
          } catch (error) {
            allValid = false
            break
          }
        }
      }

      if (!allValid) {
        this.$message.error('请检查表单填写是否正确')
        return
      }

      this.editLoading = true
      try {
        const params = {
          id: this.form.id,
          userId: this.form.userId, // 添加 userId 字段，后端需要此字段来更新用户状态
          ...this.editForm,
          // 确保布尔值转换为正确的格式
          isHouseMerchant: this.editForm.isHouseMerchant ? 1 : 0,
          is3dsOn: this.editForm.is3dsOn ? 1 : 0,
          // 处理 endpoint2Percent：空字符串转为 null，否则转为数字
          endpoint2Percent: this.editForm.endpoint2Percent === '' || this.editForm.endpoint2Percent === null 
            ? null 
            : Number(this.editForm.endpoint2Percent)
        }

        console.log('提交修改参数:', JSON.stringify(params, null, 2))

        // 这里调用修改API（需要根据实际API进行调整）
        await updateBusinessCustomerCorporation(params)

        this.$message.success('修改成功')
        this.editDialogVisible = false

        // 重新获取数据
        this.getInfo(this.form.id)
      } catch (error) {
        console.error('修改失败:', error)
        this.$message.error('修改操作失败，请重试')
      } finally {
        this.editLoading = false
      }
    }
  }
}
</script>

<style scoped>
.status-card {
  margin-bottom: 20px;
  background: #e9ecef;
  color: white;
}

.status-card >>> .el-card__body {
  padding: 15px 20px;
}

.info-card {
  margin-bottom: 20px;
}

.info-card >>> .el-card__header {
  background-color: #f8f9fa;
  border-bottom: 2px solid #e9ecef;
  color: #495057;
}

.info-item {
  margin-bottom: 15px;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item label {
  font-weight: 600;
  color: #606266;
  display: inline-block;
  min-width: 50px;
}

.info-item span {
  color: #303133;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: '';
}

.clearfix:after {
  clear: both;
}

/* 附件信息样式 */
.attachment-item {
  text-align: center;
  margin-bottom: 20px;
}

.attachment-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
  font-weight: 500;
}

.attachment-box {
  width: 120px;
  height: 80px;
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.attachment-box:hover {
  border-color: #409eff;
  background-color: #f0f9ff;
}

.attachment-box.has-file {
  border-style: solid;
  border-color: #67c23a;
  background-color: #f0f9ff;
}

.attachment-box.has-file:hover {
  border-color: #409eff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.attachment-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 8px;
  width: 100%;
  height: 100%;
}

.file-icon {
  font-size: 24px;
  margin-bottom: 4px;
  color: #409eff;
}

.file-info {
  text-align: center;
  width: 100%;
}

.file-name {
  font-size: 10px;
  color: #303133;
  font-weight: 500;
  line-height: 1.2;
  margin-bottom: 2px;
  word-break: break-all;
}

.file-type {
  font-size: 8px;
  color: #909399;
  line-height: 1;
}

.no-file {
  font-size: 10px;
  color: #c0c4cc;
}

.download-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(64, 158, 255, 0.9);
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
  font-size: 10px;
}

.attachment-box:hover .download-overlay {
  opacity: 1;
}

.download-overlay i {
  font-size: 16px;
  margin-bottom: 2px;
}

.attachment-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #c0c4cc;
  font-size: 12px;
}

.attachment-placeholder i {
  font-size: 24px;
  margin-bottom: 4px;
}

/* 表格样式优化 */
.el-table {
  border-radius: 6px;
  overflow: hidden;
}

.el-table th {
  font-weight: 600;
}

.el-table td,
.el-table th {
  padding: 12px 0;
}

/* 图片预览弹框样式 */
.image-preview-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  padding: 20px;
}

.preview-image {
  max-width: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.image-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #c0c4cc;
  font-size: 14px;
  min-height: 200px;
}

.image-error i {
  font-size: 48px;
  margin-bottom: 12px;
}

.dialog-footer {
  text-align: right;
}

/* 表格按钮样式 */
.el-button--text {
  color: #409eff;
  border: none;
  background: transparent;
  padding: 0;
  font-size: 12px;
}

.el-button--text:hover {
  color: #66b1ff;
  text-decoration: underline;
}

/* 审核弹框样式 */
.audit-form {
  padding: 10px 0;
}

.audit-type {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.audit-label {
  font-weight: 600;
  color: #606266;
  min-width: 80px;
  margin-right: 10px;
}

.approval-config {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 6px;
  border: 1px solid #e9ecef;
}

.config-item {
  display: flex;
  align-items: center;
}

.config-label {
  font-weight: 500;
  color: #606266;
  min-width: 150px;
  margin-right: 10px;
  font-size: 14px;
}

.rejection-reason {
  background-color: #fef5f5;
  padding: 20px;
  border-radius: 6px;
  border: 1px solid #fde2e2;
}

.reason-item {
  display: flex;
  flex-direction: column;
}

.reason-label {
  font-weight: 600;
  color: #e53e3e;
  margin-bottom: 8px;
  font-size: 14px;
}

.el-radio-group {
  display: flex;
  flex-wrap: wrap;
}

.el-radio {
  margin-right: 20px;
  margin-bottom: 8px;
}

.el-select {
  width: 100%;
}

.el-input {
  width: 100%;
}

/* 修改弹框样式 */
.edit-form {
  padding: 10px 0;
  max-height: 70vh;
  overflow-y: auto;
}

.edit-card {
  margin-bottom: 20px;
  border: 1px solid #e6e6e6;
  border-radius: 8px;
}

.edit-card:last-child {
  margin-bottom: 0;
}

.edit-card >>> .el-card__header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #e6e6e6;
  padding: 15px 20px;
}

.edit-card >>> .el-card__body {
  padding: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  font-weight: 600;
  color: #303133;
  font-size: 16px;
}

.card-header i {
  margin-right: 8px;
  font-size: 18px;
  color: #409eff;
}

.el-form-item {
  margin-bottom: 18px;
}

.el-form-item__label {
  font-weight: 500;
  color: #606266;
}

.el-date-editor.el-input,
.el-select {
  width: 100%;
}

.el-radio-group .el-radio {
  margin-right: 15px;
}

/* 单选按钮组垂直对齐优化 */
.radio-form-item .el-form-item__content {
  display: flex;
  align-items: center;
  min-height: 32px;
}

.radio-form-item .el-radio-group {
  display: flex;
  align-items: center;
  height: 32px;
}

.radio-form-item .el-radio {
  margin-right: 15px;
  height: 32px;
  line-height: 32px;
  display: flex;
  align-items: center;
}

/* 审核弹框中的单选按钮组对齐 */
.config-item {
  display: flex;
  align-items: center;
  min-height: 32px;
}

.config-item .el-radio-group {
  display: flex;
  align-items: center;
  margin-left: 10px;
}

.config-item .el-radio {
  height: 32px;
  line-height: 32px;
  display: flex;
  align-items: center;
}

/* OAuth授权弹框样式 */
.oauth-container {
  width: 100%;
  height: 600px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.oauth-iframe {
  width: 100%;
  height: 100%;
  border: none;
  border-radius: 4px;
}

/* OAuth回调loading样式 */
.oauth-callback-loading {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #ffffff;
}
</style>
