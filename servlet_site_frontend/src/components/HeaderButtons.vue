<template>
    <header>
      <div id="header-image">
        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Amazon_logo.svg/1200px-Amazon_logo.svg.png"
             alt="normal"/>
      </div>
      <div id="header-entity-buttons">
        <button @click="setEntity(PRIVATE_ENTITIES.Customers)">Customers</button>
        <button @click="setEntity(PRIVATE_ENTITIES.Products)">Products</button>
        <button @click="setEntity(PRIVATE_ENTITIES.Deals)">Deals</button>
      </div>
      <div id="header-log">
        <button>Log in</button>
      </div>
    </header>
</template>

<script>
import {ENTITIES} from '@/components/enums/ENTITIES'

export default {
  data() {
    return {
      PRIVATE_ENTITIES: ENTITIES,
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