<template>
  <div class="check-modal">
    <div class="check-modal-box slide-top">
      <div class="check-modal-head">
        <div></div>
        <div class="check-modal-head-check">
          <img
            class="head-logo"
            style="width: 40%"
            src="/logo/modify-review-logo.png"
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
              v-if="flag"
              :src="preview"
              class="check-modal-body-img"
              alt="없음"
            />
            <img
              v-else
              :src="
                'https://neonaduri.s3.ap-northeast-2.amazonaws.com/' +
                review.reviewImage
              "
              alt=""
              style="width: 90%"
              class="check-modal-body-img"
            />
            <v-file-input
              v-if="flag"
              v-model="reviewForm.reviewImage"
              :placeholder="fileInfo?.name"
              style="margin: 0 15% 0 0"
              @change="previewFile(reviewForm.reviewImage)"
            />
            <div v-else class="modify-box">
              <button class="modify-img" @click="modifyImg">사진 수정</button>
            </div>
          </div>
          <div class="check-modal-body-input">
            <div class="input-name">한줄평</div>

            <v-text-field
              v-model="reviewForm.reviewContent"
              class="check-modal-body-input-line"
              type="text"
              style="padding: 0px flex:none"
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
          </div>
        </div>
        <div class="check-model-body-bot">
          <div class="check-model-body-bot-left"></div>
          <div class="check-model-body-bot-right">
            <v-btn
              class="mx-5"
              fab
              dark
              large
              color="cyan"
              style="right: 8%"
              @click="modifyReview"
            >
              <v-icon dark> mdi-pencil </v-icon>
            </v-btn>
            <v-btn
              class="mx-1"
              fab
              dark
              large
              color="cyan"
              style="margin-left: 10%"
              @click="deleteReview"
            >
              <v-icon dark> mdi-delete </v-icon>
            </v-btn>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
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
      preview: '/banner/no-img.png',
      // preview: '/banner/no-image1.png',
      // preview: '/banner/no-image2.png',
      fileInfo: null,
      // 리뷰 post
      reviewForm: {
        spotId: null,
        reviewImage: null,
        reviewContent: null,
        reviewPassword: null,
        tags: '',
      },
      flag: false,
    }
  },

  computed: {
    ...mapState('spot', ['spot']),
    ...mapState('review', ['review']),
  },
  // watch: {
  //   reviewForm(newReview) {
  //     console.log(111111111111)
  //     console.log(newReview)
  //     console.log(11111111111)
  //   },
  // },

  created() {
    const promise = new Promise((resolve, reject) => {
      resolve()
    })
    promise.then(async () => {
      await this.callReview(this.review.reviewId)
      // this.reviewForm.reviewImage = this.review.reviewImage
      this.reviewForm.reviewContent = this.review.reviewContent
      const arr = []
      this.review.tagContents.forEach((element) => {
        arr.push(element)
      })
      this.tagifyStuff.value = arr
    })
  },
  methods: {
    ...mapActions('review', [
      'changeReview',
      'callReviews',
      'callReview',
      'removeReview',
    ]),
    CloseCheck() {
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
        this.preview = '/banner/no-image.png'
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
    async deleteReview() {
      await this.removeReview(this.review.reviewId)
      await this.callReviews(this.spot.spotId)
      this.$emit('updateStatus', !this.pvalue)
    },
    async modifyReview() {
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
      reviewData.append('reviewId', this.review.reviewId)
      reviewData.append('reviewImage', this.reviewForm.reviewImage)
      reviewData.append('reviewContent', this.reviewForm.reviewContent)
      reviewData.append('tags', this.reviewForm.tags)
      await this.changeReview(reviewData)
      await this.callReviews(this.spot.spotId)
      this.$emit('updateStatus', !this.pvalue)
    },
    modifyImg() {
      this.flag = true
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
.slide-top {
  -webkit-animation: slide-top 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  animation: slide-top 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
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
  /* color: white; */
  /* 15% from the top and centered */
  /* margin: 15% auto;
  padding: 15px; */
  /* width: 35%; Could be more or less, depending on screen size */
  /* height: 30%; */
  width: 70%;
  margin: 20vh auto;
  border-radius: 15px;
  border: 2px solid black;
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
  margin: 4% 4% 4% 0;
  display: flex;
  flex-direction: column;
}
.modify-box {
  display: flex;
  justify-content: flex-end;
  margin-right: 10%;
}
.modify-img {
  margin-top: 20px;
  font-size: 15px;
  background-color: #96c8f4;
  padding: 10px;
  border-radius: 5px;
  color: white;
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
.check-model-body-bot-pass > input {
  width: 50%;
  border: 2px solid #a1d6e9;
  border-radius: 10px;
  margin: auto 0 5% 5%;
}
.theme--dark.v-btn.v-btn--has-bg {
  background-color: rgba(5, 203, 203, 0.992);
  position: fixed;
  bottom: 6%;
  right: 3%;
}
</style>
