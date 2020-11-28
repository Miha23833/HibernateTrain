<template>
  <label>
    <input class="filter-input" type="number" v-model="queryParams.customer_id" placeholder="customer id"><br>
    <input class="filter-input" type="text" v-model="queryParams.name" placeholder="name"><br>
    <input class="filter-input" type="text" v-model="queryParams.surname" placeholder="surname"><br>
    <input class="filter-input" type="number" v-model="queryParams.age" placeholder="age"><br>
    <input class="filter-input" type="number" v-model="queryParams.favourite_product"
           placeholder="favourite product"><br>
    <button @click="getServerData">getData!</button>
  </label>
</template>

<script>
export default {
  data() {
    return {
      queryParams: {
        customer_id: null,
        placeholder: null,
        surname: null,
        age: null,
        favourite_product: null
      }
    }
  },
  name: "Customers"
}
</script>

<style scoped>

</style>