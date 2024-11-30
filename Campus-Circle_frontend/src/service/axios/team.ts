/*
import axios from '../index'

export function addTeam(postData: any) {
  return axios({
    url: '/team/add',
    method: 'POST',
    data: postData  // 直接发送 postData
  })
    .then((response) => {
      console.log('添加队伍返回的数据', response)
      return response // 返回响应数据以便后续处理
    })
    .catch((error) => {
      console.error('/user/search/tags', error)
      throw error // 重新抛出错误以便外部处理
    })
}

export function getTeamList(searchText = '', status = 0) {
  return axios({
    url: '/team/list',
    method: 'GET',
    params: {
      searchText,
      pageNum: 1,
      status
    },
  }).then((response) => {
    console.log('队伍列表', response.data)
    return response // 返回响应数据以便后续处理
  })
    .catch((error) => {
      console.error('/user/search/tags', error)
      throw error // 重新抛出错误以便外部处理
    })
}

export function joinTeam(joinTeamId: any, password: any) {
  return axios({
    url: '/team/join',
    method: 'POST',
    data: {
      teamId: joinTeamId,
      password: password
    }
  })
    .then((response) => {
      console.log('加入队伍返回的信息', response.data)
      return response // 返回响应数据以便后续处理
    })
    .catch((error) => {
      console.error('请求错误', error)
      throw error // 重新抛出错误以便外部处理
    })
}


export function MyCreateTeam(searchText = '') {
  return axios({
    url: '/team/list/my/create',
    method: 'GET',
    params: {
      searchText: searchText,
      pageNum: 1
    }
  })
    .then((response) => {
      console.log('我创建的队伍返回的信息', response.data)
      return response // 返回响应数据以便后续处理
    })
}

export function MyJoinTeam(searchText = '') {
  return axios({
    method: 'GET',
    url: '/team/list/my/join',
    params: {
      searchText: searchText,
      pageNum: 1
    }
  }).then((response) => {
    console.log('我加入的队伍返回的信息', response.data)
    return response // 返回响应数据以便后续处理
  })
}


export function QuitTeam(id: number) {
  return axios({
    method: 'POST',
    url: '/team/quit',
    data:
      {
        teamId: id
      }
  }).then((response) => {
    console.log('离开队伍返回的信息', response.data)
    return response // 返回响应数据以便后续处理
  })
}

export function DeleteTeam(id: number) {
  return axios({
    method: 'POST',
    url: '/team/delete',
    data:
      {
        teamId: id
      }
  }).then((response) => {
    console.log('删除队伍返回的信息', response.data)
    return response // 返回响应数据以便后续处理
  })
}
*/
