import { apiInstance } from '.'

const api = apiInstance()

async function getSidos(success, fail) {
  await api.get(`/api/region/sido`).then(success).catch(fail)
}

async function getSigungus(sido, success, fail) {
  await api.get(`/api/region/sigungu/${sido}`).then(success).catch(fail)
}

async function getMyeon(location, success, fail) {
  await api
    .get(`/api/region/myeon/${location.sido}/${location.sigungu}`)
    .then(success)
    .catch(fail)
}

export { getSidos, getSigungus, getMyeon }
