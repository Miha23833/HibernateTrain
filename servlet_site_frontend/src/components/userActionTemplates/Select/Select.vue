<template>
  <div id="main">
    <div id="table">
      <Table v-bind:columns="columns" v-bind:data-rows="dataRows"></Table>
    </div>
    <div class="div-sep"></div>
    <div id="filter">
      <EntityFilter @data-received="setTableData($event)"></EntityFilter>
    </div>
  </div>
</template>

<script>
import EntityFilter from "@/components/userActionTemplates/Select/EntityFilter";
import Table from "@/components/userActionTemplates/Select/Table";

export default {
  name: "Select",
  components: {Table, EntityFilter},
  data() {
    return {
      columns: [],
      dataRows: []
    }
  },
  methods: {
    setTableData(data) {
      this.columns = data.columns;
      this.dataRows = data.response;
    },
    addRow(row) {
      this.dataRows.push(row);
    }
  }
}
</script>

<style scoped>
#main {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding-left: 20%;
  padding-right: 20%;
  margin-top: 2em;
}

#table {
  width: 75%;
  overflow: auto;
}

#filter {
  width: 20%;
}

.div-sep {
  width: 1px;
  background-image: radial-gradient(#bfc8e5, white);
  height: inherit;
}
</style>