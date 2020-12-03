<template>
  <div>
    <Header/>
    <Main/>
    <ErrorMessage></ErrorMessage>
  </div>

</template>

<script>
import Header from "@/components/Header";
import Main from "@/components/Main";
import ErrorMessage from "@/components/ErrorMessage";
export default {
  name: 'App',
  components: {ErrorMessage, Main, Header},
  methods:{
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
