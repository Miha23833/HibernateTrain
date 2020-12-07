<template>
  <label>
    <input type="datetime-local" placeholder="Deal date" v-model="insertingValue.DealDate">
    <input type="number" placeholder="Customer ID" v-model="insertingValue.CustomerID">
    <input type="number" placeholder="Product ID" v-model="insertingValue.ProductID">
    <input type="number" placeholder="Discount" min="0" max="100" v-model="insertingValue.Discount">
    <input type="number" placeholder="Price" v-model="insertingValue.Price">
    <button v-on:click="insertData()">Insert data!</button>
  </label>
</template>

<script>
export default {
  name: "Deals",
  data() {
    return {
      insertingValue: {
        DealDate: null,
        CustomerID: null,
        Discount: null,
        ProductID: null,
        Price: null
      }
    }
  },
  methods: {
    insertData() {
      let condition = [this.insertingValue.DealDate, this.insertingValue.CustomerID, this.insertingValue.Discount, this.insertingValue.ProductID, this.insertingValue.Price].some(
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