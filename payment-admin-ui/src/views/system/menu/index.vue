<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true">
      <el-form-item :label="$t('system.menu.menuName')" prop="menuName">
        <el-input
          v-model="searchKeyword"
          :placeholder="$t('system.menu.pleaseEnterMenuName')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('system.menu.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('system.menu.menuStatus')" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('system.menu.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('system.menu.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:menu:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >
          {{ $t('system.menu.add') }}
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table
      v-loading="loading"
      :data="menuList"
      row-key="menuId"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column :label="$t('system.menu.menuName')" :show-overflow-tooltip="true" width="160">
        <template slot-scope="scope">
          {{ getMenuDisplayName(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column prop="icon" :label="$t('system.menu.icon')" align="center" width="100">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" :label="$t('system.menu.sortOrder')" width="100" />
      <el-table-column prop="perms" :label="$t('system.menu.permissionIdentifier')" :show-overflow-tooltip="true" />
      <el-table-column prop="component" :label="$t('system.menu.componentPath')" :show-overflow-tooltip="true" />
      <el-table-column prop="status" :label="$t('system.menu.status')" :formatter="statusFormat" width="80" />
      <el-table-column :label="$t('system.menu.createTime')" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.menu.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:menu:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >
            {{ $t('system.menu.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['system:menu:add']"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
          >
            {{ $t('system.menu.add') }}
          </el-button>
          <el-button
            v-hasPermi="['system:menu:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >
            {{ $t('system.menu.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="170px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('system.menu.parentMenu')">
              <treeselect
                v-model="form.parentId"
                :options="menuOptions"
                :normalizer="normalizer"
                :show-count="true"
                :placeholder="$t('system.menu.selectParentMenu')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('system.menu.menuType')" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">{{ $t('system.menu.directory') }}</el-radio>
                <el-radio label="C">{{ $t('system.menu.menu') }}</el-radio>
                <el-radio label="F">{{ $t('system.menu.button') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.menuType != 'F'" :label="$t('system.menu.menuIcon')">
              <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()">
                <IconSelect ref="iconSelect" @selected="selected" />
                <el-input
                  slot="reference"
                  v-model="form.icon"
                  :placeholder="$t('system.menu.clickToSelectIcon')"
                  readonly
                >
                  <svg-icon
                    v-if="form.icon"
                    slot="prefix"
                    :icon-class="form.icon"
                    class="el-input__icon"
                    style="height: 32px; width: 16px"
                  />
                  <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('system.menu.menuNameEn')" prop="menuName">
              <el-input v-model="form.menuName" :placeholder="$t('system.menu.pleaseEnterMenuNameEn')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('system.menu.menuNameZh')" prop="menuNameZh">
              <el-input v-model="form.menuNameZh" :placeholder="$t('system.menu.pleaseEnterMenuNameZh')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('system.menu.displayOrder')" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" :label="$t('system.menu.isExternalLink')">
              <el-radio-group v-model="form.isFrame">
                <el-radio label="0">{{ $t('system.menu.yes') }}</el-radio>
                <el-radio label="1">{{ $t('system.menu.no') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" :label="$t('system.menu.routePath')" prop="path">
              <el-input v-model="form.path" :placeholder="$t('system.menu.pleaseEnterRoutePath')" />
            </el-form-item>
          </el-col>
          <el-col v-if="form.menuType == 'C'" :span="12">
            <el-form-item :label="$t('system.menu.componentPathLabel')" prop="component">
              <el-input v-model="form.component" :placeholder="$t('system.menu.pleaseEnterComponentPath')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'M'" :label="$t('system.menu.permissionMark')">
              <el-input
                v-model="form.perms"
                :placeholder="$t('system.menu.pleaseEnterPermissionMark')"
                maxlength="50"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" :label="$t('system.menu.displayStatus')">
              <el-radio-group v-model="form.visible">
                <el-radio v-for="dict in visibleOptions" :key="dict.dictValue" :label="dict.dictValue">
                  {{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" :label="$t('system.menu.menuStatus')">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictValue">
                  {{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType == 'C'" :label="$t('system.menu.isCache')">
              <el-radio-group v-model="form.isCache">
                <el-radio label="0">{{ $t('system.menu.cache') }}</el-radio>
                <el-radio label="1">{{ $t('system.menu.noCache') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('system.menu.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('system.menu.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addMenu, delMenu, getMenu, listMenu, updateMenu } from '@/api/system/menu'
import IconSelect from '@/components/IconSelect'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'Menu',
  components: { Treeselect, IconSelect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 菜单表格树数据
      menuList: [],
      // 菜单树选项
      menuOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 显示状态数据字典
      visibleOptions: [],
      // 菜单状态数据字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        menuName: undefined,
        menuNameZh: undefined,
        visible: undefined
      },
      searchKeyword: undefined,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        menuName: [{ required: true, message: this.$t('system.menu.menuNameEnRequired'), trigger: 'blur' }],
        menuNameZh: [{ required: true, message: this.$t('system.menu.menuNameZhRequired'), trigger: 'blur' }],
        orderNum: [{ required: true, message: this.$t('system.menu.orderNumRequired'), trigger: 'blur' }],
        path: [{ required: true, message: this.$t('system.menu.routePathRequired'), trigger: 'blur' }]
      }
    }
  },
  computed: {
    isZhLocale() {
      return this.$i18n.locale === 'zh'
    }
  },
  watch: {
    '$i18n.locale'() {
      this.getList()
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_show_hide').then(response => {
      this.visibleOptions = response.data
    })
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
  },
  methods: {
    // 选择图标
    selected(name) {
      this.form.icon = name
    },
    getMenuDisplayName(menu) {
      if (!menu) {
        return ''
      }
      return this.isZhLocale ? menu.menuNameZh || menu.menuName : menu.menuName
    },
    applyMenuNameFilter() {
      const keyword = this.searchKeyword ? this.searchKeyword.trim() : undefined
      if (this.isZhLocale) {
        this.queryParams.menuNameZh = keyword
        this.queryParams.menuName = undefined
      } else {
        this.queryParams.menuName = keyword
        this.queryParams.menuNameZh = undefined
      }
    },
    /** 查询菜单列表 */
    getList() {
      this.loading = true
      this.applyMenuNameFilter()
      listMenu(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data, 'menuId')
        this.loading = false
      })
    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.menuId,
        label: this.getMenuDisplayName(node),
        children: node.children
      }
    },
    /** 查询菜单下拉树结构 */
    getTreeselect() {
      listMenu().then(response => {
        this.menuOptions = []
        const menu = { menuId: 0, menuName: this.$t('system.menu.mainCategory'), children: [] }
        menu.children = this.handleTree(response.data, 'menuId')
        this.menuOptions.push(menu)
      })
    },
    // 显示状态字典翻译
    visibleFormat(row, column) {
      if (row.menuType == 'F') {
        return ''
      }
      return this.selectDictLabel(this.visibleOptions, row.visible)
    },
    // 菜单状态字典翻译
    statusFormat(row, column) {
      if (row.menuType == 'F') {
        return ''
      }
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        menuNameZh: undefined,
        icon: undefined,
        menuType: 'M',
        orderNum: undefined,
        isFrame: '1',
        isCache: '0',
        visible: '0',
        status: '0'
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.searchKeyword = undefined
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      this.getTreeselect()
      if (row != null && row.menuId) {
        this.form.parentId = row.menuId
      } else {
        this.form.parentId = 0
      }
      this.open = true
      this.title = this.$t('system.menu.addMenu')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getTreeselect()
      getMenu(row.menuId).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('system.menu.editMenu')
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.menuId != undefined) {
            updateMenu(this.form).then(response => {
              this.msgSuccess(this.$t('system.menu.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addMenu(this.form).then(response => {
              this.msgSuccess(this.$t('system.menu.addSuccess'))
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm(this.$t('system.menu.confirmDelete', { name: row.menuName }), this.$t('system.menu.warning'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      })
        .then(function () {
          return delMenu(row.menuId)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(this.$t('system.menu.deleteSuccess'))
        })
    }
  }
}
</script>
