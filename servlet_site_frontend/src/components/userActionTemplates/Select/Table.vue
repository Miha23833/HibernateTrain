<template>
  <table id="select-table">
    <thead>
    <tr>
      <th scope="col" v-for="column in columns" v-bind:key="column">
        {{ column }}
      </th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="data in dataRows" v-bind:key="data">
      <td v-for="item in columns" v-bind:key="item">
        {{ data[item] }}
      </td>
    </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  props: ['columns', 'dataRows'],
  name: "Table"
}
</script>

<style scoped>
table {
  border-spacing: 0;
  width: 100%;
  text-align: center;
  vertical-align: middle;
}

td {
  padding: .5em 0;
  border-width: 1px 0;
  border-style: solid;
  border-image: radial-gradient(#84d6ea, #ffffff) 5;
}

th {
  padding: 1em 2em;
  text-align: center;
  vertical-align: middle;
}

</style>