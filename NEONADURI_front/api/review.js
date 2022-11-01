import { apiInstance, multiInstance } from '.'

const api = apiInstance()
const multiApi = multiInstance()

async function getReviews(spotId, success, fail) {
  await api.get(`/api/spot/${spotId}`).then(success).catch(fail)
}

async function writeReview(review, success, fail) {
  await multiApi.post(`/api/review`, review).then(success).catch(fail)
}

async function deleteReview(reviewId, success, fail) {
  await api.delete(`/api/review/${reviewId}`).then(success).catch(fail)
}

async function modifyReview(review, success, fail) {
  await multiApi.put(`/api/review`, review).then(success).catch(fail)
}

async function comparePass(reviewId, pass, success, fail) {
  await api
    .get(`/api/review/pass/${reviewId}/${pass}`)
    .then(success)
    .catch(fail)
}

async function getReview(reviewId, success, fail) {
  await api.get(`/api/review/${reviewId}`).then(success).catch(fail)
}

export {
  writeReview,
  getReviews,
  deleteReview,
  modifyReview,
  comparePass,
  getReview,
}
