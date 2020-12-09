<template>
  <div id="wrapper">
    <div id="header">Entity:</div>
    <button v-on:click="setCurrentEntity(LOCAL_ENTITY.Customer)">Customers</button>
    <button v-on:click="setCurrentEntity(LOCAL_ENTITY.Product)">Products</button>
    <button v-on:click="setCurrentEntity(LOCAL_ENTITY.Deal)">Deals</button>
  </div>
</template>

<script>
import {ENTITY} from '@/enums/ENTITIES';

export default {
  name: "EntitySelector",
  data() {
    return {
      LOCAL_ENTITY: ENTITY,
    }
  },
  methods: {
    setCurrentEntity(newValue) {
      this.$store.commit("setCurrentEntity", newValue);
    }
  }
}
</script>

<style scoped>
#wrapper {
  margin-top: 1em;
  margin-bottom: 2em;
  width: 100%;
  text-align: center;
  border-color: #c6c6c6;
  border-radius: .5em;
  border-style: solid;
  border-width: 1px;
}

button:last-child {
  margin-bottom: .5em;
}
</style>