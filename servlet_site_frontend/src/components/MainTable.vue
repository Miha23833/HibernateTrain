<template>
  <table class="fl-table">
    <tr>
      <th v-for="column in columns" v-bind:key="column">
        {{column}}
      </th>
    </tr>
    <tr v-for="data in response" v-bind:key="data">
      <td v-for="item in columns" v-bind:key="item">
        {{data[item]}}
      </td>
    </tr>
  </table>

</template>

<script>
export default {
  name: "MainTable",
  data(){
    return{
      columns: {},
      response: {}
    }
  }
}
</script>

<style scoped>

</style>