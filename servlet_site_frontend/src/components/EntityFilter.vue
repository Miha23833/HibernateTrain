<template>
  <div v-if="entityFilter === PRIVATE_ENTITIES.Customers" id="filter-customers">
    <Customers/>
  </div>
  <div v-else-if="entityFilter === PRIVATE_ENTITIES.Products" id="filter-products">
    <Products/>
  </div>
  <div v-else-if="entityFilter === PRIVATE_ENTITIES.Deals" id="filter-deals">
    <Deals/>
  </div>
  <div v-else>
    pls pick entity!
  </div>
</template>

<script>
import axios from 'axios'

import {ENTITIES} from '@/components/enums/ENTITIES'

import Customers from "@/components/entityFilters/Customers";
import Products from "@/components/entityFilters/Products";
import Deals from "@/components/entityFilters/Deals";

export default {
  components:{
    Customers,
    Products,
    Deals
  },
  data() {
    return {
      PRIVATE_ENTITIES: ENTITIES,
      entityFilter: {},
      queryParams: {}
    }
  },
  methods: {
    getServerData() {
      let queryParams = this.queryParams;
      axios.post(this.PRIVATE_ENTITIES.mapping, {
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