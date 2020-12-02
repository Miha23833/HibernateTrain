<template>
  <div v-if="currentEntity === ENTITY.Customer">
    <Customers/>
  </div>
  <div v-else-if="currentEntity === ENTITY.Product">
    <Products/>
  </div>
  <div v-else-if="currentEntity === ENTITY.Deal">
    <Deals/>
  </div>
  <div v-else>
    pls pick entity!
  </div>
</template>

<script>
import Customers from "@/components/userActionTemplates/Select/entityFilter/Customers";
import Products from "@/components/userActionTemplates/Select/entityFilter/Products";
import Deals from "@/components/userActionTemplates/Select/entityFilter/Deals";
import {ENTITY} from "@/components/enums/ENTITIES";

export default {
  props: ['currentEntity'],
  components:{
    Customers,
    Products,
    Deals
  },
  data() {
    return {
      ENTITY,
      entityFilter: {},
      queryParams: {}
    }
  }
}
</script>

<style scoped>
</style>