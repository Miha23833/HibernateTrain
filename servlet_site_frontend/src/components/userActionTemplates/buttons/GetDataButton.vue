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
    return {localFilter: this.filter};
  },
  name: "GetDataButton",
  emits: ['data-received'],
  methods: {
    askForData(){
      axios.get(this.mapping, this.localFilter).then(response => {
        this.$emit('data-received', response)
      })
    }
  }
}
</script>

<style scoped>

</style>