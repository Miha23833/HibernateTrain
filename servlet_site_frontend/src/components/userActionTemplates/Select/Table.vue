<template>
  <table id="select-table">
    <tr>
      <th v-for="column in columns" v-bind:key="column">
        {{ column }}
      </th>
    </tr>
    <tr v-for="data in dataRows" v-bind:key="data">
      <td v-for="item in columns" v-bind:key="item">
        {{ data[item] }}
      </td>
    </tr>
  </table>
</template>

<script>
export default {
  props: ['columns', 'dataRows'],
  name: "Table"
}
</script>

<style scoped>

</style>