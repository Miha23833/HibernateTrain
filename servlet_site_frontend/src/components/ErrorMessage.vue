<template>
  <div id="main" v-if="showing" ref="errorMessage">
    <div id="error-message-header">
      <button @click="this.showing=false"></button>
    </div>
    <div id="error-message-body">
      {{ errorMessage }}
    </div>
    <div id="time-line"></div>
  </div>
</template>

<script>
export default {
  name: "ErrorMessage",
  data() {
    return {
      showing: false
    }
  },
  methods: {
    showErrorMessage(message) {
      this.showing = false;
      this.showing = true;
      this.errorMessage = message;
      setTimeout(() => this.showing = false, 2000)
    }
  },
  computed: {
    errorMessage() {
      return this.$store.getters.getErrorMessage;
    }
  },
  watch: {
    errorMessage() {
      this.showErrorMessage()
    }
  }
}
</script>

<style scoped>
#main {
  background-color: inherit;
  position: fixed;
  bottom: 1em;
  width: 25em;
  right: -25em;
  height: 7em;
  -webkit-animation: slide 0.5s forwards;
  -webkit-animation-delay: 2s;
  animation: slide 0.5s forwards;
  border: #53ea93 solid 1px;
}

@-webkit-keyframes slide {
  100% { right: 1em; }
}

@keyframes slide {
  100% { right: 1em; }
}

#error-message-header{
  width: 100%;
  height: 1.5em;
  background-color: #eaeaea;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

#error-message-header button{
  margin-left: auto;
  width: 1.5em;
  height: 1.5em;
}

#time-line {
  width: 100%;
  height: .5em;
}

</style>