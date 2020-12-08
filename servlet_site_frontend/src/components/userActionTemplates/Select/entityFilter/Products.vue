<template>
  <label v-on:input="filterChanged">
    <input class="filter-input" type="text" v-model="queryParams.product_name" placeholder="product_name"><br>
    <input class="filter-input" type="text" v-model="queryParams.description" placeholder="description"><br>
    <input class="filter-input" type="number" v-model="queryParams.price" placeholder="price"><br>
    <input class="filter-input" type="number" v-model="queryParams.product_id" placeholder="product_id"><br>
  </label>
</template>

<script>
import {USER_ACTION} from "@/enums/USER_ACTIONS";
import {ENTITY} from "@/enums/ENTITIES";

export default {
  name: "Products",
  data() {
    return {
      queryParams: {
        product_name: null,
        description: null,
        price: null,
        product_id: null
      },
      mapping: USER_ACTION.SELECT.mapping + ENTITY.Product.mapping
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