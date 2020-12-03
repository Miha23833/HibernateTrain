<template>
  <div id="main">
    <div id="table">
    <Table v-bind:columns="columns" v-bind:data-rows="dataRows"></Table>
    </div>
    <div class="div-sep"></div>
    <div id="filter">
    <EntityFilter v-bind:current-entity="this.currentEntity"></EntityFilter>
    </div>
  </div>
</template>

<script>
import EntityFilter from "@/components/userActionTemplates/Select/EntityFilter";
import Table from "@/components/userActionTemplates/Select/Table";

export default {
  name: "Select",
  props:['currentEntity'],
  components: {Table, EntityFilter},
  data(){
    return {
      columns: [],
      dataRows: []
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
    },
    setCurrentEntity(newValue){
      this.columns = [];
      this.dataRows = [];
      this.$parent.setCurrentEntity(newValue);
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

.div-sep{
  width: 1px;
  background-image: radial-gradient(#bfc8e5, white);
  height: inherit;
}
</style>