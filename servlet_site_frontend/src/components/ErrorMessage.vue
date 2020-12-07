<template>
  <div id="wrapper">
    <div id="error-message-header">
      <button id="close-button" @click="destroy"></button>
    </div>
    <div id="error-message-body">
      {{ data.message }}
    </div>
  </div>
</template>

<script>
export default {
  name: "ErrorMessage",
  emits: ['messageAdded'],
  props: {
    data: Object,
    timeout: {
      type: Number,
      default: 0
    }
  },
  mounted() {
    this.$emit('message-added');
    if (this.timeout > 0) {
      setTimeout(this.destroy, this.timeout)
    }
  },
  methods: {
    destroy() {
      this.$store.commit('removeErrorMessageByKey', this.data.key);
    }
  }
}
</script>

<style scoped>
#wrapper {
  margin-left: 100%;
  width: 100%;
  height: 7em;
  -webkit-animation: slide 0.5s forwards;
  -webkit-animation-delay: 2s;
  animation: slide 0.5s forwards;
  background-color: #99de7c;
  margin-top: .5em;
  -moz-border-radius-bottomleft: 1em;
  -webkit-border-bottom-left-radius: 1em;
}

@-webkit-keyframes slide {
  100% {
    margin-left: 0;
  }
}

@keyframes slide {
  100% {
    margin-left: 0;
  }
}

#error-message-header {
  width: 100%;
  height: 20%;
  background-color: #eaeaea;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

#error-message-header button {
  margin-left: auto;
  margin-right: .45em;
  width: 1.5em;
  height: 1.5em;
}

#close-button {
  opacity: 0.3;
  transition: .5s;
  border: none;
  background-image: url("https://image.flaticon.com/icons/png/512/61/61155.png");
  background-size: 100%;
}

#close-button:hover {
  opacity: 1;
  border: none;
}

#close-button:active{
  outline: none;
  transition: .2s;
  background-color: #d2d2d2;
}

#close-button:focus{
  outline: none;
}

#error-message-body{
  height: 60%;
  padding-top: .5em;
  padding-left: 1em;
  padding-right: 1em;
  word-wrap: break-word;
  overflow: auto;
  font-family: Comic Sans MS, Comic Sans, cursive;
  text-shadow: 0 0 1px #376632;
  color: #0e6f9f;
}

</style>