import {getToken} from '@/utils/auth'
import {Message} from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import router from './router'
import store from './store'

NProgress.configure({showSpinner: false})

const whiteList = ['/login', '/register', '/emi/register', '/auth-redirect']

router.beforeEach((to, from, next) => {
    NProgress.start()
    if (getToken()) {
        /* has token*/
        if (to.path === '/login') {
            next({path: '/'})
            NProgress.done()
        } else {
            if (store.getters.roles.length === 0) {
                // 判断当前用户是否已拉取完user_info信息
                store
                    .dispatch('GetInfo')
                    .then(res => {
                        // 拉取user_info
                        const roles = res.roles

                        if (res.user.enableStatus === 1) {
                            store.dispatch('LogOut').then(() => {
                                Message.error('您的emi账号已被禁用')
                                next({path: '/'})
                            })
                            return
                        }

                        if (res.user.type === 2) {
                            //1:普通后台账号 2:emi账号,需要完善同时还需要status(0-待提交资料,1-审核中,4-审核不通过, 才可以跳转这个界面.)
                            if (
                                res.user.materialStatus === 0 ||
                                res.user.materialStatus === 1 ||
                                res.user.materialStatus === 2 ||
                                res.user.materialStatus === 4 ||
                                res.user.materialStatus === null
                            ) {
                                next('/shopAuth/emi')
                                NProgress.done()
                                return
                            }
                        } else if (res.shopStatus === 'FAIL' && to.path !== '/shopAuth' && res.user.tpye !== 2) {
                            // 检查商户状态
                            next('/shopAuth')
                            NProgress.done()
                            return
                        }

                        //如果res.user.status=1,需要将用户直接登出,表示被禁用了
                        if (res.user.status === 1) {
                            store.dispatch('LogOut').then(() => {
                                Message.error('您的账号已被禁用')
                                next({path: '/'})
                            })
                            return
                        }
                        store.dispatch('GenerateRoutes', {roles}).then(accessRoutes => {
                            // 根据roles权限生成可访问的路由表
                            router.addRoutes(accessRoutes) // 动态添加可访问路由表

                            // EMI账号跳转到emi用户信息页面，而不是首页
                            if (res.user.type === 2 && (to.path === '/' || to.path === '/index' || to.path === '')) {
                                next({path: '/emi/emiUserInfo', replace: true})
                            } else {
                                next({...to, replace: true}) // hack方法 确保addRoutes已完成
                            }
                        })
                    })
                    .catch(err => {
                        store.dispatch('LogOut').then(() => {
                            Message.error(err)
                            next({path: '/'})
                        })
                    })
            } else {
                // 已登录用户，如果是EMI账号且访问首页，则重定向到EMI用户信息页面
                if (store.getters.userType === 2 && (to.path === '/' || to.path === '/index')) {
                    next({path: '/emi/emiUserInfo', replace: true})
                } else {
                    next()
                }
            }
        }
    } else {
        // 没有token
        if (whiteList.indexOf(to.path) !== -1) {
            // 在免登录白名单，���接进入
            next()
        } else {
            next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
            NProgress.done()
        }
    }
})

router.afterEach(() => {
    NProgress.done()
})
