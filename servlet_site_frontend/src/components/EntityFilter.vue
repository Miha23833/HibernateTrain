<template>
  <div v-if="filterTemplate === 'Customers'" id="filter-customers">
    <label>
      <input class="filter-input" type="number" v-model="queryParams.customer_id" placeholder="customer id"><br>
      <input class="filter-input" type="text" v-model="queryParams.name" placeholder="name"><br>
      <input class="filter-input" type="text" v-model="queryParams.surname" placeholder="surname"><br>
      <input class="filter-input" type="number" v-model="queryParams.age" placeholder="age"><br>
      <input class="filter-input" type="number" v-model="queryParams.favourite_product"
             placeholder="favourite product"><br>
      <button @click="getServerData">getData!</button>
    </label><br> Date after deal:<br>
  </div>
  <div v-else-if="filterTemplate === 'Products'" id="filter-products">
    <label>
      <input class="filter-input" type="text" v-model="queryParams.product_name" placeholder="product_name"><br>
      <input class="filter-input" type="text" v-model="queryParams.description" placeholder="description"><br>
      <input class="filter-input" type="number" v-model="queryParams.price" placeholder="price"><br>
      <input class="filter-input" type="number" v-model="queryParams.product_id" placeholder="product_id"><br>
      <button @click="getServerData">getData!</button>
    </label><br> Date after deal:<br>
  </div>
  <div v-else-if="filterTemplate === 'Deals'" id="filter-deals">
    Date before deal:<br>
    <label>
      <input class="filter-input" type="date" v-model="queryParams.deal_date_before">
      <input class="filter-input" type="date" v-model="queryParams.deal_date_after" placeholder="deal_date_after"><br>
      <input class="filter-input" type="number" v-model="queryParams.customer_id" placeholder="customer_id"><br>
      <input class="filter-input" type="number" v-model="queryParams.discount" placeholder="discount"><br>
      <input class="filter-input" type="number" v-model="queryParams.product_id" placeholder="product_id"><br>
      <input class="filter-input" type="number" v-model="queryParams.min_price" placeholder="min_price"><br>
      <input class="filter-input" type="number" v-model="queryParams.deal_id" placeholder="deal_id"><br>
      <button @click="getServerData">getData!</button>
    </label><br> Date after deal:<br>
  </div>
  <div v-else>
    pls pick entity!
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      filterTemplate: "",
      filterMapping: "",
      queryParams: {}
    }
  },
  methods: {
    getServerData() {
      let queryParams = this.queryParams;
      axios.post(this.filterMapping, {
        queryParams
      }).then(resp => {
        resp.data.response.forEach(
            (elem) => this.$emit('addDataToMainTable', elem)
        )
        this.$emit('setMainDataColumns', resp.data.columns)
      });
    }
  }
}
</script>

<style scoped>

</style>