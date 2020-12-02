<template>
  <div id="wrapper">
    <div id="header">Entity: </div>
    <button>Customers</button>
    <button>Products</button>
    <button>Deals</button>
  </div>
</template>

<script>
export default {
  name: "EntitySelector"
}
</script>

<style scoped>
#wrapper{
  margin-top: 1em;
  margin-bottom: 2em;
  width: 100%;
  height: 100%;
  text-align: center;
  border-color: #c6c6c6;
  border-radius: .5em;
  border-style: solid;
  border-width: 1px;
}

button:last-child{
  margin-bottom: .5em;
}
</style>