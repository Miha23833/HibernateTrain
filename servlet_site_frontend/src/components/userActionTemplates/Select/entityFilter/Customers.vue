<template>
  <div class="filter-input-wrapper">
  <label class="abc">
    <input class="filter-input" type="number" v-model="queryParams.customer_id" placeholder="customer id"><br>
    <input class="filter-input" type="text" v-model="queryParams.name" placeholder="name"><br>
    <input class="filter-input" type="text" v-model="queryParams.surname" placeholder="surname"><br>
    <input class="filter-input" type="number" v-model="queryParams.age" placeholder="age"><br>
    <input class="filter-input" type="number" v-model="queryParams.favourite_product"
           placeholder="favourite product"><br>
  </label>

  </div>
</template>

<script>
import {ENTITY} from "@/enums/ENTITIES";
import {USER_ACTION} from "@/enums/USER_ACTIONS";

export default {
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
  name: "Customers"
}
</script>
<style scoped>
.filter-input-wrapper {
  border-color: #c6c6c6;
  border-radius: .5em;
  border-style: solid;
  border-width: 1px;
  text-align: center;
}
</style>