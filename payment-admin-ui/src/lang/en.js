import common from './en/common'
import monitor from './en/monitor'
import payment from './en/payment'
import dashboard from './en/payment/dashboard'
import system from './en/system'
import notice from './en/system/notice'
import tool from './en/system/tool'

export default {
  route: {
    dashboard: 'Dashboard',
    system: 'System',
    user: 'User',
    role: 'Role',
    menu: 'Menu',
    dept: 'Department',
    dict: 'Dictionary',
    config: 'Config',
    notice: 'Notice',
    log: 'Log',
    monitor: 'Monitor',
    druid: 'Druid',
    server: 'Server',
    job: 'Job',
    generator: 'Generator',
    swagger: 'Swagger',
    tool: 'Tool',
    payment: 'Payment',
    order: 'Order'
  },
  navbar: {
    dashboard: 'Dashboard',
    github: 'Github',
    logOut: 'Log Out',
    profile: 'Profile',
    settings: 'Layout Settings',
    theme: 'Theme',
    size: 'Layout Size'
  },
  login: {
    title: 'Login Form',
    logIn: 'Login',
    username: 'Username',
    password: 'Password'
  },
  tagsView: {
    refresh: 'Refresh',
    close: 'Close',
    closeOthers: 'Close Others',
    closeAll: 'Close All'
  },
  settings: {
    title: 'Layout Settings',
    themeStyle: 'Theme Style',
    theme: 'Theme Color',
    tagsView: 'Enable Tags-View',
    fixedHeader: 'Fixed Header',
    sidebarLogo: 'Show Logo'
  },
  common,
  payment,
  dashboard,
  tool,
  notice,
  system,
  monitor
}
