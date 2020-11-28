<template>
  <label>
    Date before deal:<br>
    <input class="filter-input" type="date" v-model="queryParams.deal_date_before">
    Date after deal:<br>
    <input class="filter-input" type="date" v-model="queryParams.deal_date_after" placeholder="deal_date_after"><br>
    <input class="filter-input" type="number" v-model="queryParams.customer_id" placeholder="customer_id"><br>
    <input class="filter-input" type="number" v-model="queryParams.discount" placeholder="discount"><br>
    <input class="filter-input" type="number" v-model="queryParams.product_id" placeholder="product_id"><br>
    <input class="filter-input" type="number" v-model="queryParams.min_price" placeholder="min_price"><br>
    <input class="filter-input" type="number" v-model="queryParams.deal_id" placeholder="deal_id"><br>
    <button @click="getServerData">getData!</button>
  </label>
</template>

<script>
export default {
  data() {
    return {
      queryParams: {
        deal_date_before: null,
        deal_date_after: null,
        customer_id: null,
        discount: null,
        product_id: null,
        min_price: null,
        deal_id: null
      }
    }
  },
  name: "Deals"
}
</script>

<style scoped>

</style>