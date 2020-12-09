<template>
  <div id="main">
    <div id="insert-template">
      <div v-if="currentEntity===LOCAL_ENTITY.Customer">
        <Customer @params-changed="queryParams = $event"></Customer>
      </div>
      <div v-else-if="currentEntity===LOCAL_ENTITY.Product">
        <Products @params-changed="queryParams = $event"></Products>
      </div>
      <div v-else-if="currentEntity===LOCAL_ENTITY.Deal">
        <Deals @params-changed="queryParams = $event"></Deals>
      </div>
    </div>
    <div id="entity-selector">
      <EntitySelector></EntitySelector>
      <PostDataButton @data-received="notifyResponse"
                      v-bind:mapping="USER_ACTION.INSERT.mapping + currentEntity.mapping"
                      v-bind:button-text="'Insert'" v-bind:query-params="queryParams"/>
    </div>
  </div>
</template>

<script>
import {ENTITY} from "@/enums/ENTITIES";
import {USER_ACTION} from "@/enums/USER_ACTIONS";
import EntitySelector from "@/components/userActionTemplates/Select/entityFilter/EntitySelector";
import Customer from "@/components/userActionTemplates/Insert/InsertEntityForms/Customer";
import Products from "@/components/userActionTemplates/Insert/InsertEntityForms/Product";
import Deals from "@/components/userActionTemplates/Insert/InsertEntityForms/Deal";
import PostDataButton from "@/components/userActionTemplates/buttons/PostDataButton";

export default {
  name: "Insert",
  components: {Deals, Products, Customer, EntitySelector, PostDataButton},
  data() {
    return {
      LOCAL_ENTITY: ENTITY,
      USER_ACTION,
      queryParams: {}
    };
  },
  computed: {
    currentEntity() {
      return this.$store.getters.getCurrentEntity;
    }
  },
  methods: {
    notifyResponse(resp){
      if (resp.response === true){
        this.$store.commit('addErrorMessage', {message: 'Entity was inserted.'})
      }
      else if (resp.response === false) {
        this.$store.commit('addErrorMessage', {message: 'Failed to insert entity.'})
      }
    }
  }
}
</script>

<style scoped>
#main {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding-left: 20%;
  padding-right: 20%;
}

#insert-template {
  margin-top: 2em;
  width: 80%;
}


</style>