<template>
  <div>
    <EntitySelector/>
    <div v-if="currentEntity === ENTITY.Customer">
      <Customers/>
      <button @click="getData()">getData!</button>
    </div>
    <div v-else-if="currentEntity === ENTITY.Product">
      <Products/>
      <button @click="getData()">getData!</button>
    </div>
    <div v-else-if="currentEntity === ENTITY.Deal">
      <Deals/>
      <button @click="getData()">getData!</button>
    </div>
    <div v-else>
      pls pick entity!
    </div>
  </div>
</template>

<script>
import Customers from "@/components/userActionTemplates/Select/entityFilter/Customers";
import Products from "@/components/userActionTemplates/Select/entityFilter/Products";
import Deals from "@/components/userActionTemplates/Select/entityFilter/Deals";
import {ENTITY} from "@/components/enums/ENTITIES";
import {USER_ACTION} from "@/components/enums/USER_ACTIONS";
import axios from 'axios';
import EntitySelector from "@/components/userActionTemplates/Select/entityFilter/EntitySelector";

export default {
  props: ['currentEntity'],
  components: {
    EntitySelector,
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
  },
  methods: {
    getData() {
      axios.get(USER_ACTION.SELECT.mapping + this.currentEntity.mapping, this.queryParams).then(
          resp => {
            this.$parent.setTableData(resp.response);
            this.$parent.setColumns(resp.columns);
          }
      )
    }
  }
}
</script>

<style scoped>
>>> input {
  font-size: 1em;
  padding: .5em;
  margin-bottom: .4em;
  font-family: Helvetica, sans-serif;
  transition: .3s;
  border: none;
  width: 100%;
}


>>> input:focus {
  outline: none;
  box-shadow: 0 2px #53ea93;
}

>>> button {
  background-color: #f1f1f1;
  border: none;
  width: 80%;
  height: 2em;
  font-family: Helvetica, sans-serif;
  font-size: 1em;
  transition: .5s;
  margin-top: .5em;
}

>>> button:hover {
  background-color: #dff6df;
}

>>> button:focus {
  outline: none;
}

>>> button:active {
  background-color: #d0e5d0;
  transition: 0s ease-in-out;
}

</style>