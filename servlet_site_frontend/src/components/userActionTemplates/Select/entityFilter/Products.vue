<template>
  <label>
    <input class="filter-input" type="text" v-model="queryParams.product_name" placeholder="product_name"><br>
    <input class="filter-input" type="text" v-model="queryParams.description" placeholder="description"><br>
    <input class="filter-input" type="number" v-model="queryParams.price" placeholder="price"><br>
    <input class="filter-input" type="number" v-model="queryParams.product_id" placeholder="product_id"><br>
    <GetDataButton v-bind:filter="queryParams" v-bind:mapping="mapping"/>
  </label>
</template>

<script>
import {USER_ACTION} from "@/components/enums/USER_ACTIONS";
import {ENTITY} from "@/components/enums/ENTITIES";

export default {
  data(){
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
  name: "Products"
}
</script>

<style scoped>

</style>