import axios from 'axios'
import { API_BASE_URL } from '~/config'

function apiInstance() {
  const instance = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-type': 'application/json',
    },
  })

  return instance
}
function multiInstance() {
  const instance = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-type': 'multipart/form-data',
    },
  })
  return instance
}

export { apiInstance, multiInstance }
