<template>
  <div class="statistics">
    <div class="banner-container">
      <img src="/banner/statistics-logo2.png" alt="banner" class="banner" />
    </div>
    <div class="white-back slide-in-right">
      <div v-show="statisticsClass === 'sightNum'" class="map-box">
        <div id="myMap"></div>
        <div class="map-right">
          <div class="map-title">지역 별 여행객들의 여행 횟수입니다.</div>
          <div class="map-desc">HOT한 여행지가 어딘지 확인해보세요!</div>
          <div class="map-desc">사람이 많은 게 싫다구요?</div>
          <div class="map-desc">우리만 아는 꿀여행지가 어딜지 찾아봐요!</div>
        </div>
      </div>
      <div v-show="statisticsClass === 'object'" id="wordCloud">
        <vue-word-cloud
          style="height: 40vh; width: 70vw"
          :words="words"
          :color="([, selPercent]) => colorPick(selPercent)"
          font-family="GmarketSansMedium"
          :font-size-ratio="5"
        />
      </div>
      <div v-show="statisticsClass === 'satisfaction'" class="sat-box">
        <canvas id="myChart" width="800" height="450"></canvas>
        <div>
          <div class="sat-title">여행을 할때, 무엇이 중요하신가요?</div>
          <div class="sat-desc">물가를 비교해서 가성비 여행이 가고싶다!</div>
          <div class="sat-desc">역시 여행은 먹으러 가는 거지!</div>
          <div class="sat-desc">자연을 보면서 힐링하는 여행이 좋아!</div>
        </div>
      </div>
      <div class="buttonPlace">
        <div>
          <div
            :class="{ btn2: buttonActive[0], btnActive: !buttonActive[0] }"
            @click="changePage(0)"
          >
            여행 횟수
          </div>
        </div>
        <div>
          <div
            :class="{ btn2: buttonActive[1], btnActive: !buttonActive[1] }"
            @click="changePage(1)"
          >
            관광 목적
          </div>
        </div>
        <div>
          <div
            :class="{ btn2: buttonActive[2], btnActive: !buttonActive[2] }"
            @click="changePage(2)"
          >
            만족도
          </div>
        </div>
      </div>
    </div>
    <navbar-component></navbar-component>
  </div>
</template>

<script>
import * as echarts from 'echarts' // echart를 전역으로 불러옴
import VueWordCloud from 'vuewordcloud'
import { Chart, registerables } from 'chart.js'
import { mapActions, mapState, mapGetters } from 'vuex'

export default {
  name: '',
  components: {
    [VueWordCloud.name]: VueWordCloud,
  },
  data() {
    return {
      buttonActive: [false, true, true],
      statisticsOption: [
        { value: 'sightNum', text: '관광여행횟수' },
        { value: 'object', text: '관광목적' },
        { value: 'satisfaction', text: '만족도' },
      ],
      statisticsClass: 'sightNum',
      fontSizeMapper: (word) => Math.log2(word.value) * 5,
      colorIndex: 4,
      myChart: null,
    }
  },
  computed: {
    ...mapState('statistics', [
      'words',
      'regionList',
      'satList',
      'koreaMap',
      'introData', // 지도의 범례를 위한 값. 최대값과 최솟값 가짐
    ]),
    ...mapGetters('statistics', ['priceList', 'foodList', 'natureList']),
    chartData() {
      const chartData = {
        labels: [
          '서울',
          '부산',
          '대구',
          '인천',
          '광주',
          '대전',
          '울산',
          '세종',
          '경기',
          '강원',
          '충북',
          '충남',
          '전북',
          '전남',
          '경북',
          '경남',
          '제주',
        ],
        datasets: [
          {
            label: '물가',
            backgroundColor: '#f87979',
            borderColor: '#f87979',
            data: this.priceList,
            borderWidth: 5,
          },
          {
            label: '식당 및 음식',
            backgroundColor: '#00FFFF',
            borderColor: '#00FFFF',
            data: this.foodList,
            borderWidth: 5,
          },
          {
            label: '자연경관',
            backgroundColor: '#FF00FF',
            borderColor: '#FF00FF',
            data: this.natureList,
            borderWidth: 5,
          },
        ],
      }
      return chartData
    },
  },
  watch: {
    statisticsClass(newVal, oldVal) {
      if (newVal === 'sightNum') {
        this.mapopen()
      }
    },
  },
  created() {},
  async mounted() {
    // Initialize the echarts instance based on the prepared dom
    await this.mapopen()
    this.fillSat()
  },
  methods: {
    fillSat() {
      Chart.register(...registerables)

      const ctx = document.getElementById('myChart').getContext('2d')
      this.myChart = new Chart(ctx, {
        type: 'line',
        data: this.chartData,
        options: {
          plugins: {
            legend: {
              labels: {
                // This more specific font property overrides the global property
                font: {
                  size: 15,
                  family: 'GmarketSansMedium',
                },
              },
            },
          },
          scales: {
            x: {
              ticks: {
                font: {
                  size: 15,
                  family: 'GmarketSansMedium',
                },
              },
            },
            y: {
              ticks: {
                font: {
                  size: 15,
                  family: 'GmarketSansMedium',
                },
              },
            },
          },
          responsive: false,
          maintainAspectRatio: true,
        },
      })
    },
    ...mapActions('statistics', [
      'callSatList',
      'callSelList',
      'callVisitedList',
    ]),
    changePage(idx) {
      if (idx === 0) {
        this.statisticsClass = 'sightNum'
        this.buttonActive[0] = false
        this.buttonActive[1] = true
        this.buttonActive[2] = true
      } else if (idx === 1) {
        this.statisticsClass = 'object'
        this.buttonActive[0] = true
        this.buttonActive[1] = false
        this.buttonActive[2] = true
      } else {
        this.statisticsClass = 'satisfaction'
        this.buttonActive[0] = true
        this.buttonActive[1] = true
        this.buttonActive[2] = false
      }
    },
    async mapopen() {
      await this.callSatList()
      await this.callSelList()
      await this.callVisitedList()

      const chartDom2 = document.getElementById('myMap')

      const myChart2 = echarts.init(chartDom2)

      // const kr
      const geoJson = this.koreaMap
      myChart2.showLoading()
      myChart2.hideLoading()
      echarts.registerMap('korea', geoJson)

      const option = {
        // title: {
        //   text: '대한민국 방문횟수(2021)',
        //   // subtext: 'Data from www.census.gov',
        //   // sublink: 'http://www.census.gov/popest/data/datasets.html',
        //   left: 'center'
        // },
        tooltip: {
          trigger: 'item',
          showDelay: 0,
          transitionDuration: 0.2,
        },
        visualMap: {
          left: 'right',
          top: 'center',
          min: 0,
          max: 45000,
          inRange: {
            color: [
              '#313695',
              '#4575b4',
              '#74add1',
              '#abd9e9',
              '#e0f3f8',
              '#ffffbf',
              '#fee090',
              '#fdae61',
              '#f46d43',
              '#d73027',
              '#a50026',
            ],
          },
          text: ['High', 'Low'],
          calculable: true,
        },
        // toolbox: {
        //   show: true,
        //   left: 'left',
        //   top: 'top',
        //   feature: {
        //     dataView: { readOnly: false },
        //     restore: {},
        //     saveAsImage: {}
        //   }
        // },
        series: [
          {
            name: '방문횟수',
            type: 'map',
            roam: 'false',
            map: 'korea',
            emphasis: {
              label: {
                show: true,
              },
            },
            data: this.regionList,
          },
        ],
      }
      myChart2.setOption(option)

      option && myChart2.setOption(option)

      window.onresize = function () {
        myChart2.resize()
      }

      // console.log(JSON.stringify(myChart2.getOption(),null,2))
    },
    colorPick(selPercent) {
      // const colorArr=["#F24D98","#813B7C","#59D044","#F3A002","#F2F44D"]
      // const colorArr=["#3F6F76","#69B7CE","#C65840","#F4CE4B","#62496F"]
      // const colorArr=["#4368B6","#78A153","#DEC23B","#E4930A","#C53211"]
      // const colorArr=["#C1395E","#AEC17B","#F0CA50","#E07B42","#89A7C2"]
      // const colorArr=["#21344F","#8AAD05","#E2CE1B","#DF5D22","#E17976"]
      const colorArr = ['#C13E43', '#376EA5', '#565654', '#F9D502', '#E7CA6B']
      if (selPercent > 10) {
        this.colorIndex = (this.colorIndex + 1) % 5
      } else {
        this.colorIndex = (this.colorIndex + 1) % 5
      }
      return colorArr[this.colorIndex]
    },
  },
}
</script>

<style scoped>
.btnActive {
  cursor: pointer;
  width: 100%;
  padding: 15px 15px;
  margin: 10px 4px;
  color: rgb(46, 31, 31);
  background: #ddf416;
  font-family: 'SEBANG_Gothic_Bold';
  text-transform: uppercase;
  text-align: center;
  position: relative;
  text-decoration: none;
  display: inline-block;
  /* border: 1px solid black; */
  border-radius: 10%;
}

@font-face {
  font-family: 'SEBANG_Gothic_Bold';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2104@1.0/SEBANG_Gothic_Bold.woff')
    format('woff');
  font-weight: normal;
  font-style: normal;
}
.map-box {
  display: flex;
  width: 100%;
  height: 100%;
}
.btn2 {
  cursor: pointer;
  width: 100%;
  padding: 15px 15px;
  margin: 10px 4px;
  color: rgb(46, 31, 31);
  font-family: 'SEBANG_Gothic_Bold';
  text-transform: uppercase;
  text-align: center;
  position: relative;
  text-decoration: none;
  display: inline-block;
  /* border: 1px solid black; */
  border-radius: 10%;
}
.map-right {
  margin-top: 5%;
  margin-left: 3%;
}

.map-title {
  font-family: 'SEBANG_Gothic_Bold';
  font-size: 40px;
  margin-bottom: 5%;
}
.map-desc {
  font-family: 'GmarketSansMedium';
  font-size: 20px;
  margin-bottom: 2%;
}
.btn2::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  display: block;
  width: 100%;
  height: 100%;
  z-index: -1;
  background-color: rgb(220, 38, 38);
  -webkit-transform: scaleY(0.3);
  transform: scaleY(0.3);
  opacity: 0;
  transition: all 0.3s;
}
.btn2:hover {
  color: #ddf416;
}
.btn2:hover::before {
  opacity: 1;
  background-color: #3aa8e3;
  -webkit-transform: scaleY(1);
  transform: scaleY(1);
  transition: -webkit-transform 0.6s cubic-bezier(0.08, 0.35, 0.13, 1.02),
    opacity 0.4s;
  transition: transform 0.6s cubic-bezier(0.08, 0.35, 0.13, 1.02), opacity;
  border-radius: 10%;
}
.btn2:active {
  opacity: 1;
  background-color: #3aa8e3;
  -webkit-transform: scaleY(1);
  transform: scaleY(1);
  transition: -webkit-transform 0.6s cubic-bezier(0.08, 0.35, 0.13, 1.02),
    opacity 0.4s;
  transition: transform 0.6s cubic-bezier(0.08, 0.35, 0.13, 1.02), opacity;
  border-radius: 10%;
}

a:active {
  opacity: 1;
  background-color: #3aa8e3;
  -webkit-transform: scaleY(1);
  transform: scaleY(1);
  transition: -webkit-transform 0.6s cubic-bezier(0.08, 0.35, 0.13, 1.02),
    opacity 0.4s;
  transition: transform 0.6s cubic-bezier(0.08, 0.35, 0.13, 1.02), opacity;
  border-radius: 10%;
}
@font-face {
  font-family: 'GmarketSansMedium';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff')
    format('woff');
  font-weight: normal;
  font-style: normal;
}
.banner-container {
  display: flex;
  justify-content: center;
}
.banner {
  margin-top: 1vh;
  width: 40%;
  margin-bottom: -1vh;
}
.slide-in-right {
  -webkit-animation: slide-in-right 1s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  animation: slide-in-right 1s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}
@-webkit-keyframes slide-in-right {
  0% {
    -webkit-transform: translateX(100%);
    transform: translateX(100%);
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
    -webkit-transform: translateX(100%);
    transform: translateX(100%);
    opacity: 0;
  }
  100% {
    -webkit-transform: translateX(0);
    transform: translateX(0);
    opacity: 1;
  }
}
.sat-title {
  margin-top: 10%;
  margin-bottom: 10%;
  font-family: 'SEBANG_Gothic_Bold';
  font-size: 20px;
}

.sat-desc {
  font-family: 'GmarketSansMedium';
  margin-bottom: 5%;
}
.selected {
  position: absolute;
  top: 5vh;
  left: 20vh;
}

.custom-select {
  width: 10%;
}
.statistics {
  background-image: url('/background/cloud-background.svg');
  background-size: cover;
  width: 100%;
  height: 100vh;
}

.white-back {
  position: relative;
  background-image: url('/banner/statistics-ticket-background.png');
  background-position-y: 100%;
  background-position-x: 20px;
  background-repeat: no-repeat;
  background-size: 100% 100%;
  width: 100%;
  height: 70vh;
}

.map {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50%;
}
.sat-box {
  position: absolute;
  display: flex;
  left: 10%;
  top: 6%;
}
.buttonPlace {
  position: absolute;
  top: 24px;
  right: 1%;
  width: 14%;
  height: 43%;

  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
}

/* style="width: 780px; height: 700px; left:25%" */
#myMap {
  /* width: 780px; */
  /* margin-top:3vh; */
  padding-top: 10px;
  height: 100%;
  width: 40%;
  /* left: 3%; */
}

#main {
  position: absolute;

  /* bottom: 5vh; */
  /* right: 65vh; */
  padding-top: 2%;
  left: 20%;
  /* width: 40%;
  height: 100%; */
  /* width: 600px;
  height: 600px; */
  width: 100vh;
  height: 70vh;
}
#wordCloud {
  position: absolute;
  width: 40vw;
  bottom: 20vh;
  left: 20vh;
}

.changeButton {
  color: '#00BFFF';
}
</style>
