<template>
  <header>
    <div id="logo">
      <img src="https://aiconica.net/previews/amazon-logo-empty-square-icon-822.png"
           alt="normal"/>
    </div>
    <div id="main-buttons">
      <button @click="setCurrentUserAction(USER_ACTION.SELECT)"
              :style="currentUserAction===USER_ACTION.SELECT? this.buttonPressedStyle : null ">Select
      </button>
      <button @click="setCurrentUserAction(USER_ACTION.INSERT)"
              :style="currentUserAction===USER_ACTION.INSERT? this.buttonPressedStyle : null ">Insert
      </button>
      <button @click="setCurrentUserAction(USER_ACTION.UPDATE)"
              :style="currentUserAction===USER_ACTION.UPDATE? this.buttonPressedStyle : null ">Update
      </button>
      <button @click="setCurrentUserAction(USER_ACTION.DELETE)"
              :style="currentUserAction===USER_ACTION.DELETE? this.buttonPressedStyle : null ">Delete
      </button>
    </div>
    <div id="header-login">
      <button @click="addErrorMessage({message: ++clickedCount})">Log in</button>
    </div>
  </header>
</template>

<script>
import {USER_ACTION} from '@/enums/USER_ACTIONS'

export default {
  name: 'Header',
  data() {
    return {
      USER_ACTION,
      buttonPressedStyle: {
        background: '#53ea93',
        outline: 'none',
        fontSize: '1.2em',
        boxShadow: '12px 0 15px 1px #53ea93, -12px 0 15px 1px #53ea93'
      },
      clickedCount: 0
    };
  },
  computed: {
    currentUserAction() {
      return this.$store.getters.getCurrentUserAction;
    }
  },
  methods: {
    setCurrentUserAction(newValue) {
      this.$store.commit('setCurrentUserAction', newValue);
    },
    addErrorMessage(newValue) {
      this.$store.commit('addErrorMessage', newValue);
    }
  }
}
</script>

<style scoped>
header {
  background-color: #f1f1f1;
  height: 4em;
  display: flex;
  width: 100%;
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

#main-buttons {
  height: 100%;
  width: 80%;
  display: flex;
  align-items: center;
  justify-content: center;
}

#main-buttons button {
  width: 20em;
  height: 100%;
  border: 0;
  background-color: inherit;
  transition: .5s;
  font-size: 1em;
}

#main-buttons button:hover {
  background: #53ea93;
}

#main-buttons button:focus {
  background: #53ea93;
  outline: none;
  font-size: 1.2em;
  box-shadow: 12px 0 15px 1px #53ea93, -12px 0 15px 1px #53ea93;
}

#header-login {
  display: flex;
  align-items: center;
  justify-content: center;
}

</style>