import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import permission from './directive/permission'
import router from './router'
import store from './store'

import { getConfigKey } from '@/api/system/config'
import { getDicts } from '@/api/system/dict/data'
import Pagination from '@/components/Pagination'
import {
  addDateRange,
  download,
  handleTree,
  parseTime,
  resetForm,
  selectDictLabel,
  selectDictLabels
} from '@/utils/ruoyi'
import './assets/icons' // icon
import './permission' // permission control
// 自定义表格工具扩展
import RightToolbar from '@/components/RightToolbar'

// 国际化
import locale from 'element-ui/lib/locale'
import i18n from './lang'
// 设置Element UI的语言
if (i18n.locale === 'en') {
  locale.use(require('element-ui/lib/locale/lang/en').default)
} else {
  locale.use(require('element-ui/lib/locale/lang/zh-CN').default)
}

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: 'success' })
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: 'error' })
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg)
}

// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)

Vue.use(permission)

Vue.config.productionTip = false

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently, MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false
Vue.config.devtools = process.env.NODE_ENV === 'development'

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})

Vue.prototype.formatDay = function (time) {
  return time + '天'
}

Vue.prototype.formatPasent = function (value) {
  return value + '%'
}

Vue.prototype.formatFil = function (value) {
  return value + 'FIL'
}
Vue.prototype.formatUsdt = function (value) {
  return value + 'USDT'
}

Vue.prototype.formatPower = function (value) {
  return value + 'T'
}
// 追加 ↓↓↓
Vue.mixin({
  computed: {
    $isAdmin() {
      return this.$store.getters.isAdmin
    }
  }
})
/* =========  v-copy-hover 指令  ========= */
Vue.directive('copy-hover', {
  /* 首次挂载 */
  inserted: function (el, binding) {
    initPopover(el, binding)
  },
  /* 数据更新时重新赋值 */
  update: function (el, binding) {
    const pop = el._copyHoverPop // 我们之前挂在 el 上缓存的节点
    if (pop) {
      const raw = binding.value || el.innerText || ''
      pop.querySelector('.copy-hover-body').innerText = raw
      /* 更新复制按钮的值 */
      pop._copyText = raw
    }
  }
})

/* 把初始化逻辑抽出来，避免重复代码 */
function initPopover(el, binding) {
  /* 创建节点并缓存到 el 上，方便 update 时拿 */
  const pop = document.createElement('div')
  pop.className = 'copy-hover-popover'
  const body = document.createElement('div')
  body.className = 'copy-hover-body'
  const actions = document.createElement('div')
  actions.style.textAlign = 'center'
  actions.style.marginTop = '8px'
  const copyButton = document.createElement('button')
  copyButton.className = 'el-button el-button--primary el-button--mini copy-btn'
  copyButton.type = 'button'
  copyButton.textContent = '复制'
  actions.appendChild(copyButton)
  pop.appendChild(body)
  pop.appendChild(actions)
  el._copyHoverPop = pop
  el._copyText = ''

  /* 复制按钮事件（用缓存值） */
  pop.querySelector('.copy-btn').addEventListener('click', () => {
    navigator.clipboard.writeText(el._copyText).then(() => Vue.prototype.msgSuccess('已复制'))
  })

  /* 悬浮逻辑 */
  let timer
  const show = () => {
    /* 每次显示前先把最新值写进去 */
    const currentValue = binding.value || el.innerText || ''
    
    // 如果没有值或者值为空字符串，不显示悬停效果
    if (!currentValue || currentValue.trim() === '') {
      return
    }
    
    el._copyText = currentValue
    pop.querySelector('.copy-hover-body').innerText = el._copyText

    clearTimeout(timer)
    document.body.appendChild(pop)
    
    // 基于文字元素本身来定位弹窗
    const rect = el.getBoundingClientRect()
    const popWidth = 300 // 弹窗宽度
    const popHeight = 100 // 弹窗高度
    
    // 计算最佳位置
    let left = rect.left
    let top = rect.top - popHeight - 10
    
    // 如果左侧空间不够，显示在右侧
    if (left < 10) {
      left = rect.right + 10
    }
    
    // 如果上方空间不够，显示在下方
    if (top < 10) {
      top = rect.bottom + 10
    }
    
    // 如果右侧空间不够，调整到左侧
    if (left + popWidth > window.innerWidth - 10) {
      left = rect.left - popWidth - 10
    }
    
    pop.style.left = left + 'px'
    pop.style.top = top + 'px'
  }
  const hide = () => {
    timer = setTimeout(() => pop.remove(), 200)
  }

  el.addEventListener('mouseenter', show)
  el.addEventListener('mouseleave', hide)
  pop.addEventListener('mouseenter', () => clearTimeout(timer))
  pop.addEventListener('mouseleave', hide)
}
