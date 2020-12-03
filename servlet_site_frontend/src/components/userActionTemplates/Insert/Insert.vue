<template>
  <div id="main">
    <div id="insert-template">
      <div v-if="currentEntity===LOCAL_ENTITY.Customer">
        <Customer></Customer>
      </div>
      <div v-else-if="currentEntity===LOCAL_ENTITY.Product"></div>
      <div v-else-if="currentEntity===LOCAL_ENTITY.Deal"></div>
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

export default {
  name: "Insert",
  components: {Customer, EntitySelector},
  props: ['currentEntity'],
  data() {
    return {
      LOCAL_ENTITY: ENTITY
    };
  },
  methods:{
    setCurrentEntity(newValue){
      this.$parent.setCurrentEntity(newValue);
    },
    showErrorMessage(message) {
      this.$parent.showErrorMessage(message)
    }
  }
}
</script>

<style scoped>
#main{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding-left: 20%;
  padding-right: 20%;
}

#insert-template{
  width: 80%;
  background-color: red;
}


</style>