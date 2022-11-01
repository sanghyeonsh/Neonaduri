<template>
  <div class="result-page">
    <div class="result-title mb-1 pt-1">
      <div v-if="keyword != ''" class="title-text">
        '{{ keyword }}'의 검색 결과입니다.
      </div>
      <div v-if="keyword == ''" class="title-text">검색 결과입니다.</div>
    </div>
    <div id="main" class="search-container">
      <div class="search-result" @wheel="handleScroll">
        <div class="container">
          <div class="slider">
            <div
              v-for="(spot, index) in spotList.content"
              :key="index"
              class="slide"
              @click="slideClick(spot)"
            >
              <div class="spot-image-container">
                <img
                  :src="
                    'https://neonaduri.s3.ap-northeast-2.amazonaws.com/' +
                    spot.spotImage
                  "
                  class="spot-image"
                  alt="spotImage"
                />
              </div>
              <div class="ml-4">
                <div class="spot-name">{{ spot.spotName }}</div>
                <div class="spot-content">
                  {{
                    spot.spotContent
                      ? spot.spotContent
                      : '직접 내용을 채워주세요! ^0^'
                  }}
                </div>
              </div>
            </div>
            <!-- <div class="slide" style="background-color: green"></div>
            <div class="slide" style="background-color: red"></div>
            <div class="slide" style="background-color: orange"></div>
            <div
              class="slide slide--active"
              style="background-color: blue"
            ></div>
            <div class="slide" style="background-color: violet"></div>
            <div class="slide" style="background-color: black"></div>
            <div class="slide" style="background-color: pink"></div>
            <div class="slide" style="background-color: teal"></div> -->
          </div>
        </div>
      </div>
    </div>
    <navbar-component></navbar-component>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from 'vuex'
export default {
  data() {
    return {
      timer: 0,
      scrollCount: 0,
    }
  },
  computed: {
    ...mapState('spot', ['keyword', 'spotList', 'query', 'page']),
  },
  async mounted() {
    this.SET_QUERY(this.query.replace(`page=${this.page}`, `page=0`))
    this.CLEAR_PAGE()
    await this.searchSpot(this.query)
    if (this.spotList?.content.length > 0) {
      this.renderSlides()
    }
  },
  methods: {
    ...mapMutations('spot', [
      'SET_SPOT',
      'SET_QUERY',
      'ADD_PAGE',
      'CLEAR_PAGE',
    ]),
    ...mapActions('spot', ['searchSpot', 'addSpot']),
    slideClick(spot) {
      this.SET_SPOT(spot)
      this.$router.push('/review')
    },

    handleScroll(e) {
      if (this.spotList !== []) {
        if (this.timer) {
          clearTimeout(this.timer)
        }

        this.timer = setTimeout(() => {
          if (e.wheelDeltaY > 0) {
            this.prevSlide()
          } else if (e.wheelDeltaY < 0) {
            this.nextSlide()
          }
        }, 50)
      }
    },
    nextSlide() {
      this.scrollCount += 1
      if (this.scrollCount === 40) {
        this.SET_QUERY(
          this.query.replace(`page=${this.page}`, `page=${this.page + 1}`)
        )
        this.ADD_PAGE()
        this.addSpot(this.query)
        this.scrollCount = 0
      }
      const activeSlide = this.$el.querySelector('.slide--active')
      const nextSlide = activeSlide.nextElementSibling
      if (nextSlide) {
        activeSlide.classList.remove('slide--active')
        nextSlide.classList.remove('next')
        nextSlide.classList.add('slide--active')
        this.renderSlides()
      }
    },
    prevSlide() {
      const activeSlide = this.$el.querySelector('.slide--active')
      const prevSlide = activeSlide.previousElementSibling
      if (prevSlide) {
        activeSlide.classList.remove('slide--active')
        prevSlide.classList.remove('prev')
        prevSlide.classList.add('slide--active')
        this.renderSlides()
      }
    },
    renderSlides() {
      const slides = this.$el.querySelectorAll('.slide')
      if (!slides) {
        return
      }
      let activeSlide = this.$el.querySelector('.slide--active')
      if (!activeSlide) {
        activeSlide = slides.item(0)
        activeSlide.classList.add('slide--active')
      }
      ;[].forEach.call(slides, function (slide) {
        slide.classList.remove('prev', 'next')
      })

      const prevSlide = activeSlide.previousElementSibling
      prevSlide && prevSlide.classList.add('prev')

      const nextSlide = activeSlide.nextElementSibling
      nextSlide && nextSlide.classList.add('next')
    },
  },
}
</script>

<style scoped>
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
.spot-name {
  font-family: 'SEBANG_Gothic_Bold';
  font-size: 5vh;
  width: 100%;
  height: 50%;
}
.spot-content {
  font-family: 'GmarketSansMedium';
  font-size: 3vh;
  width: 100%;
  height: 60%;
}
.spot-image-container {
  width: 40%;
  height: 100%;
}

.spot-image {
  width: 100%;
  height: 100%;
}
.container {
  display: flex;
  flex-flow: column;
  justify-content: center;
  width: 100%;
  height: 600px;
}

.controls {
  display: flex;
  justify-content: center;
}

.controls div {
  padding: 12px 14px;
  cursor: pointer;
  border: 1px solid #000000;
  color: #000000;
  font-size: 16px;
  font-weight: bold;
  margin: 0 5px;
  border-radius: 10px;
}

.controls div.disabled {
  opacity: 0;
  cursor: default;
}

.slider {
  position: relative;
  width: 100%;
  height: 100%;
}

.slide {
  cursor: pointer;
  position: absolute;
  display: flex;
  align-items: center;
  top: 20%;
  /* left: 1%; */
  width: 100%;
  height: 60%;
  background-color: #e3fdfe;
  border: 1px solid #fff;
  border-radius: 15px;
  opacity: 0;
  padding: 2vh;
  transition: all 0.7s ease-out;
  box-shadow: 10px 10px 10px rgb(196, 196, 196);
}

.slide--active {
  opacity: 1;
  z-index: 9;
}

.prev,
.next {
  opacity: 0.3;
}

.prev {
  transform: translate(0, -30%) scale(0.8);
}

.next {
  transform: translate(0, 30%) scale(0.8);
}

.search-container {
  width: 100%;
  display: flex;
  justify-content: center;
}
.search-result {
  background-color: white;
  border-radius: 15px;
  width: 80%;
  padding: 2%;
  box-shadow: 10px 10px 10px rgb(196, 196, 196);
}
.title-text {
  font-family: 'SEBANG_Gothic_Bold';
  font-size: 40px;
}

.result-page {
  background-image: url('/background/cloud-background.svg');
  background-size: cover;
  width: 100%;
  height: 100vh;
}

.result-title {
  width: 100%;
  display: flex;
  justify-content: center;
}
</style>
