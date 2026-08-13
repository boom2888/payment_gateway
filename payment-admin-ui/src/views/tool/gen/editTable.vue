<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane :label="$t('tool.basicInfo')" name="basic">
        <basic-info-form ref="basicInfo" :info="info" />
      </el-tab-pane>
      <el-tab-pane :label="$t('tool.fieldInfo')" name="cloum">
        <el-table ref="dragTable" :data="cloumns" row-key="columnId" :max-height="tableHeight">
          <el-table-column :label="$t('tool.index')" type="index" min-width="5%" class-name="allowDrag" />
          <el-table-column
            :label="$t('tool.columnName')"
            prop="columnName"
            min-width="10%"
            :show-overflow-tooltip="true"
          />
          <el-table-column :label="$t('tool.columnComment')" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.columnComment" />
            </template>
          </el-table-column>
          <el-table-column
            :label="$t('tool.physicalType')"
            prop="columnType"
            min-width="10%"
            :show-overflow-tooltip="true"
          />
          <el-table-column :label="$t('tool.javaType')" min-width="11%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.javaType">
                <el-option label="Long" value="Long" />
                <el-option label="String" value="String" />
                <el-option label="Integer" value="Integer" />
                <el-option label="Double" value="Double" />
                <el-option label="BigDecimal" value="BigDecimal" />
                <el-option label="Date" value="Date" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column :label="$t('tool.javaProperty')" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.javaField" />
            </template>
          </el-table-column>

          <el-table-column :label="$t('tool.insert')" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.isInsert" true-label="1" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('tool.edit')" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.isEdit" true-label="1" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('tool.list')" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.isList" true-label="1" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('tool.query')" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.isQuery" true-label="1" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('tool.queryMethod')" min-width="10%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.queryType">
                <el-option label="=" value="EQ" />
                <el-option label="!=" value="NE" />
                <el-option label=">" value="GT" />
                <el-option label=">=" value="GTE" />
                <el-option label="<" value="LT" />
                <el-option label="<=" value="LTE" />
                <el-option label="LIKE" value="LIKE" />
                <el-option label="BETWEEN" value="BETWEEN" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column :label="$t('tool.required')" min-width="6%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.isRequired" true-label="1" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('tool.displayType')" min-width="12%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.htmlType">
                <el-option :label="$t('tool.textBox')" value="input" />
                <el-option :label="$t('tool.textArea')" value="textarea" />
                <el-option :label="$t('tool.dropdown')" value="select" />
                <el-option :label="$t('tool.radioButton')" value="radio" />
                <el-option :label="$t('tool.checkbox')" value="checkbox" />
                <el-option :label="$t('tool.dateControl')" value="datetime" />
                <el-option :label="$t('tool.imageUpload')" value="imageUpload" />
                <el-option :label="$t('tool.fileUpload')" value="fileUpload" />
                <el-option :label="$t('tool.richTextControl')" value="editor" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column :label="$t('tool.dictType')" min-width="12%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.dictType" clearable filterable :placeholder="$t('tool.pleaseSelect')">
                <el-option
                  v-for="dict in dictOptions"
                  :key="dict.dictType"
                  :label="dict.dictName"
                  :value="dict.dictType"
                >
                  <span style="float: left">{{ dict.dictName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ dict.dictType }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane :label="$t('tool.genInfo')" name="genInfo">
        <gen-info-form ref="genInfo" :info="info" :tables="tables" :menus="menus" />
      </el-tab-pane>
    </el-tabs>
    <el-form label-width="100px">
      <el-form-item style="text-align: center; margin-left: -100px; margin-top: 10px">
        <el-button type="primary" @click="submitForm()">{{ $t('tool.submit') }}</el-button>
        <el-button @click="close()">{{ $t('tool.return') }}</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import { optionselect as getDictOptionselect } from '@/api/system/dict/type'
import { listMenu as getMenuTreeselect } from '@/api/system/menu'
import { getGenTable, updateGenTable } from '@/api/tool/gen'
import Sortable from 'sortablejs'
import basicInfoForm from './basicInfoForm'
import genInfoForm from './genInfoForm'

export default {
  name: 'GenEdit',
  components: {
    basicInfoForm,
    genInfoForm
  },
  data() {
    return {
      // 选中选项卡的 name
      activeName: 'cloum',
      // 表格的高度
      tableHeight: document.documentElement.scrollHeight - 245 + 'px',
      // 表信息
      tables: [],
      // 表列信息
      cloumns: [],
      // 字典信息
      dictOptions: [],
      // 菜单信息
      menus: [],
      // 表详细信息
      info: {}
    }
  },
  created() {
    const tableId = this.$route.params && this.$route.params.tableId
    if (tableId) {
      // 获取表详细信息
      getGenTable(tableId).then(res => {
        this.cloumns = res.data.rows
        this.info = res.data.info
        this.tables = res.data.tables
      })
      /** 查询字典下拉列表 */
      getDictOptionselect().then(response => {
        this.dictOptions = response.data
      })
      /** 查询菜单下拉列表 */
      getMenuTreeselect().then(response => {
        this.menus = this.handleTree(response.data, 'menuId')
      })
    }
  },
  mounted() {
    const el = this.$refs.dragTable.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
    const sortable = Sortable.create(el, {
      handle: '.allowDrag',
      onEnd: evt => {
        const targetRow = this.cloumns.splice(evt.oldIndex, 1)[0]
        this.cloumns.splice(evt.newIndex, 0, targetRow)
        for (const index in this.cloumns) {
          this.cloumns[index].sort = parseInt(index) + 1
        }
      }
    })
  },
  methods: {
    /** 提交按钮 */
    submitForm() {
      const basicForm = this.$refs.basicInfo.$refs.basicInfoForm
      const genForm = this.$refs.genInfo.$refs.genInfoForm
      Promise.all([basicForm, genForm].map(this.getFormPromise)).then(res => {
        const validateResult = res.every(item => !!item)
        if (validateResult) {
          const genTable = Object.assign({}, basicForm.model, genForm.model)
          genTable.columns = this.cloumns
          genTable.params = {
            treeCode: genTable.treeCode,
            treeName: genTable.treeName,
            treeParentCode: genTable.treeParentCode,
            parentMenuId: genTable.parentMenuId
          }
          updateGenTable(genTable).then(res => {
            this.msgSuccess(res.msg)
            if (res.code === 200) {
              this.close()
            }
          })
        } else {
          this.msgError(this.$t('tool.formValidationFailed'))
        }
      })
    },
    getFormPromise(form) {
      return new Promise(resolve => {
        form.validate(res => {
          resolve(res)
        })
      })
    },
    /** 关闭按钮 */
    close() {
      this.$store.dispatch('tagsView/delView', this.$route)
      this.$router.push({ path: '/tool/gen', query: { t: Date.now() } })
    }
  }
}
</script>
