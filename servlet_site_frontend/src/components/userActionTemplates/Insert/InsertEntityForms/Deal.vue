<template>
  <label>
    <input type="datetime-local" placeholder="Deal date">
    <input type="number" placeholder="Customer ID">
    <input type="number" placeholder="Product ID">
    <input type="number" placeholder="Discount" min="0" max="100">
    <input type="number" placeholder="Price">
    <button v-on:click="insertData()">Insert data!</button>
  </label>
</template>

<script>
export default {
  name: "Deals",
  data(){
    return{
      insertingValue: {
        DealDate: null,
        CustomerID: null,
        Discount: null,
        ProductID: null,
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