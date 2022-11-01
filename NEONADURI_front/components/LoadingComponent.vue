<template>
  <div class="page-loader">
    <div class="spinner">
      <div class="back">
        <img
          class="load-spinner"
          src="/icon/loading.gif"
          alt="loading-spinner-plane"
        />
      </div>
    </div>
    <!-- <div id="spinner"></div> -->
    <!-- <img class="spinner-logo" src="/logo.png" alt /> -->
  </div>
</template>

<script>
export default {
  name: 'PageLoader',
  props: {
    toggle: Boolean,
  },
  data: () => {
    return {
      isloaded: false,
    }
  },
  mounted() {
    document.onreadystatechange = () => {
      if (document.readyState === 'complete') {
        this.isloaded = true
      }
    }
  },
}
</script>

<style scoped>
.page-loader {
  position: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2);
  z-index: 999;
}
.load-spinner {
  width: 100%;
  height: 100%;
}
.spinner {
  width: 10%;
}
</style>
