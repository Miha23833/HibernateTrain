<template>
  <label v-on:input="filterChanged">
    <input class="filter-input" type="number" v-model="queryParams.customer_id" placeholder="customer id"><br>
    <input class="filter-input" type="text" v-model="queryParams.name" placeholder="name"><br>
    <input class="filter-input" type="text" v-model="queryParams.surname" placeholder="surname"><br>
    <input class="filter-input" type="number" v-model="queryParams.age" placeholder="age"><br>
    <input class="filter-input" type="number" v-model="queryParams.favourite_product"
           placeholder="favourite product"><br>
  </label>
</template>

<script>
import {ENTITY} from "@/enums/ENTITIES";
import {USER_ACTION} from "@/enums/USER_ACTIONS";

export default {
  name: "Customers",
  data() {
    return {
      queryParams: {
        customer_id: null,
        placeholder: null,
        surname: null,
        age: null,
        favourite_product: null
      },
      mapping: USER_ACTION.SELECT.mapping + ENTITY.Customer.mapping,
    }
  },
  methods: {
    filterChanged(){
      this.$emit('filter-changed', this.queryParams)
    }
  }
}
</script>
<style scoped>
</style>