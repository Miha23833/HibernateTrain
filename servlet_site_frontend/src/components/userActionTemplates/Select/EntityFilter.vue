<template>
  <div>
    <EntitySelector/>
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
    <div id="get-data-button" v-if="currentEntity === ENTITY.Customer ||
              currentEntity === ENTITY.Product ||
              currentEntity === ENTITY.Deal" @click="getData()">
      <button @click="getData()">get data!</button>
    </div>
  </div>
</template>

<script>
import Customers from "@/components/userActionTemplates/Select/entityFilter/Customers";
import Products from "@/components/userActionTemplates/Select/entityFilter/Products";
import Deals from "@/components/userActionTemplates/Select/entityFilter/Deals";
import {ENTITY} from "@/enums/ENTITIES";
import {USER_ACTION} from "@/enums/USER_ACTIONS";
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
    },
    setCurrentEntity(newValue){
      this.$parent.setCurrentEntity(newValue);
    }
  }
}
</script>

<style scoped>

#get-data-button{
  text-align: center;
}
#get-data-button button{
  border-radius: 10px;
}
#get-data-button button:hover{
  background-color: #d0ea99;
}
#get-data-button button:active{
  transition: .1s;
  background-color: #b4cd85;
}
</style>