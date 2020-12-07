<template>
  <label>
    <input type="text" placeholder="User name" v-model="insertingValue.Name">
    <input type="text" placeholder="User surname" v-model="insertingValue.Surname">
    <input type="number" max="120" min="0" placeholder="age" v-model="insertingValue.Age">
    <button v-on:click="insertData()">Insert data!</button>
  </label>
</template>

<script>

export default {
  name: "Customer",
  data() {
    return {
      insertingValue: {
        Name: null,
        Surname: null,
        Age: null
      }
    }
  },
  methods: {
    insertData() {
      let condition = [this.insertingValue.Name, this.insertingValue.Surname, this.insertingValue.Age].some(
          x => [null, undefined, NaN, ''].includes(x))
      if (condition) {
        this.$store.commit("addErrorMessage", {message: 'Please fill every field!'});
      }
    }
  }
}
</script>

<style scoped>

</style>