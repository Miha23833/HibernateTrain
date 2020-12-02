<template>
  <label>
    <input class="filter-input" type="number" v-model="queryParams.customer_id" placeholder="customer id"><br>
    <input class="filter-input" type="text" v-model="queryParams.name" placeholder="name"><br>
    <input class="filter-input" type="text" v-model="queryParams.surname" placeholder="surname"><br>
    <input class="filter-input" type="number" v-model="queryParams.age" placeholder="age"><br>
    <input class="filter-input" type="number" v-model="queryParams.favourite_product"
           placeholder="favourite product"><br>
    <GetDataButton v-bind:filter="queryParams" v-bind:mapping="mapping"/>
  </label>
</template>

<script>
import GetDataButton from "@/components/userActionTemplates/Select/entityFilter/GetDataButton";
import {ENTITY} from "@/components/enums/ENTITIES";
import {USER_ACTION} from "@/components/enums/USER_ACTIONS";

export default {
  components: {GetDataButton},
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
label {
  width: 100%;
  padding-inline: 5% 2%;
}
</style>