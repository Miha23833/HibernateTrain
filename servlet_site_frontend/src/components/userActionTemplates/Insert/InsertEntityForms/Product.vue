<template>
  <label v-on:input="emitUp">
    <input type="text" placeholder="Name" v-model="insertingValue.product_name">
    <input type="text" placeholder="Description" v-model="insertingValue.description">
    <input type="number" max="120" min="0" placeholder="Price" v-model="insertingValue.price">
  </label>
</template>

<script>
export default {
  name: "Products",
  data() {
    return {
      insertingValue: {
        product_name: null,
        description: null,
        price: null
      }
    }
  },
  methods: {
    insertData() {
      let condition = [this.insertingValue.Name, this.insertingValue.Description, this.insertingValue.Price].some(
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