<template>
  <div>
    <Table v-bind:columns="columns" v-bind:data-rows="dataRows"></Table>
    <EntityFilter v-bind:current-entity="currentEntity"></EntityFilter>
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

</style>