<template>
  <main>
    <HeaderButtons/>
    <MainTable/>
    <EntityFilter/>
  </main>
</template>

<script>
import EntityFilter from "@/components/EntityFilter";
import HeaderButtons from "@/components/HeaderButtons";
import MainTable from "@/components/MainTable";

export default {
  name: 'App',
  data(){
    return {
      entity: {}
    }
  },
  components: {
    EntityFilter,
    HeaderButtons,
    MainTable
  },
  methods: {
    setCurrentEntity(entity){
      this.entity = entity;
    }
  }
}
</script>

<style>
main {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
