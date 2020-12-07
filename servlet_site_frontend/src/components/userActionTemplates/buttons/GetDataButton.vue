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
  data(){
    let lastMappingIndex = this.mapping.length;
    for (let i = this.mapping.length-1; i > 0; i--) {
      if (['/', '\\'].some( x => x === this.mapping[i])){
        lastMappingIndex = i;
      }
      else{
        break;
      }
    }
    return {
      localFilter: this.filter,
      localMapping: this.mapping.slice(0, lastMappingIndex)
    };
  },
  name: "GetDataButton",
  emits: ['data-received'],
  methods: {
    askForData(){
      let postData = {
        queryParams: this.localFilter
      }

      axios.post(this.localMapping, postData).then(response => {
        this.$emit('data-received', response)
      });
    }
  }
}
</script>

<style scoped>

</style>