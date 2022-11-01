import { getSat, getVisited, getSel } from '~/api/statistics'
import koreaMap from '~/static/map/koreaMap.json'

export const state = () => ({
  words: [],
  satList: [],
  regionList: [],
  koreaMap,
  introData: {},
})

export const mutations = {
  SET_SAT(state, payload) {
    state.satList = payload
  },
  SET_SEL(state, payload) {
    state.words = []
    payload.forEach((element) => {
      const arr = []

      arr.push(element.selReason)
      arr.push(element.selPercent)

      state.words.push(arr)
    })
  },
  SET_VISITED(state, payload) {
    state.regionList = []

    state.introData.maxValue = 0
    state.introData.minValue = payload[0].visitedNum

    payload.forEach((element) => {
      const obj = {}

      obj.name = element.visitedRegion
      obj.value = element.visitedNum

      state.regionList.push(obj)
      // ///////////////////////
      if (state.introData.maxValue < obj.value) {
        state.introData.maxValue = obj.value
      }

      if (state.introData.minValue > obj.value) {
        state.introData.minValue = obj.value
      }
    })
  },
}

export const getters = {
  priceList(state) {
    const priceList = state.satList.filter((element) => {
      return element.satType === '0'
    })
    const result = []
    priceList.forEach((element) => {
      result.push(element.satScore)
    })

    return result
  },
  foodList(state) {
    const foodList = state.satList.filter((element) => {
      return element.satType === '1'
    })
    const result = []
    foodList.forEach((element) => {
      result.push(element.satScore)
    })

    return result
  },
  natureList(state) {
    const natureList = state.satList.filter((element) => {
      return element.satType === '2'
    })
    const result = []
    natureList.forEach((element) => {
      result.push(element.satScore)
    })

    return result
  },
}

export const actions = {
  async callSatList({ commit }) {
    await getSat(
      ({ data }) => {
        commit('SET_SAT', data)
      },
      (error) => {
        console.log(error)
      }
    )
  },
  async callSelList({ commit }) {
    await getSel(
      ({ data }) => {
        commit('SET_SEL', data)
      },
      (error) => {
        console.log(error)
      }
    )
  },
  async callVisitedList({ commit }) {
    await getVisited(
      ({ data }) => {
        commit('SET_VISITED', data)
      },
      (error) => {
        console.log(error)
      }
    )
  },
}
