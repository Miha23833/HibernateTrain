<template>
  <div>
    <Header v-bind:current-user-action="userAction"/>
    <Main v-bind:current-user-action="userAction"/>
    <ErrorMessage></ErrorMessage>
  </div>

</template>

<script>
import Header from "@/components/Header";
import {USER_ACTION} from "@/enums/USER_ACTIONS";
import Main from "@/components/Main";
import ErrorMessage from "@/components/ErrorMessage";
export default {
  name: 'App',
  components: {ErrorMessage, Main, Header},
  data() {
    return {
      userAction: USER_ACTION.SELECT
    }
  },
  methods:{
    setUserAction(newValue){
      this.userAction = newValue;
    },
    showErrorMessage(message){
      this.$refs.errorMessage.showErrorMessage(message);
    }
  }
}
</script>

<style>
body {
  margin: 0;
}
</style>
