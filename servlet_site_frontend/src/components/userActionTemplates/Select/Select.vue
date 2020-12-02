<template>
  <div id="main">
    <div id="table">
    <Table v-bind:columns="columns" v-bind:data-rows="dataRows"></Table>
    </div>
    <div id="filter">
    <EntityFilter v-bind:current-entity="currentEntity"></EntityFilter>
    </div>
  </div>
</template>

<script>
import EntityFilter from "@/components/userActionTemplates/Select/EntityFilter";
import Table from "@/components/userActionTemplates/Select/Table";
import {ENTITY} from "@/components/enums/ENTITIES"

export default {
  name: "Select",
  components: {Table, EntityFilter},
  data(){
    return {
      columns: [],
      dataRows: [],
      currentEntity: ENTITY.Customer
    }
  },
  methods: {
    setColumns(columns) {
      this.columns = columns;
    },
    setTableData(data) {
      this.dataRows = data;
    },
    addRow(row) {
      this.dataRows.push(row);
    }
  }
}
</script>

<style scoped>
#main{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding-left: 20%;
  padding-right: 20%;
}
#table{
  width: 75%;
  overflow: auto;
}
#filter{
  width: 20%;
}
</style>