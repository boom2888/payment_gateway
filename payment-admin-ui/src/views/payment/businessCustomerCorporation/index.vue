<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="150px">
      <el-form-item :label="$t('payment.shop.businessName')" prop="businessName">
        <el-input
            v-model="queryParams.businessName"
            :placeholder="$t('payment.shop.businessName')"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.shop.id')" prop="id">
        <el-input
            v-model="queryParams.id"
            :placeholder="$t('payment.shop.id')"
            clearable
            size="small"
            @input="handleIntegerInput('id')"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.shop.descriptor')" prop="descriptor">
        <el-input
            v-model="queryParams.descriptor"
            :placeholder="$t('payment.shop.descriptor')"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.shop.station')" prop="station">
        <el-input
            v-model="queryParams.station"
            :placeholder="$t('payment.shop.station')"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('payment.shop.feeModel')" prop="merchantFeeModelId">
        <el-select
            v-model="queryParams.merchantFeeModelId"
            :placeholder="$t('payment.shop.feeModel')"
            clearable
            size="small"
            filterable
        >
          <el-option v-for="model in feeModels" :key="model.id" :label="model.name" :value="model.id"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.shop.businessProfileStatus')" prop="businessProfileStatus">
        <el-select
            v-model="queryParams.businessProfileStatus"
            :placeholder="$t('payment.shop.businessProfileStatus')"
            clearable
            size="small"
            filterable
        >
          <el-option
              v-for="dict in businessProfileStatuses"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>

      <!-- <el-form-item :label="$t('payment.shop.isSiftOn')" prop="isSiftOn">
        <el-select
          v-model="queryParams.isSiftOn"
          :placeholder="$t('payment.shop.isSiftOn')"
          clearable
          size="small"
          filterable
        >
          <el-option
            v-for="dict in isSiftOnOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item> -->

      <el-form-item :label="$t('payment.shop.is3dsOn')" prop="is3dsOn">
        <el-select
            v-model="queryParams.is3dsOn"
            :placeholder="$t('payment.shop.is3dsOn')"
            clearable
            size="small"
            filterable
        >
          <el-option
              v-for="dict in is3dsOnOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.shop.cryptoDeliveryMethod')" prop="cryptoDeliveryMethod">
        <el-select
            v-model="queryParams.cryptoDeliveryMethod"
            :placeholder="$t('payment.shop.cryptoDeliveryMethod')"
            clearable
            size="small"
            filterable
        >
          <el-option
              v-for="dict in cryptoDeliveryMethodOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.shop.isHouseMerchant')" prop="isHouseMerchant">
        <el-select
            v-model="queryParams.isHouseMerchant"
            :placeholder="$t('payment.shop.isHouseMerchant')"
            clearable
            size="small"
            filterable
        >
          <el-option
              v-for="dict in isHouseMerchantOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.shop.isWhitelistOn')" prop="isWhitelistOn">
        <el-select
            v-model="queryParams.isWhitelistOn"
            :placeholder="$t('payment.shop.isWhitelistOn')"
            clearable
            size="small"
            filterable
        >
          <el-option
              v-for="dict in isWhitelistOnOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('payment.shop.registrationDate')" prop="registrationDate">
        <el-date-picker
            v-model="queryParams.registrationDate"
            clearable
            size="small"
            type="date"
            value-format="yyyy-MM-dd"
            :placeholder="$t('payment.shop.registrationDate')"
        />
      </el-form-item>
      <el-form-item :label="$t('common.createdAt')" prop="createdAt">
        <el-date-picker
            v-model="queryParams.createdAt"
            clearable
            size="small"
            type="date"
            value-format="yyyy-MM-dd"
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
            v-hasPermi="['payment:businessCustomerCorporation:add']"
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
        >
          {{ $t('common.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['payment:businessCustomerCorporation:edit']"
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
        >
          {{ $t('common.edit') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['payment:businessCustomerCorporation:remove']"
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
        >
          {{ $t('common.delete') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['payment:businessCustomerCorporation:export']"
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
        >
          {{ $t('common.export') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <el-table v-loading="loading" :data="businessCustomerCorporationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column :label="$t('payment.shop.id')" align="center" prop="id" width="120"/>
      <el-table-column :label="$t('payment.shop.businessName')" align="center" prop="businessName" width="120"/>
      <el-table-column :label="$t('payment.shop.stripeAccount')" align="center" prop="stripeAccount" width="150"/>
      <el-table-column :label="$t('payment.shop.stripeFee')" align="center" prop="stripeFee" width="80"/>
<!--      <el-table-column :label="$t('payment.shop.station')" align="center" prop="station" width="120"/>-->
<!--      <el-table-column :label="$t('payment.shop.descriptor')" align="center" prop="descriptor" width="120"/>-->
      <el-table-column :label="$t('payment.shop.feeModel')" align="center" prop="feeModelName"/>
      <!-- <el-table-column :label="$t('payment.shop.country')" align="center" prop="country" /> -->
      <!-- <el-table-column :label="$t('payment.shop.turnoverYear')" align="center" prop="turnoverYear" width="130" /> -->
      <!-- <el-table-column :label="$t('payment.shop.turnoverWeek')" align="center" prop="turnoverWeek" width="130" /> -->
      <el-table-column :label="$t('payment.shop.businessProfileStatus')" align="center" prop="businessProfileStatus">
        <template slot-scope="scope">
          <el-tooltip
              :content="scope.row.remark || $t('payment.shop.noRemark')"
              placement="top"
              :disabled="!scope.row.remark"
          >
            <el-tag :type="getBusinessProfileStatusType(scope.row.businessProfileStatus)" size="small">
              {{ businessProfileStatusFormat(scope.row) }}
            </el-tag>
          </el-tooltip>
        </template>
      </el-table-column>
      <!--      <el-table-column label="董事文件状态" align="center" prop="directorDocumentStatus" width="120"/>-->

      <el-table-column :label="$t('payment.shop.status')" align="center" prop="status" width="210">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.status"
              :active-value="0"
              :inactive-value="1"
              :active-text="$t('common.enabled')"
              :inactive-text="$t('common.disabled')"
              active-color="#13ce66"
              inactive-color="#C0C4CC"
              @change="handleSiftStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.shop.is3dsOn')" align="center" prop="is3dsOn" width="140">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.is3dsOn"
              :active-value="1"
              :inactive-value="0"
              :active-text="$t('common.on')"
              :inactive-text="$t('common.off')"
              active-color="#13ce66"
              inactive-color="#C0C4CC"
              @change="handleThreeDsStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column :label="$t('payment.shop.isWhitelistOn')" align="center" prop="isWhitelistOn" width="150">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.isWhitelistOn"
              :active-value="1"
              :inactive-value="0"
              :active-text="$t('common.on')"
              :inactive-text="$t('common.off')"
              active-color="#13ce66"
              inactive-color="#C0C4CC"
              @change="handleIsWhitelistOnChange(scope.row)"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="ANI检查标志" align="center" prop="aniCheck" width="120" /> -->
      <el-table-column :label="$t('common.createdAt')" align="center" prop="createdAt" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
          :label="$t('payment.businessCustomerCorporation.operation')"
          align="center"
          class-name="small-padding fixed-width"
          width="240"
      >
        <template slot-scope="scope">
          <el-button
              v-hasPermi="['payment:businessCustomerCorporation:edit']"
              size="mini"
              type="text"
              icon="el-icon-info"
              @click="showDetail(scope.row)"
          >
            {{ $t('common.detail') }}
          </el-button>
          <el-button
              v-hasPermi="['payment:businessCustomerCorporation:remove']"
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
          >
            {{ $t('common.delete') }}
          </el-button>
          <el-button
              v-hasPermi="['payment:businessCustomerCorporation:resetPassword']"
              size="mini"
              type="text"
              icon="el-icon-setting"
              @click="handleResetPwd(scope.row)"
          >
            {{ $t('common.resetPassword') }}
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

    <!-- {{ $t('payment.businessCustomerCorporation.addDialog') }} -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="198px">
        <el-row>
          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="用户ID" prop="userId">-->
          <!--              <el-input v-model="form.userId" placeholder="请输入用户ID" disabled/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.businessName')" prop="businessName">
              <el-input v-model="form.businessName" :placeholder="$t('payment.shop.businessName')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.businessNo')" prop="businessNo">
              <el-input v-model="form.businessNo" :placeholder="$t('payment.shop.businessNo')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.descriptor')" prop="descriptor">
              <el-input v-model="form.descriptor" :placeholder="$t('payment.shop.descriptor')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.station')" prop="station">
              <el-input v-model="form.station" :placeholder="$t('payment.shop.station')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.feeModel')" prop="merchantFeeModelId">
              <el-select v-model="form.merchantFeeModelId" :placeholder="$t('payment.shop.feeModel')" filterable>
                <el-option v-for="model in feeModels" :key="model.id" :label="model.name" :value="parseInt(model.id)"/>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.businessAddress')" prop="businessAddress">
              <el-input v-model="form.businessAddress" :placeholder="$t('payment.shop.businessAddress')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.stripeAccount')" prop="stripeAccount">
              <el-input v-model="form.stripeAccount" :placeholder="$t('payment.shop.stripeAccount')"/>
            </el-form-item>
          </el-col>


          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.stripeFee')" prop="stripeFee">
              <el-input v-model="form.stripeFee" :placeholder="$t('payment.shop.stripeFee')"/>
            </el-form-item>
          </el-col>


          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.country')" prop="country">
              <el-input v-model="form.country" :placeholder="$t('payment.shop.country')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.industry')" prop="industry">
              <el-input v-model="form.industry" :placeholder="$t('payment.shop.industry')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.sideIndustry')" prop="sideIndustry">
              <el-input v-model="form.sideIndustry" :placeholder="$t('payment.shop.sideIndustry')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.registrationDate')" prop="registrationDate">
              <el-date-picker
                  v-model="form.registrationDate"
                  clearable
                  size="small"
                  disabled
                  type="date"
                  value-format="yyyy-MM-dd"
                  :placeholder="$t('payment.shop.registrationDate')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.registrationAddress')" prop="registrationAddress">
              <el-input
                  v-model="form.registrationAddress"
                  :placeholder="$t('payment.shop.registrationAddress')"
                  disabled
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.shareholdingStructure')" prop="shareholdingStructure">
              <el-input v-model="form.shareholdingStructure" :placeholder="$t('payment.shop.shareholdingStructure')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.turnoverYear')" prop="turnoverYear">
              <el-input v-model="form.turnoverYear" :placeholder="$t('payment.shop.turnoverYear')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.turnoverWeek')" prop="turnoverWeek">
              <el-input v-model="form.turnoverWeek" :placeholder="$t('payment.shop.turnoverWeek')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.website')" prop="website">
              <el-input v-model="form.website" :placeholder="$t('payment.shop.website')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.supplierName')" prop="supplierName">
              <el-input v-model="form.supplierName" :placeholder="$t('payment.shop.supplierName')"/>
            </el-form-item>
          </el-col>

          <!-- <el-col :span="12">
            <el-form-item :label="$t('payment.shop.supplierContracts')" prop="supplierContracts">
              <el-input v-model="form.supplierContracts" :placeholder="$t('payment.shop.supplierContracts')" />
            </el-form-item>
          </el-col> -->

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.businessProfileStatus')" prop="businessProfileStatus">
              <el-select
                  v-model="form.businessProfileStatus"
                  :placeholder="$t('payment.shop.businessProfileStatus')"
                  filterable
              >
                <el-option
                    v-for="dict in businessProfileStatusOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col v-if="form.businessProfileStatus === 2" :span="12">
            <el-form-item :label="$t('payment.shop.remark')" prop="remark">
              <el-input v-model="form.remark" type="textarea" :placeholder="$t('payment.shop.remark')" :rows="2"/>
            </el-form-item>
          </el-col>
          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="董事资料状态">-->
          <!--              <el-radio-group v-model="form.directorProfileStatus">-->
          <!--                <el-radio label="1">请选择字典生成</el-radio>-->
          <!--              </el-radio-group>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="董事文件状态">-->
          <!--              <el-radio-group v-model="form.directorDocumentStatus">-->
          <!--                <el-radio label="1">请选择字典生成</el-radio>-->
          <!--              </el-radio-group>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.saasUserCorporationId')" prop="saasUserCorporationId">
              <el-input
                  v-model="form.saasUserCorporationId"
                  :placeholder="$t('payment.shop.saasUserCorporationId')"
                  disabled
              />
            </el-form-item>
          </el-col>

          <!--          <el-col :span="24">-->
          <!--            <el-form-item label="记录描述" prop="remark">-->
          <!--              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.businessDesc')" prop="businessDesc">
              <el-input v-model="form.businessDesc" :placeholder="$t('payment.shop.businessDesc')"/>
            </el-form-item>
          </el-col>
          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="收单机构" prop="mid">-->
          <!--              <el-input v-model="form.mid" placeholder="请输入收单机构"/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->

          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="回调地址" prop="requestCryptoTransactionEndpoint">-->
          <!--              <el-input v-model="form.requestCryptoTransactionEndpoint" placeholder="请输入回调地址"/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.merchantHashSecretKey')" prop="merchantHashSecretKey">
              <el-input v-model="form.merchantHashSecretKey" :placeholder="$t('payment.shop.merchantHashSecretKey')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.notifyOrderStatusEndpoint')" prop="notifyOrderStatusEndpoint">
              <el-input
                  v-model="form.notifyOrderStatusEndpoint"
                  :placeholder="$t('payment.shop.notifyOrderStatusEndpoint')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.notifyOrderStatusEndpoint2')" prop="notifyOrderStatusEndpoin2">
              <el-input
                  v-model="form.notifyOrderStatusEndpoint2"
                  :placeholder="$t('payment.shop.notifyOrderStatusEndpoint2')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.endpoint2Percent')" prop="endpoint2Percent">
              <el-input v-model="form.endpoint2Percent" :placeholder="$t('payment.shop.endpoint2Percent')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.notifyOrderStatusEndpointNft')" prop="notifyOrderStatusEndpointNft">
              <el-input
                  v-model="form.notifyOrderStatusEndpointNft"
                  :placeholder="$t('payment.shop.notifyOrderStatusEndpointNft')"
              />
            </el-form-item>
          </el-col>

          <!-- <el-col :span="12">
            <el-form-item :label="$t('payment.shop.isSiftOn')" prop="isSiftOn">
              <el-select v-model="form.isSiftOn" :placeholder="$t('payment.shop.isSiftOn')" filterable>
                <el-option
                  v-for="dict in isSiftOnOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.is3dsOn')" prop="is3dsOn">
              <el-select v-model="form.is3dsOn" :placeholder="$t('payment.shop.is3dsOn')" filterable>
                <el-option
                    v-for="dict in is3dsOnOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.cryptoDeliveryMethod')" prop="cryptoDeliveryMethod">
              <el-select
                  v-model="form.cryptoDeliveryMethod"
                  :placeholder="$t('payment.shop.cryptoDeliveryMethod')"
                  filterable
              >
                <el-option
                    v-for="dict in cryptoDeliveryMethodOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.isHouseMerchant')" prop="isHouseMerchant">
              <el-select v-model="form.isHouseMerchant" :placeholder="$t('payment.shop.isHouseMerchant')" filterable>
                <el-option
                    v-for="dict in isHouseMerchantOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="第二个商户订单状态webhook URL" prop="notifyOrderStatusEndpoint2">-->
          <!--              <el-input v-model="form.notifyOrderStatusEndpoint2" placeholder="请输入第二个商户订单状态webhook URL"/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.reservePercentage')" prop="reservePercentage">
              <el-input v-model="form.reservePercentage" :placeholder="$t('payment.shop.reservePercentage')"/>
            </el-form-item>
          </el-col>
          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="预留持有期" prop="reserveHoldingPeriod">-->
          <!--              <el-input v-model="form.reserveHoldingPeriod" placeholder="请输入预留持有期"/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.settlementCurrency')" prop="settlementCurrency">
              <el-select
                  v-model="form.settlementCurrency"
                  :placeholder="$t('payment.shop.settlementCurrency')"
                  filterable
              >
                <el-option
                    v-for="dict in settlementCurrencyOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!--          <el-col :span="24">-->
          <!--            <el-form-item label="利润分成" prop="profitSharing">-->
          <!--              <el-input v-model="form.profitSharing" type="textarea" placeholder="请输入内容"/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.isWhitelistOn')" prop="isWhitelistOn">
              <el-select v-model="form.isWhitelistOn" :placeholder="$t('payment.shop.isWhitelistOn')" filterable>
                <el-option
                    v-for="dict in isWhitelistOnOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="parseInt(dict.dictValue)"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="出金通知商户订单状态" prop="notifyOrderStatusEndpoint3">-->
          <!--              <el-input v-model="form.notifyOrderStatusEndpoint3" placeholder="请输入出金通知商户订单状态"/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->

          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="ANI检查标志" prop="aniCheck">-->
          <!--              <el-input v-model="form.aniCheck" placeholder="请输入ANI检查标志"/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->

          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="收单机构ID" prop="acquirerId">-->
          <!--              <el-input v-model="form.acquirerId" placeholder="请输入收单机构ID" disabled/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.cryptoDeliveryDelayTime')" prop="cryptoDeliveryDelayTime">
              <el-input
                  v-model="form.cryptoDeliveryDelayTime"
                  :placeholder="$t('payment.shop.cryptoDeliveryDelayTime')"
              />
            </el-form-item>
          </el-col>

          <!--          <el-col :span="12">-->
          <!--            <el-form-item :label="$t('payment.shop.descriptor')" prop="descriptor">-->
          <!--              <el-input-->
          <!--                  v-model="form.descriptor"-->
          <!--                  :placeholder="$t('payment.shop.descriptor')"-->
          <!--              />-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->

          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="单订状态变更回调" prop="orderStatusChangeNotification">-->
          <!--              <el-input v-model="form.orderStatusChangeNotification" placeholder="请输入单订状态变更回调"/>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.threeDSuccessUrl')" prop="threeDSuccessUrl">
              <el-input v-model="form.threeDSuccessUrl" :placeholder="$t('payment.shop.threeDSuccessUrl')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.threeDFailureUrl')" prop="threeDFailureUrl">
              <el-input v-model="form.threeDFailureUrl" :placeholder="$t('payment.shop.threeDFailureUrl')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.defaultRedirectSuccessUrl')" prop="defaultRedirectSuccessUrl">
              <el-input
                  v-model="form.defaultRedirectSuccessUrl"
                  :placeholder="$t('payment.shop.defaultRedirectSuccessUrl')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.defaultRedirectFailureUrl')" prop="defaultRedirectFailureUrl">
              <el-input
                  v-model="form.defaultRedirectFailureUrl"
                  :placeholder="$t('payment.shop.defaultRedirectFailureUrl')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.onRampMin')" prop="onRampMin">
              <el-input v-model="form.onRampMin" :placeholder="$t('payment.shop.onRampMin')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.onRampMax')" prop="onRampMax">
              <el-input v-model="form.onRampMax" :placeholder="$t('payment.shop.onRampMax')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.offRampMin')" prop="offRampMin">
              <el-input v-model="form.offRampMin" :placeholder="$t('payment.shop.offRampMin')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.offRampMax')" prop="offRampMax">
              <el-input v-model="form.offRampMax" :placeholder="$t('payment.shop.offRampMax')"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.fromSource')" prop="fromSource">
              <el-select v-model="form.fromSource" :placeholder="$t('payment.shop.fromSource')" filterable>
                <el-option
                    v-for="dict in paymentChannelOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('payment.shop.thunesTransferFee')" prop="thunesTransferFee">
              <el-input v-model="form.thunesTransferFee" :placeholder="$t('payment.shop.thunesTransferFee')"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('common.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addBusinessCustomerCorporation,
  delBusinessCustomerCorporation,
  exportBusinessCustomerCorporation,
  getBusinessCustomerCorporation,
  getFeeModels,
  listBusinessCustomerCorporation,
  resetPassword,
  updateBusinessCustomerCorporation
} from '@/api/payment/businessCustomerCorporation'

export default {
  name: 'BusinessCustomerCorporation',
  components: {},
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
      // 企业客户公司表格数据
      businessCustomerCorporationList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 资料状态字典
      businessProfileStatusOptions: [],
      // 类型字典
      deletedOptions: [],
      // 收费模型
      feeModels: [],
      businessProfileStatuses: [],
      // sift类型字典
      isSiftOnOptions: [],
      // 3ds类型字典
      is3dsOnOptions: [],
      // 加密货币交付方式字典
      cryptoDeliveryMethodOptions: [],
      // 直营户字典
      isHouseMerchantOptions: [],
      // 类型字典
      isWhitelistOnOptions: [],
      settlementCurrencyOptions: [],
      paymentChannelOptions: [], // 支付渠道字典
      // 查询参数
      queryParams: {
        type: 1,//1:一级商户 2:二级商户
        pageNum: 1,
        pageSize: 10,
        businessName: undefined,
        shopId: undefined,
        merchantFeeModelId: undefined,
        registrationDate: undefined,
        createdAt: undefined,
        isSiftOn: undefined,
        is3dsOn: undefined,
        cryptoDeliveryMethod: undefined,
        isHouseMerchant: undefined,
        isWhitelistOn: undefined,
        descriptor: undefined,
        station: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          {required: true, message: this.$t('payment.businessCustomerCorporation.userIdRequired'), trigger: 'blur'}
        ],
        businessName: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporation.businessNameRequired'),
            trigger: 'blur'
          }
        ],
        merchantFeeModelId: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporation.feeModelRequired'),
            trigger: 'change'
          }
        ],
        saasUserCorporationId: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporation.saasUserCorporationIdRequired'),
            trigger: 'blur'
          }
        ],
        isSiftOn: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporation.isSiftOnRequired'),
            trigger: 'change'
          }
        ],
        is3dsOn: [
          {required: true, message: this.$t('payment.businessCustomerCorporation.is3dsOnRequired'), trigger: 'change'}
        ],
        isWhitelistOn: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporation.isWhitelistOnRequired'),
            trigger: 'change'
          }
        ],
        notifyOrderStatusEndpoint3: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporation.notifyOrderStatusEndpoint3Required'),
            trigger: 'blur'
          }
        ],
        aniCheck: [
          {required: true, message: this.$t('payment.businessCustomerCorporation.aniCheckRequired'), trigger: 'blur'}
        ],
        acquirerId: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporation.acquirerIdRequired'),
            trigger: 'blur'
          }
        ],
        remark: [
          {
            required: true,
            message: this.$t('payment.businessCustomerCorporation.rejectReasonRequired'),
            trigger: 'blur',
            validator: (rule, value, callback) => {
              if (this.form.businessProfileStatus === 2 && !value) {
                callback(new Error(this.$t('payment.businessCustomerCorporation.pleaseInputFailureReason')))
              } else {
                callback()
              }
            }
          }
        ]
      }
    }
  },
  watch: {
    'form.businessProfileStatus'(newVal) {
      if (newVal !== 2) {
        this.form.remark = undefined
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate(['remark'])
        })
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_BUSINESS_PROFILE_STATUS').then(response => {
      this.businessProfileStatusOptions = response.data
    })
    this.getDicts('DELETE_STATUS').then(response => {
      this.deletedOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_IS_SIFT_ON').then(response => {
      this.isSiftOnOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_IS_3DS_ON').then(response => {
      this.is3dsOnOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_CRYPTO_DELIVERY_METHOD').then(response => {
      this.cryptoDeliveryMethodOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_IS_HOUSE_MERCHANT').then(response => {
      this.isHouseMerchantOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_IS_WHITELIST_ON').then(response => {
      this.isWhitelistOnOptions = response.data
    })
    this.getDicts('BUSINESS_CUSTOMER_CORPORATION_BUSINESS_PROFILE_STATUS').then(response => {
      this.businessProfileStatuses = response.data
    })
    this.getDicts('CURRENCY_ID').then(response => {
      this.settlementCurrencyOptions = response.data
    })
    this.getDicts('PAYMENT_CHANNEL').then(response => {
      this.paymentChannelOptions = response.data
    })
    this.getFeeModels()
  },
  methods: {
    /** 限制输入框只能输入整数 */
    handleIntegerInput(field) {
      const value = this.queryParams[field]
      if (value) {
        // 移除所有非数字字符
        this.queryParams[field] = value.replace(/[^\d]/g, '')
      }
    },
    /** 查询企业客户公司列表 */
    getList() {
      this.loading = true
      listBusinessCustomerCorporation(this.queryParams).then(response => {
        this.businessCustomerCorporationList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询收费模型 */
    getFeeModels() {
      getFeeModels().then(response => {
        this.feeModels = response.rows
      })
    },
    // 资料状态字典翻译
    businessProfileStatusFormat(row, column) {
      return this.selectDictLabel(this.businessProfileStatusOptions, row.businessProfileStatus)
    },
    // 获取资料状态对应的标签类型
    getBusinessProfileStatusType(status) {
      // 根据状态值返回不同的标签类型
      switch (status) {
        case 0: // 待审核
          return 'warning'
        case 1: // 通过
          return 'success'
        case 2: // 拒绝
          return 'danger'
        case 3: // 审核中
          return 'primary'
        default:
          return 'info'
      }
    },
    // 类型字典翻译
    deletedFormat(row, column) {
      return this.selectDictLabel(this.deletedOptions, row.deleted)
    },
    // sift类型字典翻译
    isSiftOnFormat(row, column) {
      const text = this.selectDictLabel(this.isSiftOnOptions, row.isSiftOn)
      return text
    },
    // 3ds类型字典翻译
    is3dsOnFormat(row, column) {
      return this.selectDictLabel(this.is3dsOnOptions, row.is3dsOn)
    },
    // 加密货币交付方式字典翻译
    cryptoDeliveryMethodFormat(row, column) {
      return this.selectDictLabel(this.cryptoDeliveryMethodOptions, row.cryptoDeliveryMethod)
    },
    // 直营户字典翻译
    isHouseMerchantFormat(row, column) {
      return this.selectDictLabel(this.isHouseMerchantOptions, row.isHouseMerchant)
    },
    // 类型字典翻译
    isWhitelistOnFormat(row, column) {
      return this.selectDictLabel(this.isWhitelistOnOptions, row.isWhitelistOn)
    },

    // 处理启用/禁用
    async handleSiftStatusChange(row) {
      const statusText =
          row.status === 1
              ? this.$t('payment.businessCustomerCorporation.disable')
              : this.$t('payment.businessCustomerCorporation.enable')

      try {
        await this.$confirm(
            this.$t('payment.businessCustomerCorporation.confirmDisableEnable', {action: statusText}),
            this.$t('payment.businessCustomerCorporation.tip'),
            {
              confirmButtonText: this.$t('common.confirm'),
              cancelButtonText: this.$t('common.cancel'),
              type: 'warning'
            }
        )

        const auditData = {
          id: row.id,
          status: row.status,
          userId: row.userId
        }

        await updateBusinessCustomerCorporation(auditData)
        this.$message.success(this.$t('payment.businessCustomerCorporation.actionSuccess', {action: statusText}))
        this.getList() // 刷新列表
      } catch (error) {
        // 如果取消操作或请求失败，恢复原状态
        row.status = row.status === 1 ? 0 : 1

        // 只有在不是用户取消操作时才显示错误消息
        if (error !== 'cancel') {
          this.$message.error(this.$t('payment.businessCustomerCorporation.actionFailed', {action: statusText}))
        }
      }
    },

    // 处理3ds类型状态变化
    async handleThreeDsStatusChange(row) {
      const statusText =
          row.is3dsOn === 1
              ? this.$t('payment.businessCustomerCorporation.open')
              : this.$t('payment.businessCustomerCorporation.close')

      try {
        await this.$confirm(
            this.$t('payment.businessCustomerCorporation.confirm3dsToggle', {action: statusText}),
            this.$t('payment.businessCustomerCorporation.tip'),
            {
              confirmButtonText: this.$t('common.confirm'),
              cancelButtonText: this.$t('common.cancel'),
              type: 'warning'
            }
        )

        const auditData = {
          id: row.id,
          is3dsOn: row.is3dsOn
        }

        await updateBusinessCustomerCorporation(auditData)
        this.$message.success(
            this.$t('payment.businessCustomerCorporation.threeDsActionSuccess', {action: statusText})
        )
        this.getList() // 刷新列表
      } catch (error) {
        // 如果取消操作或请求失败，恢复原状态
        row.is3dsOn = row.is3dsOn === 1 ? 0 : 1

        // 只有在不是用户取消操作时才显示错误消息
        if (error !== 'cancel') {
          this.$message.error(
              this.$t('payment.businessCustomerCorporation.threeDsActionFailed', {action: statusText})
          )
        }
      }
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
        userId: undefined,
        businessName: undefined,
        businessNo: undefined,
        businessAddress: undefined,
        stripeAccount: undefined,
        stripeFeeType: undefined,
        stripeFee: undefined,
        country: undefined,
        industry: undefined,
        sideIndustry: undefined,
        registrationDate: undefined,
        registrationAddress: undefined,
        shareholdingStructure: undefined,
        turnoverYear: undefined,
        turnoverWeek: undefined,
        station: undefined,
        website: undefined,
        supplierName: undefined,
        supplierContracts: undefined,
        businessProfileStatus: undefined,
        directorProfileStatus: '0',
        directorDocumentStatus: '0',
        saasUserCorporationId: undefined,
        createdAt: undefined,
        createdBy: undefined,
        deletedAt: undefined,
        deletedBy: undefined,
        remark: undefined,
        deleted: undefined,
        businessDesc: undefined,
        mid: undefined,
        requestCryptoTransactionEndpoint: undefined,
        merchantHashSecretKey: undefined,
        notifyOrderStatusEndpoint: undefined,
        isSiftOn: undefined,
        is3dsOn: undefined,
        cryptoDeliveryMethod: undefined,
        isHouseMerchant: undefined,
        notifyOrderStatusEndpoint2: undefined,
        endpoint2Percent: undefined,
        reservePercentage: undefined,
        reserveHoldingPeriod: undefined,
        settlementCurrency: undefined,
        profitSharing: undefined,
        isWhitelistOn: undefined,
        notifyOrderStatusEndpoint3: undefined,
        aniCheck: undefined,
        acquirerId: undefined,
        cryptoDeliveryDelayTime: undefined,
        descriptor: undefined,
        orderStatusChangeNotification: undefined,
        threeDSuccessUrl: undefined,
        threeDFailureUrl: undefined,
        defaultRedirectSuccessUrl: undefined,
        defaultRedirectFailureUrl: undefined,
        onRampMin: undefined,
        onRampMax: undefined,
        offRampMin: undefined,
        offRampMin: undefined,
        offRampMax: undefined,
        thunesTransferFee: undefined
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
      this.title = this.$t('payment.shop.addShop')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getBusinessCustomerCorporation(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('payment.shop.editShop')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBusinessCustomerCorporation(this.form).then(response => {
              this.msgSuccess(this.$t('payment.businessCustomerCorporation.modifySuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addBusinessCustomerCorporation(this.form).then(response => {
              this.msgSuccess(this.$t('payment.businessCustomerCorporation.addSuccess'))
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
      this.$confirm(
          this.$t('payment.businessCustomerCorporation.confirmDelete', {id: ids}),
          this.$t('payment.businessCustomerCorporation.warning'),
          {
            confirmButtonText: this.$t('common.confirm'),
            cancelButtonText: this.$t('common.cancel'),
            type: 'warning'
          }
      )
          .then(function () {
            return delBusinessCustomerCorporation(ids)
          })
          .then(() => {
            this.getList()
            this.msgSuccess(this.$t('payment.businessCustomerCorporation.deleteSuccess'))
          })
    },
    handleResetPwd(row) {
      this.$confirm(
          this.$t('payment.businessCustomerCorporation.confirmResetPassword', {id: row.id}),
          this.$t('payment.businessCustomerCorporation.warning'),
          {
            confirmButtonText: this.$t('common.confirm'),
            cancelButtonText: this.$t('common.cancel'),
            type: 'warning'
          }
      )
          .then(function () {
            return resetPassword(row.userId)
          })
          .then(() => {
            // this.getList()
            this.msgSuccess(this.$t('payment.businessCustomerCorporation.resetSuccess'))
          })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm(
          this.$t('payment.businessCustomerCorporation.confirmExport'),
          this.$t('payment.businessCustomerCorporation.warning'),
          {
            confirmButtonText: this.$t('common.confirm'),
            cancelButtonText: this.$t('common.cancel'),
            type: 'warning'
          }
      )
          .then(function () {
            return exportBusinessCustomerCorporation(queryParams)
          })
          .then(response => {
            this.download(response.msg)
          })
    },
    /** 详情按钮操作 */
    showDetail(row) {
      // 跳转到详情页面，传递商户ID作为路由参数
      // 添加时间戳确保路由变化被检测到
      this.$router.push({
        path: '/payment/businessInfo',
        query: {
          id: row.id
        }
      })
    },
    async handleIsWhitelistOnChange(row) {
      const statusText =
          row.isWhitelistOn === 1
              ? this.$t('payment.businessCustomerCorporation.open')
              : this.$t('payment.businessCustomerCorporation.close')

      try {
        await this.$confirm(
            this.$t('payment.businessCustomerCorporation.confirmWhitelistToggle', {
              action: statusText,
              businessName: row.businessName
            }),
            this.$t('payment.businessCustomerCorporation.tip'),
            {
              confirmButtonText: this.$t('common.confirm'),
              cancelButtonText: this.$t('common.cancel'),
              type: 'warning'
            }
        )

        const auditData = {
          id: row.id,
          isWhitelistOn: row.isWhitelistOn
        }

        await updateBusinessCustomerCorporation(auditData)
        this.$message.success(
            this.$t('payment.businessCustomerCorporation.whitelistActionSuccess', {action: statusText})
        )
        this.getList() // 刷新列表
      } catch (error) {
        // 如果取消操作或请求失败，恢复原状态
        row.isWhitelistOn = row.isWhitelistOn === 1 ? 0 : 1

        // 只有在不是用户取消操作时才显示错误消息
        if (error !== 'cancel') {
          this.$message.error(
              this.$t('payment.businessCustomerCorporation.whitelistActionFailed', {action: statusText})
          )
        }
      }
    }
  }
}
</script>
