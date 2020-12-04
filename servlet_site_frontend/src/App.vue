<template>
  <div>
    <Header/>
    <Main/>

    <div id="error-message-wrapper-wrapper" v-if="Object.keys(errorMessages).length > 0">
      <div id="error-message-wrapper">
        <div v-for="value in errorMessages" :key="value.key">
          <ErrorMessage :data="value" :timeout="0" @message-added="setScrollbarDown()"></ErrorMessage>
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
  },
  methods: {
    setScrollbarDown(){
      let scrollingDiv = document.getElementById("error-message-wrapper");
      scrollingDiv.scrollTop = -scrollingDiv.scrollHeight;
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
  display: flex;
  flex-direction: column-reverse;
  -moz-border-radius-bottomleft: 1em;
  -webkit-border-bottom-left-radius: 1em;
}

#error-message-wrapper-wrapper ::-webkit-scrollbar {
  display: none;
}
</style>
