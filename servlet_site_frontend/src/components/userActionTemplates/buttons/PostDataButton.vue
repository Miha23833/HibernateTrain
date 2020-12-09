<template>
  <button @click="postData"> {{ buttonText }}</button>
</template>

<script>
import axios from 'axios'
export default {
  props: {
    buttonText: String,
    mapping: String,
    queryParams: Object
  },
  name: "GetDataButton",
  methods: {
    postData(){
      let postData = {
        queryParams: this.queryParams
      }
      let map = this.getFormattedMapping();
      axios.post(map, postData).then(response => {
        this.$emit('data-received', response.data)
      }).catch((() => {
        this.$emit('data-received', {response: false})
      }));
    },
    getFormattedMapping(){
      let lastMappingIndex = this.mapping.length;
      for (let i = this.mapping.length-1; i > 0; i--) {
        if (['/', '\\'].some( x => x === this.mapping[i])){
          lastMappingIndex = i;
        }
        else{
          break;
        }
      }
      return this.mapping.slice(0, lastMappingIndex)
    }
  }
}
</script>

<style scoped>

</style>