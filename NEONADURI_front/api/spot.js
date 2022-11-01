import { apiInstance } from '.'

const api = apiInstance()

// 검색 옵션에 따른 관광지들을 가져옴
// async function getSpotList(condition, success, fail) {
//   await api.get(`/api/spot/search/~~~`).then(success).catch(fail)
// }

// 특정 관광지에 대한 정보와 리뷰를 가져옴
async function getSpot(spotId, success, fail) {
  await api.get(`/api/spot/${spotId}`).then(success).catch(fail)
}

async function modifyContent(spotId, content, success, fail) {
  await api.put(`/api/spot/${spotId}/${content}`).then(success).catch(fail)
}

async function search(query, success, fail) {
  await api.get(`/api/spot/search?${query}`).then(success).catch(fail)
}

async function recommend(success, fail) {
  await api.get(`/api/recommendation`).then(success).catch(fail)
}

export { getSpot, modifyContent, search, recommend }
