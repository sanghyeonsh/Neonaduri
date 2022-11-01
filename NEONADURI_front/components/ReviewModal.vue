<template>
  <div class="check-modal">
    <div id="check-modal-box" class="check-modal-box slide-top">
      <div class="check-modal-head">
        <div></div>
        <div class="check-modal-head-check">
          <img
            class="head-logo"
            style="width: 40%"
            src="/logo/write-review-logo.png"
            alt=""
          />
        </div>
        <div class="check-modal-head-close" @click="CloseCheck">
          <v-icon style="margin-right: 40%" large>mdi-close</v-icon>
        </div>
      </div>
      <div class="check-modal-body">
        <div class="check-model-body-top">
          <div class="check-model-body-left">
            <img
              :src="preview === null ? '/banner/no-img.png' : preview"
              class="check-modal-body-img"
              alt="없음"
            />
            <v-file-input
              v-model="reviewForm.reviewImage"
              :placeholder="fileInfo?.name"
              style="margin: 0 15% 0 0"
              @change="previewFile(reviewForm.reviewImage)"
            />
          </div>

          <div class="check-modal-body-input">
            <div class="input-name">한줄평</div>
            <v-text-field
              v-model="reviewForm.reviewContent"
              class="check-modal-body-input-line"
              style="padding: 0px flex:none"
              type="text"
            />
            <div class="input-name">태그</div>
            <div class="check-modal-body-input-tag">
              <!-- <tagify-component></tagify-component> -->
              <Tags
                id="tag-input"
                ref="myRef"
                :settings="tagifyStuff.tagifySettings"
                :suggestions="tagifyStuff.suggestions"
                :value="tagifyStuff.value"
                :on-change="onTagsChange"
              />
            </div>
            <div class="check-model-body-bot-pass">
              <div class="input-name">비밀번호 설정</div>
              <v-text-field
                v-model="reviewForm.reviewPassword"
                class="password-input"
                style="padding: 0px"
                type="password"
              />
            </div>
            <!-- <input
              v-model="review.tag"
              class="check-modal-body-input-tag"
              type="text"
              placeholder="태그를 입력하세요"
            /> -->
          </div>
        </div>
        <div class="check-model-body-bot">
          <div class="check-model-body-bot-left">
            <!-- <v-file-input
              v-model="file"
              text="fileInfo?.name"
              @change="previewFile(file)"
            /> -->
          </div>
          <div class="check-model-body-bot-right">
            <v-btn
              class="mx-2"
              fab
              dark
              large
              color="cyan"
              @click="writeReview"
            >
              <v-icon dark> mdi-pencil </v-icon>
            </v-btn>
          </div>
        </div>
      </div>
    </div>
    <b-modal id="create-modal" size="md" hide-footer>
      <template #modal-title> 안내 </template>
      <div class="d-block text-center">
        <div>이미지를 등록해주세요!</div>
      </div>
      <div style="display: flex; justify-content: flex-end">
        <b-button
          class="mt-3"
          variant="danger"
          @click="$bvModal.hide('create-modal')"
          >닫기</b-button
        >
      </div>
    </b-modal>
    <b-modal id="pass-modal" size="md" hide-footer>
      <template #modal-title> 안내 </template>
      <div class="d-block text-center">
        <div>랜덤으로 비밀번호를 생성했습니다!</div>
        <input
          id="copy-text"
          v-model="reviewForm.reviewPassword"
          style="width: 30%"
          readonly
        />
        <v-icon @click="copyToClipboard">mdi-content-copy</v-icon>
      </div>
      <div style="display: flex; justify-content: flex-end">
        <b-button
          class="mt-3"
          variant="danger"
          @click="$bvModal.hide('pass-modal')"
          >닫기</b-button
        >
      </div>
    </b-modal>
  </div>
</template>

<script>
import { mapState, mapActions, mapMutations } from 'vuex'
import { ref, reactive } from 'vue'
// import Tags from "@yaireo/tagify/dist/tagify.vue"
import Tags from '@/components/TagifyInput'

export default {
  name: 'CheckModal',
  components: { Tags },
  props: {
    value: Boolean,
  },
  setup(props) {
    const myRef = ref(null)

    const tagifyStuff = reactive({
      value: '',

      tagifySettings: {
        whitelist: [],
        dropdown: {
          enabled: 0,
        },
        callbacks: {
          add(e) {
            // console.log("tag added:", e.detail);
          },
        },
      },

      suggestions: [],
    })

    function onTagsChange(e) {
      //   console.log('tags changed:', e.target.value)
    }

    return {
      onTagsChange,
      myRef,
      tagifyStuff,
    }
  },
  data() {
    return {
      pvalue: this.value,
      preview: null,
      // preview: '/banner/no-image1.png',
      // preview: '/banner/no-image2.png',
      fileInfo: null,
      // 리뷰 post
      reviewForm: {
        spotId: null,
        reviewImage: null,
        reviewContent: '',
        reviewPassword: null,
        tags: '',
      },
      flag: false,
    }
  },
  computed: {
    ...mapState('spot', ['spot']),
    ...mapState('review', ['review', 'reviewList']),
  },

  methods: {
    ...mapActions('review', ['registReview', 'callReviews']),
    ...mapMutations('review', ['PLUS_REVIEWLIST']),
    CloseCheck() {
      // document.getElementById('check-modal-box').className += 'slide-out-bottom'
      this.$emit('updateStatus', !this.pvalue)
      const modal = document.getElementsByClassName('check-modal')[0]
      const span = document.getElementsByClassName('check-modal-head-close')[0]
      span.onclick = function () {
        modal.style.display = 'none'
      }
      window.onclick = function (event) {
        if (event.target === modal) {
          modal.style.display = 'none'
        }
      }
    },
    previewFile(file) {
      if (file) {
        const fileData = (data) => {
          this.preview = data
        }
        this.fileInfo = file
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.addEventListener(
          'load',
          function () {
            fileData(reader.result)
          },
          false
        )
      } else if (file === null) {
        this.fileInfo = null
        this.preview = null
      } else {
        file = this.fileInfo
        const fileData = (data) => {
          this.preview = data
        }
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.addEventListener(
          'load',
          function () {
            fileData(reader.result)
          },
          false
        )
      }
    },
    async writeReview() {
      const reviewData = new FormData()
      this.reviewForm.tags = ''
      const arr = this.$el
        .querySelector(`#tag-input`)
        .value.split('"},{"value":"')
      arr[0] = arr[0].substr(11, arr[0].length - 11)
      arr[arr.length - 1] = arr[arr.length - 1].substr(
        0,
        arr[arr.length - 1].length - 3
      )

      arr.forEach((element) => {
        const word = element.replace('#', '')
        if (word !== '') {
          this.reviewForm.tags += word
          this.reviewForm.tags += ', '
        }
      })
      this.reviewForm.tags = this.reviewForm.tags.substr(
        0,
        this.reviewForm.tags.length - 2
      )
      if (this.reviewForm.reviewImage === null) {
        this.$bvModal.show('create-modal')
        return
      }
      if (this.reviewForm.reviewPassword === null) {
        this.flag = true
        this.reviewForm.reviewPassword = Math.random().toString(36).slice(2)
      }
      if (this.flag) {
        this.$bvModal.show('pass-modal')
        this.flag = false
        return
      }

      reviewData.append('spotId', this.spot.spotId)
      // this.reviewForm.reviewImage.name = 'RV' + this.reviewList.length
      reviewData.append('reviewImage', this.reviewForm.reviewImage)
      reviewData.append('reviewContent', this.reviewForm.reviewContent)
      reviewData.append('reviewPassword', this.reviewForm.reviewPassword)
      reviewData.append('tags', this.reviewForm.tags)

      await this.registReview(reviewData)
      console.log(this.reviewList)
      await this.callReviews(this.spot.spotId)
      this.$emit('updateStatus', !this.pvalue)
      this.$emit('refresh')
    },
    copyToClipboard() {
      const copyText = document.getElementById('copy-text')
      copyText.select()
      document.execCommand('copy')
    },
  },
}
</script>

<style scoped>
@-webkit-keyframes slide-top {
  0% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
  }
  100% {
    -webkit-transform: translateY(-100px);
    transform: translateY(-100px);
  }
}
@keyframes slide-top {
  0% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
  }
  100% {
    -webkit-transform: translateY(-100px);
    transform: translateY(-100px);
  }
}
@-webkit-keyframes slide-out-bottom {
  0% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
    opacity: 1;
  }
  100% {
    -webkit-transform: translateY(1000px);
    transform: translateY(1000px);
    opacity: 0;
  }
}
@keyframes slide-out-bottom {
  0% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
    opacity: 1;
  }
  100% {
    -webkit-transform: translateY(1000px);
    transform: translateY(1000px);
    opacity: 0;
  }
}
.slide-top {
  -webkit-animation: slide-top 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  animation: slide-top 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}
.slide-out-bottom {
  -webkit-animation: slide-out-bottom 0.5s cubic-bezier(0.55, 0.085, 0.68, 0.53)
    both;
  animation: slide-out-bottom 0.5s cubic-bezier(0.55, 0.085, 0.68, 0.53) both;
}
input {
  padding-left: 3%;
}
.check-modal {
  /* display: none; */
  position: fixed;
  z-index: 1;
  padding-top: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100vw;
  height: 100vh;
  left: 0;
  top: 0;
  /* overflow: auto; */
  font-size: 17px;
  background-color: rgba(0, 0, 0, 0.5);
}

/* Modal Content/Box */
.check-modal-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: white;
  width: 70%;
  margin: 20vh auto;
  border: 2px solid black;
  border-radius: 15px;
}

.check-modal-head {
  width: 100%;
  height: 15%;
  display: flex;
  justify-content: space-between;
  margin-top: 2%;
}
.check-modal-head-check {
  width: 90%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.head-logo {
  width: 50%;
}
.check-modal-head-close {
  width: 5%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}
.check-modal-head-close img {
  width: 50%;
  height: 50%;
}

.check-modal-body {
  width: 100%;
  height: 85%;
  display: flex;
  flex-direction: column;
  background-color: white;
  border-radius: 15px;
}

.check-modal-body-head {
  display: flex;
  width: 100%;
  height: 10%;
  border-top: solid;
  border-bottom: solid;
  background-color: #dadada;
}
.check-model-body-top {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  margin: 5% 0;
}
/* .check-model-body-left {
  position: fixed;
  width: 70%;
} */
.check-model-body-left {
  width: 50%;
}
.check-modal-body-img {
  width: 400px;
  height: 60%;
  max-height: 400px;
  margin: 2% 2% 0 2%;
}
.check-modal-body-input {
  width: 50%;
  /* position: fixed;
  right: 0; */
  margin: 4% 4% 4% 0;
  display: flex;
  flex-direction: column;
}
.check-modal-body-input > input {
  margin-bottom: 10%;
  border: 2px solid #a1d6e9;
  border-radius: 10px;
}
.check-modal-body-input-tag {
  font-size: 15px;
}
.check-model-body-bot {
  height: 30%;
  display: flex;
}
.check-model-body-bot-left {
  width: 40%;
  margin: 0px 4% 0px 0px;
}
.check-model-body-bot-right {
  position: relative;
  width: 16%;
  margin-left: 40%;
  display: flex;
  justify-content: flex-end;
}
.check-model-body-bot-pass {
  /* width: 60%; */
  font-size: 15px;
  margin-top: auto;
  margin-right: 30%;
}

.check-model-body-bot-pass > input {
  width: 50%;
  border: 2px solid #a1d6e9;
  border-radius: 10px;
  margin: auto 0 5% 5%;
}
.password-input {
  font-family: '맑은고딕', '돋움';
}
.theme--dark.v-btn.v-btn--has-bg {
  background-color: rgba(5, 203, 203, 0.992);
  position: fixed;
  bottom: 6%;
  right: 3%;
}
</style>
