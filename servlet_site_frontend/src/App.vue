<template>
  <div>
    <Header/>
    <Main/>

    <div id="error-message-wrapper-wrapper" v-if="Object.keys(errorMessages).length > 0">
      <div id="error-message-wrapper">
        <div v-for="value in errorMessages" :key="value.key">
          <ErrorMessage :data="value"></ErrorMessage>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import Header from "@/components/Header";
import Main from "@/components/Main";
import ErrorMessage from "@/components/ErrorMessage";

export default {
  name: 'App',
  components: {ErrorMessage, Main, Header},
  computed: {
    errorMessages() {
      return this.$store.getters.getErrorMessages;
    }
  }
}
</script>

<style>
body {
  margin: 0;
}

#error-message-wrapper {
  max-height: 21em;
  height: 21em;
  width: 26em;
  position: fixed;
  bottom: 1em;
  right: 0;
  overflow-y: scroll;
}

#error-message-wrapper-wrapper ::-webkit-scrollbar {
  display: none;
}
</style>
