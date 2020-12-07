<template>
  <main>
    <div v-if="currentUserAction === USER_ACTION.SELECT">
      <Select/>
    </div>
    <div v-else-if="currentUserAction === USER_ACTION.INSERT">
      <Insert/>
    </div>
    <div v-else-if="currentUserAction === USER_ACTION.UPDATE">
      <!-- <Update/>-->
    </div>
    <div v-else-if="currentUserAction === USER_ACTION.DELETE">
      <!--      <Delete/>-->
    </div>
  </main>
</template>

<script>
import {USER_ACTION} from "@/enums/USER_ACTIONS";
import Select from "@/components/userActionTemplates/Select/Select";
import Insert from "@/components/userActionTemplates/Insert/Insert";

export default {
  name: "Main",
  components: {Select, Insert},
  data() {
    return {
      USER_ACTION
    }
  },
  computed: {
    currentUserAction() {
      return this.$store.getters.getCurrentUserAction;
    }
  },
  methods: {
    setCurrentEntity(newValue) {
      this.$store.commit('setCurrentEntity', newValue)
    }
  }
}
</script>

<style scoped>
>>> input {
  font-size: 1em;
  padding: .5em;
  margin-bottom: .4em;
  font-family: Helvetica, sans-serif;
  transition: .3s;
  border: none;
  width: 80%;
  box-shadow: 0 1px #9f9f9f;
}


>>> input:focus {
  outline: none;
  box-shadow: 0 2px #53ea93;
}

>>> button {
  background-color: #f1f1f1;
  border: none;
  width: 80%;
  height: 2em;
  font-family: Helvetica, sans-serif;
  font-size: 1em;
  transition: .5s;
  margin-top: .5em;
}

>>> button:hover {
  background-color: #dff6df;
}

>>> button:focus {
  outline: none;
}

>>> button:active {
  background-color: #d0e5d0;
  transition: 0s ease-in-out;
}

</style>