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
  width: 100%;
  height: 2em;
  font-family: Helvetica, sans-serif;
  font-size: 1em;
  transition: .5s;
  margin-top: .5em;
}

>>> button:hover {
  background-color: #dff6df;
}

>>> button:focus{
  outline:none;
}

>>> button:active{
  background-color: #d0e5d0;
  /*box-shadow: 0 1px #d0e5d0;*/
  /*transform: translateY(1px);*/
  transition: 0s ease-in-out;
}

</style>