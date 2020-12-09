<template>
  <label v-on:input="emitUp">
    <input type="text" placeholder="User name" v-model="insertingValue.name">
    <input type="text" placeholder="User surname" v-model="insertingValue.surname">
    <input type="number" max="120" min="0" placeholder="age" v-model="insertingValue.age">
  </label>
</template>

<script>

export default {
  name: "Customer",
  data() {
    return {
      insertingValue: {
        name: null,
        surname: null,
        age: null
      }
    }
  },
  methods: {
    insertData() {
      let condition = [this.insertingValue.name, this.insertingValue.surname, this.insertingValue.age].some(
          x => [null, undefined, NaN, ''].includes(x))
      if (condition) {
        this.$store.commit("addErrorMessage", {message: 'Please fill every field!'});
      }
    },
    emitUp() {
      this.$emit('params-changed', this.insertingValue);
    }
  }
}
</script>

<style scoped>

</style>