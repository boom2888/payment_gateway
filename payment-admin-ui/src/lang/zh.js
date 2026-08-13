import common from './zh/common'
import monitor from './zh/monitor'
import payment from './zh/payment'
import dashboard from './zh/payment/dashboard'
import system from './zh/system'
import notice from './zh/system/notice'
import tool from './zh/system/tool'

export default {
  route: {
    dashboard: '首页',
    system: '系统管理',
    user: '用户管理',
    role: '角色管理',
    menu: '菜单管理',
    dept: '部门管理',
    dict: '字典管理',
    config: '参数配置',
    notice: '通知公告',
    log: '日志管理',
    monitor: '系统监控',
    druid: '数据监控',
    server: '服务监控',
    job: '定时任务',
    generator: '代码生成',
    swagger: '系统接口',
    tool: '系统工具',
    payment: '支付管理',
    order: '订单管理'
  },
  navbar: {
    dashboard: '首页',
    github: '项目地址',
    logOut: '退出登录',
    profile: '个人中心',
    settings: '布局设置',
    theme: '换肤',
    size: '布局大小'
  },
  login: {
    title: '系统登录',
    logIn: '登录',
    username: '账号',
    password: '密码'
  },
  tagsView: {
    refresh: '刷新',
    close: '关闭',
    closeOthers: '关闭其它',
    closeAll: '关闭所有'
  },
  settings: {
    title: '系统布局配置',
    themeStyle: '主题风格设置',
    theme: '主题颜色',
    tagsView: '开启 Tags-Views',
    fixedHeader: '固定 Header',
    sidebarLogo: '显示 Logo'
  },
  common,
  payment,
  dashboard,
  tool,
  notice,
  system,
  monitor
}
