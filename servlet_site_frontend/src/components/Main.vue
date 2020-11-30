<template>
  <main>
    <div v-if="currentUserAction === USER_ACTION.SELECT">
      <Select/>
    </div>
    <div v-if="currentUserAction === USER_ACTION.UPDATE">
<!--      <Update/>-->
    </div>
    <div v-if="currentUserAction === USER_ACTION.INSERT">
<!--      <Insert/>-->
    </div>
    <div v-if="currentUserAction === USER_ACTION.DELETE">
<!--      <Delete/>-->
    </div>
  </main>
</template>

<script>
import {USER_ACTION} from "@/components/enums/USER_ACTIONS";
import Select from "@/components/userActionTemplates/Select/Select";

export default {
  name: "Main",
  components: {Select},
  props: ['currentUserAction'],
  data(){
    return {
      USER_ACTION
    }
  }
}
</script>

<style scoped>

</style>