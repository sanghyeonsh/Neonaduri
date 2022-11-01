<template v-once>
  <textarea v-if="mode === 'textarea'" v-model="value" />
  <input v-else class="tagify-input" :value="value" @change="onChange" />
</template>

<script src="https://cdn.jsdelivr.net/npm/@yaireo/tagify"></script>
<script src="https://cdn.jsdelivr.net/npm/@yaireo/tagify/dist/tagify.polyfills.min.js"></script>
<script>
// import Tagify from '@yaireo/tagify/dist/tagify.min.js'
import '@yaireo/tagify/dist/tagify.css'

export default {
  name: 'Tags',
  head: {
    script: [
      {
        src: 'https://cdn.jsdelivr.net/npm/@yaireo/tagify',
      },
      {
        src: 'https://cdn.jsdelivr.net/npm/@yaireo/tagify/dist/tagify.polyfills.min.js',
      },
    ],
  },
  props: {
    mode: String,
    settings: Object,
    value: [String, Array],
    onChange: Function,
  },
  watch: {
    value(newVal, oldVal) {
      this.tagify.loadOriginalValues(newVal)
    },
  },
  mounted() {
    this.tagify = new Tagify(this.$el, this.settings)
  },
}
</script>
<style>
.tagify {
  /* --tags-disabled-bg: white; */
  --tags-disabled-bg: #f1f1f1;
  --tag-inset-shadow-size: 0px;
  --tags-border-color: white;
  --tags-hover-border-color: white;
}
.tagify__tag {
  background-color: #f1f1f1;
  font-size: 15px;
}
.tagify__input {
  width: 100%;
  margin-bottom: 10%;
  border-bottom: 0.5px solid gray;
}
</style>
