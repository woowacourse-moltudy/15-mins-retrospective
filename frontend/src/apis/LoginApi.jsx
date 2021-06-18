import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_URL

export const login = async (name) => {
  return axios({
    method: 'post',
    url: `${BASE_URL}/login`,
    data: {
      name: name
    }
  })
}

export const getMember = async (token) => {
  return axios({
    method: 'get',
    url: `${BASE_URL}/member`,
    headers: {
      Authorization: `Bearer ${token}`
    }
  })
}