<template>
  <el-form ref="genInfoForm" :model="info" :rules="rules" label-width="180px">
    <el-row>
      <el-col :span="12">
        <el-form-item prop="tplCategory">
          <span slot="label">{{ $t('tool.genTemplate') }}</span>
          <el-select v-model="info.tplCategory" @change="tplSelectChange">
            <el-option :label="$t('tool.singleTable')" value="crud" />
            <el-option :label="$t('tool.treeTable')" value="tree" />
            <el-option :label="$t('tool.mainSubTable')" value="sub" />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="packageName">
          <span slot="label">
            {{ $t('tool.packagePath') }}
            <el-tooltip :content="$t('tool.packagePathTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-input v-model="info.packageName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="moduleName">
          <span slot="label">
            {{ $t('tool.moduleName') }}
            <el-tooltip :content="$t('tool.moduleNameTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-input v-model="info.moduleName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="businessName">
          <span slot="label">
            {{ $t('tool.businessName') }}
            <el-tooltip :content="$t('tool.businessNameTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-input v-model="info.businessName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="functionName">
          <span slot="label">
            {{ $t('tool.functionName') }}
            <el-tooltip :content="$t('tool.functionNameTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-input v-model="info.functionName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            {{ $t('tool.parentMenu') }}
            <el-tooltip :content="$t('tool.parentMenuTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <treeselect
            v-model="info.parentMenuId"
            :append-to-body="true"
            :options="menus"
            :normalizer="normalizer"
            :show-count="true"
            :placeholder="$t('tool.parentMenuPlaceholder')"
          />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="genType">
          <span slot="label">
            {{ $t('tool.genCodeMethod') }}
            <el-tooltip :content="$t('tool.genCodeMethodTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-radio v-model="info.genType" label="0">{{ $t('tool.zipPackage') }}</el-radio>
          <el-radio v-model="info.genType" label="1">{{ $t('tool.customPath') }}</el-radio>
        </el-form-item>
      </el-col>

      <el-col v-if="info.genType == '1'" :span="24">
        <el-form-item prop="genPath">
          <span slot="label">
            {{ $t('tool.customPath') }}
            <el-tooltip :content="$t('tool.customPathTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-input v-model="info.genPath">
            <el-dropdown slot="append">
              <el-button type="primary">
                {{ $t('tool.recentPathQuickSelect') }}
                <i class="el-icon-arrow-down el-icon--right" />
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="info.genPath = '/'">
                  {{ $t('tool.restoreDefaultPath') }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row v-show="info.tplCategory == 'tree'">
      <h4 class="form-header">{{ $t('tool.otherInfo') }}</h4>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            {{ $t('tool.treeCodeField') }}
            <el-tooltip :content="$t('tool.treeCodeFieldTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-select v-model="info.treeCode" :placeholder="$t('tool.pleaseSelect')">
            <el-option
              v-for="(column, index) in info.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            {{ $t('tool.treeParentCodeField') }}
            <el-tooltip :content="$t('tool.treeParentCodeFieldTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-select v-model="info.treeParentCode" :placeholder="$t('tool.pleaseSelect')">
            <el-option
              v-for="(column, index) in info.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            {{ $t('tool.treeNameField') }}
            <el-tooltip :content="$t('tool.treeNameFieldTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-select v-model="info.treeName" :placeholder="$t('tool.pleaseSelect')">
            <el-option
              v-for="(column, index) in info.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row v-show="info.tplCategory == 'sub'">
      <h4 class="form-header">{{ $t('tool.relationInfo') }}</h4>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            {{ $t('tool.subTableName') }}
            <el-tooltip :content="$t('tool.subTableNameTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-select v-model="info.subTableName" :placeholder="$t('tool.pleaseSelect')" @change="subSelectChange">
            <el-option
              v-for="(table, index) in tables"
              :key="index"
              :label="table.tableName + '：' + table.tableComment"
              :value="table.tableName"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            {{ $t('tool.subTableFkName') }}
            <el-tooltip :content="$t('tool.subTableFkNameTooltip')" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
          </span>
          <el-select v-model="info.subTableFkName" :placeholder="$t('tool.pleaseSelect')">
            <el-option
              v-for="(column, index) in subColumns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'BasicInfoForm',
  components: { Treeselect },
  props: {
    info: {
      type: Object,
      default: null
    },
    tables: {
      type: Array,
      default: null
    },
    menus: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      subColumns: [],
      rules: {
        tplCategory: [{ required: true, message: this.$t('tool.selectGenTemplateRequired'), trigger: 'blur' }],
        packageName: [{ required: true, message: this.$t('tool.inputPackagePathRequired'), trigger: 'blur' }],
        moduleName: [{ required: true, message: this.$t('tool.inputModuleNameRequired'), trigger: 'blur' }],
        businessName: [{ required: true, message: this.$t('tool.inputBusinessNameRequired'), trigger: 'blur' }],
        functionName: [{ required: true, message: this.$t('tool.inputFunctionNameRequired'), trigger: 'blur' }]
      }
    }
  },
  watch: {
    'info.subTableName': function (val) {
      this.setSubTableColumns(val)
    }
  },
  created() {},
  methods: {
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      }
    },
    /** 选择子表名触发 */
    subSelectChange(value) {
      this.info.subTableFkName = ''
    },
    /** 选择生成模板触发 */
    tplSelectChange(value) {
      if (value !== 'sub') {
        this.info.subTableName = ''
        this.info.subTableFkName = ''
      }
    },
    /** 设置关联外键 */
    setSubTableColumns(value) {
      for (var item in this.tables) {
        const name = this.tables[item].tableName
        if (value === name) {
          this.subColumns = this.tables[item].columns
          break
        }
      }
    }
  }
}
</script>
