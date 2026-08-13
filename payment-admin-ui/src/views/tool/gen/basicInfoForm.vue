<template>
  <el-form ref="basicInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item :label="$t('tool.tableName')" prop="tableName">
          <el-input v-model="info.tableName" :placeholder="$t('tool.tableNamePlaceholder')" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('tool.tableComment')" prop="tableComment">
          <el-input v-model="info.tableComment" :placeholder="$t('tool.tableCommentPlaceholder')" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item :label="$t('tool.className')" prop="className">
          <el-input v-model="info.className" :placeholder="$t('tool.classNamePlaceholder')" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('tool.functionAuthor')" prop="functionAuthor">
          <el-input v-model="info.functionAuthor" :placeholder="$t('tool.functionAuthorPlaceholder')" />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item :label="$t('tool.remark')" prop="remark">
          <el-input v-model="info.remark" type="textarea" :rows="3" :placeholder="$t('tool.remarkPlaceholder')" />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
export default {
  name: 'BasicInfoForm',
  props: {
    info: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      rules: {
        tableName: [{ required: true, message: this.$t('tool.tableNameRequired'), trigger: 'blur' }],
        tableComment: [{ required: true, message: this.$t('tool.tableCommentRequired'), trigger: 'blur' }],
        className: [{ required: true, message: this.$t('tool.classNameRequired'), trigger: 'blur' }],
        functionAuthor: [{ required: true, message: this.$t('tool.functionAuthorRequired'), trigger: 'blur' }]
      }
    }
  },
  mounted() {
    // this.initSortable()
    // 检查当前语言
    console.log(123123)
    console.log(this.$i18n.locale)

    // 检查 tool 模块是否存在
    console.log(this.$i18n.messages.zh.tool)
    console.log(this.$i18n.messages.en.tool)

    // 测试翻译
    console.log(this.$t('tool.tableName'))
  }
}
</script>
