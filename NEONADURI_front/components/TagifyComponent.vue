<template>
  <div>
    <Tags
      ref="myRef"
      :settings="tagifyStuff.tagifySettings"
      :suggestions="tagifyStuff.suggestions"
      :value="tagifyStuff.value"
      :on-change="onTagsChange"
    />
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
// import Tags from "@yaireo/tagify/dist/tagify.vue"
import Tags from '@/components/TagifyInput'

export default {
  name: 'App',
  components: { Tags },

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
}
</script>

<style></style>
