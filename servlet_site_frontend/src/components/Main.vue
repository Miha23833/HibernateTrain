<template>
  <main>
    <div v-if="currentUserAction === USER_ACTION.SELECT">
      <Select v-bind:current-entity="this.currentEntity"/>
    </div>
    <div v-else-if="currentUserAction === USER_ACTION.INSERT">
      <!--      <Insert/>-->
    </div>
    <div v-else-if="currentUserAction === USER_ACTION.UPDATE">
      <!-- <Update/>-->
    </div>
    <div v-else-if="currentUserAction === USER_ACTION.DELETE">
      <!--      <Delete/>-->
    </div>
  </main>
</template>

<script>
import {USER_ACTION} from "@/components/enums/USER_ACTIONS";
import Select from "@/components/userActionTemplates/Select/Select";
import {ENTITY} from "@/components/enums/ENTITIES";

export default {
  name: "Main",
  components: {Select},
  props: ['currentUserAction'],
  data() {
    return {
      USER_ACTION,
      currentEntity: ENTITY.Customer
    }
  },
  methods:{
    setCurrentEntity(newValue){
      this.currentEntity = newValue;
    }
  }
}
</script>

<style scoped>

</style>