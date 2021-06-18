import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_URL

export const getTimes = async (token) => {
  return axios({
    method: 'get',
      url: `${BASE_URL}/time`,
      headers: {
        Authorization: `Bearer ${token}`
      }
  })
}

export const getMembers = async (timeId, token) => {
  return axios({
    method: 'get',
    url: `${BASE_URL}/time/${timeId}`,
    headers: {
      Authorization: `Bearer ${token}`
    }
  })
}

export const getPairs = async (timeId, date, token) => {
  return axios({
    method: 'get',
    url: `${BASE_URL}/pairs?date=${date}&conferenceTimeId=${timeId}`,
    headers: {
      Authorization: `Bearer ${token}`
    }
  })
}

export const addInMembers = (timeId, memberId, token) => {
  return axios({
    method: 'post',
    url: `${BASE_URL}/time`,
    data: {
      conferenceTimeId: timeId,
      memberId: memberId
    },
    headers: {
      Authorization: `Bearer ${token}`
    }
  })
}

export const deleteInMembers = (timeId, memberId, token) => {
  return axios({
    method: 'delete',
    url: `${BASE_URL}/time`,
    data: {
      conferenceTimeId: timeId,
      memberId: memberId
    },
    headers: {
      Authorization: `Bearer ${token}`
    }
  })
}