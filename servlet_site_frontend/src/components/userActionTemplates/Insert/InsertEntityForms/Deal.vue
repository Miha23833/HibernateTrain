<template>
  <label v-on:input="emitUp">
    <input type="datetime-local" placeholder="Deal date" v-model="insertingValue.deal_date">
    <input type="number" placeholder="Customer ID" v-model="insertingValue.customer_id">
    <input type="number" placeholder="Product ID" v-model="insertingValue.product_id">
    <input type="number" placeholder="Discount" min="0" max="100" v-model="insertingValue.discount">
    <input type="number" placeholder="Price" v-model="insertingValue.price">
  </label>
</template>

<script>
export default {
  name: "Deals",
  data() {
    return {
      insertingValue: {
        deal_date: null,
        customer_id: null,
        discount: null,
        product_id: null,
        price: null
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
    },
    emitUp() {
      this.$emit('params-changed', this.insertingValue);
    }
  }
}
</script>

<style scoped>

</style>