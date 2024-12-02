import axios from '../index'
import type { AxiosResponse } from 'axios'
import { useUserStore } from '@/stores/counter'

export function userRegister(userAccount: string, userPassword: string, checkPassword: string, email: string) {
  return axios({
    url: '/user/register', // 假设注册接口是这个路径
    method: 'POST',
    data: {
      userAccount,
      userPassword,
      checkPassword,
      email
    }
  }).then((response) => {
    return response; // 返回响应数据
  })
}

/**
 * 用户登录
 *
 * @param {string} userAccount 用户账号
 * @param {string} userPassword 用户密码
 */
export function userLogin(userAccount: string, userPassword: string): Promise<AxiosResponse> {
  return axios({
    url: '/user/login',
    method: 'POST',
    data: {
      userAccount,
      userPassword
    }
  }).then((response) => {
    return response; // 返回响应数据以便后续处理
  })
}
export function getUserInfo(id: string) {
  const token = useUserStore().CacheToken;
  return axios({
    url: '/user/getUserInfo',
    method: 'GET',
    params: {
      id
    },
    headers: {
      Authorization: `${token}`, // 设置 Authorization 头
      'Content-Type': 'application/json'
    }
  }).then((response) => {
    return response; // 返回响应数据以便后续处理
  });
}

export function updateUserInfo(id: string, userData: any) {
  const token = useUserStore().CacheToken;
  return axios({
    url: '/user/update',
    method: 'PUT',
    data: {
      id,
      ...userData // 展开 userData 以传递动态字段
    },
    headers: {
      Authorization: `${token}`, // 设置 Authorization 头
      'Content-Type': 'application/json'
    }
  }).then((response) => {
    return response; // 返回响应数据以便后续处理
  });
}


/*
// 获取用户信息
export function getAbout(tags: any) {
  return axios({
    url: '/user/search/tags',
    method: 'GET',
    params: {
      tagNameList: tags
    },
    paramsSerializer: (params) => {
      return qs.stringify(params, { indices: false })
    }
  }).then((response) => {
      console.log('/user/search/tags', response)
      return response?.data // 返回响应数据以便后续处理
    })

}

//修改用户信息
export function updateUser(userData: { id: number; [key: string]: any }) {
  if (userData.id === null) {
    alert('用户权限过期，请重新登录')
  }
  return axios({
    url: '/user/update',
    method: 'POST',
    data: {
      ...userData // 展开 userData 以传递动态字段
    }
  })
}

// 获取用户推荐列表
// todo 功能还不完善
export function getRecommendUser(): Promise<any> {
  return axios({
    url: '/user/recommend',
    method: 'GET',
    params: {
      PageCurrent: 1,
      PageSize: 8
    }
  }).then(function(response) {
    console.log('/user/recommend succeed', response)
    return response?.data.records
  })
    .catch(function(error) {
      console.error('/user/recommend error', error)
      showToast('请求失败')
    })
}


//获取推荐列表
export function getMatchUser(num: any): Promise<any> {
  return axios({
    url: '/user/match',
    method: 'GET',
    params: {
      num
    }
  }).then(function(response) {
    console.log('/user/match succeed', response)
    return response?.data
  })
    .catch(function(error) {
      console.error('/user/match error', error)
      showToast('请求失败')
    })
}*/
