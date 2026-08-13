<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item :label="$t('common.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common.status')" clearable size="small" filterable>
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('common.importedAt')" prop="createdAt">
        <el-date-picker clearable size="small"
                        v-model="queryParams.createdAt"
                        type="date"
                        value-format="yyyy-MM-dd"
                        :placeholder="$t('common.importedAt')">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['payment:movementImport:import']"
        >{{ $t('common.import') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleDailySummaryBatchExport"
          :disabled="this.ids.length ===0"
        >{{ $t('payment.movementImport.dailySummary') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="movementImportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column :label="$t('payment.movementImport.reportId')" align="center" prop="id"/>
      <el-table-column :label="$t('payment.movementImport.reportName')" align="center" prop="originName" width="230"/>
      <!--      <el-table-column label="导入报告地址" align="center" prop="fileUrl"/>-->
      <el-table-column :label="$t('payment.movementImport.totalRecords')" align="center" prop="importTotalNum" width="110"/>
      <el-table-column :label="$t('payment.movementImport.successRecords')" align="center" prop="dataCount" width="130"/>
      <el-table-column :label="$t('payment.movementImport.failedRecords')" align="center" prop="failNum" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.importTotalNum - scope.row.dataCount }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.status')" align="center" prop="status" :formatter="statusFormat"/>
      <el-table-column :label="$t('payment.movementImport.b2c2FxRate')" align="center" prop="b2c2FxRate" width="110">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.b2c2FxRate"
            @blur="handleB2c2FxRateBlur(scope.row)"
            @input="formatB2c2FxRateInput(scope.row)"
            size="mini"
            style="width: 80px;"
          />
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.importedAt')" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="350">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleShopBill(scope.row)"
          >{{ $t('payment.movementImport.reconciliationDetails') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            v-if="scope.row.status === 1"
            @click="handleReconReportExport(scope.row)"
          >{{ $t('payment.movementImport.reconReport') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            v-if="scope.row.status === 1"
            @click="handleDailySummaryExport(scope.row)"
          >{{ $t('payment.movementImport.dailySummary') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleRegenerate(scope.row)"
            v-if="scope.row.status === 2 || scope.row.status === 3"
          >{{ $t('payment.movementImport.regenerate') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleInvalid(scope.row)"
            v-if="scope.row.status < 3"
          >{{ $t('payment.movementImport.invalid') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
            v-if="scope.row.deleted !== 1"
          >{{ $t('common.delete') }}
          </el-button>
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
    <!-- 导入对话框 -->
    <el-dialog :title="$t('payment.movementImport.importDialogTitle')" :visible.sync="upload.open" width="400px" append-to-body @close="handleDialogClose">
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        action="#"
        :file-list="upload.fileList"
        :on-change="handleFileChange"
        :on-remove="handleFileRemove"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          {{ $t('payment.movementImport.dragFileHere') }}
          <em>{{ $t('payment.movementImport.clickToUpload') }}</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <!--          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>-->
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">{{ $t('payment.movementImport.fileFormatTip') }}</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm" :loading="upload.isUploading">{{ $t('common.confirm') }}</el-button>
        <el-button @click="upload.open = false">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addMovementImport,
  delMovementImport,
  exportDailySummary,
  getMovementImport,
  importNuvieReport,
  invalidMovementImport,
  listMovementImport,
  regenerate,
  updateMovementImport
} from "@/api/payment/movementImport";

import {downLoadZip} from "@/utils/zipdownload";
import router from "@/router";

export default {
  name: "MovementImport",
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中id数组
      ids: [],
      // 选中行数组
      selectedRows: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // nuvei报告导入记录表格数据
      movementImportList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: undefined,
        createdAt: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      // 导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 当前选择的文件
        fileList: []
      },
      shopBillList: [],
    };
  },
  created() {
    this.getList();
    this.getDicts("MOVEMENT_IMPOT_STATUS").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询nuvei报告导入记录列表 */
    getList() {
      this.loading = true;
      listMovementImport(this.queryParams).then(response => {
        // 为每行数据添加原始b2c2FxRate值
        this.movementImportList = response.rows.map(item => {
          return {
            ...item,
            originalB2c2FxRate: item.b2c2FxRate
          };
        });
        this.total = response.total;
        this.loading = false;
      });
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
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
        originName: undefined,
        fileUrl: undefined,
        importTotalNum: undefined,
        successNum: undefined,
        failNum: undefined,
        status: undefined,
        createdAt: undefined
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
      this.selectedRows = selection
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t('common.add');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMovementImport(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('common.edit');
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMovementImport(this.form).then(response => {
              this.msgSuccess(this.$t('payment.movementImport.updateSuccess'));
              this.open = false;
              this.getList();
            });
          } else {
            addMovementImport(this.form).then(response => {
              this.msgSuccess(this.$t('common.addSuccess'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id;
      if (row.status !== 3) {
        this.msgError(this.$t('payment.movementImport.onlyInvalidCanDelete'));
        return
      }
      this.$confirm(
        this.$t('payment.movementImport.confirmDelete', { id: ids }),
        this.$t('payment.movementImport.warning'),
        {
          confirmButtonText: this.$t('payment.movementImport.confirm'),
          cancelButtonText: this.$t('payment.movementImport.cancel'),
          type: "warning"
        }
      ).then(function () {
        return delMovementImport(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess(this.$t('payment.movementImport.deleteSuccess'));
      })
    },
    /**  对账明细 */
    handleShopBill(row) {
      router.push({
        path: "/check/movementData",
        query: {
          movementImportId: row.id
        }
      });
    },
    // 导出Recon报告
    handleReconReportExport(row) {
      if (row.status !== 1) {
        this.msgError(this.$t('payment.movementImport.onlyGeneratedCanExportRecon'));
        return
      }
      downLoadZip("/payment/movementImport/exportReconReport/" + row.id, "ruoyi");
    },
    // 导出日报
    handleDailySummaryExport(row) {
      if (row.status !== 1) {
        this.msgError(this.$t('payment.movementImport.onlyGeneratedCanExportDaily'));
        return
      }
      if (!row.b2c2FxRate) {
        this.msgError(this.$t('payment.movementImport.pleaseSetB2c2FxRate'));
        return
      }
      exportDailySummary([row.id])
        .then(response => {
          this.download(response.msg);
        });
    },
    // 批量导出日报
    handleDailySummaryBatchExport() {
      const failedItem = this.selectedRows.find(item => item.status !== 1);
      if (failedItem) {
        this.msgError(this.$t('payment.movementImport.reportGenerationFailed', { id: failedItem.id }));
        return
      }

      const noB2c2FxRateItem = this.selectedRows.find(item => item.b2c2FxRate == null);
      if (noB2c2FxRateItem) {
        this.msgError(this.$t('payment.movementImport.reportMissingB2c2FxRate', { id: noB2c2FxRateItem.id }));
        return
      }

      exportDailySummary(this.ids).then(response => {
        this.download(response.msg);
      });
    },
    // 重新生成对账
    handleRegenerate(row) {
      this.$confirm(
        this.$t('payment.movementImport.regenerateConfirm', { id: row.id }),
        this.$t('payment.movementImport.warning'),
        {
          confirmButtonText: this.$t('payment.movementImport.confirm'),
          cancelButtonText: this.$t('payment.movementImport.cancel'),
          type: 'warning'
        }
      )
        .then(() => {
          return regenerate(row.id)
        })
        .then(() => {
          this.msgSuccess(this.$t('payment.movementImport.regenerateSuccess'))
        })
        .catch(() => {})
    },
    // 失效
    handleInvalid(row) {
      const ids = row.id
      this.$confirm(
        this.$t('payment.movementImport.invalidConfirm', { id: ids }),
        this.$t('payment.movementImport.warning'),
        {
          confirmButtonText: this.$t('payment.movementImport.confirm'),
          cancelButtonText: this.$t('payment.movementImport.cancel'),
          type: 'warning'
        }
      )
        .then(function () {
          return invalidMovementImport(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('payment.movementImport.invalidSuccess'))
        })
        .catch(() => {})
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = this.$t('payment.movementImport.importDialogTitle')
      this.upload.open = true
      this.upload.fileList = []
    },
    // 文件选择变化处理
    handleFileChange(file, fileList) {
      this.upload.fileList = fileList
    },
    // 文件移除处理
    handleFileRemove(file, fileList) {
      this.upload.fileList = fileList
    },
    // 对话框关闭处理
    handleDialogClose() {
      this.upload.fileList = []
      this.upload.isUploading = false
    },
    // 提交上传文件
    async submitFileForm() {
      // 检查是否选择了文件
      if (this.upload.fileList.length === 0) {
        this.msgError(this.$t('payment.movementImport.pleaseSelectFile'))
        return
      }

      this.upload.isUploading = true

      try {
        const file = this.upload.fileList[0].raw
        const response = await importNuvieReport(file)

        this.upload.open = false
        this.upload.isUploading = false
        this.upload.fileList = []

        this.$alert(response.msg, this.$t('payment.movementImport.importResult'), {
          dangerouslyUseHTMLString: true
        })
        this.getList()
      } catch (error) {
        this.upload.isUploading = false
        this.msgError(this.$t('payment.movementImport.importFailed'))
        console.error('上传失败:', error)
      }
    },
    // 处理b2c2FxRate失去焦点事件
    handleB2c2FxRateBlur(row) {
      // 只有当值发生变化时才调用接口
      if (row.originalB2c2FxRate !== row.b2c2FxRate) {
        updateMovementImport(row)
          .then(response => {
            this.msgSuccess(this.$t('payment.movementImport.updateSuccess'))
            // 更新原始值
            row.originalB2c2FxRate = row.b2c2FxRate
            this.getList()
          })
          .catch(error => {
            console.error('修改失败', error)
            this.msgError(this.$t('payment.movementImport.updateFailed'))
            // 恢复原始值
            row.b2c2FxRate = row.originalB2c2FxRate
          })
      }
    },
    // 格式化b2c2FxRate输入值，只允许数字和小数点
    formatB2c2FxRateInput(row) {
      // 只允许数字和一个小数点
      row.b2c2FxRate = row.b2c2FxRate.replace(/[^\d.]/g, '');
      // 确保只有一个小数点
      const parts = row.b2c2FxRate.split('.');
      if (parts.length > 2) {
        row.b2c2FxRate = parts[0] + '.' + parts.slice(1).join('');
      }
      // 限制小数点后最多4位
      if (parts[1] && parts[1].length > 4) {
        row.b2c2FxRate = parts[0] + '.' + parts[1].substring(0, 4);
      }
    }
  }
}
;
</script>
