<template>
  <div id="wrapper">
    <div id="error-message-header">
      <button @click="destroy"></button>
    </div>
    <div id="error-message-body">
      {{ data.errorText }}
    </div>
    <div id="time-line"></div>
  </div>
</template>

<script>
export default {
  name: "ErrorMessage",
  props: {
    data: Object,
    timeout: {
      type: Number,
      default: 2000
    }
  },
  mounted() {
    setTimeout(this.destroy, this.timeout)
  },
  methods:{
    destroy(){
      this.$store.commit('removeErrorMessageByKey', this.data.key);
    }
  }
}
</script>

<style scoped>
#wrapper {
  background-color: inherit;
  width: 25em;
  height: 7em;
  right: 50em;
  -webkit-animation: slide 0.5s forwards;
  -webkit-animation-delay: 2s;
  animation: slide 0.5s forwards;
  border: #53ea93 solid 1px;
  margin-top: .5em;
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