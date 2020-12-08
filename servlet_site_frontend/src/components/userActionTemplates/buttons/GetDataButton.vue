<template>
  <button @click="askForData"> {{ buttonText }}</button>
</template>

<script>
import axios from 'axios'
export default {
  props: {
    buttonText: String,
    mapping: String,
    filter: Object
  },
  name: "GetDataButton",
  methods: {
    askForData(){
      let postData = {
        queryParams: this.filter
      }
      let map = this.getFormattedMapping();
      axios.post(map, postData).then(response => {
        this.$emit('data-received', response.data)
      });
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