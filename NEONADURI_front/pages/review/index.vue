<template>
  <div class="review">
    <div class="main-content">
      <div class="review-top">
        <img class="review-logo" src="/logo/review-detail-logo.png" alt="" />
      </div>
      <div class="spot-main slide-in-right">
        <div class="spot-left">
          <img
            :src="
              'https://neonaduri.s3.ap-northeast-2.amazonaws.com/' +
              spot.spotImage
            "
            alt=""
          />
        </div>
        <div class="spot-right">
          <div class="spot-main-title">
            <div class="title-text">
              {{ spot.spotName }}
            </div>
          </div>

          <div class="content-modify" style="display: none">
            <div class="content-modify-sub">
              <textarea :value="spot.spotContent" class="modify-input" />
              <v-btn
                class="modify-button"
                style="background-color: lightgreen; color: black"
                @click="contentSave"
                >저장</v-btn
              >
            </div>
          </div>
          <div class="spot-content-box">
            <div class="spot-content">
              <div>
                {{ spot.spotContent }}
              </div>
            </div>
            <div class="content-btns">
              <div class="title-icon" style="z-index: 10">
                <v-btn
                  class="mx-2"
                  fab
                  dark
                  large
                  style="background-color: rgba(5, 203, 203, 0.992)"
                  @click="modifyContent"
                >
                  <v-icon dark> mdi-pencil </v-icon>
                </v-btn>
              </div>
              <div class="spot-add">
                <!-- <button class="add-btn" @click="addSpot">경유지추가</button> -->
                <v-btn x-large class="review-btns ml-3" @click="addSpot">
                  경유지추가
                </v-btn>
              </div>
            </div>
          </div>
        </div>
        <div class="spot-routeList">
          <draggable v-model="stopOverList">
            <transition-group>
              <div
                v-for="(stopOver, i) in stopOverList"
                :key="i"
                class="spot-route"
              >
                {{ stopOver.name }}
                <v-icon class="mb-1" @click="deleteStopOver(i)"
                  >mdi-delete</v-icon
                >
              </div>
            </transition-group>
          </draggable>
          <div class="spot-route-search">
            <v-btn class="review-btns" @click="routeSearch">경로찾기</v-btn>
          </div>
        </div>
      </div>
      <div class="spot-review">
        <div class="spot-review-top">
          <div class="spot-review-title">후기</div>
          <div>
            <review-modal
              v-if="inputToggle"
              :value="inputToggle"
              @updateStatus="changeInput"
              @refresh="forceRerender"
            />
            <modify-modal
              v-if="modifyToggle"
              :value="modifyToggle"
              @updateStatus="changeModify"
            />
            <v-icon large style="cursor: pointer" @click="MoveCheck"
              >mdi-plus-box</v-icon
            >
          </div>
        </div>
        <div class="spot-review-content">
          <div class="left-button">
            <v-icon x-large @click="countDown">mdi-chevron-left</v-icon>
          </div>
          <div
            v-for="(review, i) in reviewList.slice(count, count + 3)"
            :key="i"
          >
            <v-card class="mx-auto my-12" max-width="374">
              <template slot="progress">
                <v-progress-linear
                  color="deep-purple"
                  height="10"
                  indeterminate
                ></v-progress-linear>
              </template>
              <v-img
                height="250"
                :src="
                  'https://neonaduri.s3.ap-northeast-2.amazonaws.com/' +
                  review.reviewImage
                "
              ></v-img>
              <v-card-title>{{ review.reviewContent }}</v-card-title>
              <v-divider class="mx-4"></v-divider>
              <v-card-text>
                <v-chip-group
                  active-class="deep-purple accent-4 white--text"
                  column
                >
                  <div class="tag-box">
                    <div
                      v-for="(tag, index) in reviewList[count + i].tagContents"
                      :key="index"
                      class="tags"
                    >
                      #{{ tag }}
                    </div>
                  </div>
                </v-chip-group>
              </v-card-text>

              <v-card-actions style="justify-content: flex-end">
                <div class="password-main">
                  <div class="password-content">
                    <div class="password-container" tabindex="1">
                      <div class="search-container" tabindex="1">
                        <input
                          :class="
                            `password-input password-input` + review.reviewId
                          "
                          placeholder="password"
                          type="password"
                        />
                        <a class="password-button">
                          <v-icon
                            :id="'key-icon' + (count + i)"
                            @click="enterPass(review, count + i)"
                            >mdi-key</v-icon
                          >
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              </v-card-actions>
            </v-card>
          </div>
          <div class="right-button">
            <v-icon x-large @click="countUp">mdi-chevron-right</v-icon>
          </div>
        </div>
      </div>
    </div>
    <navbar-component> </navbar-component>
  </div>
</template>

<script>
import { mapActions, mapState, mapMutations } from 'vuex'
import draggable from 'vuedraggable'
import ModifyModal from '~/components/ModifyModal.vue'

export default {
  components: { ModifyModal, draggable },
  data() {
    return {
      inputToggle: false,
      modifyToggle: false,
      stopOverList: [],
      reviewForm: [],
      count: 0,
    }
  },
  computed: {
    ...mapState('spot', ['spot']),
    ...mapState('review', ['reviewList']),
    ...mapState('route', ['routeList']),
  },
  watch: {
    stopOverList() {
      this.CHANGE_ROUTE(this.stopOverList)
    },
  },
  created() {
    // 불러올 때 review_id도 불러옴
    this.callReviews(this.spot.spotId)
    this.stopOverList = JSON.parse(JSON.stringify(this.routeList))
  },

  mounted() {},
  methods: {
    ...mapMutations('route', ['ADD_ROUTE', 'DELETE_ROUTE', 'CHANGE_ROUTE']),
    ...mapMutations('review', ['CLEAR_REVIEW', 'SET_REVIEW']),
    ...mapActions('spot', ['changeContent']),
    ...mapActions('review', ['callReviews', 'confirmPass']),
    MoveCheck() {
      this.inputToggle = !this.inputToggle
    },
    changeInput() {
      this.inputToggle = !this.inputToggle
    },
    changeModify() {
      this.modifyToggle = !this.modifyToggle
    },

    modifyContent() {
      this.$el.querySelector(`.content-modify`).style.display = 'block'
      this.$el.querySelector(`.spot-content`).style.display = 'none'
      this.$el.querySelector(`.modify-input`).focus()
    },
    contentSave() {
      this.$el.querySelector(`.spot-content`).innerHTML =
        this.$el.querySelector(`.modify-input`).value
      this.$el.querySelector(`.spot-content`).style.display = 'block'
      this.$el.querySelector(`.content-modify`).style.display = 'none'
      this.changeContent(this.$el.querySelector(`.modify-input`).value)
    },
    // review 불러오면 idx -> review_id로 바꾸기
    async enterPass(review, index) {
      // review_id와 review_password받아오기
      const info = {
        idx: index,
        id: review.reviewId,
        password: await this.$el.querySelector(
          `.password-input${review.reviewId}`
        ).value,
      }
      await this.confirmPass(info)
      if (this.reviewList[index].pass) {
        this.modifyToggle = !this.modifyToggle
        this.CLEAR_REVIEW()
        this.SET_REVIEW(review)
      }
    },
    addSpot() {
      const route = {
        id: this.spot.spotId,
        name: this.spot.spotName,
        lat: this.spot.lat,
        lng: this.spot.lng,
      }
      let flag = false
      this.routeList.forEach((element) => {
        if (element.id === route.id) {
          flag = true
        }
      })
      if (!flag) {
        this.ADD_ROUTE(route)
      }
    },
    deleteStopOver(index) {
      this.DELETE_ROUTE(index)
      // 지울 부분을 리스트에서 제거
    },
    routeSearch() {
      this.$router.push('/findRoute')
    },
    spotSearch() {
      this.$router.push('/search/searchResult')
    },
    forceRerender() {
      this.$forceUpdate()
    },
    countDown() {
      document.getElementsByClassName(`password-input`)[0].value = ''
      document.getElementsByClassName(`password-input`)[1].value = ''
      document.getElementsByClassName(`password-input`)[2].value = ''
      this.count = Math.max(this.count - 1, 0)
    },
    countUp() {
      document.getElementsByClassName(`password-input`)[0].value = ''
      document.getElementsByClassName(`password-input`)[1].value = ''
      document.getElementsByClassName(`password-input`)[2].value = ''
      this.count = Math.min(this.count + 1, this.reviewList.length - 3)
    },
  },
}
</script>

<style scoped>
.slide-in-right {
  -webkit-animation: slide-in-right 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94)
    both;
  animation: slide-in-right 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}
.slide-out-top {
  -webkit-animation: slide-out-top 0.5s cubic-bezier(0.55, 0.085, 0.68, 0.53)
    both;
  animation: slide-out-top 0.5s cubic-bezier(0.55, 0.085, 0.68, 0.53) both;
}
@-webkit-keyframes slide-in-right {
  0% {
    -webkit-transform: translateX(1000px);
    transform: translateX(1000px);
    opacity: 0;
  }
  100% {
    -webkit-transform: translateX(0);
    transform: translateX(0);
    opacity: 1;
  }
}
@keyframes slide-in-right {
  0% {
    -webkit-transform: translateX(1000px);
    transform: translateX(1000px);
    opacity: 0;
  }
  100% {
    -webkit-transform: translateX(0);
    transform: translateX(0);
    opacity: 1;
  }
}
@-webkit-keyframes slide-out-top {
  0% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
    opacity: 1;
  }
  100% {
    -webkit-transform: translateY(-1000px);
    transform: translateY(-1000px);
    opacity: 0;
  }
}
@keyframes slide-out-top {
  0% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
    opacity: 1;
  }
  100% {
    -webkit-transform: translateY(-1000px);
    transform: translateY(-1000px);
    opacity: 0;
  }
}
@font-face {
  font-family: 'GmarketSansMedium';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff')
    format('woff');
  font-weight: normal;
  font-style: normal;
}

@font-face {
  font-family: 'SEBANG_Gothic_Bold';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2104@1.0/SEBANG_Gothic_Bold.woff')
    format('woff');
  font-weight: normal;
  font-style: normal;
}

.review {
  font-family: 'GmarketSansMedium';
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: space-between;
  overflow: auto;
}
.main-content {
  width: 100%;
}
.review-top {
  width: 100%;
  height: 20%;
  display: flex;
  justify-content: center;
}
.review-logo {
  width: 30%;
  height: 100%;
  margin: 0 auto;
}
.spot-main {
  position: relative;
  height: 60%;
  background-image: url('/banner/ticket-background.png');
  background-position-y: 0%;
  background-position-x: 20px;
  background-repeat: no-repeat;
  background-size: 100% 100%;
}
.spot-left {
  position: absolute;
  /* text-align: center; */
  /* margin: 40px 0; */
  left: 10%;
  top: 7%;
  bottom: inherit;
}
.review-btns {
  background-color: #96c8f4 !important;
}
.spot-left > img {
  width: 40.2vw;
  height: 51.6vh;
}
.spot-right {
  position: absolute;
  display: flex;
  flex-direction: column;
  width: 40%;
  height: 70%;
  max-height: 100%;
  left: 50%;
  top: 13%;
  margin-left: 5%;
}
.spot-routeList {
  position: absolute;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  height: 55%;
  top: 8%;
  right: 1.3%;
}
.spot-route-search {
  height: 7%;
  display: flex;
  justify-content: flex-end;
  margin: 15px 0 0 15px;
  padding: 5px;
  border-radius: 5px;
  color: white;
}
.spot-route {
  margin-bottom: 3%;
  cursor: pointer;
}
.spot-main-title {
  width: 90%;
  height: 20%;
  display: flex;
  font-size: 35px;
  font-weight: bold;
  margin-bottom: 20px;
  font-family: 'SEBANG_Gothic_Bold';
}
.title-icon {
  margin-left: 1%;
  cursor: pointer;
}
.spot-content {
  width: 70%;
  height: 60%;
}
.content-btns {
  display: flex;
}
.content-modify-sub {
  display: flex;
}
.modify-input {
  width: 55%;
  margin-bottom: 40px;
}
.modify-button {
  height: 5%;
  margin: 15px 0 0 15px;
  padding: 5px;
  background-color: #81c147;
  border-radius: 5px;
  color: white;
}
.spot-add {
  display: flex;
  justify-content: flex-start;
  align-content: flex-start;
  margin-right: 10%;
}
.add-btn {
  padding: 10px;
  background-color: #81c147;
  border-radius: 5px;
  color: white;
}
.spot-review {
  width: 85%;
  margin: auto;
  /* height: 30%; */
}
.spot-review-top {
  display: flex;
  align-items: center;
  margin-left: 4%;
}
.spot-review-title {
  font-size: 25px;
  font-weight: bold;
  margin: 1% 1% 0.5% 1%;
}
.left-button {
  display: flex;
  align-items: center;
}
.right-button {
  display: flex;
  align-items: center;
}
.left-button > .mdi[data-v-30921a58]:before {
  font-size: 50px;
}
.right-button > .mdi[data-v-30921a58]:before {
  font-size: 50px;
}
.spot-review-content {
  display: flex;
  justify-content: space-between;
}
.tag-box {
  width: 100%;
  height: 20%;
  display: flex;
}
.tags {
  margin-right: 3%;
}
.password-main {
  display: flex;
  align-items: flex-end;
}
.password-input[type='password'] {
  font-family: '맑은고딕', '돋움';
}
.password-input[type='password']::placeholder {
  font-family: 'Cafe24Ssurround';
}
.password-button {
  display: flex;
  align-items: center;
  justify-content: center;
  float: right;
  width: 1em;
  height: 1em;
  margin: 0.125em;
  /* background: #6a5d4f; */
  text-align: center;
  font-size: 2em;
  color: white;

  border-radius: 50%;
  cursor: pointer;
  /* box-shadow: 0 -1px 1px rgba(255, 255, 255, 0.25),
    0 1px 1px rgba(0, 0, 0, 0.25); */
  /* text-shadow: 0 -2px 1px rgba(0, 0, 0, 0.3); */
}
.search-container {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.search-container > input {
  width: 40%;
}
.Pass {
  display: block;
}
.Enter {
  display: block;
}
.v-btn--fab.v-size--large {
  width: 48px;
  height: 48px;
}
.mdi:before {
  font-size: 24px;
}
</style>
