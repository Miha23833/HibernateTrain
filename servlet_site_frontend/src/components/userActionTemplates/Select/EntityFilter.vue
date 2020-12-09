<template>
  <div>
    <EntitySelector/>
    <div class="filter-wrapper">
      <div v-if="currentEntity === ENTITY.Customer">
          <Customers @filter-changed="setFilter"/>
      </div>
      <div v-else-if="currentEntity === ENTITY.Product">
          <Products @filter-changed="setFilter"/>
      </div>
      <div v-else-if="currentEntity === ENTITY.Deal">
          <Deals @filter-changed="setFilter"/>
      </div>
      <div v-if="checkEntitiesContains(currentEntity)" class="get-data-button">
        <GetDataButton @data-received="emmitUp" v-bind:queryParams="this.entityFilter"
                       v-bind:button-text="'Get data'"
                       v-bind:mapping="this.USER_ACTION.SELECT.mapping + this.currentEntity.mapping"></GetDataButton>
      </div>
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
import GetDataButton from "@/components/userActionTemplates/buttons/PostDataButton";

export default {
  components: {
    GetDataButton,
    EntitySelector,
    Customers,
    Products,
    Deals
  },
  emits: ['data-received'],
  computed: {
    currentEntity() {
      this.$emit('entity-changed');
      this.setFilter({});
      return this.$store.getters.getCurrentEntity;
    }
  },
  data() {
    return {
      ENTITY,
      USER_ACTION,
      entityFilter: {},
    }
  },
  methods: {
    checkEntitiesContains(value){
      for (const key in ENTITY){
        if (ENTITY[key] === value){
          return true;
        }
      }
      return false;
    },
    emmitUp(data) {
      this.$emit('data-received', data)
    },
    setFilter(filter){
      this.entityFilter = filter;
    }
  }
}
</script>

<style scoped>

.get-data-button {
  text-align: center;
}

.get-data-button button {
  border-radius: 10px;
}

.get-data-button button:hover {
  background-color: #d0ea99;
}

.get-data-button button:active {
  transition: .1s;
  background-color: #b4cd85;
}

.filter-wrapper {
  border-color: #c6c6c6;
  border-radius: .5em;
  border-style: solid;
  border-width: 1px;
  text-align: center;
  padding-top: .5em;
  padding-bottom: 1em;
}
</style>