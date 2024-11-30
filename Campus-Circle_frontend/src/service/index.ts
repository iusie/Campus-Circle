import axios from 'axios'

//const isDev = process.env.NODE_ENV === 'development';

const instance = axios.create({
  baseURL:  'http://localhost:8080/api'
})

instance.defaults.withCredentials = true

instance.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    console.log('发送的请求:' + config)
    return config
  },
  function(error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)
// 添加响应拦截器
instance.interceptors.response.use(
  (response) => {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么

    //todo code不对页面跳转,待做
/*    if (response.data?.code === 40101) {
        const redirectUrl = window.location.href;
        window.location.href = `/user/login?redirect=${redirectUrl}`;
    }*/

    console.log('进入网页响应的数据:' + response.data.code)
    return response;
  },
  function(error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error)
  }
)

export default instance
