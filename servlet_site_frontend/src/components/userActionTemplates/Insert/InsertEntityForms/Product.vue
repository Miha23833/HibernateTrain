<template>
  <label>
    <input type="text" placeholder="Name" v-model="insertingValue.Name">
    <input type="text" placeholder="Description" v-model="insertingValue.Description">
    <input type="number" max="120" min="0" placeholder="Price" v-model="insertingValue.Price">
    <button v-on:click="insertData()">Insert data!</button>
  </label>
</template>

<script>
export default {
  name: "Products",
  data(){
    return{
      insertingValue: {
        Name: null,
        Description: null,
        Price: null
      }
    }
  },
  methods:{
    insertData(){
      let condition = [this.insertingValue.Name,this.insertingValue.Surname,this.insertingValue.Age].some(
          x => [null, undefined, NaN, ''].includes(x))
      if(condition){
        this.$store.commit("addErrorMessage", {message:'Please fill every field!'});
      }
    }
  }
}
</script>

<style scoped>

</style>