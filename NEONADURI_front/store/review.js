import {
  getReviews,
  deleteReview,
  modifyReview,
  writeReview,
  comparePass,
  getReview,
} from '~/api/review'

export const state = () => ({
  reviewList: [],
  review: null,
})

export const mutations = {
  SET_REVIEWLIST(state, reviewList) {
    state.reviewList = reviewList
  },
  SET_REVIEW(state, review) {
    state.review = review
  },
  CHANGE_PASS(state, object) {
    state.reviewList[object.idx].pass = object.pass
  },
  PLUS_REVIEWLIST(state, review) {
    state.reviewList.push(review)
  },
  // SET_TAG(state, reviewList){
  //   state.reviewList.forEach((element)=>{
  //     element.tagContents = element.tagContents
  //   })
  // },
  CLEAR_REVIEWLIST(state) {
    state.reviewList = []
  },
  CLEAR_REVIEW(state) {
    state.review = null
  },
}

export const getters = {}

export const actions = {
  async callReviews({ commit }, spotId) {
    await getReviews(
      spotId,
      ({ data }) => {
        data.reviewContainsTags.forEach((element) => {
          element.pass = false
        })
        console.log(data)
        commit('SET_REVIEWLIST', data.reviewContainsTags)
      },
      (error) => {
        console.log(error)
      }
    )
  },

  async registReview({ commit }, review) {
    await writeReview(
      review,
      ({ data }) => {
        console.log('등록 성공')
      },
      (error) => {
        console.log(error)
      }
    )
  },
  async changeReview({ commit }, review) {
    await modifyReview(
      review,
      ({ data }) => {
        console.log('수정 성공')
      },
      (error) => {
        console.log(error)
      }
    )
  },
  callReview({ commit }, reviewId) {
    getReview(
      reviewId,
      ({ data }) => {
        console.log(data)
      },
      (error) => {
        console.log(error)
      }
    )
  },
  async confirmPass({ commit }, info) {
    await comparePass(
      info.id,
      info.password,
      ({ data }) => {
        const object = { pass: data, idx: info.idx }
        commit('CHANGE_PASS', object)
      },
      (error) => {
        console.log(error)
      }
    )
  },
  async removeReview({ commit }, reviewId) {
    await deleteReview(
      reviewId,
      ({ data }) => {
        console.log('삭제 성공')
      },
      (error) => {
        console.log(error)
      }
    )
  },

  // 필요한 것들
  // 1. 리뷰 등록, 수정하기
  // 2. 리뷰 수정하기 전 기본정보 불러오기
}
