<template>
  <div class="navbar">
    <div class="navbar-btn">
      <v-icon large @click="toggle">mdi-menu</v-icon>
    </div>
    <div
      class="navbar-content"
      :class="{ 'go-bottom': isExpanded, 'go-top': !isExpanded }"
    >
      <div></div>
      <div class="navbar-content-icon" @click="mvHome">
        <v-icon large>mdi-home</v-icon>
        <div style="display: flex; justify-content: center">홈</div>
      </div>
      <div class="navbar-content-icon" @click="mvSearch">
        <v-icon large>mdi-magnify</v-icon>
        <div>여행지 찾기</div>
      </div>
      <div class="navbar-content-icon" @click="mvStatistics">
        <v-icon large>mdi-alpha-i-circle-outline</v-icon>
        <div>여행 정보</div>
      </div>
      <div class="navbar-content-icon" @click="mvFindRoute">
        <v-icon large> mdi-call-split </v-icon>
        <div>여행 경로찾기</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isExpanded: false,
    }
  },
  methods: {
    toggle() {
      this.isExpanded = !this.isExpanded
    },
    mvSearch() {
      this.$router.push('/search')
    },
    mvFindRoute() {
      this.$router.push('/findRoute')
    },
    mvStatistics() {
      this.$router.push('/statistics')
    },
    mvHome() {
      this.$router.push('/')
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

.navbar {
  position: fixed;
  top: 0;
  right: 0;
  display: flex;
  justify-content: center;
}
.navbar-btn {
  position: fixed;
  top: 0;
  right: 1;
  z-index: 100;
  margin: 25px 20px 0 0;
}

.navbar-content {
  height: 400px;
  margin: 10px 20px 0 0;
  background-color: #fffff0;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  font-size: 12px;
  border-radius: 20px;
}
.go-bottom {
  max-height: 500px;
  transition: max-height 0.35s ease-in;
}

.go-top {
  max-height: 0;
  transition: max-height 0.35s ease-out;
  overflow: hidden;
}
.navbar-content-icon {
  font-family: 'GmarketSansMedium';
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
</style>
