<template>
  <div v-if="filterTemplate === 'Customers'" id="filter-customers">
    <Customers/>
  </div>
  <div v-else-if="filterTemplate === 'Products'" id="filter-products">
    <Products/>
  </div>
  <div v-else-if="filterTemplate === 'Deals'" id="filter-deals">
    <Deals/>
  </div>
  <div v-else>
    pls pick entity!
  </div>
</template>

<script>
import axios from 'axios'

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