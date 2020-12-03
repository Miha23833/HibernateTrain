<template>
  <label>
    <input type="text" placeholder="User name">
    <input type="text" placeholder="User surname">
    <input type="number" max="120" min="0" placeholder="age">
    <button v-on:click="insertData()">Insert data!</button>
  </label>
</template>

<script>

export default {
  name: "Customer",
  data(){
    return{
      insertingValue: {
        Name: null,
        Surname: null,
        Age: null
      }
    }
  },
  methods:{
    insertData(){
      if (this.insertingValue.Name == null || this.insertingValue.Surname == null || this.insertingValue.Age == null ||
          this.insertingValue.Name === '' || this.insertingValue.Surname === '' || this.insertingValue.Age === ''){
        this.$parent.showErrorMessage('Please fill every field!')
      }
    }
  }
}
</script>

<style scoped>

</style>