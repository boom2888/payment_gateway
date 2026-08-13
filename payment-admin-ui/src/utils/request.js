import store from '@/store'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { getLangCode } from '@/utils/langUtils'
import { getClientTimezone } from '@/utils/timezoneUtils'
import axios from 'axios'
import { Message, MessageBox, Notification } from 'element-ui'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超时
  timeout: 10000
})
// request拦截器
service.interceptors.request.use(
  config => {
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false
    if (getToken() && !isToken) {
      config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    // 设置语言头部
    config.headers['lang'] = getLangCode()
    // 设置时区头部
    config.headers['timezone'] = getClientTimezone()
    // get请求映射params参数
    if (config.method === 'get' && config.params) {
      let url = config.url + '?'
      for (const propName of Object.keys(config.params)) {
        const value = config.params[propName]
        var part = encodeURIComponent(propName) + '='
        if (value !== null && typeof value !== 'undefined' && value !== '') {
          if (typeof value === 'object') {
            for (const key of Object.keys(value)) {
              const params = propName + '[' + key + ']'
              var subPart = encodeURIComponent(params) + '='
              url += subPart + encodeURIComponent(value[key]) + '&'
            }
          } else {
            url += part + encodeURIComponent(value) + '&'
          }
        }
      }
      url = url.slice(0, -1)
      config.params = {}
      config.url = url
      console.info(url)
    }
    // 正式
    // config.url = 'http://wwwmad.xmublockchain.com/adminapi' + config.url;
    config.url = process.env.VUE_APP_BASE_API + config.url
    // config.url = 'http://localhost:8888' + config.url;
    // config.url = 'http://13.229.184.159:8888' + config.url;
    console.info('URL====>' + config.url)
    return config
  },
  error => {
    console.log(error)
    Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  res => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200
    // 获取错误信息
    const msg = errorCode[code] || res.data.msg || errorCode['default']
    if (code === 401) {
      MessageBox.confirm('Login has expired, you can stay on this page or log in again', 'System Prompt', {
        confirmButtonText: 'Login again',
        cancelButtonText: '',
        type: 'warning'
      }).then(() => {
        store.dispatch('LogOut').then(() => {
          location.href = '/index'
        })
      })
    } else if (code === 500) {
      Message({
        message: msg,
        type: 'error'
      })
      return Promise.reject(new Error(msg))
    } else if (code !== 200) {
      Notification.error({
        title: msg
      })
      return Promise.reject('error')
    } else {
      return res.data
    }
  },
  error => {
    console.log('err' + error)
    let { message } = error
    if (message == 'Network Error') {
      message = 'Back-end interface connection error'
    } else if (message.includes('timeout')) {
      message = 'System interface request timeout'
    } else if (message.includes('Request failed with status code')) {
      message = 'System interface ' + message.substr(message.length - 3) + 'Error'
    }
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
