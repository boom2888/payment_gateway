// src/utils/request/base.js
import axios from 'axios'
import { Notification, MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'

export default function createRequest(baseURL) {
  const service = axios.create({
    baseURL,
    timeout: 10000,
    headers: { 'Content-Type': 'application/json;charset=utf-8' }
  })

  // 请求拦截器
  service.interceptors.request.use(config => {
    const isToken = (config.headers || {}).isToken === false
    if (getToken() && !isToken) {
      config.headers['Authorization'] = 'Bearer ' + getToken()
    }
    // 参数处理略，可保留你的逻辑...
    return config
  }, error => {
    return Promise.reject(error)
  })

  // 响应拦截器
  service.interceptors.response.use(res => {
    const code = res.data.code || 200
    const msg = errorCode[code] || res.data.msg || errorCode['default']

    if (code === 401) {
      MessageBox.confirm('Login expired, relogin?', 'System Prompt', {
        confirmButtonText: 'Login again',
        type: 'warning'
      }).then(() => {
        store.dispatch('LogOut').then(() => {
          location.href = '/index'
        })
      })
    } else if (code === 500) {
      Message({ message: msg, type: 'error' })
      return Promise.reject(new Error(msg))
    } else if (code !== 200) {
      Notification.error({ title: msg })
      return Promise.reject(new Error(msg))
    } else {
      return res.data
    }
  }, error => {
    let { message } = error
    if (message == "Network Error") {
      message = "Back-end interface connection error"
    } else if (message.includes("timeout")) {
      message = "System interface request timeout"
    } else if (message.includes("Request failed with status code")) {
      message = `System interface ${message.slice(-3)} error`
    }
    Message({ message, type: 'error', duration: 5000 })
    return Promise.reject(error)
  })

  return service
}
