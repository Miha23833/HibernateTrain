<template>
  <header>
    <div id="logo">
      <img src="https://aiconica.net/previews/amazon-logo-empty-square-icon-822.png"
           alt="normal"/>
    </div>
    <div id="main-buttons">
      <button @click="currentUserActionChanged(USER_ACTION.SELECT)">Select</button>
      <button @click="currentUserActionChanged(USER_ACTION.INSERT)">Insert</button>
      <button @click="currentUserActionChanged(USER_ACTION.UPDATE)">Update</button>
      <button @click="currentUserActionChanged(USER_ACTION.DELETE)">Delete</button>
    </div>
    <div id="header-login">
      <button>Log in</button>
    </div>
  </header>
</template>

<script>
import {USER_ACTION} from '@/components/enums/USER_ACTIONS'

export default {
  name: 'Header',
  props: ['currentUserAction'],
  data() {
    return {
      USER_ACTION
    };
  },
  methods: {
    currentUserActionChanged(newValue){
      this.$parent.setUserAction(newValue)
    }
  }
}
</script>

<style scoped>
header{
  background-color: #f1f1f1;
  height:4em;
  display: flex;
  width:100%;
}

#logo {
  height: 4em;
  overflow: hidden;
  min-width: 4em;
  float: left;
  margin-left: .4em;
  width: 10%;
}

#logo img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

#main-buttons{
  height: 100%;
  width: 80%;
  display: flex;
  align-items: center;
  justify-content: center;
}

#main-buttons button{
  width: 20em;
  height: 100%;
  border: 0;
  background-color: inherit;
  transition: .5s;
}

#main-buttons button:hover{
  background:#53ea93;
}

/* TODO: сделать так, чтобы по нажатию на кнопку стиль ниже сохранялся */
#main-buttons button:focus{
  background: #53ea93;
  outline:none;
  font-size: 1.5em;
  box-shadow: 12px 0 15px 1px #53ea93, -12px 0 15px 1px #53ea93;
}

#header-login {
  width: 10%;
}

</style>