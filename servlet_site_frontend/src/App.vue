<template>
  <div>
    <Header v-bind:current-user-action="userAction"/>
    <Main v-bind:current-user-action="userAction"/>
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
      userAction: USER_ACTION.SELECT
    }
  },
  methods:{
    setUserAction(newValue){
      this.userAction = newValue;
    }
  }
}
</script>

<style>
body {
  margin: 0;
}
</style>
