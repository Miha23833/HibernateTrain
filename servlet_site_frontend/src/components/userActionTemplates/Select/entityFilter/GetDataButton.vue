<template>
  <button @click="getServerData">getData!</button>
</template>

<script>
import axios from "axios";

export default {
  props: ['filter', 'mapping'],
  name: "GetDataButton",
  methods: {
    getServerData() {
      let filter = this.filter;
      axios.post(this.mapping, {filter})
          .then(resp => this.$emit('setTableData', resp.data));
    }
  }
}
</script>

<style scoped>

</style>