<template>
  <label v-on:input="filterChanged">
    Date before deal:<br>
    <input class="filter-input" type="date" v-model="queryParams.deal_date_before">
    Date after deal:<br>
    <input class="filter-input" type="date" v-model="queryParams.deal_date_after" placeholder="deal_date_after"><br>
    <input class="filter-input" type="number" v-model="queryParams.customer_id" placeholder="customer_id"><br>
    <input class="filter-input" type="number" v-model="queryParams.discount" placeholder="discount"><br>
    <input class="filter-input" type="number" v-model="queryParams.product_id" placeholder="product_id"><br>
    <input class="filter-input" type="number" v-model="queryParams.min_price" placeholder="min_price"><br>
    <input class="filter-input" type="number" v-model="queryParams.deal_id" placeholder="deal_id"><br>
  </label>
</template>

<script>
import {USER_ACTION} from "@/enums/USER_ACTIONS";
import {ENTITY} from "@/enums/ENTITIES";

export default {
  name: "Deals",
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
      },
      mapping: USER_ACTION.SELECT.mapping + ENTITY.Deal.mapping
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
label input {
  width: 80%;
}
</style>