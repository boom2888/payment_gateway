<template>
  <el-dropdown trigger="click" @command="handleLanguageChange" class="language-switch">
    <div>
      <svg-icon icon-class="language" class="language-icon" />
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="zh" :disabled="currentLanguage === 'zh'">中文</el-dropdown-item>
      <el-dropdown-item command="en" :disabled="currentLanguage === 'en'">English</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import locale from 'element-ui/lib/locale'
import elementEnLocale from 'element-ui/lib/locale/lang/en'
import elementZhLocale from 'element-ui/lib/locale/lang/zh-CN'
import Cookies from 'js-cookie'

export default {
  name: 'LanguageSwitch',
  computed: {
    currentLanguage() {
      // 确保 i18n 已经正确加载
      return this.$i18n ? this.$i18n.locale : 'zh'
    }
  },
  methods: {
    handleLanguageChange(lang) {
      // 确保 i18n 存在
      if (this.$i18n) {
        this.$i18n.locale = lang
        Cookies.set('language', lang)
        // 设置Element UI的语言
        if (lang === 'en') {
          locale.use(elementEnLocale)
        } else {
          locale.use(elementZhLocale)
        }
        // 最简单方案：立即刷新页面
        console.log('立即刷新页面')
        location.reload()
      } else {
        console.error('i18n 不存在!')
      }
    }
  }
}
</script>

<style scoped>
.language-switch {
  cursor: pointer;
  padding: 0 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #606266;
  font-size: 18px;
}

.language-icon {
  font-size: 20px;
  transition: all 0.3s ease;
}

.language-switch:hover .language-icon {
  transform: scale(1.1);
  color: #409eff;
}
</style>
