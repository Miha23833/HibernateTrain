<template>
  <div>
    <EntitySelector/>
    <div v-if="currentEntity === ENTITY.Customer">
      <Customers/>
      <div class="get-data-button">
        <GetDataButton v-bind:filter="this.entityFilter" v-bind:button-text="'Get data'" v-bind:mapping="this.USER_ACTION.SELECT.mapping + currentEntity.mapping"></GetDataButton>
      </div>
    </div>
    <div v-else-if="currentEntity === ENTITY.Product">
      <Products/>
      <div class="get-data-button">
        <GetDataButton v-bind:filter="this.entityFilter" v-bind:button-text="'Get data'" v-bind:mapping="this.USER_ACTION.SELECT.mapping + currentEntity.mapping"></GetDataButton>
      </div>
    </div>
    <div v-else-if="currentEntity === ENTITY.Deal">
      <Deals/>
      <div class="get-data-button">
        <GetDataButton v-bind:filter="this.entityFilter" v-bind:button-text="'Get data'" v-bind:mapping="this.USER_ACTION.SELECT.mapping + currentEntity.mapping"></GetDataButton>
      </div>
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
import {ENTITY} from "@/enums/ENTITIES";
import {USER_ACTION} from "@/enums/USER_ACTIONS";
import EntitySelector from "@/components/userActionTemplates/Select/entityFilter/EntitySelector";
import GetDataButton from "@/components/userActionTemplates/buttons/GetDataButton";

export default {
  components: {
    GetDataButton,
    EntitySelector,
    Customers,
    Products,
    Deals
  },
  computed: {
    currentEntity(){
      this.$emit('entity-changed');
      return this.$store.getters.getCurrentEntity;
    }
  },
  data() {
    return {
      ENTITY,
      USER_ACTION,
      entityFilter: {},
      queryParams: {}
    }
  }
}
</script>

<style scoped>

.get-data-button{
  text-align: center;
}
.get-data-button button{
  border-radius: 10px;
}
.get-data-button button:hover{
  background-color: #d0ea99;
}
.get-data-button button:active{
  transition: .1s;
  background-color: #b4cd85;
}
</style>