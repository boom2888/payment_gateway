import { getRouters } from '@/api/menu'
import ParentView from '@/components/ParentView'
import i18n from '@/lang'
import Layout from '@/layout/index'
import { constantRoutes } from '@/router'

const permission = {
  state: {
    routes: [],
    addRoutes: [],
    sidebarRouters: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    },
    SET_SIDEBAR_ROUTERS: (state, routers) => {
      state.sidebarRouters = constantRoutes.concat(routers)
    },
    SET_SIDEBAR_ROUTERS_FOR_EMI: (state, routers) => {
      // EMI账号过滤掉首页路由
      const filteredConstantRoutes = constantRoutes.filter(route => {
        // 过滤掉首页路由（path为空字符串且有index子路由的）
        if (route.path === '' && route.children && route.children.some(child => child.path === 'index')) {
          return false
        }
        return true
      })
      state.sidebarRouters = filteredConstantRoutes.concat(routers)
    }
  },
  actions: {
    // 生成路由
    GenerateRoutes({ commit, rootGetters }) {
      return new Promise(resolve => {
        // 向后端请求路由数据
        getRouters().then(res => {
          const sdata = JSON.parse(JSON.stringify(res.data))
          const rdata = JSON.parse(JSON.stringify(res.data))
          const sidebarRoutes = filterAsyncRouter(sdata)
          const rewriteRoutes = filterAsyncRouter(rdata, false, true)
          rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })
          commit('SET_ROUTES', rewriteRoutes)

          // 如果是EMI账号，使用特殊的mutation来过滤首页
          const userType = rootGetters.userType
          if (userType === 2) {
            commit('SET_SIDEBAR_ROUTERS_FOR_EMI', sidebarRoutes)
          } else {
            commit('SET_SIDEBAR_ROUTERS', sidebarRoutes)
          }

          resolve(rewriteRoutes)
        })
      })
    },

    // 语言切换后重新处理路由菜单名称
    RefreshRoutesForLanguage({ commit, state, rootGetters }) {
      return new Promise(resolve => {
        if (state.addRoutes.length > 0) {
          // 重新处理已存在的路由数据
          const sdata = JSON.parse(JSON.stringify(state.addRoutes.filter(route => route.path !== '*')))
          const rdata = JSON.parse(JSON.stringify(state.addRoutes.filter(route => route.path !== '*')))

          const sidebarRoutes = filterAsyncRouter(sdata)
          const rewriteRoutes = filterAsyncRouter(rdata, false, true)
          rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })

          commit('SET_ROUTES', rewriteRoutes)

          // 如果是EMI账号，使用特殊的mutation来过滤首页
          const userType = rootGetters.userType
          if (userType === 2) {
            commit('SET_SIDEBAR_ROUTERS_FOR_EMI', sidebarRoutes)
          } else {
            commit('SET_SIDEBAR_ROUTERS', sidebarRoutes)
          }

          resolve(rewriteRoutes)
        } else {
          // 如果没有现有路由，重新获取
          this.dispatch('permission/GenerateRoutes').then(resolve)
        }
      })
    }
  }
}

// 根据当前语言处理菜单名称和标题
function processMenuNames(route) {
  const currentLang = i18n.locale

  // 根据语言设置菜单名称
  if (route.menuName && route.menuNameZh) {
    // 根据当前语言选择合适的菜单名称
    const selectedMenuName = currentLang === 'en' ? route.menuName : route.menuNameZh

    // 更新路由的 meta.title
    if (route.meta) {
      route.meta.title = selectedMenuName
    }

    // 保留原有的 menuName 和 menuNameZh 供其他地方使用
    route.displayName = selectedMenuName
  }

  return route
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    // 处理菜单名称的多语言
    processMenuNames(route)

    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    // 处理子路由的菜单名称多语言
    processMenuNames(el)

    if (el.children && el.children.length) {
      if (el.component === 'ParentView') {
        el.children.forEach(c => {
          // 处理嵌套子路由的菜单名称
          processMenuNames(c)
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

export const loadView = view => {
  // 路由懒加载
  return resolve => require([`@/views/${view}`], resolve)
}

export default permission
