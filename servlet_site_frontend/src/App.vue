<template>
  <div>
    <Header v-bind:current-user-action="currentUserAction"/>
    <Main v-bind:current-user-action="currentUserAction"/>
  </div>

</template>

<script>
import Header from "@/components/Header";
import {USER_ACTION} from "@/components/enums/USER_ACTIONS";
import Main from "@/components/Main";
export default {
  name: 'App',
  components: {Main, Header},
  data() {
    return {
      currentUserAction: USER_ACTION.SELECT
    }
  }
}
</script>

<style>
body {
  margin: 0;
}
</style>
