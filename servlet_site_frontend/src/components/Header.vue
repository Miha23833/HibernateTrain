<template>
  <header>
    <div id="header-image">
      <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Amazon_logo.svg/1200px-Amazon_logo.svg.png"
           alt="normal"/>
    </div>
    <div id="header-user-action-buttons">
      <button @click="currentUserAction = USER_ACTION.SELECT">Select</button>
      <button @click="currentUserAction = USER_ACTION.INSERT">Insert</button>
      <button @click="currentUserAction = USER_ACTION.UPDATE">Update</button>
      <button @click="currentUserAction = USER_ACTION.DELETE">Delete</button>
    </div>
    <div id="header-log">
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
  }
}
</script>

<style scoped>

</style>