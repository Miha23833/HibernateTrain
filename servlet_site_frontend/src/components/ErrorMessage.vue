<template>
  <div id="main" v-if="showing" ref="errorMessage">
    {{errorMessage}}
    <div id="time-line"></div>
  </div>
</template>

<script>
export default {
  name: "ErrorMessage",
  data(){
    return{
      errorMessage: 'Error message is missed',
      showing: false
    }
  },
  methods:{
    showErrorMessage(message){
      this.errorMessage = message;
      setTimeout(() => this.showing=false, 5000)
    }
  }
}
</script>

<style scoped>
#main{
  background-color: #394d63;
  position: fixed;
  bottom: 0;
  right: 0;
  width: 100em;
  height: 40em;
}

#time-line{
  width: 100%;
  height: .1em;
}

</style>