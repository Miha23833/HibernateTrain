<template>
    <header>
      <div id="header-image">
        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Amazon_logo.svg/1200px-Amazon_logo.svg.png"
             alt="normal"/>
      </div>
      <div id="header-entity-buttons">
        <button @click="setEntity(ENTITIES.Customers)">Customers</button>
        <button @click="setEntity(ENTITIES.Products)">Products</button>
        <button @click="setEntity(ENTITIES.Deals)">Deals</button>
      </div>
      <div id="header-log">
        <button>Log in</button>
      </div>
    </header>
</template>

<script>
const ENTITIES = Object.freeze({
      "Customers": {
        asNum: 1,
        mapping: "/get-data/Customers"
      },
      "Products": {
        asNum: 2,
        mapping: "/get-data/Products"
      },
      "Deals": {
        asNum: 3,
        mapping: "/get-data/Deals"
      }
    }
);

export default {
  data() {
    return {
      ENTITIES,
      entity: ENTITIES.Customers
    };
  },
  methods: {
    setEntity(entity) {
      this.entity = entity
      this.$emit('setCurrentEntity', this.entity)
    }
  }
}
</script>

<style scoped>

</style>