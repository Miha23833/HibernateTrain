<template>
  <div id="main">
    <div id="insert-template">
      <div v-if="currentEntity===LOCAL_ENTITY.Customer">
        <Customer></Customer>
      </div>
      <div v-else-if="currentEntity===LOCAL_ENTITY.Product">
        <Products></Products>
      </div>
      <div v-else-if="currentEntity===LOCAL_ENTITY.Deal">
        <Deals></Deals>
      </div>
    </div>
    <div id="entity-selector">
      <EntitySelector></EntitySelector>
    </div>
  </div>
</template>

<script>
import {ENTITY} from "@/enums/ENTITIES";
import EntitySelector from "@/components/userActionTemplates/Select/entityFilter/EntitySelector";
import Customer from "@/components/userActionTemplates/Insert/InsertEntityForms/Customer";
import Products from "@/components/userActionTemplates/Insert/InsertEntityForms/Product";
import Deals from "@/components/userActionTemplates/Insert/InsertEntityForms/Deal";

export default {
  name: "Insert",
  components: {Deals, Products, Customer, EntitySelector},
  data() {
    return {
      LOCAL_ENTITY: ENTITY
    };
  },
  computed: {
    currentEntity() {
      return this.$store.getters.getCurrentEntity;
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